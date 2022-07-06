package com.holelin.sundry.handler;

import com.alibaba.fastjson.JSON;
import com.holelin.sundry.annotation.Log;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.UUID;

/**
 * @Description: 日志切面
 * @Author: HoleLin
 * @CreateDate: 2022/1/12 2:39 PM
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/1/12 2:39 PM
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 * @see com.holelin.sundry.annotation.Log
 * 注解的实现类
 */
@Aspect
@Slf4j
@Component
public class LogHandler {

    @Pointcut("@annotation(com.holelin.sundry.annotation.Log)")
    public void logPointcut() {
    }
    /**
     * @param joinPoint
     * @author fu
     * @description 切面方法入参日志打印
     * @date 2020/7/15 10:30
     */
    @Before("logPointcut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String methodDetailDescription = this.getAspectMethodLogDesc(joinPoint);
        log.info("------------------------------- start --------------------------");
        /**
         * 打印自定义方法描述
         */
        log.info("Method detail Description: {}", methodDetailDescription);
        /**
         * 打印请求入参
         */
        log.info("Request Args: {}", JSON.toJSONString(joinPoint.getArgs()));
        /**
         * 打印请求方式
         */
        log.info("Request method: {}", request.getMethod());
        /**
         * 打印请求 url
         */
        log.info("Request URL: {}", request.getRequestURL().toString());

        /**
         * 打印调用方法全路径以及执行方法
         */
        log.info("Request Class and Method: {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
    }

    @Around("logPointcut()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        String aspectMethodLogDescPJ = getAspectMethodLogDesc(point);
        StopWatch stopWatch = new StopWatch(UUID.randomUUID().toString());
        stopWatch.start();
        final Object result = point.proceed();
        stopWatch.stop();
        log.info("{} Response result: {}", aspectMethodLogDescPJ, JSON.toJSONString(result));
        log.info("------------------------------- end ------------------------------");
        return result;
    }


    @AfterReturning(returning = "ref", pointcut = "logPointcut()")
    public void doAfterReturning(JoinPoint point, Object ref) {
        final Method method = getMethod(point);
        if (Objects.nonNull(method.getAnnotation(Log.class))) {
            log.info("doAfterReturning,Object:{}", ref);
        }
    }

    @AfterThrowing("logPointcut()")
    public void doAfterThrowing(JoinPoint point) {
        final Method method = getMethod(point);
        if (Objects.nonNull(method.getAnnotation(Log.class))) {
            log.info("doAfterThrowing");
        }
    }


    /**
     * 获取方法
     *
     * @param point
     * @return
     */
    private Method getMethod(JoinPoint point) {
        final MethodSignature signature = (MethodSignature) point.getSignature();
        return signature.getMethod();
    }


    /**
     * @param joinPoint
     * @description @PrintlnLog 注解作用的切面方法详细细信息
     * @date 2020/7/15 10:34
     */
    public String getAspectMethodLogDesc(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        return getAspectMethodLogDesc(targetName, methodName, arguments);
    }

    /**
     * @param proceedingJoinPoint
     * @description @PrintlnLog 注解作用的切面方法详细细信息
     */
    public String getAspectMethodLogDesc(ProceedingJoinPoint proceedingJoinPoint) throws Exception {
        String targetName = proceedingJoinPoint.getTarget().getClass().getName();
        String methodName = proceedingJoinPoint.getSignature().getName();
        Object[] arguments = proceedingJoinPoint.getArgs();
        return getAspectMethodLogDesc(targetName, methodName, arguments);
    }

    /**
     * @param targetName
     * @param methodName
     * @param arguments
     * @description 自定义注解参数
     */
    public String getAspectMethodLogDesc(String targetName, String methodName, Object[] arguments) throws Exception {
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        StringBuilder description = new StringBuilder("");
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description.append(method.getAnnotation(Log.class).description());
                    break;
                }
            }
        }
        return description.toString();
    }
}
