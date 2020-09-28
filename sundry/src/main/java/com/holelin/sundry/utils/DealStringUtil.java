package com.holelin.sundry.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.holelin.sundry.constants.StringConstants;
import com.holelin.sundry.domain.DepartmentConfigInfo;
import com.holelin.sundry.enums.NumberEnum;
import com.holelin.sundry.enums.SymbolEnum;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
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

    /**
     * 处理组织机构配置项
     *
     * @param departmentInfo 组织机构信息
     * @return
     * @throws
     * @author HoleLin
     * @date 2020/8/3 18:46
     */
    public static List<DepartmentConfigInfo> handleDepartmentFromConfig(String departmentInfo) {
        List<DepartmentConfigInfo> result = Lists.newArrayList();
        for (String department : departmentInfo.split(SymbolEnum.COMMA.getDesc())) {
            String[] split = department.split(SymbolEnum.COLON.getDesc());
            if (NumberEnum.TWO.getNum() == split.length) {
                DepartmentConfigInfo infoDTO = new DepartmentConfigInfo();
                String departmentCode = split[0].replaceAll(SymbolEnum.BLANK_SPACE.getDesc(),
                        SymbolEnum.EMPTY_STRING.getDesc());
                String departmentNameAndSort = split[1].trim();
                infoDTO.setDepartmentCode(departmentCode);
                // 将departmentCode后面的0都替换掉
                infoDTO.setHandleAfterDepartmentCode(replaceLastZero(departmentCode));
                String[] departmentNameAndSortArray = departmentNameAndSort.split(SymbolEnum.MIDDLE_LINE.getDesc());
                if (NumberEnum.TWO.getNum() == departmentNameAndSortArray.length) {
                    infoDTO.setDepartmentName(departmentNameAndSortArray[0]);
                    infoDTO.setSort(Integer.parseInt(departmentNameAndSortArray[1]));
                }
                result.add(infoDTO);
            }
        }
        return result;
    }

    /**
     * 将departmentCode后面的0都替换掉
     * @param departmentCode 组织机构
     * @return java.lang.String
     * @throws
     * @author HoleLin
     * @date 2020/7/28 16:57
     */
    public static String replaceLastZero(String departmentCode) {
        // 翻转字符串
        StringBuilder sb = new StringBuilder(departmentCode).reverse();
        // 计算0的个数
        int zeroCount = 0;
        while (StringConstants.CHAR_ZERO == sb.charAt(zeroCount)) {
            zeroCount++;
        }
        // 替换掉0,最后进行翻转复原
        sb.replace(NumberEnum.ZERO.getNum(), zeroCount, SymbolEnum.EMPTY_STRING.getDesc()).reverse();
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(distinctCharsWithCount("13132"));
        System.out.println(reverse("HoleLin"));
        System.out.println(checkPalindrome("LinniL"));
        System.out.println(checkPalindrome("LinLin"));
        System.out.println(checkPalindrome(""));

    }


}
