package com.std.message.rabbitmq.producer;

import com.std.message.rabbitmq.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RabbitMQProducer {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQProducer.class);
    
    @Autowired
    private RabbitTemplate rabbitTemplate;

    
    // 发送到直连交换机
    public void sendDirectMessage(String message) {
        log.info("发送到直连交换机：{}", message);
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.DIRECT_EXCHANGE,
            RabbitMQConfig.DIRECT_ROUTING_KEY,
            message,
            // 设置消息持久化
            msg -> {
                msg.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                return msg;
            },
                new CorrelationData(UUID.randomUUID().toString())
        );
    }
    
    // 发送到主题交换机
    public void sendTopicMessage(String routingKey, String message) {
        log.info("发送到主题交换机：{} - {}", routingKey, message);
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.TOPIC_EXCHANGE,
            routingKey,
            message, new CorrelationData(UUID.randomUUID().toString())
        );
    }
    
    // 发送带确认的消息
    public void sendMessageWithConfirm(String message) {
        log.info("发送带确认消息：{}", message);
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.DIRECT_EXCHANGE,
            RabbitMQConfig.DIRECT_ROUTING_KEY,
            message,
            correlationData
        );
    }
}