package com.holelin.kafka.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2020/9/28 10:54
 * @UpdateUser: HoleLin
 * @UpdateDate: 2020/9/28 10:54
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Slf4j
@Component
public class KafkaConsumer {

    @KafkaListener(topics = {"cjl-test"})
    public void listen1(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());

        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            log.info("消费信息的Topic:{},Offset:{},Value:{}", record.topic(), record.offset(), record.value());
            log.info("消费信息的Record:{}", message);
        }
    }

    @KafkaListener(topics = {"topic_input"})
    public void listen2(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());

        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            log.info("消费信息的Topic:{},Offset:{},Value:{}", record.topic(), record.offset(), record.value());
            log.info("消费信息的Record:{}", message);
        }
    }

    @KafkaListener(id = "webGroup1", topics = "topic-kl")
    @SendTo
    public String listen(String input) {
        log.info("input value: {}", input);
        return "successful";
    }


    @KafkaListener(id = "webGroup3", topics = "topic_input2",autoStartup = "false")
    public String listen3(String input) {
        log.info("input value: {}", input);
        return "successful";
    }


    @KafkaListener(id = "webGroup4", topics = "topic-kl")
    public String listen4(String input) {
        log.info("input value: {}", input);
        throw new RuntimeException("dlt");
    }

    @KafkaListener(id = "dltGroup", topics = "topic-kl.DLT")
    public void dltListen(String input) {
        log.info("Received from DLT: " + input);
    }
}
