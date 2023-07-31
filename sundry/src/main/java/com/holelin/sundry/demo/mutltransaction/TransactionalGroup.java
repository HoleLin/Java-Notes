package com.holelin.sundry.demo.mutltransaction;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import java.lang.annotation.*;

/**
 *    @TransactionalGroup({"onetx", "twotx"})
 *     public void save(Demo demo) {
 *         oneDemoDao.updateById(demo);
 *         twoDemoDao.updateById(demo);
 *     }
 *
 * @author bugcat
 * */
@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface TransactionalGroup {

    /**
     * 
     * 配置的事务id
     * 
     * 
     * <bean id="onetx" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
     *     <property name="dataSource" ref="onetxdataSource" />
     * </bean>
     * 
     * 
     * <p> 在方法上添加：@TransactionalGroup("onetx","twotx") </p>
     * 
     * */
    String[] value();


    /*******  以下参数，参见 @Transactional  ********/

    //事务传播行为
    Propagation propagation() default Propagation.REQUIRED;

    //事务隔离级别
    Isolation isolation() default Isolation.DEFAULT;

    // 哪些异常不会滚事务
    Class<? extends Throwable>[] noRollbackFor() default {};
    
}
