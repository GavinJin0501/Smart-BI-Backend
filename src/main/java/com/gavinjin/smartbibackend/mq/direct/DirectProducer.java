package com.gavinjin.smartbibackend.mq.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class DirectProducer {
    private static final String EXCHANGE_NAME = "direct-exchange";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String[] userInput = scanner.nextLine().split(" ");
                if (userInput.length < 1) {
                    continue;
                }

                String message = userInput[0];
                String routingKey = userInput[1];

                channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println(" [x] Sent '" + message + "' with routingKey " + routingKey);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
