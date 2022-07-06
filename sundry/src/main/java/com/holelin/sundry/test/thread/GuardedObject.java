package com.holelin.sundry.test.thread;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.locks.LockSupport;

@Slf4j
public class











GuardedObject {
    private Object response;
    private Object lock = new Object();

    public Object get() {
        synchronized (lock) {
            // 条件不满足则等待
            while (response == null) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return response;
        }
    }

    public void complete(Object response) {
        synchronized (lock) {
            // 条件满足,通知等待线程
            this.response = response;
            lock.notifyAll();
        }
    }

    public static void main(String[] args) {
        GuardedObject guardedObject = new GuardedObject();
        Thread t1 = new Thread(() -> {
            List<String> response = download();
            guardedObject.complete(response);
        });
        t1.start();
        // 主线程阻塞等待
        Object response = guardedObject.get();
        log.info("get response:[{}] lines", ((List<String>) response).size());

    }

    public static List<String> download() {

        List<String> list = Lists.newArrayList();
        list.add("GuardedObject");
        try {
            log.info("等待两秒");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("获取数据");
        return list;
    }
}
