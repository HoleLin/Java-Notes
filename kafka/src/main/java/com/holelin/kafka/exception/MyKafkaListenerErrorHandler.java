package com.holelin.kafka.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2020/9/28 14:20
 * @UpdateUser: HoleLin
 * @UpdateDate: 2020/9/28 14:20
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

@Slf4j
@Service("myErrorHandler")
public class MyKafkaListenerErrorHandler implements KafkaListenerErrorHandler {
    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception) {
        log.info(message.getPayload().toString());
        return null;
    }

    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception, Consumer<?, ?> consumer) {
        log.info(message.getPayload().toString());
        return null;
    }
}