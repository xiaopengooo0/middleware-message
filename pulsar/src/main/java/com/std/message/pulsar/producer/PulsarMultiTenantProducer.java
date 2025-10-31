package com.std.message.pulsar.producer;

import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PulsarMultiTenantProducer {
    
    @Autowired
    private PulsarClient pulsarClient;
    
    // 多租户主题格式: persistent://tenant/namespace/topic
    public void sendToTenant(String tenant, String namespace, String topic, String message) 
            throws PulsarClientException {
        String fullTopic = String.format("persistent://%s/%s/%s", tenant, namespace, topic);
        
        try (Producer<String> producer = pulsarClient.newProducer(Schema.STRING)
                .topic(fullTopic)
                .create()) {
            producer.send(message);
        }
    }
}