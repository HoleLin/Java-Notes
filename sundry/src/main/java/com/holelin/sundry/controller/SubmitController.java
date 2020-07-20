package com.holelin.sundry.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.LRUMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 重复提交测试控制器
 * @Author: HoleLin
 * @CreateDate: 2020/7/20 10:59
 * @UpdateUser: HoleLin
 * @UpdateDate: 2020/7/20 10:59
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

@Slf4j
@RestController
@RequestMapping("/submit")
public class SubmitController {

    /**
     * 缓存ID集合
     */
    private Map<String, Integer> reqCache = new HashMap<>();

    @GetMapping("/add-by-plan-a")
    public String planA(String id) {
        if (check(id)) return "Error";
        synchronized (this.getClass()) {
            if (reqCache.containsKey(id)) {
                log.info("重复提交");
                return "重复提交";
            }
            reqCache.put(id, 1);
        }
        return "执行成功";
    }

    private boolean check(String id) {
        if (StringUtils.isEmpty(id)) {
            return true;
        }
        return false;
    }

    /**
     * PlanB 请求ID存储集合
     */
    private static String[] reqCacheB = new String[100];
    /**
     * 请求计数器(指示ID存储的位置)
     */
    private static Integer reqCacheCounterB = 0;

    @GetMapping("/add-by-plan-b")
    public String planB(String id) {
        if (check(id)) return "Error";
        synchronized (this.getClass()) {
            if (Arrays.asList(reqCacheB).contains(id)) {
                log.info("重复提交");
                return "重复提交";
            }
            // 重置计数器
            if (reqCacheCounterB >= reqCacheB.length) {
                reqCacheCounterB = 0;
            }
            // 将Id加入到缓存
            reqCacheB[reqCacheCounterB] = id;
            reqCacheCounterB++;
        }
        return "执行成功";
    }

    /**
     * PlanC 请求ID存储集合
     */
    private static String[] reqCacheC = new String[100];
    /**
     * 请求计数器(指示ID存储的位置)
     */
    private static Integer reqCacheCounterC = 0;

    @GetMapping("/add-by-plan-c")
    public String planC(String id) {
        if (check(id)) return "Error";
        if (Arrays.asList(reqCacheC).contains(id)) {
            log.info("重复提交");
            return "重复提交";
        }
        synchronized (this.getClass()) {
            // 双重检查锁（DCL,double checked locking）提高程序的执行效率
            if (Arrays.asList(reqCacheC).contains(id)) {
                log.info("重复提交");
                return "重复提交";
            }
            // 重置计数器
            if (reqCacheCounterC >= reqCacheC.length) {
                reqCacheCounterC = 0;
            }
            // 将Id加入到缓存
            reqCacheC[reqCacheCounterC] = id;
            reqCacheCounterC++;
        }
        return "执行成功";
    }

    private LRUMap<String, Integer> reqCacheFinalEdition = new LRUMap<>(100);

    @GetMapping("/add-final-edition")
    public String finalEdition(String id) {
        if (check(id)) return "Error";

        synchronized (this.getClass()) {
            // 重复请求判断
            if (reqCache.containsKey(id)) {
                // 重复请求
                System.out.println("请勿重复提交！！！" + id);
                return "重复提交";
            }
            // 存储请求 ID
            reqCache.put(id, 1);
        }
        return "执行成功";
    }
}
