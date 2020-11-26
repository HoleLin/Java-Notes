package com.holelin.sundry.common;

/**
* @Description:
* @Author:         HoleLin
* @CreateDate:     2020/7/29 13:28
* @UpdateUser:     HoleLin
* @UpdateDate:     2020/7/29 13:28
* @UpdateRemark:   修改内容

* @Version:        1.0
*/

public class StringTest {
    public static void main(String[] args) {
        // a==>常量池
        String a = "abc";
        String b = new String("abc");
        String d = new String();
        // c==>常量池
        String c = b.intern();
        System.out.println(a == b);
        System.out.println(b == c);
        System.out.println(a == c);

    }
}
