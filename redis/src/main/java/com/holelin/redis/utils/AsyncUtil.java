package com.holelin.redis.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

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

}
