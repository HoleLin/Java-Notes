package com.holelin.sundry.domain;

import lombok.Data;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/7/25 11:35
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/7/25 11:35
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
public class MeetingNotice implements Delayed {

    private long noticeTime;

    private long meetingId;
    private String title;

    public MeetingNotice(String title, long meetingId, long noticeTime, TimeUnit unit) {
        this.title = title;
        this.meetingId = meetingId;
        this.noticeTime = System.currentTimeMillis() + (noticeTime > 0 ? unit.toMillis(noticeTime) : 0);
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return noticeTime - System.currentTimeMillis();
    }

    @Override
    public int compareTo(Delayed o) {
        MeetingNotice meetingNotice = (MeetingNotice) o;
        long diff = this.noticeTime - meetingNotice.noticeTime;
        if (diff <= 0) {
            return -1;
        } else {
            return 1;
        }
    }
}
