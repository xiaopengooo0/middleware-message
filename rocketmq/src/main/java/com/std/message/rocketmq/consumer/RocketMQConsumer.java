package com.std.message.rocketmq.consumer;

import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class RocketMQConsumer {

    private static final Logger log = LoggerFactory.getLogger(RocketMQConsumer.class);
    @Service
    // 普通消费 - 只接收normal标签的消息
    @RocketMQMessageListener(
        topic = "user-topic",
        consumerGroup = "common-consumer-group",
        selectorExpression = "normal"
    )
    public static class CommonConsumer implements RocketMQListener<String> {
        @Override
        public void onMessage(String message) {
            log.info("Received normal message: " + message);
        }
    }
    @Service
    // 带tag过滤的消费 - 只接收tagA标签的消息
    @RocketMQMessageListener(
        topic = "user-topic",
        selectorExpression = "tagA",
        consumerGroup = "tag-consumer-group"
    )
    public static class TagConsumer implements RocketMQListener<String> {
        @Override
        public void onMessage(String message) {
            log.info("Received tagged message: " + message);
        }
    }
    @Service
    // 顺序消费
    @RocketMQMessageListener(
        topic = "order-topic",
        consumerGroup = "orderly-consumer-group",
        consumeMode = ConsumeMode.ORDERLY
    )
    public static class OrderlyConsumer implements RocketMQListener<String> {
        @Override
        public void onMessage(String message) {
            log.info("Received orderly message: " + message);
        }
    }
}