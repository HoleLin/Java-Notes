package com.holelin.mysql.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @Description: 用户测试类
 * @Author: HoleLin
 * @CreateDate: 2020/7/30 10:01
 * @UpdateUser: HoleLin
 * @UpdateDate: 2020/7/30 10:01
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Version
    private Long version;
}
