package com.holelin.mysql.dao;

import com.holelin.mysql.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    /**
     * 创建临时表
     */
    @Modifying
    @Query(value = "create temporary table `temp_user`  (\n" +
            "  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
            "  `user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,\n" +
            "  PRIMARY KEY (`user_id`) USING BTREE\n" +
            ")", nativeQuery = true)
    void createTemporaryTable();

    /**
     * 插入数据到临时表
     *
     * @param username 用户名称
     * @return
     */
    @Modifying
    @Query(value = "insert into `temp_user`(`user_name`) values(:username)", nativeQuery = true)
    int insertDataToTemporaryTable(@Param("username") String username);

}
