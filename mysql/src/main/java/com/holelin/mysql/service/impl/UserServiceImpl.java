package com.holelin.mysql.service.impl;

import com.holelin.mysql.dao.IUserDAO;
import com.holelin.mysql.entity.User;
import com.holelin.mysql.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2020/7/30 10:04
 * @UpdateUser: HoleLin
 * @UpdateDate: 2020/7/30 10:04
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

@Service
@Slf4j
@Transactional(isolation = Isolation.REPEATABLE_READ,propagation = Propagation.REQUIRES_NEW )
public class UserServiceImpl implements IUserService {


    @Autowired
    private IUserDAO mIUserDAO;

    @Override
    public void testSave() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user1 = mIUserDAO.findByName("1");
                user1.setEmail(UUID.randomUUID().toString());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                user1.setVersion(user1.getVersion());
                mIUserDAO.saveAndFlush(user1);
            }
        }).start();

    }
}
