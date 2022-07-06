package com.holelin.sundry.controller;

import com.holelin.sundry.vo.request.ValidTestOne;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/7/18 22:54
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/7/18 22:54
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@RestController
@Validated
@RequestMapping("/valid-test")
public class ValidTestController {

    @RequestMapping("/test1")
    public void test1(@RequestBody @Validated ValidTestOne request) {

    }

    /**
     * 路径变量
     *
     * @param userId
     */
    @GetMapping("/{userId}")
    public void test2(@PathVariable("userId") @Min(10000000000000000L) Long userId) {
    }


    /**
     * RequestParam
     * @param name
     */
    @GetMapping("/test3")
    public void test3(@RequestParam("name") @Length(min = 1, max = 10) @NotNull String name) {
    }


    @PostMapping("/save")
    public void save(@RequestBody @Validated(ValidTestOne.Save.class) ValidTestOne request) {
        // 校验通过，才会执行业务逻辑处理
    }

    @PostMapping("/update")
    public void update(@RequestBody @Validated(ValidTestOne.Update.class) ValidTestOne request) {
        // 校验通过，才会执行业务逻辑处理
    }
}
