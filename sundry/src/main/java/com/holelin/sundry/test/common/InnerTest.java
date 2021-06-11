package com.holelin.sundry.test.common;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;

public class InnerTest {
    public static void main(String[] args) {
//        External external = new External();
//        external.externalMethod();
//        external.repeatMethod();
//        // 方法一: 创建内部类 `外部类实例.new 内部类()`
//        External.Inner inner1 = external.new Inner();
//        // 方法二: 通过外部类间接创建内部类
//        External.Inner inner2 = external.getInner();
//
//        new AnonymousInner(){
//            @Override
//            public void doSomething() {
//
//            }
//        };
//        new External(){
//            @Override
//            public void externalMethod() {
//
//            }
//        };
        External.Inner inner = new External.Inner();
        inner.innerMethod();
        External external = new External();

    }
}

// 外部类
//@Slf4j
//class External {
//    private int age = 20;
//    private String name = "External";
//    private Date time;
//
//    public void externalMethod() {
//        log.info("外部类的方法externalMethod");
//        Inner inner = new Inner();
//        log.info("内部类的属性age: {}", inner.innerAge);
//        log.info("内部类的属性name: {}", inner.innerName);
//        inner.innerMethod();
//    }
//
//    public void repeatMethod() {
//        log.info("外部类的方法repeatMethod");
//    }
//
//    public Inner getInner() {
//        return new Inner();
//    }
//
//    // 内部类
//    class Inner {
//        private int innerAge = 10;
//        private String innerName = "Inner";
//        private Date time;
//
//        public void innerMethod() {
//            // 无条件获取外部类的属性和方法
//            log.info("内部类的方法innerMethod");
//            log.info("外部类的属性age: {}", age);
//            log.info("外部类的属性name: {}", name);
//        }
//
//        public void innerMethod2() {
//            log.info("内部类的方法innerMethod2");
//            externalMethod();
//        }
//
//        public void repeatMethod() {
//            log.info("内部类的方法repeatMethod");
//            // 内部类和外部类方法同名 需要按照`外部类.this.同名方法名称`
//            External.this.repeatMethod();
//            // 内部类和外部类方法同名 需要按照`外部类.this.属性名称`
//            Date tempTime = External.this.time;
//        }
//    }
//}


interface AnonymousInner {
    void doSomething();
}

@Slf4j
class External {
    private static int age = 10;
    private String name = "name";

    public void externalMethod() {
        log.info("外部类的方法externalMethod");
    }

    public static void externalStaticMethod() {
        log.info("外部类的方法externalMethod");
    }

    static class Inner {
        public void innerMethod() {
            log.info("外部类私有静态成员变量: {}", age);
            externalStaticMethod();
            // 报错,静态内部类无法引用非外部类的非静态属性/方法
//            log.info("外部类私有成员变量: {}", name);
//            externalMethod();

        }
    }
}