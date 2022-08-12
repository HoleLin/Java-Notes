package com.holelin.sundry.test.yaml;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;

import java.io.IOException;
import java.util.Properties;
/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/8/2 11:29
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/8/2 11:29
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class YmlConﬁgFactory extends DefaultPropertySourceFactory {
    @Override
    public PropertySource<?> createPropertySource(String name,
                                                  EncodedResource resource) throws IOException {
        String sourceName = name != null ? name :
                resource.getResource().getFilename();
        if (!resource.getResource().exists()) {
            return new PropertiesPropertySource(sourceName, new Properties());
        } else {
            final String suffix = ".yml";
            final String suffix1 = ".yaml";
            if (sourceName.endsWith(suffix) ||
                    sourceName.endsWith(suffix1)) {
                Properties propertiesFromYaml = loadYml(resource);
                return new PropertiesPropertySource(sourceName,
                        propertiesFromYaml);
            } else {
                return super.createPropertySource(name, resource);
            }
        }
    }

    private Properties loadYml(EncodedResource resource) throws
            IOException {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(resource.getResource());
        factory.afterPropertiesSet();
        return factory.getObject();
    }
}