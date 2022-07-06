package com.holelin.sundry.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/7/22 17:45
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/7/22 17:45
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

public class LoggerUtils {

    public static <T> Logger Logger(Class<T> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    /**
     * 打印到指定的文件下
     *
     * @param desc 日志文件名称
     * @return
     */
    public static Logger Logger(String  desc) {
        return LoggerFactory.getLogger(desc);
    }


    public static Logger getBusinessALogger(){
        return Logger("businessALogger");
    }

    public static Logger getBusinessBLogger(){
        return Logger("businessBLogger");
    }
}
