package com.holelin.sundry.utils;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/8/25 12:56
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/8/25 12:56
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class PerfMonAgent {

    private static Instrumentation inst = null;

    public static void permain(String agentArgs, Instrumentation _inst) {
        System.out.println("PerfMonAgent.permain() was called.");
        inst = _inst;
        ClassFileTransformer transformer = new PerfMonXformer();
        System.out.println("Adding a PerfMonxformer instance to the JVM");
        inst.addTransformer(transformer);
    }

}
