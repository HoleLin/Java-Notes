package com.holelin.sundry.utils.features;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FeatureUtil {
    /**
     * 此特性是否开启
     *
     * @param features 特性值
     * @param feature  某个特性
     * @return 是否开启
     */
    public static boolean isEnabled(int features, Feature feature) {
        return (features & feature.getMask()) != 0;
    }
 
    /**
     * 配置某个特性
     *
     * @param features 特性值
     * @param feature  某个特性
     * @param state    是否开启
     * @return
     */
    public static int config(int features, Feature feature, boolean state) {
        if (state) {
            features |= feature.getMask();
        } else {
            features &= ~feature.getMask();
        }
 
        return features;
    }
 
    /**
     * 开启某些特性的值
     *
     * @param features 特性数组
     * @return 特性值
     */
    public static int of(Feature... features) {
        if (features == null) {
            return 0;
        }
        return of(Stream.of(features).collect(Collectors.toSet()));
    }
 
    /**
     * 开启某些特性的值
     *
     * @param features 特性数组
     * @return 特性值
     */
    public static int of(Set<Feature> features) {
        if (features == null) {
            return 0;
        }
 
        int value = 0;
 
        for (Feature feature : features) {
            value |= feature.getMask();
        }
 
        return value;
    }
 
    /**
     * 判断特性值包含哪些特性
     *
     * @param features     特性值
     * @param featureArray 特性数组
     * @return 包含的特性
     */
    public static Set<Feature> resolve(Integer features, Feature[] featureArray) {
        if (featureArray == null) {
            throw new IllegalArgumentException("特征数组不为null");
        }
        if (features == null || features == 0) {
            return new HashSet<>(0);
        }
 
        Set<Feature> featureSet = new HashSet<>();
        for (Feature feature : featureArray) {
            if ((features & feature.getMask()) != 0) {
                featureSet.add(feature);
            }
        }
        return featureSet;
    }
}
