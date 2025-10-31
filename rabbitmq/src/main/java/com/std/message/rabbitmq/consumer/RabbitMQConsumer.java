package com.std.message.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import com.std.message.rabbitmq.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RabbitMQConsumer {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQConsumer.class);

    // 直连队列消费者

    /**
     *  监听直连队列
     * @param message 消息
     * @param channel   频道
     * @param deliveryTag 消息的标识
     */
    @RabbitListener(queues = RabbitMQConfig.DIRECT_QUEUE)
    public void processDirectMessage(String message, Channel channel,
                                   @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        try {
            log.info("Received direct message: {}", message);
            // 业务处理
            processBusiness(message);
            // 手动确认
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("处理直连消息失败: ", e);
            try {
                // 拒绝消息，重新入队
                channel.basicNack(deliveryTag, false, true);
            } catch (IOException ex) {
                log.error("拒绝消息失败: ", ex);
            }
        }
    }
    
    // 主题队列消费者

    /**
     *  监听主题队列
     * @param message   消息
     * @param amqpMessage  AMQP消息
     */
    @RabbitListener(queues = RabbitMQConfig.TOPIC_QUEUE)
    public void processTopicMessage(String message, Message amqpMessage) {
        String routingKey = amqpMessage.getMessageProperties().getReceivedRoutingKey();
        log.info("Received topic message with routing key {}: {}", routingKey, message);
        processBusiness(message);
    }
    
    // 死信队列处理
    @RabbitListener(queues = "dlx.queue")
    public void processDeadLetterMessage(String message) {
        log.info("---------Received dead letter message: {}", message);
        // 处理死信消息
    }
    
    private void processBusiness(String message) {
        // 具体的业务处理逻辑
        log.info("---------Processing message: {}", message);
    }
}