package com.holelin.sundry.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/7/22 10:27
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/7/22 10:27
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Slf4j
public class SseUtils {
    private static Map<String, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

    /**
     * 创建连接
     *
     * @date: 2022/7/12 14:51
     * @auther: 公众号：程序员小富
     */
    public static SseEmitter connect(String userId) {
        try {
            // 设置超时时间，0表示不过期。默认30秒
            SseEmitter sseEmitter = new SseEmitter(0L);
            // 注册回调
            sseEmitter.onCompletion(() -> {

            });
            sseEmitter.onError(throwable -> {

            });
            sseEmitter.onTimeout(() -> {

            });
            sseEmitterMap.put(userId, sseEmitter);
            return sseEmitter;
        } catch (Exception e) {
            log.info("创建新的sse连接异常，当前用户：{}", userId);
        }
        return null;
    }

    /**
     * 给指定用户发送消息
     *
     * @date: 2022/7/12 14:51
     * @auther: 公众号：程序员小富
     */
    public static void sendMessage(String userId, String message) {

        if (sseEmitterMap.containsKey(userId)) {
            try {
                sseEmitterMap.get(userId).send(message);
            } catch (IOException e) {
                log.error("用户[{}]推送异常:{}", userId, e.getMessage());
            }
        }
    }
}
