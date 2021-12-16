package cn.holelin.activiti.listeners.alpha;

import cn.holelin.activiti.domain.DetectionBean;
import cn.holelin.activiti.enums.CodeEnum;
import cn.holelin.activiti.mq.RabbitSender;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2021/11/22 3:46 下午
 * @UpdateUser: HoleLin
 * @UpdateDate: 2021/11/22 3:46 下午
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

@Slf4j
@Component(value = "scrynCpuCreateListener")
public class AlphaCpuCreateListener implements TaskListener {

    @Autowired
    private RabbitSender rabbitSender;

    @Override
    public void notify(DelegateTask delegateTask) {
        final String id = delegateTask.getId();
        final DetectionBean detectionBean = new DetectionBean();
        detectionBean.setTaskId(id);
        detectionBean.setCode(CodeEnum.SCRYN_CPU_CODE.value);
        try {
            rabbitSender.send(detectionBean, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
