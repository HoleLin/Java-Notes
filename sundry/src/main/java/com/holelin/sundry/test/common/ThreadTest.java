package com.holelin.sundry.test.common;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@Slf4j
public class ThreadTest {
    static int num = 0;

    @Test
    public void test() throws InterruptedException {
        log.info("开始");
        Thread t1 = new Thread(() -> {
            log.info("开始");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("结束");
            num = 20;
        }, "t1");
        t1.start();
        t1.join();
        log.info("结果为:{}", num);
        log.info("结束");
        log.info("打断状态:{}", t1.isInterrupted());
    }

    @Test
    public void testInterrupt() {
        Thread t = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t");
        t.start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t.interrupt();
        log.info("打断状态:{}", t.isInterrupted());
    }

    @Test
    public void testInterrupt2() {
        Thread t = new Thread(() -> {

        }, "t");
        t.start();
        t.interrupt();
        log.info("打断状态:{}", t.isInterrupted());
    }

    @Test
    public void testPark() {
        Thread t = new Thread(() -> {
            log.info("park...");
            LockSupport.park();
            log.info("打断状态:{}", Thread.currentThread().isInterrupted());
        }, "t");
        t.start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t.interrupt();
    }

    @Test
    public void testPark2() {
        Thread t = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                log.info("park...");
                LockSupport.park();
                log.info("打断状态:{}", Thread.currentThread().isInterrupted());
            }
        }, "t");
        t.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t.interrupt();
    }

    @Test
    public void testSleep() {
        while (true) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testTwoPhaseTermination() {
        Thread t = new Thread(() -> {
            while (true) {
                Thread currentThread = Thread.currentThread();
                if (currentThread.isInterrupted()) {
                    log.info("料理后事");
                    break;
                }
                try {
                    Thread.sleep(1000);
                    log.info("保存结果");
                } catch (InterruptedException e) {
                    currentThread.interrupt();
                }
            }
        });
        t.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("准备终止线程");
        t.interrupt();
    }

    private volatile boolean isStop = false;

    @Test
    public void testTwoPhaseTermination2() {
        Thread t = new Thread(() -> {
            while (true) {
                Thread currentThread = Thread.currentThread();
                if (isStop) {
                    log.info("料理后事");
                    break;
                }
                try {
                    Thread.sleep(1000);
                    log.info("保存结果");
                } catch (InterruptedException e) {
                    currentThread.interrupt();
                }
            }
        });
        t.start();
        t.setDaemon(true);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("准备终止线程");
        isStop = true;
        t.interrupt();
    }

    @Test
    public void testTwoPhaseTermination3() {
        List<String> list = Lists.newArrayList();
        for (int i = 0; i < 2; i++) {
            list.add(UUID.randomUUID().toString());
        }
        CountDownLatch latch = new CountDownLatch(list.size());
        log.info("任务开始");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
//        try {
            list.parallelStream().forEach(item -> {
                String lower;
                try {
                    lower = toLowerCase(item).get(0);
                    log.info("lower:{}", lower);
                    int i = 1 / 0;
                } catch (Exception e) {
                    log.info("内部");
                    throw e;
                } finally {
                    latch.countDown();
                }

            });
//        } catch (Exception e) {
//            log.info("外部");
//            e.printStackTrace();
//        } finally {
//            latch.countDown();
//        }

        try {
            latch.await();
            log.info("任务结束");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        stopWatch.stop();
        log.info("耗时:{}s", stopWatch.getTotalTimeSeconds());
    }

    public static List<String> toLowerCase(String item) {
        List<String> result = new ArrayList<>();
        result.add(item.toLowerCase());
        return result;
    }
}
