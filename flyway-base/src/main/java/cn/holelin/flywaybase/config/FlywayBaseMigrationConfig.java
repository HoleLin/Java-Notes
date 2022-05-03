package cn.holelin.flywaybase.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: 基础Flyway配置类
 * @Author: HoleLin
 * @CreateDate: 2022/5/3 6:28 PM
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/5/3 6:28 PM
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Configuration
public class FlywayBaseMigrationConfig {

    @Bean
    public FluentConfiguration moduleBaseFlywayMigrationConfig() {
        return Flyway.configure()
                .sqlMigrationPrefix("BASE_")
                .table("flyway_base_schema_history")
                .locations("db/migration/base");
    }
}

