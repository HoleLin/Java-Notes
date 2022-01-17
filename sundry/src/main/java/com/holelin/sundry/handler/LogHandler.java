package com.holelin.sundry.handler;

import cn.hutool.json.JSONUtil;
import com.holelin.sundry.annotation.Log;
import com.holelin.sundry.constants.StringConstants;
import com.holelin.sundry.domain.OperationLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @Description: 日志切面,
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

    @Pointcut("execution(* com.holelin.sundry.controller.*.*(..))")
    public void logPointcut() {
    }

    @Around("logPointcut()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        StopWatch stopWatch = new StopWatch(UUID.randomUUID().toString());
        stopWatch.start();
        final Object result = point.proceed();
        stopWatch.stop();
        final Method method = getMethod(point);
        if (Objects.nonNull(method.getAnnotation(Log.class))) {
            OperationLog operationLog = new OperationLog();
            final Class<?> clazz = getClass(point);
            final String params = getParams(point);
            operationLog.setElapsedTimeWithSeconds(stopWatch.getTotalTimeSeconds());
            operationLog.setElapsedTimeWithMillis(stopWatch.getTotalTimeMillis());
            operationLog.setMethodName(method.getName());
            operationLog.setParameters(params);
            final RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
            if (Objects.nonNull(requestMapping)) {
                final String[] classValue = requestMapping.value();
                final PostMapping postMapping = method.getAnnotation(PostMapping.class);
                if (Objects.nonNull(postMapping)) {
                    final String[] methodValue = postMapping.value();
                    final String classPath = classValue[0];
                    final String methodPath = methodValue[0];
                    operationLog.setFullPath(applyPrefix(classPath) + applyPrefix(methodPath));
                }
            }
            operationLog.setResponse(JSONUtil.parse(result).toString());
            log.info("logBean:{}", operationLog);
        }
        return result;
    }

    /**
     * 补充前缀
     *
     * @param classPath 访问路径
     * @return
     */
    private String applyPrefix(String classPath) {
        if (StringUtils.indexOf(classPath, StringConstants.SLASH) != 0) {
            return StringConstants.SLASH + classPath;
        } else {
            return classPath;
        }
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
     * 获取类
     *
     * @param point
     * @return
     */
    private Class<?> getClass(JoinPoint point) {
        return point.getTarget().getClass();
    }

    /**
     * 获取
     *
     * @param point
     * @return
     */
    private String getParams(JoinPoint point) {
        List<String> list = new ArrayList<>();
        final Object[] args = point.getArgs();
        if (Objects.nonNull(args)) {
            for (Object arg : args) {
                list.add(JSONUtil.parse(arg).toString());
            }
        }
        return list.toString();
    }

}
