package com.gavinjin.smartbibackend.business_mq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.gavinjin.smartbibackend.business_mq.BiMqConstant.BI_EXCHANGE_NAME;
import static com.gavinjin.smartbibackend.business_mq.BiMqConstant.BI_ROUTING_KEY;

@Component
public class BiMessageProducer {
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * Send message to the message queue
     * @param message
     */
    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(BI_EXCHANGE_NAME, BI_ROUTING_KEY, message);
    }
}
