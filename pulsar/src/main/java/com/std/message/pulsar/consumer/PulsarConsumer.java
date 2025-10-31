package com.std.message.pulsar.consumer;

import org.apache.pulsar.client.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class PulsarConsumer {

    private static final Logger log = LoggerFactory.getLogger(PulsarConsumer.class);
    @Autowired
    private PulsarClient pulsarClient;
    
    private Consumer<String> consumer;
    
    private static final String TOPIC = "persistent://public/default/user-topic";
    private static final String SUBSCRIPTION = "my-subscription";
    private static final String BATCH_SUBSCRIPTION = "batch-subscription";

    /**
     * 初始化消费者
     */
    @PostConstruct
    public void init() throws PulsarClientException {
        consumer = pulsarClient
                .newConsumer(Schema.STRING) // 消息格式
                .topic(TOPIC) // 主题
                .subscriptionName(SUBSCRIPTION) // 订阅名称
                .subscriptionType(SubscriptionType.Shared) // Shared, Failover, Exclusive, Key_Shared
                .subscriptionInitialPosition(SubscriptionInitialPosition.Earliest) // 订阅初始位置
                .negativeAckRedeliveryDelay(10, TimeUnit.SECONDS) // 否定确认延迟
                .subscribe();
        
        startConsuming(); // 启动消费
//        startBatchConsuming();
    }
    
    @PreDestroy
    public void cleanup() throws PulsarClientException {
        if (consumer != null) {
            consumer.close();
        }
    }
    
    private void startConsuming() {
        Executors.newSingleThreadExecutor().submit(() -> {
            while (true) {
                try {
                    // 接收消息（阻塞）
                    Message<String> message = consumer.receive();
                    
                    try {
                        log.info("Received message: " + message.getValue()+ (message.getProperty("key")==null?"":" key:"+message.getProperty("key")));
                        // 处理业务
                        processMessage(message.getValue());
                        // 确认消息
                        consumer.acknowledge(message);
                    } catch (Exception e) {
                        log.error("Failed to process message, negative ack: " + e.getMessage());
                        // 否定确认，消息会重新投递
                        consumer.negativeAcknowledge(message);
                    }
                    
                } catch (PulsarClientException e) {
                    log.error("Error receiving message: " + e.getMessage());
                }
            }
        });
    }
    
    // 批量监听方式
    public void startBatchConsuming() throws PulsarClientException {
        try (Consumer<String> batchConsumer = pulsarClient.newConsumer(Schema.STRING)
                .topic(TOPIC)
                .subscriptionName("batch-subscription")
                .batchReceivePolicy(BatchReceivePolicy.builder()
                        .maxNumMessages(100)
                        .maxNumBytes(10 * 1024 * 1024)
                        .timeout(1, TimeUnit.SECONDS)
                        .build())
                .subscribe()) {

            Executors.newSingleThreadExecutor().submit(() -> {
                while (true) {
                    try {
                        // 批量接收
                        Messages<String> messages = batchConsumer.batchReceive();

                        for (Message<String> message : messages) {
                            try {
                                log.info("Received message: " + message.getValue()+ (message.getProperty("key")==null?"":" key:"+message.getProperty("key")));
                                processMessage(message.getValue());
                                batchConsumer.acknowledge(message);
                            } catch (Exception e) {
                                batchConsumer.negativeAcknowledge(message);
                            }
                        }

                    } catch (PulsarClientException e) {
                        log.error("Error in batch consumption: " + e.getMessage());
                    }
                }
            });
        }
    }
    
    private void processMessage(String message) {
        // 具体的业务处理逻辑
        log.info("Processing: " + message);
    }
}