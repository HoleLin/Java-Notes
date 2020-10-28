package com.holelin.sundry;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

@Slf4j
public class AsyncTest {

    @Test
    public void asyncByFuture(){
        ExecutorService executor = Executors.newFixedThreadPool(2);
        //jdk1.8之前的实现方式
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            log.info("task started!");
            //模拟耗时操作
            try {
                log.info("耗时操作中.......");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "task finished!";
        }, executor);

        //采用lambada的实现方式
        future.thenAccept(e -> log.info(e + " ok"));
        log.info("main thread is running");
    }
}
