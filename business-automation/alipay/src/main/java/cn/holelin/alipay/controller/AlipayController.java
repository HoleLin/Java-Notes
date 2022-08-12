package cn.holelin.alipay.controller;

import cn.holelin.alipay.service.AlipayService;
import cn.holelin.alipay.vo.request.PayRequest;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.AlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/8/5 15:13
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/8/5 15:13
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Slf4j
//@RestController
@Controller
@RequestMapping("/alipay")
public class AlipayController {

    @Autowired
    private AlipayService alipayService;

    @GetMapping("/create")
    @ResponseBody
    public String create(@RequestParam("orderId") String orderId,
                         @RequestParam("returnUrl") String returnUrl) {

        //发起支付
        String payUrl = alipayService.create(orderId,returnUrl);
        return payUrl;
    }

    /**
     * 支付宝异步通知
     */
    @PostMapping("/notify")
    public void notify(HttpServletRequest request) {
        alipayService.notify(request);
    }



    @GetMapping("/index")
    public String index() {
        return "index.html";
    }
}
