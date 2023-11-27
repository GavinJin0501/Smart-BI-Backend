package com.gavinjin.smartbibackend.mq.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class TopicConsumer {
    private static final String EXCHANGE_NAME = "topic-exchange";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        // Create queues
        String frontendQueue = "frontend_queue";
        channel.queueDeclare(frontendQueue, true, false, false, null);
        channel.queueBind(frontendQueue, EXCHANGE_NAME, "#.frontend.#");

        String backendQueue = "backend_queue";
        channel.queueDeclare(backendQueue, true, false, false, null);
        channel.queueBind(backendQueue, EXCHANGE_NAME, "#.backend.#");

        String productQueue = "product_queue";
        channel.queueDeclare(productQueue, true, false, false, null);
        channel.queueBind(productQueue, EXCHANGE_NAME, "#.product.#");

        // Create handlers
        DeliverCallback frontendDeliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [frontend group] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        DeliverCallback backendDeliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [backend group] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        DeliverCallback productDeliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [product group] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };

        channel.basicConsume(frontendQueue, frontendDeliverCallback, consumerTag -> {});
        channel.basicConsume(backendQueue, backendDeliverCallback, consumerTag -> {});
        channel.basicConsume(productQueue, productDeliverCallback, consumerTag -> {});
    }
}
