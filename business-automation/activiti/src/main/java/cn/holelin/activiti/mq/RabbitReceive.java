package cn.holelin.activiti.mq;

import cn.holelin.activiti.domain.DetectionBean;
import cn.holelin.activiti.enums.CodeEnum;
import com.mysql.cj.jdbc.exceptions.MySQLTransactionRollbackException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2021/11/22 4:11 下午
 * @UpdateUser: HoleLin
 * @UpdateDate: 2021/11/22 4:11 下午
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Slf4j
@Component
public class RabbitReceive {

    @Autowired
    private TaskService taskService;

    /**
     * 组合使用监听
     *
     * @param message
     * @param channel
     * @throws Exception
     * @RabbitListener @QueueBinding @Queue @Exchange
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${spring.rabbitmq.listener.algorithm.exchange.queue}",
                    durable = "${spring.rabbitmq.listener.algorithm.exchange.durable}"),
            exchange = @Exchange(name = "${spring.rabbitmq.listener.algorithm.exchange.name}",
                    durable = "${spring.rabbitmq.listener.algorithm.exchange.durable}",
                    type = "${spring.rabbitmq.listener.algorithm.exchange.type}",
                    ignoreDeclarationExceptions = "true"),
            key = "${spring.rabbitmq.listener.algorithm.exchange.key}"
    )
    )
    @RabbitHandler
    public void onMessage(Message message, Channel channel) throws Exception {
        //	1. 收到消息以后进行业务端消费处理
        DetectionBean detectionBean = (DetectionBean) message.getPayload();
        final String code = detectionBean.getCode();
        final String taskId = detectionBean.getTaskId();
        final Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Long deliveryTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        if (Objects.nonNull(task)) {
            log.info("消费消息:" + message.getPayload());
            if (CodeEnum.SCRYN_CPU_CODE.value.equals(code)) {
                taskService.setVariableLocal(taskId,CodeEnum.SCRYN_CPU_CODE.value, 200);
            } else if (CodeEnum.SCRYN_GPU_CODE.value.equals(code)) {
                taskService.setVariableLocal(taskId,CodeEnum.SCRYN_GPU_CODE.value, 200);
            } else if (CodeEnum.SURGI_CPU_CODE.value.equals(code)) {
                taskService.setVariableLocal(taskId,CodeEnum.SURGI_CPU_CODE.value, 200);
            } else if (CodeEnum.SURGI_GPU_CODE.value.equals(code)) {
                taskService.setVariableLocal(taskId,CodeEnum.SURGI_GPU_CODE.value, 200);
            }
            try {
                Thread.sleep(new Random().nextInt(10000));
                taskService.complete(taskId);
                channel.basicAck(deliveryTag, false);
            }catch (Exception e){
                log.error("异常");
                channel.basicNack(deliveryTag, false, true);
            }
        }
        channel.basicNack(deliveryTag, false, true);
    }

}
