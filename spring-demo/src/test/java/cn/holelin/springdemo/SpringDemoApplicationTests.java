package cn.holelin.springdemo;

import cn.holelin.springdemo.core.customElement.bean.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

class SpringDemoApplicationTests {

    @Test
    void testCustomElement() {
        final ApplicationContext application = new ClassPathXmlApplicationContext("spring-config.xml");
        final User testBean = (User) application.getBean("testBean");
        System.out.println(testBean);
    }

}
