package com.holelin.redis.limit.enums;

/**
 * @Description: 限流类型
 * @Author: HoleLin
 * @CreateDate: 2021/2/26 16:16
 * @UpdateUser: HoleLin
 * @UpdateDate: 2021/2/26 16:16
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

public enum LimitType {

    /**
     * 自定义key
     */
    CUSTOMER,

    /**
     * 请求者IP
     */
    IP;
} 