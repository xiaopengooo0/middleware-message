package com.std.message.rocketmq;

import com.std.message.rocketmq.producer.RocketMQProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @Author: xiaopeng
 * @Description: TODO
 * @DateTime: 2025/10/31 上午11:47 星期五
 **/
@SpringBootTest(classes = RocketMQApplication.class)
@RunWith(SpringRunner.class)
public class ApiTest {

    @Resource
    private RocketMQProducer rocketMQProducer;

    @Test
    public void sendMessage() throws InterruptedException {
        rocketMQProducer.sendMessage("hello rocketmq");
        rocketMQProducer.sendMessageWithTag("hello rocketmq with tag ");
        rocketMQProducer.sendOrderlyMessage("1", "hello rocketmq with orderly");

        rocketMQProducer.sendTransactionMessage("hello rocketmq with transaction", " ----rocketmq with transaction---");
        // 增加等待时间确保消费者处理完消息
        Thread.sleep(5000);
    }
}