package com.holelin.sundry.enums;

/**
 * @Description: 常用符号枚举类
 * 逗号,分号,下划线,中划线,句号(英文状态)
 * @Author: HoleLin
 * @CreateDate: 2020/8/26 9:45
 * @UpdateUser: HoleLin
 * @UpdateDate: 2020/8/26 9:45
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

public enum SymbolEnum {
    /**
     * 逗号
     */
    COMMA(","),
    /**
     * 分号
     */
    SEMICOLON(";"),
    /**
     * 下划线
     */
    UNDERLINE("_"),
    /**
     * 中划线
     */
    MIDDLE_LINE("-"),
    /**
     * 句号(英文状态);点
     */
    POINT("."),
    ;

    SymbolEnum(String desc) {
        this.desc = desc;
    }

    private String desc;
}
