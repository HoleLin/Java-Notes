package com.holelin.sundry.demo;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class CountDownLatchTest {

    public static final int N = 5;

    public static void main(String[] args) {
//        scenes1();
        scenes2();
    }

    private static void scenes1() {
        CountDownLatch latch = new CountDownLatch(1);
        for (int i = 0; i < N; i++) {
            new Thread(() -> {
                try {
                    latch.await();
                    log.info("[{}] 开始执行", Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        latch.countDown();
    }

    private static void scenes2() {
        CountDownLatch latch = new CountDownLatch(5);
        for (int i = 0; i < N; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    Thread.sleep(1000 + ThreadLocalRandom.current().nextInt(1000));
                    log.info("Finish {},[{}]", finalI, Thread.currentThread().getName());
                    latch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("主线程:在所有任务运行完成后，进行结果汇总");
    }
}
