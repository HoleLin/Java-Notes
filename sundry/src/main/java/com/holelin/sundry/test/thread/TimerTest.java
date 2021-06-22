package com.holelin.sundry.test.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TimerTest {
    public static void main(String[] args) {
        useTimer();
//        useScheduledExecutorService();
    }

    private static void useScheduledExecutorService() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        // 添加两个任务,希望它们都在1s后执行
        executor.schedule(() -> {
            log.info("任务1,执行时间:{}", new Date());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 1000, TimeUnit.MILLISECONDS);
        executor.schedule(() -> {
            log.info("任务2,执行时间:{}", new Date());
        }, 1000, TimeUnit.MILLISECONDS);
    }

    private static void useTimer() {
        Timer timer = new Timer();
        TimerTask timerTask1 = new TimerTask() {
            @Override
            public void run() {
                log.info("task 1");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        TimerTask timerTask2 = new TimerTask() {
            @Override
            public void run() {
                log.info("task 2");
            }
        };
        // 使用timer添加两个任务,希望它们都是1s执行
        // 但由于timer内只有一个线程来顺序执行队列中任务,因此[任务1]的延迟,影响了[任务2]的执行
        timer.schedule(timerTask1, 1000);
        timer.schedule(timerTask2, 1000);
    }
}
