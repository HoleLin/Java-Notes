package com.holelin.redis.service.impl;

import com.holelin.redis.service.RedisLockService;
import com.holelin.redis.utils.AsyncUtil;
import com.holelin.redis.utils.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2020/11/18 15:23
 * @UpdateUser: HoleLin
 * @UpdateDate: 2020/11/18 15:23
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Service
@Slf4j
public class RedisLockServiceImpl implements RedisLockService {
    @Autowired
    private RedisLock redisLock;
    @Autowired
    private AsyncUtil asyncUtil;


    public static final String PREFIX_PERSON = "person:";
    private static final int DEFAULT_TIMEOUT = 60;

    @Override
    public String startTask(String key) {
        boolean lock = redisLock.lock(PREFIX_PERSON, key);
        if (lock) {
            try {
                asyncUtil.doTask(key);
            } catch (Exception e) {
                redisLock.release(PREFIX_PERSON, key);
            }
            return "任务进行中";
        } else {
            log.info("已有任务进行中,请稍后再试.....");
            return "已有任务进行中,请稍后再试.....";
        }
    }


}
