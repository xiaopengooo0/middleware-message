package com.std.message.kafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * @Author: xiaopeng
 * @Description: TODO
 * @DateTime: 2025/10/30 下午2:57 星期四
 **/
@Component
public class KafkaProducer {


    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    private static final String TOPIC = "kafka";



    public void sendMessage(String key, String message) {
        log.info("【KafkaProducer】发送消息：{}", message);
        // 发送消息
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(TOPIC, key, message);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("【KafkaProducer】发送消息失败：{}", ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("【KafkaProducer】发送消息成功：{}", result.toString());
            }
        });
    }

    /**
     * 发送多条消息
     * @param key
     * @param message
     */
    @Transactional
    public void sendMessageTransaction(String key, String message) {
        log.info("【KafkaProducer】发送事务消息：{}", message);
        kafkaTemplate.executeInTransaction(operations -> {
            operations.send(TOPIC, key, message);
            return true;
        });
    }
}
