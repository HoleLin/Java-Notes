package com.holelin.redis.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @Description: Redis锁工具类
 * @Author: HoleLin
 * @CreateDate: 2020/11/18 15:50
 * @UpdateUser: HoleLin
 * @UpdateDate: 2020/11/18 15:50
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Component
@Slf4j
public class RedisLock implements ApplicationRunner, ApplicationContextAware {

    private StringRedisTemplate redisTemplate;

    private TaskScheduler taskScheduler;

    private ApplicationContext applicationContext;


    private static final int DEFAULT_TIMEOUT = 60;

    private final static Map<String, Future<?>> HOLDER = new ConcurrentHashMap<>();

    @Autowired
    public RedisLock(@Qualifier("stringRedisTemplate") StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Boolean lock(String lockPrefix, String lockKey, int timeout, Boolean autoRenew) {
        String key = lockPrefix + lockKey;
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, System.currentTimeMillis() + "");
        if (result) {
            if (autoRenew) {
                if (HOLDER.containsKey(key)) {
                    HOLDER.remove(key).cancel(true);
                }
                Future future = taskScheduler.scheduleAtFixedRate(() -> {
                    if (redisTemplate.hasKey(key)) {
                        redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
                    } else {
                        log.error("redis已不存在key{},但定时器仍在执行", key);
                    }
                }, timeout * 1000 / 5 * 4);

                HOLDER.put(key, future);
            } else {
                redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
            }
        }
        return result;
    }

    public Boolean lock(String lockPrefix, String lockKey) {
        return lock(lockPrefix, lockKey, DEFAULT_TIMEOUT, false);
    }

    /**
     * 防止超时后,锁被释放
     *
     * @param lockPrefix 锁前缀
     * @param lockKey    锁key
     * @return 是否得到锁
     */
    public Boolean lockAutoRenew(String lockPrefix, String lockKey) {
        return lock(lockPrefix, lockKey, DEFAULT_TIMEOUT, true);
    }

    public void release(String lockPrefix, String lockKey) {
        String key = lockPrefix + lockKey;
        if (HOLDER.containsKey(key)) {
            HOLDER.remove(key).cancel(true);
        }
        redisTemplate.delete(key);

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        try {
            this.taskScheduler = applicationContext.getBean(TaskScheduler.class);
        } catch (NoSuchBeanDefinitionException e) {
            try {
                ScheduledAnnotationBeanPostProcessor scheduledAnnotationBeanPostProcessor = applicationContext.getBean(ScheduledAnnotationBeanPostProcessor.class);
                ScheduledTaskRegistrar scheduledTaskRegistrar = (ScheduledTaskRegistrar) ReflectionUtils.getFieldValue(scheduledAnnotationBeanPostProcessor, "registrar");
                this.taskScheduler = scheduledTaskRegistrar.getScheduler();
            } catch (Exception noScheduledProcessorException) {
                this.taskScheduler = new ConcurrentTaskScheduler(Executors.newSingleThreadScheduledExecutor());
            }
        }
    }

}
