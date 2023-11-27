package com.gavinjin.smartbibackend.mq.dlx;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class DeadLetterQueueProducer {
    private static final String WORK_EXCHANGE_NAME = "direct2-exchange";
    private static final String DEAD_EXCHANGE_NAME = "dlx-direct-exchange";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            // 1. Declare an exchange for a dead letter queue
            channel.exchangeDeclare(DEAD_EXCHANGE_NAME, "direct");

            // 2. Declare and bind the dead letter queues
            String dlqName1 = "boss_queue";
            channel.queueDeclare(dlqName1, true, false, false, null);
            channel.queueBind(dlqName1, DEAD_EXCHANGE_NAME, "boss");

            String dlqName2 = "waibao_queue";
            channel.queueDeclare(dlqName2, true, false, false, null);
            channel.queueBind(dlqName2, DEAD_EXCHANGE_NAME, "waibao");

            DeliverCallback bossDeliverCallback = ((consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(" [boss-dlq] Received '" + delivery.getEnvelope().getRoutingKey() + "': '" + message + "'");
                channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
            });
            DeliverCallback waibaoDeliverCallback = ((consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(" [waibao-dlq] Received '" + delivery.getEnvelope().getRoutingKey() + "': '" + message + "'");
                channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
            });
            channel.basicConsume(dlqName1, false, bossDeliverCallback, consumerTag -> {});
            channel.basicConsume(dlqName2, false, waibaoDeliverCallback, consumerTag -> {});

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String userInput = scanner.nextLine();
                String[] strings = userInput.split(" ");
                if (strings.length < 1) {
                    continue;
                }

                String message = strings[0];
                String routingKey = strings[1];

                channel.basicPublish(WORK_EXCHANGE_NAME, routingKey, null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println(" [x] Sent '" + message + "' with routing: '" + routingKey + "'");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
