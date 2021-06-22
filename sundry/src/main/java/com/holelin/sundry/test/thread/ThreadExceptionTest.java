package com.holelin.sundry.test.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
public class ThreadExceptionTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(1);
        Future<Boolean> task = pool.submit(() -> {
            log.info("task1");
            int i = 1 / 0;
            return true;
        });
        log.info("result:{}", task.get());
    }
}
