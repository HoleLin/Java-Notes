package com.holelin.sundry.demo;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2020/7/29 13:28
 * @UpdateUser: HoleLin
 * @UpdateDate: 2020/7/29 13:28
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Slf4j
public class StreamTest {
    public static final Integer TEST_MAX_NUM = 10000;

    public static void main(String[] args) {
        //使用一个容器装载100个数字，通过Stream并行处理的方式将容器中为单数的数字转移到容器parallelList
        List<Integer> integerList = new ArrayList<Integer>();
        for (int i = 0; i < 100; i++) {
            integerList.add(i);
        }
        for (int j = 0; j < TEST_MAX_NUM; j++) {
            List<Integer> parallelList = new ArrayList<Integer>();
            try {
                integerList.stream()
                        .parallel()
                        .filter(i -> i % 2 == 1)
                        .forEach(i -> parallelList.add(i));
                if (parallelList.size() != 50) {
                    log.info("情况1--出现异常: 数量少了,数量为: " + parallelList.size());
                }
            } catch (Exception e) {
                log.info("情况1--出现异常: 数组越界");
            }
        }

        log.info("=======================================================");

        for (int j = 0; j < TEST_MAX_NUM; j++) {
            try {
                List<Integer> parallelList = new Vector<>();
                integerList.stream()
                        .parallel()
                        .filter(i -> i % 2 == 1)
                        .forEach(i -> parallelList.add(i));
                if (parallelList.size() != 50) {
                    log.info("情况2--出现异常: 数量少了");
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                log.info("情况2--出现异常: 数组越界");
            }
        }
        log.info("=======================================================");

        for (int j = 0; j < TEST_MAX_NUM; j++) {
            try {
                List<Integer> parallelList = integerList.stream()
                        .parallel()
                        .filter(i -> i % 2 == 1)
                        .collect(Collectors.toList());
                if (parallelList.size() != 50) {
                    log.info("情况3--出现异常: 数量少了");
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                log.info("情况3--出现异常: 数组越界");
            }
        }
    }
}
