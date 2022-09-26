package com.holelin.sundry.test.jvm;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/9/8 10:58
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/9/8 10:58
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class JavaVMStackSOF {

    private int stackLength = 1;

    public void stackLeak() {
        stackLength++;
        stackLeak();
    }

    public static void main(String[] args) {
        final JavaVMStackSOF oom = new JavaVMStackSOF();
        try {
            oom.stackLeak();
        } catch (Exception e) {
            System.out.println("stack length: " + oom.stackLength);
            throw e;
        }
    }

}
