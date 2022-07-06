package com.holelin.redis.delayqueue.producer;

import com.alibaba.fastjson.JSON;
import com.holelin.redis.delayqueue.DelayJob;
import com.holelin.redis.delayqueue.PooledRedisClient;
import lombok.Getter;
import lombok.Setter;
import java.time.Duration;
import java.time.Instant;

import static com.holelin.redis.delayqueue.util.DQUtil.buildKey;

public class DelayQProducerImpl implements DelayQProducer {

    @Setter
    @Getter
    private String namespace;

    @Setter
    private PooledRedisClient pooledRedisClient;

    @Override
    public void submit(String queue, DelayJob<?> job, Duration delayed) {
        double score = Instant.now().toEpochMilli() + delayed.toMillis();
        pooledRedisClient.runWithRetry(jedis -> jedis.zadd(buildKey(namespace, queue), score, JSON.toJSONString(job)));
    }

    @Override
    public boolean update(String queue, DelayJob<?> job, Duration delayed) {
        String jobString = JSON.toJSONString(job);
        Double oldScore = pooledRedisClient.executeWithRetry(jedis -> jedis.zscore(buildKey(namespace, queue), jobString));
        if (oldScore == null) {
            return false;
        } else {
            double newScore = Instant.now().toEpochMilli() + delayed.toMillis();
            pooledRedisClient.runWithRetry(jedis -> jedis.zadd(buildKey(namespace, queue), newScore, JSON.toJSONString(job)));
            return true;
        }
    }

    @Override
    public void cancel(String queue, DelayJob<?> job) {
        pooledRedisClient.runWithRetry(jedis -> jedis.zrem(buildKey(namespace, queue), JSON.toJSONString(job)));
    }

    @Override
    public int getQueueSize(String queue) {
        return pooledRedisClient.executeWithRetry(jedis -> jedis.zcard(buildKey(namespace, queue))).intValue();
    }
}
