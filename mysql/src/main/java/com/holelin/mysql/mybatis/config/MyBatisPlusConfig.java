package com.holelin.mysql.mybatis.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @Description: MyBaits 配置类
 * @Author: HoleLin
 * @CreateDate: 2022/1/29 2:22 PM
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/1/29 2:22 PM
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Configuration
@MapperScan(basePackages = {"com.holelin.mysql.mybatis.mapper"},
        sqlSessionFactoryRef = "mybatisSqlSessionFactory")
public class MyBatisPlusConfig {

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Primary
    @Bean(name = "dataSource")
    public DataSource dataSource() {
        final DataSource dataSource = DataSourceBuilder.create().
                url(url).
                username(username).
                password(password).
                driverClassName(driverClassName).
                build();
        return dataSource;
    }

    /**
     * 配置  SqlSessionFactory TransactionManager SqlSessionTemplate
     */
    @Primary
    @Bean(name = "mybatisSqlSessionFactory")
    public SqlSessionFactory mybatisSqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        final MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        mybatisSqlSessionFactoryBean.setDataSource(dataSource);
        // 指定XML配置路径
        // 注 此处需要使用 getResources()
        // getResources() 获取所有类路径下的指定文件
        // getResource()  表示从当前类的根路径去查找资源，能获取到同一个包下的文件
        final Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath*:/mapping/**/*.xml");
        mybatisSqlSessionFactoryBean.setMapperLocations(resources);

        // 配置分页插件
        Interceptor[] plugins = new Interceptor[]{mybatisPlusInterceptor()};
        mybatisSqlSessionFactoryBean.setPlugins(plugins);

        return mybatisSqlSessionFactoryBean.getObject();
    }

    @Primary
    @Bean(name = "transactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Primary
    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("mybatisSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean("mybatisPlusInterceptor")
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        final MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

}
