package com.holelin.mysql.controller;

import com.holelin.mysql.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2020/7/30 10:21
 * @UpdateUser: HoleLin
 * @UpdateDate: 2020/7/30 10:21
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private IUserService mIUserService;

    @GetMapping
    public void testSave() {
        mIUserService.testSave();
    }

}
