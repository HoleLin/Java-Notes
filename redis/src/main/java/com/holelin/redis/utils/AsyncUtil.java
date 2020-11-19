package com.holelin.redis.utils;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Description: 异步处理类
 * @Author: HoleLin
 * @CreateDate: 2020/11/18 15:26
 * @UpdateUser: HoleLin
 * @UpdateDate: 2020/11/18 15:26
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

@Component
@Slf4j
public class AsyncUtil {

    @Autowired
    private RedisLock redisLock;
    @Autowired
    RedissonClient redissonClient;

    public static final String PREFIX_PERSON = "person:";

    @Async
    public void doTask(String key) {
        log.info("任务进行中....");
        try {
            Thread.sleep(10000);
            redisLock.release(PREFIX_PERSON, key);
        } catch (InterruptedException e) {
        }
        log.info("任务结束");
    }

    @Async
    public void doTaskByRedissonWithError(String key) {
        // TODO 失败
        RLock lock = redissonClient.getLock(key);
        try {
            Thread.sleep(2000);
            lock.unlock();
        } catch (InterruptedException e) {
            lock.unlock();
        }
        log.info("任务结束");
    }
}
