package com.holelin.sundry.demo.mutltransaction;

import com.holelin.sundry.utils.SpringContextUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 切面
 *
 * @author bugcat
 */
@Aspect
@Component
public class Pointcuts {

    /**
     * 多数据源组合事务
     * 手动开启一个事物
     */
    @Pointcut("@annotation(com.holelin.sundry.demo.mutltransaction.TransactionalGroup)")
    public void transactionalGroupAspect() {
    }

    @Around(value = "transactionalGroupAspect()")
    public Object transactionalGroupAspectArround(ProceedingJoinPoint pjp) throws Throwable {

        Object obj = null;

        boolean doit = false;//是否执行了方法

        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method targetMethod = methodSignature.getMethod();

        List<TransactionParam> tsParams = null;

        TransactionalGroup groupAnn = targetMethod.getAnnotation(TransactionalGroup.class);
        if (groupAnn != null) {

            Isolation isolation = groupAnn.isolation();   //事务隔离级别
            Propagation propagation = groupAnn.propagation(); //传播行为

            String[] groups = groupAnn.value();
            if (groups.length > 0) {

                doit = true;    //执行 pjp.proceed 的if分支

                Set<String> noRollbackForNameSet = new HashSet<>();
                Class<? extends Throwable>[] noRollbackFor = groupAnn.noRollbackFor();
                if (noRollbackFor != null && noRollbackFor.length > 0) {
                    for (Class<? extends Throwable> clazz : noRollbackFor) {
                        noRollbackForNameSet.add(clazz.getSimpleName().toLowerCase());
                    }
                }


                tsParams = new ArrayList<>(groups.length);
                try {
                    for (String tx : groups) {

                        TransactionParam tsParam = new TransactionParam();

                        //获取到事务服务类，SpringContextUtil 为Spring容器
                        tsParam.tm = (DataSourceTransactionManager) SpringContextUtil.getBean(tx, DataSourceTransactionManager.class);

                        tsParam.def = new DefaultTransactionDefinition();
                        // 事物隔离级别
                        tsParam.def.setIsolationLevel(isolation.value());
                        // 事物传播行为
                        tsParam.def.setPropagationBehavior(propagation.value());

                        // 获得事务状态
                        tsParam.status = tsParam.tm.getTransaction(tsParam.def);

                        tsParams.add(tsParam);
                    }


                    obj = pjp.proceed();


                    //提交事务
                    for (TransactionParam tsParam : tsParams) {
                        tsParam.tm.commit(tsParam.status);
                    }


                } catch (Throwable e) {

                    boolean rollback = true;//事务回滚

                    String errName = e.getClass().getName().toLowerCase();

                    if ("transactionexception".equals(errName)) { // 这是sql异常，救不了了
                        rollback = true;
                    } else {
                        if (noRollbackForNameSet.contains(errName)) { //发生异常，不回滚
                            rollback = false;
                        }
                    }

                    if (!rollback) { //事务不回滚
                        try {

                            for (TransactionParam tsParam : tsParams) {
                                tsParam.tm.commit(tsParam.status);
                            }
                            throw e;   //但是还是要抛出异常
                            // end


                        } catch (org.springframework.transaction.TransactionException e1) {//如果这步还出现sql异常，只能回滚事务了
                            e1.printStackTrace();
                        }

                    }

                    //事务回滚
                    for (TransactionParam tsParam : tsParams) {
                        tsParam.tm.rollback(tsParam.status);
                    }
                    throw e;
                }
            }
        }

        if (!doit) {
            obj = pjp.proceed();
        }

        return obj;
    }


}


class TransactionParam {
    //事务服务类
    DataSourceTransactionManager tm;
    //默认事务对象
    DefaultTransactionDefinition def;
    //事务状态
    TransactionStatus status;
}