package com.holelin.sundry.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class IllegalBean {

    /**
     * 不符合驼峰命名规范: 全大写
     */
    @JSONField(name = "NAME")
    private String NAME;
    /**
     * 不符合驼峰命名规范: 首字母大写
     */
    @JSONField(name = "DeviceId")
    private String DeviceId;

    private List<POJO> data;

    @Data
    public static class POJO {

        @JSONField(name = "ADDRESS")
        private String ADDRESS;
        @JSONField(name = "AGE")
        private String AGE;

    }
}
