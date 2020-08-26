package com.holelin.sundry.utils;

import com.google.common.collect.Maps;
import com.holelin.sundry.enums.NumberEnum;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @Description: 字符串处理类
 * @Author: HoleLin
 * @CreateDate: 2020/8/26 9:34
 * @UpdateUser: HoleLin
 * @UpdateDate: 2020/8/26 9:34
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

public class DealStringUtil {
    private static int start_num = 1;

    /**
     * 统计字符串中各个每个字符出现的次数
     *
     * @param input 输入的字符串
     * @return 统计好的Map
     */
    public static Map<Character, Integer> distinctCharsWithCount(String input) {
        Map<Character, Integer> result = Maps.newHashMap();
        if (StringUtils.isEmpty(input)) {
            return result;
        }
        for (char c : input.toCharArray()) {
            result.merge(c, NumberEnum.ONE.getNum(), Integer::sum);
        }
        return result;
    }

    /**
     * 通过StringBuilder翻转字符串
     *
     * @param input 待翻转的字符串
     * @return 翻转完成的字符串
     */
    public static String reverse(String input) {
        if (StringUtils.isEmpty(input)) {
            return input;
        }
        StringBuilder sb = new StringBuilder(input);
        return sb.reverse().toString();
    }

    public static boolean checkPalindrome(String input) {
        boolean result = true;
        if (StringUtils.isEmpty(input)) {
            // 空字符串 默认也是回文字符串
            return true;
        }
        int length = input.length();
        for (int i = 0; i < length / 2; i++) {
            if (input.charAt(i) != input.charAt(length - i - 1)) {
                result = false;
                break;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(distinctCharsWithCount("13132"));
        System.out.println(reverse("HoleLin"));
        System.out.println(checkPalindrome("LinniL"));
        System.out.println(checkPalindrome("LinLin"));
        System.out.println(checkPalindrome(""));

    }


}
