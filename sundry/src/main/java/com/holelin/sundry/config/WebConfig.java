package com.holelin.sundry.config;

import com.holelin.sundry.filter.TestFilter;
import com.holelin.sundry.interceptor.TestInterceptor;
import com.holelin.sundry.listener.MyHttpSessionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/7/23 15:14
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/7/23 15:14
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    TestInterceptor testInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(testInterceptor).addPathPatterns("/**");
    }

    /**
     * 注册过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new TestFilter());
        filterRegistration.addUrlPatterns("/*");
        return filterRegistration;
    }

    /**
     * 注册监听器
     * @return
     */
    @Bean
    public ServletListenerRegistrationBean registrationBean(){
        ServletListenerRegistrationBean registrationBean = new ServletListenerRegistrationBean();
        registrationBean.setListener(new MyHttpSessionListener());
        return registrationBean;
    }
}