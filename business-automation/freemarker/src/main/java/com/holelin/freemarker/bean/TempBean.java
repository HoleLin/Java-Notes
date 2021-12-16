package com.holelin.freemarker.bean;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TempBean {

    private String name;
    private Integer age;

}