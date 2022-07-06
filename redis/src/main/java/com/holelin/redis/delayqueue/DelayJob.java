package com.holelin.redis.delayqueue;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


@Data
public class DelayJob<T> implements Serializable {
    private static final long serialVersionUID = -2392357322522404381L;

    private String className;

    private T entity;

    private Map<String, Object> context;

    public DelayJob() {
    }

    public DelayJob(T entity) {
        this.entity = entity;
        this.className = entity.getClass().getName();
        this.context = new HashMap<>();
    }

    public DelayJob(T entity, Map<String, Object> context) {
        this.entity = entity;
        this.className = entity.getClass().getName();
        this.context = context;
    }
}