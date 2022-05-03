package cn.holelin.dicom.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: 子项目Flyway配置类
 * @Author: HoleLin
 * @CreateDate: 2022/5/3 6:28 PM
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/5/3 6:28 PM
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Configuration
public class FlywayMigrationConfig {

    @Autowired
    private FlywayProperties flywayProperties;
    @Bean
    public FluentConfiguration moduleFlywayMigrationConfig() {
        return Flyway.configure()
                .sqlMigrationPrefix(flywayProperties.getSqlMigrationPrefix())
                .table(flywayProperties.getTable())
                .locations(String.join(",",flywayProperties.getLocations()));
    }
}
