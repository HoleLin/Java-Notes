package com.holelin.kafka.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2020/9/28 10:53
 * @UpdateUser: HoleLin
 * @UpdateDate: 2020/9/28 10:53
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Slf4j
@RestController
public class KafkaProducer {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    private ReplyingKafkaTemplate template;
    @GetMapping("/test/{message}")
    public void sendMessage1(@PathVariable("message") String normalMessage) {
        kafkaTemplate.send("cjl-test", normalMessage).addCallback(success->{
            log.info("数据信息:{}",success.getRecordMetadata());
            log.info("数据信息:{}",success.getProducerRecord());
        },failure->{
            log.info("数据错误信息:{}",failure);
        });
    }

    @GetMapping("/send/{input}")
    public void sendFoo(@PathVariable String input) {
        kafkaTemplate.executeInTransaction(t ->{
            t.send("topic_input","kl");
            if("error".equals(input)){
                throw new RuntimeException("failed");
            }
            t.send("topic_input","ckl");
            return true;
        });
    }
    @GetMapping("/send2/{input}")
    @Transactional(rollbackFor = RuntimeException.class)
    public void sendFooByAnnotation(@PathVariable String input) {
        kafkaTemplate.send("topic_input2", "kl");
        if ("error".equals(input)) {
            throw new RuntimeException("failed");
        }
        kafkaTemplate.send("topic_input2", "ckl");
    }

    @GetMapping("/sendAndReceive/{input}")
    @Transactional(rollbackFor = RuntimeException.class)
    public void sendAndReceive(@PathVariable String input) throws Exception {
        ProducerRecord<String, String> record = new ProducerRecord<>("topic-kl", input);
        RequestReplyFuture<String, String, String> replyFuture = template.sendAndReceive(record);
        ConsumerRecord<String, String> consumerRecord = replyFuture.get();
        log.info("Return value: " + consumerRecord.value());
    }

    @Autowired
    private KafkaListenerEndpointRegistry registry;

    @GetMapping("/pause/{listenerID}")
    public void stop(@PathVariable String listenerID){
        registry.getListenerContainer(listenerID).pause();
    }
    @GetMapping("/resume/{listenerID}")
    public void resume(@PathVariable String listenerID){
        registry.getListenerContainer(listenerID).resume();
    }
    @GetMapping("/start/{listenerID}")
    public void start(@PathVariable String listenerID){
        registry.getListenerContainer(listenerID).start();
    }


    @GetMapping("/send3/{input}")
    public void sendFoo3(@PathVariable String input) {
        template.send("topic-kl", input);
    }

}
