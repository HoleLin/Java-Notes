package com.holelin.sundry.enums;

/**
 * @Description: 常用数字枚举类
 * 0~10
 * 100
 * 1000
 * 10000
 * @Author: HoleLin
 * @CreateDate: 2020/8/26 9:50
 * @UpdateUser: HoleLin
 * @UpdateDate: 2020/8/26 9:50
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public enum NumberEnum {
    /**
     * 0
     */
    ZERO(0),
    /**
     * 1
     */
    ONE(1),
    /**
     * 2
     */
    TWO(2),
    /**
     * 3
     */
    THREE(3),
    /**
     * 4
     */
    FOUR(4),
    /**
     * 5
     */
    FIVE(5),
    /**
     * 6
     */
    SIX(6),
    /**
     * 7
     */
    SEVEN(7),
    /**
     * 8
     */
    EIGHT(8),
    /**
     * 9
     */
    NINE(9),
    /**
     * 10
     */
    TEN(10),
    /**
     * 100
     */
    HUNDRED(100),
    /**
     * 1000
     */
    Thousand(1000);

    NumberEnum(int num) {
        this.num = num;
    }

    private int num;

    public int getNum() {
        return num;
    }
}
