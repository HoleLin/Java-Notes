package com.holelin.sundry.utils;

import cn.hutool.core.collection.CollectionUtil;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Map排序工具类
 *
 * @author liuwangyanghdu@163.com  明明如月
 */
public class MapSortUtil {

    private static final Integer EMPTY_COLLECTION_SIZE = 0;
    /**
     * 按键升序比较器
     */
    private static Comparator<Map.Entry> comparatorByKeyAsc = (Map.Entry o1, Map.Entry o2) -> {
        if (o1.getKey() instanceof Comparable) {
            return ((Comparable) o1.getKey()).compareTo(o2.getKey());
        }
        throw new UnsupportedOperationException("键的类型尚未实现Comparable接口");
    };

    /**
     * 按键降序比较器
     */
    private static Comparator<Map.Entry> comparatorByKeyDesc = (Map.Entry o1, Map.Entry o2) -> {
        if (o1.getKey() instanceof Comparable) {
            return ((Comparable) o2.getKey()).compareTo(o1.getKey());
        }
        throw new UnsupportedOperationException("键的类型尚未实现Comparable接口");
    };

    /**
     * 按值升序比较器
     */
    private static Comparator<Map.Entry> comparatorByValueAsc = (Map.Entry o1, Map.Entry o2) -> {
        if (o1.getValue() instanceof Comparable) {
            return ((Comparable) o1.getValue()).compareTo(o2.getValue());
        }
        throw new UnsupportedOperationException("值的类型尚未实现Comparable接口");
    };

    /**
     * 按值降序比较器
     */
    private static Comparator<Map.Entry> comparatorByValueDesc = (Map.Entry o1, Map.Entry o2) -> {
        if (o1.getValue() instanceof Comparable) {
            return ((Comparable) o2.getValue()).compareTo(o1.getValue());
        }
        throw new UnsupportedOperationException("值的类型尚未实现Comparable接口");
    };

    /**
     * 按键升序排列
     */
    public static <K, V> Map<K, V> sortByKeyAsc(Map<K, V> originMap) {
        if (CollectionUtil.isEmpty(originMap)) {
            return new HashMap<>(EMPTY_COLLECTION_SIZE);
        }
        return sort(originMap, comparatorByKeyAsc);
    }

    /**
     * 按键降序排列
     */
    public static <K, V> Map<K, V> sortByKeyDesc(Map<K, V> originMap) {
        if (CollectionUtil.isEmpty(originMap)) {
            return new HashMap<>(EMPTY_COLLECTION_SIZE);
        }
        return sort(originMap, comparatorByKeyDesc);
    }


    /**
     * 按值升序排列
     */
    public static <K, V> Map<K, V> sortByValueAsc(Map<K, V> originMap) {
        if (CollectionUtil.isEmpty(originMap)) {
            return new HashMap<>(EMPTY_COLLECTION_SIZE);
        }
        return sort(originMap, comparatorByValueAsc);
    }

    /**
     * 按值降序排列
     */
    public static <K, V> Map<K, V> sortByValueDesc(Map<K, V> originMap) {
        if (CollectionUtil.isEmpty(originMap)) {
            return new HashMap<>(EMPTY_COLLECTION_SIZE);
        }
        return sort(originMap, comparatorByValueDesc);
    }

    private static <K, V> Map<K, V> sort(Map<K, V> originMap, Comparator<Map.Entry> comparator) {
        return originMap.entrySet()
                .stream()
                .sorted(comparator)
                .collect(
                        Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));
    }

}