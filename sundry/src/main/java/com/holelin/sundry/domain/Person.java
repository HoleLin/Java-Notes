package com.holelin.sundry.domain;
/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/3/14 1:46 PM
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/3/14 1:46 PM
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class Person {
    private String name;

    public Person(String name) {
        this.name = name;
    }
    public Person(Person person) {
        this.name = person.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
