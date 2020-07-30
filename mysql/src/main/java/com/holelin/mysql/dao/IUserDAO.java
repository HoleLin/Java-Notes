package com.holelin.mysql.dao;

import com.holelin.mysql.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2020/7/30 10:03
 * @UpdateUser: HoleLin
 * @UpdateDate: 2020/7/30 10:03
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

public interface IUserDAO extends JpaRepository<User, String> {

    /**
     * 通过名称查询用户信息
     *
     * @param name
     * @return
     */
    User findByName(String name);
}
