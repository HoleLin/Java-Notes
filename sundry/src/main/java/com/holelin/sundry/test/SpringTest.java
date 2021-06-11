package com.holelin.sundry.test;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.SimpleAliasRegistry;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2020/7/29 15:24
 * @UpdateUser: HoleLin
 * @UpdateDate: 2020/7/29 15:24
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

public class SpringTest {
    @Test
    public void test() {
        SimpleAliasRegistry aliasRegistry = new SimpleAliasRegistry();
        aliasRegistry.registerAlias("beanA", "beanA_alias1");
        aliasRegistry.registerAlias("beanA_alias1", "beanA_alias2");
        aliasRegistry.registerAlias("beanA_alias1", "beanA");

        // 1. 获取别名对应的真实名称
        Assert.assertEquals("beanA", aliasRegistry.canonicalName("beanA_alias1"));
        Assert.assertEquals("beanA", aliasRegistry.canonicalName("beanA_alias2"));

        // 2. 获取 beanA 的所有别名
        Assert.assertEquals(2, aliasRegistry.getAliases("beanA").length);

        Assert.assertTrue(aliasRegistry.isAlias("beanA_alias1"));
        Assert.assertTrue(aliasRegistry.hasAlias("beanA", "beanA_alias2"));
    }
}
