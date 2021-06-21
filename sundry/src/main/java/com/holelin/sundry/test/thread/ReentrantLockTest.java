package com.holelin.sundry.test.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ReentrantLockTest {
    private static ReentrantLock reentrantLock = new ReentrantLock();
    private static Condition waitCigaretteQueue = reentrantLock.newCondition();
    private static Condition waitBreakfastQueue = reentrantLock.newCondition();
    private static volatile boolean hasCigarette = false;
    private static volatile boolean hasBreakfast = false;

    public static void main(String[] args) {
        new Thread(() -> {
            reentrantLock.lock();
            try {
                while (!hasCigarette) {
                    try {
                        waitCigaretteQueue.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.info("获取到烟");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 释放锁
                reentrantLock.unlock();
            }
        }).start();

        new Thread(() -> {
            reentrantLock.lock();
            try {
                while (!hasBreakfast) {
                    try {
                        waitBreakfastQueue.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.info("获取到早饭");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 释放锁
                reentrantLock.unlock();
            }
        }).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendBreakfast();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendCigarette();
    }

    private static void sendBreakfast() {
        reentrantLock.lock();
        try {
            log.info("送早餐");
            hasBreakfast = true;
            waitBreakfastQueue.signal();
        } finally {
            reentrantLock.unlock();
        }
    }

    private static void sendCigarette() {
        reentrantLock.lock();
        try {
            log.info("送烟");
            hasCigarette = true;
            waitCigaretteQueue.signal();
        } finally {
            reentrantLock.unlock();
        }
    }
}
