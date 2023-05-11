package com.holelin.sundry.test;

public class JITDemo {
    Integer a = 1000;

    public void setA(Integer a) {
        this.a = a;
    }

    public Integer getA() {
        return this.a;
    }

    public Integer cal(int num) {
        synchronized (new Object()) {
            Integer a = getA();
            int b = a * 10;
            b = a * 100;
            return b + num;
        }
    }

    public int test() {
        synchronized (new Object()) {
            int total = 0;
            int count = 100_000_00;
            for (int i = 0; i < count; i++) {
                total += cal(i);
                if (i % 1000 == 0) {
                    System.out.println(i * 1000);
                }
            }
            return total;
        }
    }
    public static void main(String[] args) {
        JITDemo demo = new JITDemo();
        int total = demo.test();


    }
}
