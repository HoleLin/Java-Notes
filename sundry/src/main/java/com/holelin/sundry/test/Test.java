package com.holelin.sundry.test;

import com.google.common.collect.Sets;

import java.util.Set;

public class Test {

    public static final Set<Long> sets = Sets.newConcurrentHashSet();

    @org.junit.Test
    public void test() throws InterruptedException {
        SnowFlake snowFlake = new SnowFlake(2, 3);
        for (int i = 0; i < 10; i++) {
            Thread t1 = new Thread(() -> {
                for (int j = 0; j < 1000000; j++) {
                    sets.add(snowFlake.nextId());
                }
            });
            t1.start();
            t1.join();
        }
        System.out.println(sets.size());
    }

    @org.junit.Test
    public void test1() {
        final Holder holder = new Holder(9);
    }
}

class Holder {
    private int n;

    public Holder(int n) {
        this.n = n;
    }

    public void assertSanity() {
        if (n != n) {
            throw new AssertionError("This statement is false");
        }
    }
}
