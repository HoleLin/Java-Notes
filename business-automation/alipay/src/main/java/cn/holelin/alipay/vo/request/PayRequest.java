package cn.holelin.alipay.vo.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/8/5 15:14
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/8/5 15:14
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
public class PayRequest {

    /**
     * 商品名称
     */
    private String subject;
    /**
     * 价格
     */
    private String total_amount;

    private String product_code="QUICK_WAP_WAY";

    /**
     * 商品编码
     */
    private String out_trade_no;

}
