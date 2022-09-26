package com.holelin.sundry.test.jvm;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/9/8 11:09
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/9/8 11:09
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class JavaVMStackOOM {

    private void dontStop() {
        while (true){

        }
    }

    public void stackLeakByThread(){
        while (true){
            final Thread thread = new Thread(() -> dontStop());
            thread.start();
        }
    }

    public static void main(String[] args) {
        final JavaVMStackOOM oom = new JavaVMStackOOM();
        oom.stackLeakByThread();
    }
}
