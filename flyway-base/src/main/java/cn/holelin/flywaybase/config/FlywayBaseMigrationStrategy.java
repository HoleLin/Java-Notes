package cn.holelin.flywaybase.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description: 基础Flyway迁移策略
 * @Author: HoleLin
 * @CreateDate: 2022/5/3 6:28 PM
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/5/3 6:28 PM
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Component
public class FlywayBaseMigrationStrategy implements FlywayMigrationStrategy {

    @Autowired
    private List<FluentConfiguration> migrations;

    @Override
    public void migrate(Flyway flyway) {
        this.migrations.forEach(mig -> mig.
                baselineOnMigrate(true).
                baselineVersion("0").
                dataSource(flyway.getConfiguration().getDataSource()).
                load().
                migrate());
    }
}
