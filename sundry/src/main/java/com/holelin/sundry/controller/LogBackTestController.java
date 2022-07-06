package com.holelin.sundry.controller;

import com.holelin.sundry.utils.LoggerUtils;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/7/22 17:40
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/7/22 17:40
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@RestController
@RequestMapping("/logback")
public class LogBackTestController {

    @GetMapping("/business-a")
    public void doBusinessA() {
        final Logger logger = LoggerUtils.getBusinessALogger();
        logger.info("do business a ");
    }

    @GetMapping("/business-b")
    public void doBusinessB() {
        final Logger logger = LoggerUtils.getBusinessBLogger();
        logger.info("do business b ");
    }
}
