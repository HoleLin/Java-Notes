package com.holelin.sundry.test.jvm;

import org.openjdk.jol.info.ClassLayout;

public class JolTest {

    public static void main(String[] args) {
        System.out.println(ClassLayout.parseInstance(new A()).toPrintable());
        System.out.println(ClassLayout.parseInstance(new B()).toPrintable());
    }

}

class A {
    int a; // 4 字节
    byte b; // 1 字节
    Integer c = Integer.valueOf(0); // 4 字节的引用
}

class B { }