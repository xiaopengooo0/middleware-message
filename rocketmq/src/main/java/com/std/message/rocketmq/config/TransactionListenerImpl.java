package com.std.message.rocketmq.config;

import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;

// 事务监听器
@RocketMQTransactionListener
public class TransactionListenerImpl implements RocketMQLocalTransactionListener {

    private static final Logger log = LoggerFactory.getLogger(TransactionListenerImpl.class);

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        try {
            log.info("【TransactionListenerImpl】接受到事务消息：{}", arg);
            // 执行本地事务
            boolean success = executeBusinessTransaction(msg, arg);
            return success ? RocketMQLocalTransactionState.COMMIT 
                          : RocketMQLocalTransactionState.ROLLBACK;
        } catch (Exception e) {
            return RocketMQLocalTransactionState.UNKNOWN;
        }
    }
    
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        // 检查本地事务状态
        boolean committed = checkTransactionStatus(msg);
        return committed ? RocketMQLocalTransactionState.COMMIT 
                        : RocketMQLocalTransactionState.ROLLBACK;
    }
    
    private boolean executeBusinessTransaction(Message msg, Object arg) {
        log.info("【TransactionListenerImpl】执行本地事务：{}", arg);
        // 实现业务事务逻辑
        return true;
    }
    
    private boolean checkTransactionStatus(Message msg) {
        log.info("【TransactionListenerImpl】检查事务状态：{}", msg);
        // 实现事务状态检查逻辑
        return true;
    }
}
