package com.holelin.sundry.test.thread;


import java.text.SimpleDateFormat;
import java.util.Date;

public class Concurrent {

    static boolean flag = true;
    static Object lock = new Object();

    public static void main(String[] args) {
        Thread waitThread = new Thread(new Wait(), "Wait Thread");
        waitThread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Thread notifyThread = new Thread(new Notify(), "Notify Thread");
        notifyThread.start();
    }

    public static String getData() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    static class Wait implements Runnable {
        @Override
        public void run() {
            synchronized (lock) {
                while (flag) {
                    try {
                        System.out.println(Thread.currentThread() + " flag is true,wait @ " + getData());
                        lock.wait();
                    } catch (InterruptedException e) {
                    }
                }
                System.out.println(Thread.currentThread() + " flag is false. running @ " + getData());
            }
        }
    }

    static class Notify implements Runnable {
        @Override
        public void run() {
            synchronized (lock) {
                System.out.println(Thread.currentThread() + " hold lock. notify @ " + getData());
                lock.notifyAll();
                flag = false;
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            synchronized (lock) {
                System.out.println(Thread.currentThread() + " hold lock again. sleep @ " + getData());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

