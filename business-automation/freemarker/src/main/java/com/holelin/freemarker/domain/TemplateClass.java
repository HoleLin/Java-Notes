package com.holelin.freemarker.domain;

import lombok.Data;

import java.util.List;

/**
 * @Description:  模板类
 * @Author: HoleLin
 * @CreateDate: 2021/10/28 11:37 上午
 * @UpdateUser: HoleLin
 * @UpdateDate: 2021/10/28 11:37 上午
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
public class TemplateClass {

    private String classPath;
    private String className;
    private List<Attribute> attributes;
}
