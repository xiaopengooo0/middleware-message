package com.std.message.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: xiaopeng
 * @Description:
 * @DateTime: 2025/10/30 下午3:15 星期四
 **/
@Component
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    /**
     * 使用@Header注解从Kafka消息头中提取特定信
     * @param message
     * @param ack
     * @param key
     */
    // 单条消息消费
//    @KafkaListener(topics = "kafka", groupId = "kafka-consumer-group")
    public void consumer(String message,
                         Acknowledgment ack,
                         @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key
    ) {
        try {
            log.info("Received message - Key: {}, Value: {}", key, message);

            //处理业务逻辑
            log.info("【KafkaConsumer】处理业务逻辑....");
            // 确认消费
            ack.acknowledge();
        } catch (Exception e) {
            log.error("【KafkaConsumer】处理业务逻辑失败：{}", e.getMessage());
        }
    }


    // 批量消费 - 启用这个监听器来测试批量处理
    @KafkaListener(topics = "kafka", groupId = "kafka-consumer-batch-group")
    public void consumeBatch(List<String> messages, Acknowledgment ack) {
        log.info("=== 批量消费开始 ===");
        log.info("Received batch of {} messages", messages.size());
        for (String message : messages) {
            log.info("Batch processing message: {}", message);
        }
        log.info("=== 批量消费结束 ===");
        ack.acknowledge();
    }
}