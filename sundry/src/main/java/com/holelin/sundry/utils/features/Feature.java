package com.holelin.sundry.utils.features;

/**
 * @Description: 一些框架的特性组合，以及开发中业务的某个字段是多个特征组合，
 * 如果直接用数字，组合较多保存非常复杂。
 * 参考: https://blog.csdn.net/w605283073/article/details/96463248
 * @CreateDate: 2020/7/27 17:58
 * @UpdateDate: 2020/7/27 17:58
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */


public interface Feature {

    /**
     * 获取特性（掩码）
     */
    int getMask();

    /**
     * 所有特性
     */
    Feature[] listAll();
}