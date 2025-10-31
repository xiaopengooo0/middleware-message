package com.std.message.rabbitmq;

import com.std.message.rabbitmq.producer.RabbitMQProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @Author: xiaopeng
 * @Description: TODO
 * @DateTime: 2025/10/30 下午5:00 星期四
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class ApiTest {

    @Resource
    private RabbitMQProducer rabbitMQProducer;

    @Test
    public void sendMessage() throws InterruptedException {
        rabbitMQProducer.sendDirectMessage("hello rabbitmq");
        rabbitMQProducer.sendTopicMessage("topic.message", "hello top rabbitmq");
        rabbitMQProducer.sendTopicMessage("topic.message.top", "hello top rabbitmq");
        rabbitMQProducer.sendMessageWithConfirm("hello confirm rabbitmq");
        
        // 增加等待时间确保消费者处理完消息
        Thread.sleep(10000);
    }
}