package cn.holelin.activiti.listeners;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;

import static org.activiti.engine.delegate.event.ActivitiEventType.JOB_EXECUTION_SUCCESS;
/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2021/11/20 10:28 下午
 * @UpdateUser: HoleLin
 * @UpdateDate: 2021/11/20 10:28 下午
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

public class MyEventListener implements ActivitiEventListener {

    @Override
    public void onEvent(ActivitiEvent event) {
        switch (event.getType()) {

            case JOB_EXECUTION_SUCCESS:
                System.out.println("A job well done!");
                break;

            case JOB_EXECUTION_FAILURE:
                System.out.println("A job has failed...");
                break;

            default:
                System.out.println("Event received: " + event.getType());
        }
    }

    @Override
    public boolean isFailOnException() {
        // The logic in the onEvent method of this listener is not critical, exceptions
        // can be ignored if logging fails...
        return false;
    }
}