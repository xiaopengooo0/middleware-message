package com.std.message.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * @Author: xiaopeng
 * @Description: TODO
 * @DateTime: 2025/10/30 下午2:43 星期四
 **/
@SpringBootApplication
@EnableKafka
public class KafkaApplication {
    public static void main(String[] args) {
        SpringApplication.run(KafkaApplication.class, args);
    }
}