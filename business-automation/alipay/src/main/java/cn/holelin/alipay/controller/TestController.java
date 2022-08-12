package cn.holelin.alipay.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/wappay")
public class TestController {


    @GetMapping("/pay")
    public String pay() {
        return "pay";
    }
    @GetMapping("/notify_url")
    public String notify_url() {
        return "notify_url";
    }
    @GetMapping("/return_url")
    public String return_url() {
        return "return_url";
    }
}
