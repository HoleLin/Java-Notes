package com.holelin.sundry.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/5/28 14:46
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/5/28 14:46
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                // 设置放行哪些原始域
                .allowedOriginPatterns("*")
                // 放行哪些请求方式
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                // 暴露哪些原始请求头部信息
                .allowedHeaders("*");
    }
}
