package com.holelin.redis.controller;

import cn.hutool.core.lang.UUID;
import com.holelin.redis.service.RedisLockService;
import com.holelin.redis.utils.RedisLock;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: Redis锁测试Controller
 * @Author: HoleLin
 * @CreateDate: 2020/11/18 15:16
 * @UpdateUser: HoleLin
 * @UpdateDate: 2020/11/18 15:16
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/redis-lock")
public class RedisLockController {

    @Autowired
    private RedisLockService redisLockService;

    @GetMapping("/start")
    public String startTask(@RequestParam("key") String key) {
        return redisLockService.startTask(key);
    }
    @GetMapping("/start-by-redisson")
    public String startTaskByRedison(@RequestParam("key") String key)  {
        return redisLockService.startTaskByRedisson(key);
    }
}
