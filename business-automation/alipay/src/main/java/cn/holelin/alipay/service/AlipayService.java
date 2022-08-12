package cn.holelin.alipay.service;

import javax.servlet.http.HttpServletRequest;

public interface AlipayService {
    String create(String orderId, String returnUrl);

    /**
     * 异步通知会返回一个request
     */

    void notify(HttpServletRequest request);
 
    void refund(String orderId);
}