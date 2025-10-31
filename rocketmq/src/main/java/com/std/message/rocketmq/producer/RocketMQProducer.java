package com.std.message.rocketmq.producer;

import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class RocketMQProducer {

    private static final Logger log = LoggerFactory.getLogger(RocketMQProducer.class);
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    
    private static final String TOPIC = "user-topic";
    private static final String ORDERLY_TOPIC = "order-topic";
    
    // 发送普通消息（带normal标签）
    public void sendMessage(String message) {
        log.info("【RocketMQProducer】发送消息：{}", message);
        rocketMQTemplate.convertAndSend(TOPIC + ":normal", message);
    }
    
    // 发送带tag的消息
    public void sendMessageWithTag(String message) {
        log.info("【RocketMQProducer】发送带tag的消息：{}", message);
        rocketMQTemplate.convertAndSend(TOPIC + ":tagA", message);
    }
    
    // 发送顺序消息
    public void sendOrderlyMessage(String orderId, String message) {
        log.info("【RocketMQProducer】发送顺序消息：{}", message);
        rocketMQTemplate.syncSendOrderly(ORDERLY_TOPIC, message, orderId);
    }
    
    // 发送事务消息
    public void sendTransactionMessage(String message, Object arg) {
        log.info("【RocketMQProducer】发送事务消息：{}", message);
        TransactionSendResult result = rocketMQTemplate.sendMessageInTransaction(
            TOPIC, 
            MessageBuilder.withPayload(message).build(),
            arg
        );
    }
}