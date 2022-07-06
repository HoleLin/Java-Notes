package com.holelin.redis.delayqueue.producer;

import com.holelin.redis.delayqueue.DelayJob;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class BoundedDelayQProducer implements DelayQProducer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Setter
    private DelayQProducer delegate;

    /**
     * warning: 按每个job 512字节计算，200,000个job将占用redis约100MB内存
     * 后续需要考虑对queue分片
     */
    @Setter
    private int maxQueueSize = 200000;

    @Setter
    private int warningSize = 100000;

    @Override
    public void submit(String queue, DelayJob<?> job, Duration delayed) {
        int queueSize = delegate.getQueueSize(queue);
        if (queueSize > maxQueueSize) {
            logger.error("task queue:{} exceed limit:{}, task discard", queue, maxQueueSize);
        } else if (queueSize > warningSize) {
            logger.warn("task queue:{} size warning, current size:{}", queue, queueSize);
            delegate.submit(queue, job, delayed);
        } else {
            logger.debug("task queue:{}, size:{}", queue, queueSize);
            delegate.submit(queue, job, delayed);
        }
    }

    @Override
    public boolean update(String queue, DelayJob<?> job, Duration delayed) {
        return delegate.update(queue, job, delayed);
    }

    @Override
    public void cancel(String queue, DelayJob<?> job) {
        delegate.cancel(queue, job);
    }

    @Override
    public String getNamespace() {
        return delegate.getNamespace();
    }
}
