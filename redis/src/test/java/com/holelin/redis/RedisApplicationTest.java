package com.holelin.redis;

import cn.hutool.core.lang.UUID;
import com.github.javafaker.Faker;
import com.holelin.redis.bean.Person;
import com.holelin.redis.config.RedisAutoConfiguration;
import com.holelin.redis.utils.RedisLock;
import com.holelin.redis.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Locale;

@Slf4j
@SpringBootTest
@Import({RedisAutoConfiguration.class, RedisLock.class})
class RedisApplicationTest {
    @Autowired
    private RedisUtil redisUtil;
    public static final Integer TEST_NUM = 10;
    @Autowired
    private RedisLock redisLock;
    public static final String PREFIX_PERSON = "person:";


    @Test
    void cacheValue() {
        long start = DateTime.now().getMillis();
        for (int i = 0; i < TEST_NUM; i++) {
            redisUtil.set(UUID.fastUUID().toString(), UUID.fastUUID().toString());
        }
        long end = DateTime.now().getMillis();
        log.info("测试耗时:{}", end - start);
    }

    @Test
    void cacheBean() {
        long start = DateTime.now().getMillis();
        Faker faker = new Faker();
        for (int i = 0; i < TEST_NUM; i++) {
            Person person = new Person();
            person.setAge(faker.number().randomDigit());
            person.setName(faker.name().fullName());
            person.setNickName(faker.name().fullName());
            redisUtil.set(PREFIX_PERSON + UUID.fastUUID().toString(), person);
            log.info("测试:{}", redisUtil.get(PREFIX_PERSON + UUID.fastUUID().toString()));
        }
        long end = DateTime.now().getMillis();
        log.info("测试耗时:{}", end - start);
    }

    @Test
    void removePatternValue() {
        long start = DateTime.now().getMillis();
        redisUtil.removeKeysByPattern(PREFIX_PERSON + "*");
        long end = DateTime.now().getMillis();
        log.info("测试耗时:{}", end - start);
    }

    @Test
    @Ignore
    void removeCacheValue() {
        long start = DateTime.now().getMillis();
        redisUtil.flushDB();
        long end = DateTime.now().getMillis();
        log.info("测试耗时:{}", end - start);
    }

    @Test
    void redisLockTest() {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_PERSON).append(UUID.fastUUID().toString());
        boolean lock = redisLock.lock(sb.toString(), String.valueOf(System.currentTimeMillis()));
        if (lock) {
            log.info("任务进行中....");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            log.info("任务结束");
        } else {
            log.info("已有任务进行中,请稍后再试.....");
        }

    }

}
