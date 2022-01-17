package com.holelin.sundry.domain;

import lombok.Data;


/**
 * @Description: 日志Bean
 * @Author: HoleLin
 * @CreateDate: 2022/1/17 10:14 AM
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/1/17 10:14 AM
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
public class OperationLog {
    /**
     * controller层完整访问路径
     */
    private String fullPath;

    /**
     * 被调用的方法名称
     */
    private String methodName;

    /**
     * 方法入参
     */
    private String parameters;

    /**
     * 调用耗时(单位毫秒)
     */
    private Long elapsedTimeWithMillis;

    /**
     * 方法调用时间(单位秒)
     */
    private Double elapsedTimeWithSeconds;

    /**
     * 方法返回值
     */
    private String response;

    @Override
    public String toString() {
        return "OperationLog{" +
                "fullPath='" + fullPath + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameters='" + parameters + '\'' +
                ", elapsedTimeWithMillis=" + elapsedTimeWithMillis +
                " ms, elapsedTimeWithSeconds=" + elapsedTimeWithSeconds +
                " s, response='" + response + '\'' +
                '}';
    }
}
