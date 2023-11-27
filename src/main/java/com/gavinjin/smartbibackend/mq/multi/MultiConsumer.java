package com.gavinjin.smartbibackend.mq.multi;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class MultiConsumer {
    private static final String TASK_QUEUE_NAME = "multi_queue";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        final Connection connection = factory.newConnection();

        for (int i = 0; i < 2; i++) {
            final Channel channel = connection.createChannel();

            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
            System.out.printf(" [%s] Waiting for message. To exit, press CTRL+C\n", channel);

            // Control number of tasks that one channel can handle at the same time
            channel.basicQos(1);

            DeliverCallback deliverCallback = ((consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.printf(" [%s] Received '%s'\n", consumerTag, message);

                try {
                    doWork(message);
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                } catch (Exception e) {
                    channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
                } finally {
                    System.out.printf(" [%s] Done\n", consumerTag);
                }
            });
            channel.basicConsume(TASK_QUEUE_NAME, false, deliverCallback, consumerTag -> {});
        }
    }

    private static void doWork(String task) {
        try {
            Thread.sleep(20000);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
