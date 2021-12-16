package com.holelin.freemarker.entity;

import org.hibernate.annotations.GeneratorType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2021/10/28 5:29 下午
 * @UpdateUser: HoleLin
 * @UpdateDate: 2021/10/28 5:29 下午
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Entity
@Table(name = "temp_class")
public class TempClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    private String className;
    private String packagePath;

}
