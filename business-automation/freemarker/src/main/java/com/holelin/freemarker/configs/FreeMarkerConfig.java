package com.holelin.freemarker.configs;

import com.holelin.freemarker.utils.DatabaseTemplateLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2021/10/28 10:40 上午
 * @UpdateUser: HoleLin
 * @UpdateDate: 2021/10/28 10:40 上午
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Configuration
public class FreeMarkerConfig {
    /**
     * 加载自定义模板加载器
     */
    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer(DatabaseTemplateLoader databaseTemplateLoader ) {
        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
        configurer.setPreTemplateLoaders(databaseTemplateLoader);
        return configurer;
    }


}
