package com.holelin.sundry.test.jvm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.management.GarbageCollectionNotificationInfo;
import com.sun.management.GcInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.management.ListenerNotFoundException;
import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * GC 日志监听并输出到 Log
 * JVM 启动参数示例:
 * -Xmx4g -Xms4g -XX:+UseG1GC -XX:MaxGCPauseMillis=50
 * -Xloggc:gc.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps
 */
@Configuration
public class BindGCNotifyConfig {

    public BindGCNotifyConfig() {
    }

    //
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AtomicBoolean inited = new AtomicBoolean(Boolean.FALSE);
    private final List<Runnable> notifyCleanTasks = new CopyOnWriteArrayList<Runnable>();
    private final AtomicLong maxPauseMillis = new AtomicLong(0L);
    private final AtomicLong maxOldSize = new AtomicLong(getOldGen().getUsage().getMax());
    private final AtomicLong youngGenSizeAfter = new AtomicLong(0L);

    @PostConstruct
    public void init() {
        try {
            doInit();
        } catch (Throwable e) {
            logger.warn("[GC 日志监听-初始化]失败! ", e);
        }
    }

    @PreDestroy
    public void close() {
        for (Runnable task : notifyCleanTasks) {
            task.run();
        }
        notifyCleanTasks.clear();
    }

    private void doInit() {
        //
        if (!inited.compareAndSet(Boolean.FALSE, Boolean.TRUE)) {
            return;
        }
        logger.info("[GC 日志监听-初始化]maxOldSize=" + mb(maxOldSize.longValue()));

        // 每个 mbean 都注册监听
        for (GarbageCollectorMXBean mbean : ManagementFactory.getGarbageCollectorMXBeans()) {
            if (!(mbean instanceof NotificationEmitter)) {
                continue;
            }
            final NotificationEmitter notificationEmitter = (NotificationEmitter) mbean;
            // 添加监听
            final NotificationListener notificationListener = getNewListener(mbean);
            notificationEmitter.addNotificationListener(notificationListener, null, null);

            logger.info("[GC 日志监听-初始化]MemoryPoolNames=" + JSON.toJSONString(mbean.getMemoryPoolNames()));
            // 加入清理队列
            notifyCleanTasks.add(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 清理掉绑定的 listener
                        notificationEmitter.removeNotificationListener(notificationListener);
                    } catch (ListenerNotFoundException e) {
                        logger.error("[GC 日志监听-清理]清理绑定的 listener 失败", e);
                    }
                }
            });
        }
    }

    private NotificationListener getNewListener(final GarbageCollectorMXBean mbean) {
        //
        final NotificationListener listener = new NotificationListener() {
            @Override
            public void handleNotification(Notification notification, Object ref) {
                // 只处理 GC 事件
                if (!notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    return;
                }
                CompositeData cd = (CompositeData) notification.getUserData();
                GarbageCollectionNotificationInfo notificationInfo = GarbageCollectionNotificationInfo.from(cd);
                //
                JSONObject gcDetail = new JSONObject();

                String gcName = notificationInfo.getGcName();
                String gcAction = notificationInfo.getGcAction();
                String gcCause = notificationInfo.getGcCause();
                GcInfo gcInfo = notificationInfo.getGcInfo();
                // duration 是指 Pause 阶段的总停顿时间，并发阶段没有 pause 不会通知。
                long duration = gcInfo.getDuration();
                if (maxPauseMillis.longValue() < duration) {
                    maxPauseMillis.set(duration);
                }
                long gcId = gcInfo.getId();
                //
                String type = "jvm.gc.pause";
                //
                if (isConcurrentPhase(gcCause)) {
                    type = "jvm.gc.concurrent.phase.time";
                }
                //
                gcDetail.put("gcName", gcName);
                gcDetail.put("gcAction", gcAction);
                gcDetail.put("gcCause", gcCause);
                gcDetail.put("gcId", gcId);
                gcDetail.put("duration", duration);
                gcDetail.put("maxPauseMillis", maxPauseMillis);
                gcDetail.put("type", type);
                gcDetail.put("collectionCount", mbean.getCollectionCount());
                gcDetail.put("collectionTime", mbean.getCollectionTime());

                // 存活数据量
                AtomicLong liveDataSize = new AtomicLong(0L);
                // 提升数据量
                AtomicLong promotedBytes = new AtomicLong(0L);

                // Update promotion and allocation counters
                final Map<String, MemoryUsage> before = gcInfo.getMemoryUsageBeforeGc();
                final Map<String, MemoryUsage> after = gcInfo.getMemoryUsageAfterGc();
                //
                Set<String> keySet = new HashSet<String>();
                keySet.addAll(before.keySet());
                keySet.addAll(after.keySet());
                //
                final Map<String, String> afterUsage = new HashMap<String, String>();
                //
                for (String key : keySet) {
                    final long usedBefore = before.get(key).getUsed();
                    final long usedAfter = after.get(key).getUsed();
                    long delta = usedAfter - usedBefore;
                    // 判断是 yong 还是 old，算法不同
                    if (isYoungGenPool(key)) {
                        delta = usedBefore - youngGenSizeAfter.get();
                        youngGenSizeAfter.set(usedAfter);
                    } else if (isOldGenPool(key)) {
                        if (delta > 0L) {
                            // 提升到老年代的量
                            promotedBytes.addAndGet(delta);
                            gcDetail.put("promotedBytes", mb(promotedBytes));
                        }
                        if (delta < 0L || GcGenerationAge.OLD.contains(gcName)) {
                            liveDataSize.set(usedAfter);
                            gcDetail.put("liveDataSize", mb(liveDataSize));
                            final long oldMaxAfter = after.get(key).getMax();
                            if (maxOldSize.longValue() != oldMaxAfter) {
                                maxOldSize.set(oldMaxAfter);
                                // 扩容；老年代的 max 有变更
                                gcDetail.put("maxOldSize", mb(maxOldSize));
                            }
                        }
                    } else if (delta > 0L) {
                        //
                    } else if (delta < 0L) {
                        // 判断 G1
                    }
                    afterUsage.put(key, mb(usedAfter));
                }
                //
                gcDetail.put("afterUsage", afterUsage);
                //

                logger.info("[GC 日志监听-GC 事件]gcId={}; duration:{}; gcDetail: {}", gcId, duration, gcDetail.toJSONString());
            }
        };

        return listener;
    }

    private static String mb(Number num) {
        long mbValue = num.longValue() / (1024 * 1024);
        if (mbValue < 1) {
            return "" + mbValue;
        }
        return mbValue + "MB";
    }

    private static MemoryPoolMXBean getOldGen() {
        List<MemoryPoolMXBean> list = ManagementFactory
                .getPlatformMXBeans(MemoryPoolMXBean.class);
        //
        for (MemoryPoolMXBean memoryPoolMXBean : list) {
            // 非堆的部分-不是老年代
            if (!isHeap(memoryPoolMXBean)) {
                continue;
            }
            if (!isOldGenPool(memoryPoolMXBean.getName())) {
                continue;
            }
            return (memoryPoolMXBean);
        }
        return null;
    }

    private static boolean isConcurrentPhase(String cause) {
        return "No GC".equals(cause);
    }

    private static boolean isYoungGenPool(String name) {
        return name.endsWith("Eden Space");
    }

    private static boolean isOldGenPool(String name) {
        return name.endsWith("Old Gen") || name.endsWith("Tenured Gen");
    }

    private static boolean isHeap(MemoryPoolMXBean memoryPoolBean) {
        return MemoryType.HEAP.equals(memoryPoolBean.getType());
    }

    private enum GcGenerationAge {
        OLD,
        YOUNG,
        UNKNOWN;

        private static Map<String, GcGenerationAge> knownCollectors = new HashMap<String, BindGCNotifyConfig.GcGenerationAge>() {{
            put("ConcurrentMarkSweep", OLD);
            put("Copy", YOUNG);
            put("G1 Old Generation", OLD);
            put("G1 Young Generation", YOUNG);
            put("MarkSweepCompact", OLD);
            put("PS MarkSweep", OLD);
            put("PS Scavenge", YOUNG);
            put("ParNew", YOUNG);
        }};

        static GcGenerationAge fromName(String name) {
            return knownCollectors.getOrDefault(name, UNKNOWN);
        }

        public boolean contains(String name) {
            return this == fromName(name);
        }
    }

}
