package com.holelin.sundry.controller;

import com.holelin.sundry.utils.SseUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/7/22 10:32
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/7/22 10:32
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@RestController
@RequestMapping("/sse")
public class SseController {

    @GetMapping("/sub/{userId}")
    public void connect(@PathVariable String userId) {
        SseUtils.connect(userId);
    }
}
