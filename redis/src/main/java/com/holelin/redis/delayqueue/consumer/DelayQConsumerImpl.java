package com.holelin.redis.delayqueue.consumer;

import com.alibaba.fastjson.JSON;
import com.google.common.eventbus.AsyncEventBus;
import com.holelin.redis.delayqueue.DelayJob;
import com.holelin.redis.delayqueue.DelayJobProcessor;
import com.holelin.redis.delayqueue.PooledRedisClient;
import com.holelin.redis.delayqueue.util.DQUtil;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("UnstableApiUsage")
public class DelayQConsumerImpl implements DelayQConsumer, ApplicationContextAware, DisposableBean, InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String POP_SCRIPT = "local rlt = redis.call('ZRANGEBYSCORE', KEYS[1], '-inf', ARGV[1], 'LIMIT', '0', ARGV[2])"
            + " if next(rlt) ~= nil then redis.call('ZREM', KEYS[1], unpack(rlt)) end"
            + " return rlt";

    private static final long SLEEP_FOR_EMPTY_QUEUE = 1000L;

    private static final long SLEEP_FOR_ERROR = 5000L;

    private static final int MAX_IN_FLIGHT_JOB = 2000;

    private static final int EVENT_BUS_THREAD_POOL_SIZE = 8;

    @Setter
    private String namespace;

    @Setter
    private String initWithQueue;

    @Setter
    private String batchSize = "10";

    @Setter
    private PooledRedisClient pooledRedisClient;

    private volatile State state = State.NEW;

    private AsyncEventBus asyncEventBus;

    private ExecutorService eventBusPool;

    @Override
    public void subscribe(String queue) {
        if (state == State.TERMINATED) {
            return;
        }
        logger.info("subscribe " + queue + " on namespace " + namespace);
        state = State.RUNNING;
        new Thread(new Consumer(queue)).start();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtils.isNotBlank(initWithQueue)) {
            this.subscribe(initWithQueue);
        }
    }

    @Override
    public void destroy() throws Exception {
        state = State.TERMINATED;
        eventBusPool.shutdown();
        if (!eventBusPool.awaitTermination(30, TimeUnit.SECONDS)) {
            logger.warn("force shutdown delay queue consumer eventBusPool");
            eventBusPool.shutdownNow();
        }
    }

    @SuppressWarnings("rawtypes")
    class Consumer implements Runnable {

        private String queue;

        private Map<String, Class> classCache = new HashMap<>();

        public Consumer(String queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            Thread.currentThread().setName("delay-queue-consumer-thread-" + namespace + "-" + queue);
            String key = DQUtil.buildKey(namespace, queue);
            while (state == State.RUNNING) {
                try {
                    String now = String.valueOf(Instant.now().toEpochMilli());
                    List jobs = null;
                    try {
                        jobs = (List) pooledRedisClient.executeWithRetry(jedis -> jedis.eval(POP_SCRIPT, 1, key, now, batchSize));
                    } catch (Exception e) {
                        logger.error("redis error, namespace:{}, queue:{}", namespace, queue, e);
                    }
                    if (CollectionUtils.isEmpty(jobs)) {
                        Thread.sleep(SLEEP_FOR_EMPTY_QUEUE);
                        continue;
                    }
                    for (Object o : jobs) {
                        DelayJob job = JSON.parseObject(o.toString(), DelayJob.class);
                        Object jobEntity = JSON.toJavaObject((JSON) job.getEntity(), getClass(job.getClassName()));
                        logger.info("receive delayed job:{}", jobEntity);
                        asyncEventBus.post(jobEntity);
                    }
                } catch (Throwable e) {
                    logger.error("unexpected consume error, namespace:{}, queue:{}", namespace, queue, e);
                    try {
                        Thread.sleep(SLEEP_FOR_ERROR);
                    } catch (InterruptedException ignore) {
                    }
                }
            }
            logger.info("consumer terminated, namespace:{}, queue:{}", namespace, queue);
        }

        private Class getClass(String className) throws ClassNotFoundException {
            Class clazz = classCache.get(className);
            if (clazz == null) {
                clazz = Class.forName(className);
                classCache.put(className, clazz);
            }
            return clazz;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // AsyncEventBus内部使用无界队列存储事件，无法在post阶段限流，只能在消费时丢弃，目前没有溢出风险，后续考虑更换为mbassador
        eventBusPool = new ThreadPoolExecutor(EVENT_BUS_THREAD_POOL_SIZE, EVENT_BUS_THREAD_POOL_SIZE, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(MAX_IN_FLIGHT_JOB),
                new BasicThreadFactory.Builder().namingPattern("event-bus-delay-queue-consumer-%d").build(),
                (r, executor) -> logger.error("delay task discarded due to overflow"));
        asyncEventBus = new AsyncEventBus(eventBusPool, (exception, context) -> logger.error("async event process failed with subscriber: {} method: {} event: {}",
                context.getSubscriber().getClass().getSimpleName(), context.getSubscriberMethod().getName(), context.getEvent(), exception));

        applicationContext.getBeansWithAnnotation(DelayJobProcessor.class).values().forEach(bean -> {
            logger.info("register delay queue subscriber: {}", bean.getClass().getSimpleName());
            asyncEventBus.register(bean);
        });
    }

    public enum State {
        NEW, RUNNING, TERMINATED
    }
}
