package com.std.message.pulsar;

import com.std.message.pulsar.producer.PulsarMultiTenantProducer;
import com.std.message.pulsar.producer.PulsarProducer;
import org.apache.pulsar.client.api.PulsarClientException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: xiaopeng
 * @Description: TODO
 * @DateTime: 2025/10/31 下午2:55 星期五
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class ApiTest {

    @Resource
    private PulsarProducer pulsarProducer;

    @Resource
    private PulsarMultiTenantProducer pulsarMultiTenantProducer;


    @Test
    public void sendMessage() throws PulsarClientException, InterruptedException {
        pulsarProducer.sendSyncMessage("Hello, Pulsar! ---Sync---");
        pulsarProducer.sendAsyncMessage("Hello, Pulsar! ---Async---");
        pulsarProducer.sendMessageWithProperties("Hello, Pulsar! --Property-- ", Map.of("key", "value"));
        pulsarProducer.sendDelayedMessage("Hello, Pulsar! ---Delayed---", 5, TimeUnit.SECONDS);

        Thread.sleep(5000);


        pulsarMultiTenantProducer.sendToTenant("public", "default", "user-topic", "Hello, Pulsar! ---MultiTenant---");
    }
}
