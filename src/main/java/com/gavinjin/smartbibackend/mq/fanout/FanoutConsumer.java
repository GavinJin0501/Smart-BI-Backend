package com.gavinjin.smartbibackend.mq.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class FanoutConsumer {
    private static final String EXCHANGE_NAME = "fanout-exchange";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        final Connection connection = factory.newConnection();

        for (int i = 0; i < 2; i++) {
            // declare the exchange for each channel
            final Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

            // declare a queue for this channel and bind it to the exchange
            String queueName = String.format("FanoutConsumer%d", i);
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, EXCHANGE_NAME, "");

            System.out.printf(" [%s] Waiting for message. To exit, press CTRL+C\n", channel);
            channel.basicQos(1);
            DeliverCallback deliverCallback = ((consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.printf(" [%s] Received '%s'\n", queueName, message);
            });
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
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
