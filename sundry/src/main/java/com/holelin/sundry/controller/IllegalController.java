package com.holelin.sundry.controller;

import com.holelin.sundry.annotation.Log;
import com.holelin.sundry.domain.IllegalBean;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/illegal")
public class IllegalController {

    @Log
    @PostMapping("/illegal-bean")
    public IllegalBean illegal(@Validated @RequestBody IllegalBean bean) throws InterruptedException {
        Thread.sleep(1000);
        return bean;
    }
}
