package com.holelin.sundry.test.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ThreadPool {
    /**
     * 任务队列
     */
    private BlockingQueue<Runnable> taskQueue;
    /**
     * 线程集合
     */
    private HashSet<Worker> workers = new HashSet<>();
    /**
     * 核心线程数
     */
    private int coreSize;
    /**
     * 获取任务时的超时时间
     */
    private long timeout;

    private TimeUnit timeUnit;

    private RejectPolicy<Runnable> rejectPolicy;

    public void execute(Runnable task) {
        synchronized (workers) {
            if (workers.size() < coreSize) {
                Worker worker = new Worker(task);
                log.info("新增worker{},{}", worker, task);
                workers.add(worker);
                worker.start();
            } else {
//                taskQueue.put(task);
                // 1. 死等
                // 2. 带超时时间等待
                // 3. 让调用者放弃任务执行
                // 4. 让调用者抛出异常
                // 5. 让调用者自己执行任务
                taskQueue.tryPut(rejectPolicy, task);
            }
        }
    }

    public ThreadPool(int coreSize, long timeout, TimeUnit timeUnit, int queueCapacity, RejectPolicy<Runnable> rejectPolicy) {
        this.workers = workers;
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.taskQueue = new BlockingQueue<>(queueCapacity);
        this.rejectPolicy = rejectPolicy;
    }

    class Worker extends Thread {
        private Runnable task;

        public Worker(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            /**
             * 执行任务
             * 1. 当task不为空,执行任务
             * 2. 当task执行完毕,再接着从任务队列中获取任务并执行
             */
            while (Objects.nonNull(task) || Objects.nonNull(task = taskQueue.poll(timeout, timeUnit))) {
                try {
                    log.info("正在执行...{}", task);
                    task.run();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    task = null;
                }
            }
            synchronized (workers) {
                log.info("worker被移除{}", this);
                workers.remove(this);
            }
        }
    }
}
