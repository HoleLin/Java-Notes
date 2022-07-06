package com.holelin.sundry.test;

import cn.hutool.core.collection.CollUtil;
import com.holelin.sundry.domain.MeetingNotice;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
class MeetingNoticeTest {

    @Test
    public void testDelayQueue() {
        final MeetingNotice notice1 = new MeetingNotice("晨会", 1L, 5, TimeUnit.SECONDS);
        final MeetingNotice notice2 = new MeetingNotice("晨会1", 2L, 10, TimeUnit.SECONDS);
        final MeetingNotice notice3 = new MeetingNotice("晨会2", 3L, 15, TimeUnit.SECONDS);

        final DelayQueue<MeetingNotice> meetingNotices = new DelayQueue<>();
        meetingNotices.add(notice1);
        meetingNotices.add(notice2);
        meetingNotices.add(notice3);
        while (CollUtil.isNotEmpty(meetingNotices)) {
            final MeetingNotice notice = meetingNotices.poll();
            if (Objects.nonNull(notice)) {
                log.info("会议:{},还有15分钟中开始", notice.getTitle());
            }
        }
    }
}