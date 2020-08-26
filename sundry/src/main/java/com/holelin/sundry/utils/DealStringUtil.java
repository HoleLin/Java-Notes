package com.holelin.sundry.utils;

import com.google.common.collect.Maps;
import com.holelin.sundry.enums.NumberEnum;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
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

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址。
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        String UNKNOWN = "unknown";
        String LOCAL_IPV4 = "127.0.0.1";
        String LOCAL_IPV6 = "0:0:0:0:0:0:0:1";
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (LOCAL_IPV4.equals(ip) || LOCAL_IPV6.equals(ip)) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ip = inet.getHostAddress();
            }
        }
        return ip;
    }

    public static void main(String[] args) {
        System.out.println(distinctCharsWithCount("13132"));
        System.out.println(reverse("HoleLin"));
        System.out.println(checkPalindrome("LinniL"));
        System.out.println(checkPalindrome("LinLin"));
        System.out.println(checkPalindrome(""));

    }


}
