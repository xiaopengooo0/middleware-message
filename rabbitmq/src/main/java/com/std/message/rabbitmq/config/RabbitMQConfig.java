package com.std.message.rabbitmq.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: xiaopeng
 * @Description: TODO
 * @DateTime: 2025/10/30 下午4:51 星期四
 **/
@Configuration
public class RabbitMQConfig {


    // 直连交换机
    public static final String DIRECT_EXCHANGE = "direct.exchange";
    public static final String DIRECT_QUEUE = "direct.queue";
    public static final String DIRECT_ROUTING_KEY = "direct.routing.key";

    // 主题交换机
    public static final String TOPIC_EXCHANGE = "topic.exchange";
    public static final String TOPIC_QUEUE = "topic.queue";
    // 修改绑定键以匹配测试中的路由键
    public static final String TOPIC_BINDING_KEY = "topic.message.*";
    private static final Logger log = LoggerFactory.getLogger(RabbitMQConfig.class);


    /**
     * 创建一个持久化的直连交换机，参数为：交换机名称、持久化(true)、不自动删除(false)
     * @return
     */
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE, true, false);
    }

    /**
     * 创建一个持久化的队列，参数为：队列名称、持久化(true)、排他(false)、自动删除(false)
     * @return
     */
    @Bean
    public Queue directQueue() {
        return new Queue(DIRECT_QUEUE, true, false, false);
    }


    /**
     * 创建一个直连绑定关系
     * @return
     */
    @Bean
    public Binding directBinding() {
        return BindingBuilder.bind(directQueue())
                .to(directExchange())
                .with(DIRECT_ROUTING_KEY);
    }

    /**
     * 创建一个主题交换机，参数为：交换机名称、持久化(true)、不自动删除(false)
     * @return
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE, true, false);
    }

    /**
     * 创建一个持久化的队列，参数为：队列名称、持久化(true)、排他(false)、自动删除(false)
     * @return
     */
    @Bean
    public Queue topicQueue() {
        return new Queue(TOPIC_QUEUE, true, false, false);
    }

    /**
     * 创建一个主题绑定关系
     * @return
     */
    @Bean
    public Binding topicBinding() {
        return BindingBuilder.bind(topicQueue())
                .to(topicExchange())
                // 使用能匹配测试中路由键的绑定键
                .with(TOPIC_BINDING_KEY);
    }


    @Bean
    public Queue dlxQueue() {
        return QueueBuilder
                .durable("dlx.queue") // 必须和已有队列一致
                .build();
    }


    /**
     * 创建一个消息确认回调，参数为：确认回调、失败回调
     * @return
     */
    @Bean
    public RabbitTemplate.ConfirmCallback confirmCallback() {
        // 消息确认回调 correlationData 消息唯一标识符 ,ack 是否成功 ,cause 失败原因

        return (correlationData, ack, cause) -> {
            if (ack) {
                log.info("Message confirmed, correlationData: {}", correlationData);
            } else {
                log.error("Message failed, cause: {}", cause);
            }
        };
    }
    

    
    /**
     * 配置RabbitTemplate
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setConfirmCallback(confirmCallback());
        template.setReturnsCallback(returned -> {
            log.error("Message returned: exchange={}, routingKey={}, replyCode={}, replyText={}",
                    returned.getExchange(), returned.getRoutingKey(),
                    returned.getReplyCode(), returned.getReplyText());
        });
        // 启用消息返回功能
        template.setMandatory(true);
        return template;
    }
}