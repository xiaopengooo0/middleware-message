package com.std.message.kafka;

import com.std.message.kafka.producer.KafkaProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @Author: xiaopeng
 * @Description: TODO
 * @DateTime: 2025/10/30 下午3:56 星期四
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class ApiTest {


    @Resource
    private KafkaProducer kafkaProducer;



    @Test
    public void sendMessage() throws InterruptedException {
        // 快速连续发送多条消息
        for (int i = 1; i <= 5; i++) {
            kafkaProducer.sendMessage(String.valueOf(i), "hello kafka " + i);
        }
        // 等待一段时间确保消费者处理完消息
        Thread.sleep(5000);
    }
}