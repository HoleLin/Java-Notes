package com.holelin.sundry.test.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: VM args -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
 * @Author: HoleLin
 * @CreateDate: 2022/9/8 10:39
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/9/8 10:39
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class HeapOOM {

    static class OOMObject {

    }

    public static void main(String[] args) {
        List<OOMObject> list = new ArrayList<>();
        while (true) {
            list.add(new OOMObject());
        }
    }
}
