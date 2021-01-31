package com.holelin.jpademo.dao;

import com.holelin.jpademo.dto.UserOnlyName;
import com.holelin.jpademo.dto.UserOnlyNameEmailDTO;
import com.holelin.jpademo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 接口的方式返回DTO
     * @param address
     * @return
     */
    UserOnlyName findByAddress(String address);

    //测试只返回name和email的DTO
    UserOnlyNameEmailDTO findByEmail(String email);

    @Query("From User where name=:name")
    User findByQuery(@Param("name") String nameParam);
}