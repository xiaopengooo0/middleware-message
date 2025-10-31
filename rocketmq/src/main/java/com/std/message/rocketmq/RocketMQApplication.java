package com.std.message.rocketmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.rocketmq.spring.core.RocketMQTemplate;

/**
 * @Author: xiaopeng
 * @Description: TODO
 * @DateTime: 2025/10/31 上午11:41 星期五
 **/
@SpringBootApplication
public class RocketMQApplication {
    public static void main(String[] args) {
        SpringApplication.run(RocketMQApplication.class, args);
    }
}