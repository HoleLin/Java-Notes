package com.holelin.redis.utils;

import cn.hutool.core.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Description: Redis操作
 * @Author: HoleLin
 * @CreateDate: 2020/9/4 10:44
 * @UpdateUser: HoleLin
 * @UpdateDate: 2020/9/4 10:44
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 字典类型的操作
     */
    public HashOperations<String, Object, Object> hash;
    /**
     * 链表类型的操作
     */
    private ListOperations<String, Object> list;
    /**
     * 字符类型的操作
     */
    public ValueOperations<String, Object> operations;
    /**
     * 无序集合的操作
     */
    public SetOperations<String, Object> set;
    /**
     * 有序集合的操作
     */
    private ZSetOperations<String, Object> zSet;


    @PostConstruct
    private void initOperations() {
        StringRedisSerializer redisSerializer = new StringRedisSerializer();
        redisTemplate.setHashKeySerializer(redisSerializer);
        redisTemplate.setKeySerializer(redisSerializer);
        operations = redisTemplate.opsForValue();
        hash = redisTemplate.opsForHash();
        list = redisTemplate.opsForList();
        set = redisTemplate.opsForSet();
        zSet = redisTemplate.opsForZSet();
    }


    /**
     * 根据key获取值
     *
     * @param key must not be null
     */
    public Object get(final String key) {
        return operations.get(key);
    }

    /**
     * 存储值
     *
     * @param key   must not be null
     * @param value must not be null
     * @return 操作是否成功 true -- 成功  false -- 失败
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 存储值,并设置过期时间
     *
     * @param key      must not be null
     * @param value    must not be null
     * @param timeout  the key expiration timeout.
     * @param timeUnit the key expiration timeout Unit.
     * @return 操作是否成功 true -- 成功  false -- 失败
     */
    public boolean setKeyWithExpirationTime(final String key, Object value, long timeout, TimeUnit timeUnit) {
        boolean result = false;
        try {
            operations.set(key, value, timeout, TimeUnit.HOURS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 设置key的过期时间
     *
     * @param key      must not be null
     * @param timeout  they key expiration timeout
     * @param timeUnit the key expiration timeout Unit
     */
    public void expirationKey(final String key, long timeout, TimeUnit timeUnit) {
        redisTemplate.expire(key, timeout, timeUnit);
    }

    /**
     * 批量删除key
     *
     * @param keys 缓存key列表
     * @return 删除的条数
     */
    public Long removeBatchKeys(final Collection<String> keys) {
        return redisTemplate.delete(keys);
    }

    /**
     * 根据key删除缓存
     *
     * @param key must not be null
     * @return true -- 表示删除成功 false -- 表示删除失败
     */
    public boolean removeKey(final String key) {
        boolean result = false;
        if (exists(key)) {
            result = redisTemplate.delete(key);
        }
        return result;
    }

    /**
     * 根据正则表达式删除key
     *
     * @param pattern 正则表达式
     * @return 删除的条数
     */
    public Long removeKeysByPattern(final String pattern) {
        Long deleteRows = 0L;
        Set<String> keys = redisTemplate.keys(pattern);
        if (CollectionUtil.isNotEmpty(keys)) {
            deleteRows = redisTemplate.delete(keys);
        }
        return deleteRows;
    }

    /**
     * 判断缓存中是否有对应的值
     *
     * @param key must not be null
     * @return true -- 表示存在 false -- 表示不存在
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 存储Map
     *
     * @param key     键
     * @param hashKey Map的key
     * @param value   hashKey对应的值
     */
    public void hSet(final String key, Object hashKey, Object value) {
        hash.put(key, hashKey, value);
    }

    /**
     * 根据Key和hashKey获取值
     *
     * @param key     键
     * @param hashKey Map的key
     * @return hashKey对应的值
     */
    public Object hGet(final String key, Object hashKey) {
        return hash.get(key, hashKey);
    }

    /**
     * 指定哈希是否含有指定的域
     *
     * @param key     键
     * @param hashKey hashKey
     * @return true -- 存在 false -- 不存在
     */
    public boolean hExists(final String key, Object hashKey) {
        return hash.hasKey(key, hashKey);
    }

    /**
     * 列表缓存
     *
     * @param key   键
     * @param value 值
     */
    public void lPush(final String key, Object value) {
        list.rightPush(key, value);
    }

    /**
     * 根据key获取列表[left,right]区间的数据
     *
     * @param key   键
     * @param left  左边界
     * @param right 右边界
     * @return 区间数据
     */
    public List<Object> lRange(final String key, long left, long right) {
        return list.range(key, left, right);
    }

    /**
     * Set缓存
     *
     * @param key   键
     * @param value 值
     */
    public void sAdd(final String key, Object value) {
        set.add(key, value);
    }

    /**
     * 根据key获取值
     *
     * @param key 键
     * @return key对应的值
     */
    public Set<Object> setMembers(final String key) {
        return set.members(key);
    }

    /**
     * 有序集合缓存
     *
     * @param key   键
     * @param value 值
     * @param score 得分
     */
    public void zAdd(final String key, Object value, double score) {
        zSet.add(key, value, score);
    }

    /**
     * 清空数据
     */
    public void flushDB(){
        Set<String> keys = redisTemplate.keys("*");
        redisTemplate.delete(keys);
    }
}
