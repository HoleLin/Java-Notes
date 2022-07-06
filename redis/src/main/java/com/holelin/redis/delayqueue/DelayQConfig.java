package com.holelin.redis.delayqueue;


import com.holelin.redis.delayqueue.consumer.DelayQConsumer;
import com.holelin.redis.delayqueue.consumer.DelayQConsumerImpl;
import com.holelin.redis.delayqueue.producer.BoundedDelayQProducer;
import com.holelin.redis.delayqueue.producer.DelayQProducer;
import com.holelin.redis.delayqueue.producer.DelayQProducerImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class DelayQConfig {

 /**
     * 交互事件超时结束任务延迟队列
     */
    public static final String DELAY_QUEUE_INTERACT_EVENT_AUTO_CLOSE = "interact_event_auto_close";


    @Resource
    private PooledRedisClient pooledRedisClient;

    @Value(value = "${business-card.delayqueue.namespace:business-card}")
    private String namespace;

    @Bean
    public DelayQProducer delayQProducer() {
        DelayQProducerImpl delayQProducer = new DelayQProducerImpl();
        delayQProducer.setNamespace(namespace);
        delayQProducer.setPooledRedisClient(pooledRedisClient);

        BoundedDelayQProducer boundedProducer = new BoundedDelayQProducer();
        boundedProducer.setDelegate(delayQProducer);
        return boundedProducer;
    }

    @Bean
    public DelayQConsumer delayQConsumer() {
        DelayQConsumerImpl consumer = new DelayQConsumerImpl();
        consumer.setInitWithQueue(DELAY_QUEUE_INTERACT_EVENT_AUTO_CLOSE);
        consumer.setNamespace(namespace);
        consumer.setPooledRedisClient(pooledRedisClient);
        return consumer;
    }

}
