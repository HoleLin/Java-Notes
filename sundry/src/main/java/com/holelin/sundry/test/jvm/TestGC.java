package com.holelin.sundry.test.jvm;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/9/8 14:39
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/9/8 14:39
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class TestGC {

    public static final int _1MB = 1024 * 1024;

    /**
     * -verbose:gc -Xms20M -Xmx20M -Xmn10M -Xlog:gc* -XX:SurvivorRatio=8
     */
    public static void testAllocation() {
        byte[] allocation1, allocation2, allocation3, allocation4;
        allocation1 = new byte[2 * _1MB];
        allocation2 = new byte[2 * _1MB];
        allocation3 = new byte[2 * _1MB];
        allocation4 = new byte[4 * _1MB];
    }

    public static void main(String[] args) {
        testAllocation();
    }
}
