package com.holelin.sundry.test.thread;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BalkingTest {
    static class MonitorService {
        private volatile boolean starting;

        public void start() {
            log.info("尝试启动监控线程....");
            synchronized (this) {
                if (starting) {
                    return;
                }
                starting = true;
            }
            // 真正启动监控线程
        }
    }
    static class Singleton{
        private Singleton() {
        }
        private static Singleton INSTANCE=null;

    }
}
