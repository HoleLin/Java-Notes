package cn.holelin.springdemo.core.customElement.handler;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/8/14 18:44
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/8/14 18:44
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class MyNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("user", new UserBeanDefinitionParser());
    }
}
