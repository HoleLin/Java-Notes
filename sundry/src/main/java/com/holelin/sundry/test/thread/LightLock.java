package com.holelin.sundry.test.thread;

public class LightLock {
    public static final Object obj = new Object();

    public static void m1() {
        synchronized (obj) {
            m2();
        }
    }

    public static void m2() {
        synchronized (obj) {
            // 临界区
        }
    }
}
