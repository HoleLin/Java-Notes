package com.holelin.redis.delayqueue.consumer;

public interface DelayQConsumer {

    void subscribe(String queue);

}
