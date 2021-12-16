package cn.holelin.activiti.service;

import cn.holelin.activiti.mq.RabbitSender;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2021/11/22 4:18 下午
 * @UpdateUser: HoleLin
 * @UpdateDate: 2021/11/22 4:18 下午
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Service
public class DetectionService {
    @Autowired
    private ProcessEngine processEngine;

    public void start() {
        // 部署流程定义文件
        String key = "cooperation";
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.createDeployment().addClasspathResource("processes/" + key + ".bpmn20.xml").deploy();
        final RuntimeService runtimeService = processEngine.getRuntimeService();
        final org.activiti.engine.runtime.ProcessInstance cooperationProcess = runtimeService.createProcessInstanceBuilder()
                .processDefinitionKey(key).businessKey("123").start();
    }

}
