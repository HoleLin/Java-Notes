package cn.holelin.activiti;

import cn.holelin.activiti.config.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2021/11/12 3:36 下午
 * @UpdateUser: HoleLin
 * @UpdateDate: 2021/11/12 3:36 下午
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class BootActivitiApplicationTests {

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private TaskRuntime taskRuntime;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ProcessEngine processEngine;

    @Test
    public void testActiviti1() {
        // 创建流程引擎
        ProcessEngine processEngine = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration()
                .buildProcessEngine();
        // 部署流程定义文件
        String key = "cooperation";
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.createDeployment().addClasspathResource("processes/" + key + ".bpmn20.xml").deploy();

        // 验证已部署流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
        Assert.assertEquals(key, processDefinition.getKey());

        final RuntimeService runtimeService = processEngine.getRuntimeService();
        Map<String, Object> map = new HashMap<>(2);
        map.put("orderId", UUID.randomUUID().toString().replace("-",""));
        map.put("code", 200);
        final org.activiti.engine.runtime.ProcessInstance cooperationProcess = runtimeService.startProcessInstanceByKey(
                key, map);


    }

    @Test
    public void test2() {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.createDeployment()
                .addClasspathResource("processes/VacationRequest.bpmn20.xml")
                .deploy();

        log.info("Number of process definitions: " + repositoryService.createProcessDefinitionQuery().count());
        Map<String, Object> variables = new HashMap<>();
        variables.put("employeeName", "Kermit");
        variables.put("numberOfDays", 4);
        variables.put("vacationMotivation", "I'm really tired!");

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("vacationRequest", variables);

        // Verify that we started a new process instance
        log.info("Number of process instances: " + runtimeService.createProcessInstanceQuery().count());

        // Fetch all tasks for the management group
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("management").list();
        for (Task task : tasks) {
            log.info("Task available: " + task.getName());
        }

        Task task = tasks.get(0);

        Map<String, Object> taskVariables = new HashMap<>();
        taskVariables.put("vacationApproved", "false");
        taskVariables.put("managerMotivation", "We have a tight deadline!");
        taskService.complete(task.getId(), taskVariables);

        repositoryService.suspendProcessDefinitionByKey("vacationRequest");
//        try {
//            runtimeService.startProcessInstanceByKey("vacationRequest");
//        } catch (ActivitiException e) {
//            e.printStackTrace();
//        }
        List<Task> tasks2 = taskService.createTaskQuery()
                .taskAssignee("kermit")
                .processVariableValueEquals("orderId", "0815")
                .orderByDueDateNullsFirst().asc()
                .list();
    }
}