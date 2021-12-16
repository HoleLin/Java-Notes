package cn.holelin.activiti.mq;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2021/11/22 4:11 下午
 * @UpdateUser: HoleLin
 * @UpdateDate: 2021/11/22 4:11 下午
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

@Component
public class RabbitSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Value(value = "${spring.rabbitmq.listener.algorithm.exchange.name}")

    private String exchangeName;

    @Value(value = "${spring.rabbitmq.listener.algorithm.exchange.key}")
    private String exchangeKey;

    /**
     * 	这里就是确认消息的回调监听接口，用于确认消息是否被broker所收到
     */
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        /**
         * 	@param ack broker 是否落盘成功
         * 	@param cause 失败的一些异常信息
         */
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            System.err.println("消息ACK结果:" + ack + ", correlationData: " + correlationData.getId());
        }
    };

    /**
     * 	对外发送消息的方法
     * @param message 	具体的消息内容
     * @param properties	额外的附加属性
     * @throws Exception
     */
    public void send(Object message, Map<String, Object> properties) throws Exception {

        MessageHeaders mhs = new MessageHeaders(properties);
        Message<?> msg = MessageBuilder.createMessage(message, mhs);

        rabbitTemplate.setConfirmCallback(confirmCallback);

        // 	指定业务唯一的iD
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());

        MessagePostProcessor mpp = message1 -> {
            return message1;
        };
        rabbitTemplate.convertAndSend(exchangeName, exchangeKey, msg, mpp, correlationData);
    }
}