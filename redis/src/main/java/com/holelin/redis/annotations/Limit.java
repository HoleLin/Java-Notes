package com.holelin.redis.annotations;

import com.holelin.redis.enums.LimitType;

import java.lang.annotation.*;

/**
 * @Description: 自定义限流注解
 * @Author: HoleLin
 * @CreateDate: 2021/2/26 16:17
 * @UpdateUser: HoleLin
 * @UpdateDate: 2021/2/26 16:17
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Limit {

    /**
     * 名字
     */
    String name() default "";

    /**
     * key
     */
    String key() default "";

    /**
     * Key的前缀
     */
    String prefix() default "";

    /**
     * 给定的时间范围 单位(秒)
     */
    int period();

    /**
     * 一定时间内最多访问次数
     */
    int count();

    /**
     * 限流的类型(用户自定义key或者请求ip)
     */
    LimitType limitType() default LimitType.CUSTOMER;
} 