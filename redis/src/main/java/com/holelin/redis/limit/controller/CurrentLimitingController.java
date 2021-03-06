package com.holelin.redis.limit.controller;

import com.holelin.redis.limit.annotations.Limit;
import com.holelin.redis.limit.enums.LimitType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2021/2/26 16:14
 * @UpdateUser: HoleLin
 * @UpdateDate: 2021/2/26 16:14
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@RestController
@RequestMapping("/current-limiting")
public class CurrentLimitingController {
    private static final AtomicInteger ATOMIC_INTEGER_1 = new AtomicInteger();
    private static final AtomicInteger ATOMIC_INTEGER_2 = new AtomicInteger();
    private static final AtomicInteger ATOMIC_INTEGER_3 = new AtomicInteger();


    @Limit(key = "limit_test", period = 10, count = 3)
    @GetMapping("/limit-test1")
    public int testLimiter1() {

        return ATOMIC_INTEGER_1.incrementAndGet();
    }


    @Limit(key = "customer_limit_test", period = 10, count = 3, limitType = LimitType.CUSTOMER)
    @GetMapping("/limit-test2")
    public int testLimiter2() {

        return ATOMIC_INTEGER_2.incrementAndGet();
    }

    @Limit(key = "ip_limit_test", period = 10, count = 3, limitType = LimitType.IP)
    @GetMapping("/limit-test3")
    public int testLimiter3() {

        return ATOMIC_INTEGER_3.incrementAndGet();
    }

}
