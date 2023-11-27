package com.gavinjin.smartbibackend.mq.dlx;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class DeadLetterQueueConsumer {
    private static final String WORK_EXCHANGE_NAME = "direct2-exchange";
    private static final String DEAD_EXCHANGE_NAME = "dlx-direct-exchange";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(WORK_EXCHANGE_NAME, "direct");

        // Declare dead letter queue arguments and normal task queues
        Map<String, Object> dogArgsMap = new HashMap<>();
        dogArgsMap.put("x-dead-letter-exchange", DEAD_EXCHANGE_NAME);
        dogArgsMap.put("x-dead-letter-routing-key", "waibao");
        String dogQueue = "dog_queue";
        channel.queueDeclare(dogQueue, true, false, false, dogArgsMap);
        channel.queueBind(dogQueue, WORK_EXCHANGE_NAME, "dog");

        Map<String, Object> catArgsMap = new HashMap<>();
        catArgsMap.put("x-dead-letter-exchange", DEAD_EXCHANGE_NAME);
        catArgsMap.put("x-dead-letter-routing-key", "boss");
        String catQueue = "cat_queue";
        channel.queueDeclare(catQueue, true, false, false, catArgsMap);
        channel.queueBind(catQueue, WORK_EXCHANGE_NAME, "cat");

        DeliverCallback dogDeliverCallback = ((consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [dog] Received '" + delivery.getEnvelope().getRoutingKey() + "': '" + message + "'");
            channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
        });
        DeliverCallback catDeliverCallback = ((consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [cat] Received '" + delivery.getEnvelope().getRoutingKey() + "': '" + message + "'");
            channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
        });

        channel.basicConsume(dogQueue, false, dogDeliverCallback, consumerTag -> {});
        channel.basicConsume(catQueue, false, catDeliverCallback, consumerTag -> {});



    }
}
