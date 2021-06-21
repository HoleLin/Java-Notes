package com.holelin.sundry.test.thread;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class AlternateOutputTest {
    public static void main(String[] args) {
//        waitAndNotify();
//        awaitSignal();
        syncPark();
    }

    public static void waitAndNotify() {
        SyncWaitNotify syncWaitNotify = new SyncWaitNotify(1, 5);
        new Thread(() -> {
            syncWaitNotify.print(1, 2, "a");
        }).start();
        new Thread(() -> {
            syncWaitNotify.print(2, 3, "b");
        }).start();
        new Thread(() -> {
            syncWaitNotify.print(3, 1, "c");
        }).start();
    }

    public static void awaitSignal() {
        AwaitSignal as = new AwaitSignal(5);
        Condition aWaitSet = as.newCondition();
        Condition bWaitSet = as.newCondition();
        Condition cWaitSet = as.newCondition();
        new Thread(() -> {
            as.print(aWaitSet, bWaitSet, "a");
        }).start();
        new Thread(() -> {
            as.print(bWaitSet, cWaitSet, "b");
        }).start();
        new Thread(() -> {
            as.print(cWaitSet, aWaitSet, "c");
        }).start();
        as.start(aWaitSet);

    }

    public static void syncPark() {
        SyncPark syncPark = new SyncPark(5);
        Thread t1 = new Thread(() -> {
            syncPark.print("a");
        });
        Thread t2 = new Thread(() -> {
            syncPark.print("b");
        });
        Thread t3 = new Thread(() -> {
            syncPark.print("c");
        });
        syncPark.setThreads(t1, t2, t3);
        syncPark.start();
    }

    static class SyncWaitNotify {
        private int flag;
        private int loopNumber;

        public SyncWaitNotify(int flag, int loopNumber) {
            this.flag = flag;
            this.loopNumber = loopNumber;
        }

        private void print(int waitFlag, int nextFlag, String str) {
            for (int i = 0; i < loopNumber; i++) {
                synchronized (this) {
                    while (this.flag != waitFlag) {
                        try {
                            this.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    log.info(str);
                    flag = nextFlag;
                    this.notifyAll();
                }
            }
        }
    }

    static class AwaitSignal extends ReentrantLock {
        private int loopNumber;

        public AwaitSignal(int loopNumber) {
            this.loopNumber = loopNumber;
        }

        public void start(Condition first) {
            this.lock();
            try {
                log.info("start");
                first.signal();
            } finally {
                this.unlock();
            }
        }

        public void print(Condition current, Condition next, String str) {
            for (int i = 0; i < loopNumber; i++) {
                this.lock();
                try {
                    current.await();
                    log.info(str);
                    next.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    this.unlock();
                }
            }
        }

    }

    static class SyncPark {
        private int loopNumber;
        private Thread[] threads;

        public SyncPark(int loopNumber) {
            this.loopNumber = loopNumber;
        }

        public void setThreads(Thread... threads) {
            this.threads = threads;
        }

        public void print(String str) {
            for (int i = 0; i < loopNumber; i++) {
                LockSupport.park();
                log.info(str);
                LockSupport.unpark(nextThread());
            }
        }

        private Thread nextThread() {
            Thread currentThread = Thread.currentThread();
            int index = 0;
            for (int i = 0; i < threads.length; i++) {
                if (threads[i] == currentThread) {
                    index = i;
                    break;
                }
            }
            if (index < threads.length - 1) {
                return threads[index + 1];
            } else {
                return threads[0];
            }
        }

        public void start() {
            for (Thread thread : threads) {
                thread.start();
            }
            LockSupport.unpark(threads[0]);
        }
    }
}
