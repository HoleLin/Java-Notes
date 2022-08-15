package cn.holelin.springdemo.core.customElement.handler;

import cn.holelin.springdemo.core.customElement.bean.User;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/8/14 18:40
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/8/14 18:40
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class UserBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    @Override
    protected Class<?> getBeanClass(Element element) {
        return User.class;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {
        final String userName = element.getAttribute("userName");
        final String email = element.getAttribute("email");
        final String id = element.getAttribute("id");
        if (StringUtils.hasText(userName)) {
            bean.addPropertyValue("userName", userName);
        }
        if (StringUtils.hasText(email)) {
            bean.addPropertyValue("email", email);
        }
        if (StringUtils.hasText(email)) {
            bean.addPropertyValue("id", id);
        }
    }
}
