package com.holelin.sundry.demo;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.text.NumberFormat;

@Slf4j
public class BigDecimalTest {
    public static void main(String[] args) {
        double doubleValue = 0.1D;
        float floatValue = 0.1F;
        String stringValue = "0.1";

        BigDecimal a = new BigDecimal(1);
        BigDecimal b = new BigDecimal(stringValue);
        BigDecimal c = new BigDecimal(doubleValue);
        BigDecimal d = new BigDecimal(floatValue);
        BigDecimal newC = new BigDecimal(Double.toString(doubleValue));
        BigDecimal newD = new BigDecimal(Float.toString(floatValue));
        log.info("a value is : {}", a);
        log.info("b value is : {}", b);
        log.info("c value is : {}", c);
        log.info("d value is : {}", d);
        log.info("newC value is : {}", newC);
        log.info("newD value is : {}", newD);

        // 货币格式化
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        // 百分比格式化
        NumberFormat percent = NumberFormat.getPercentInstance();
        // 设置百分比小数点最多3位
        percent.setMaximumFractionDigits(3);
        NumberFormat number = NumberFormat.getNumberInstance();
        BigDecimal principal = new BigDecimal("50000.9875");

        BigDecimal loanAmount = new BigDecimal("20210.071");
        BigDecimal interestRate = new BigDecimal("0.008");
        BigDecimal interest = loanAmount.multiply(interestRate);
        log.info("贷款金额: {}",currency.format(loanAmount));
        log.info("利率: {}",percent.format(interestRate));
        log.info("利息: {}",currency.format(interest));
        log.info("本金: {}",number.format(principal));

        BigDecimal testNum = new BigDecimal("10");
        BigDecimal three = new BigDecimal("3");
        BigDecimal divide = testNum.divide(three,2);

    }
}
