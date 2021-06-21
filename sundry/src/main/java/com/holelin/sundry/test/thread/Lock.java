package com.holelin.sundry.test.thread;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Lock {
    private boolean isLocked = false;

    public synchronized void lock() throws InterruptedException {
        while (isLocked) {
            wait();
            log.info("被唤醒了,isLocked:{}", isLocked);
        }
        isLocked = true;
    }

    public synchronized void unlock() {
        isLocked = false;
        log.info("notify前唤醒isLocked:{}", isLocked);
        notify();
        log.info("notify后唤醒isLocked:{}", isLocked);

    }
}
