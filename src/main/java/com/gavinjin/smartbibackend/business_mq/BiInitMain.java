package com.gavinjin.smartbibackend.business_mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import static com.gavinjin.smartbibackend.business_mq.BiMqConstant.*;

/**
 * Used to create exchange and queue for testing
 * Only need to be executed once before the program starts
 */
public class BiInitMain {
    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(BI_EXCHANGE_NAME, "direct");

            channel.queueDeclare(BI_QUEUE_NAME, true, false, false, null);
            channel.queueBind(BI_QUEUE_NAME, BI_EXCHANGE_NAME, BI_ROUTING_KEY);
        } catch (Exception e) {

        }


    }
}
