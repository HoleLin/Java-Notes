package com.holelin.sundry.test.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

@Slf4j
public class SequentialOutputTest {
    public static void main(String[] args) {
//        waitAndNotify();
        parkAndUnpark();
    }

    /**
     * 用来同步的对象
     */
    static Object object = new Object();
    /**
     * t2运行标志,表示t2是否执行过
     */
    static boolean t2Runned = false;

    public static void waitAndNotify() {
        Thread t1 = new Thread(() -> {
            synchronized (object) {
                while (t2Runned) {
                    try {
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            log.info("1");
        });
        Thread t2 = new Thread(() -> {
            log.info("2");
            synchronized (object) {
                // 修改运行标记
                t2Runned = true;
                object.notifyAll();
            }
        });
        t1.start();
        t2.start();
    }

    public static void parkAndUnpark() {
        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 当没有许可时,当前线程暂停运行,有许可时,用掉这个许可,当前线程恢复运行
            LockSupport.park();
            log.info("1");
        });
        Thread t2 = new Thread(() -> {
            log.info("2");
            // 给线程t1发放许可.(多次连续调用unpark,只会发放一个许可)
            LockSupport.unpark(t1);

        });
        t1.start();
        t2.start();
    }
}
