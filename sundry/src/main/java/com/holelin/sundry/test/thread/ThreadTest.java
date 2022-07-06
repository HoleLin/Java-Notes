package com.holelin.sundry.test.thread;

import org.junit.Test;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.CountDownLatch;

public class ThreadTest {
    public static void main(String[] args) {
        CountDownLatch latch1 = new CountDownLatch(100);
        CountDownLatch latch2 = new CountDownLatch(100);
        Lock lock = new Lock();
        new Thread(() -> {
            try {
                lock.lock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch2.countDown();
            try {
                latch2.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            latch1.countDown();
            try {
                latch1.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.unlock();

        }).start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReorder() {
        CountDownLatch latch1 = new CountDownLatch(100);
        CountDownLatch latch2 = new CountDownLatch(100);
        Lock lock = new Lock();
        new Thread(() -> {
            try {
                lock.lock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch2.countDown();
            try {
                latch2.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            latch1.countDown();
            try {
                latch1.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.unlock();

        }).start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
        final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        final ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);
        for (ThreadInfo threadInfo : threadInfos) {
            System.out.println("[" + threadInfo.getThreadId() + "]" + threadInfo.getThreadName());
        }
    }
}
