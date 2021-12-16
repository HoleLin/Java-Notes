package cn.holelin.activiti.service;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2021/11/22 11:59 上午
 * @UpdateUser: HoleLin
 * @UpdateDate: 2021/11/22 11:59 上午
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

@Slf4j
@Component
public class ErrorService implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {

        final String id = execution.getId();
        log.info("cjl: {}", id);
    }
}
