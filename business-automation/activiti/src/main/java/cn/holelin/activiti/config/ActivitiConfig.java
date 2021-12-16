package cn.holelin.activiti.config;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2021/11/20 10:10 下午
 * @UpdateUser: HoleLin
 * @UpdateDate: 2021/11/20 10:10 下午
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

@Configuration
public class ActivitiConfig {


//    @Bean
//    public ProcessEngine processEngine(){
//        return ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration()
//                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE)
//                .setJdbcUrl("jdbc:h2:mem:my-own-db;DB_CLOSE_DELAY=1000")
//                .setJdbcUsername("")
//                .setJdbcPassword("")
//                .setJdbcDriver("")
//                // AsyncExecutor是一个管理线程池以触发计时器和其他异步任务的组件
//                .setAsyncExecutorActivate(false)
//
//                .buildProcessEngine();
//    }
}
