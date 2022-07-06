package com.holelin.redis.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/7/26 15:45
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/7/26 15:45
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Slf4j
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
 
    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        log.info("监听到key：" + expiredKey + "已过期");
    }
}