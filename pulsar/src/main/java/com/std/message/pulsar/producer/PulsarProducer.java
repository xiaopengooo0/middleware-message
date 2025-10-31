package com.std.message.pulsar.producer;

import org.apache.pulsar.client.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class PulsarProducer {
    
    @Autowired
    private PulsarClient pulsarClient;
    
    private Producer<String> producer;

    private static final String TOPIC = "persistent://public/default/user-topic";

    @PostConstruct
    public void init() throws PulsarClientException {
        producer = pulsarClient.newProducer(Schema.STRING)
                .topic(TOPIC)
                .compressionType(CompressionType.LZ4)
                .sendTimeout(10, TimeUnit.SECONDS)
                .create();
    }

    @PreDestroy
    public void cleanup() throws PulsarClientException {
        if (producer != null) {
            producer.close();
        }
    }

    // 发送同步消息
    public void sendSyncMessage(String message) throws PulsarClientException {
        MessageId messageId = producer.send(message);
        System.out.println("Sent message with ID: " + messageId);
    }

    // 发送异步消息
    public void sendAsyncMessage(String message) {
        producer.sendAsync(message)
                .thenAccept(messageId -> {
                    System.out.println("Sent message successfully with ID: " + messageId);
                })
                .exceptionally(throwable -> {
                    System.err.println("Failed to send message: " + throwable.getMessage());
                    return null;
                });
    }

    // 发送带属性的消息
    public void sendMessageWithProperties(String message, Map<String, String> properties)
            throws PulsarClientException {
        TypedMessageBuilder<String> messageBuilder = producer.newMessage()
                .value(message);

        properties.forEach(messageBuilder::property);

        messageBuilder.send();
    }

    // 发送延迟消息
    public void sendDelayedMessage(String message, long delay, TimeUnit unit)
            throws PulsarClientException {
        producer.newMessage()
                .value(message)
                .deliverAfter(delay, unit)
                .send();
    }
}