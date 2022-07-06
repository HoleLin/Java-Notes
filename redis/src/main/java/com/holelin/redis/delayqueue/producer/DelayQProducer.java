package com.holelin.redis.delayqueue.producer;

import com.holelin.redis.delayqueue.DelayJob;

import java.time.Duration;

public interface DelayQProducer {

    /**
     * 提交任务，若存在则更新
     */
    void submit(String queue, DelayJob<?> job, Duration delayed);

    /**
     * 更新任务，不存在返回false
     */
    boolean update(String queue, DelayJob<?> job, Duration delayed);

    /**
     * 取消任务
     */
    void cancel(String queue, DelayJob<?> job);

    /**
     * 查询任务队列长度
     */
    default int getQueueSize(String queue) {
        return 0;
    }

    String getNamespace();
}