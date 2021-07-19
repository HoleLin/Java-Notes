package com.holelin.sundry.controller;

import com.holelin.sundry.domain.IllegalBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("illegal")
public class IllegalController {

    @PostMapping("illegal-bean")
    public IllegalBean illegal(@RequestBody IllegalBean bean) {

        return bean;
    }
}
