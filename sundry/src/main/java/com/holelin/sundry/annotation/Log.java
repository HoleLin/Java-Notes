package com.holelin.sundry.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/1/12 2:37 PM
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/1/12 2:37 PM
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
}
