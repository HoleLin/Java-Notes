package com.holelin.sundry.demo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExceptionTest {
    public static void main(String[] args) {
        System.out.println(testReturnAndFinally());
    }

    public static int testReturnAndFinally() {
        int x = 1;
        try {
            int temp = x / 0;
            log.info("出现异常");
            return ++x;
        } catch (Exception e) {
            System.exit(1);
            log.info("捕获异常");
        } finally {
            log.info("finally..");
            ++x;
        }
        log.info("返回..");
        return ++x;
    }
}

