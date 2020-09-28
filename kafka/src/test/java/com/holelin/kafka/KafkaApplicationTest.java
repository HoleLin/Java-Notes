package com.holelin.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @Description: kafka测试项目
 * @Author: HoleLin
 * @CreateDate: 2020/9/28 10:48
 * @UpdateUser: HoleLin
 * @UpdateDate: 2020/9/28 10:48
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KafkaApplicationTest.class)
@EmbeddedKafka(count = 4,ports = {9092,9093,9094,9095})
public class KafkaApplicationTest {

    @Autowired
    private KafkaProperties properties;
    @Test
    public void testCreateToipc(){
        AdminClient client = AdminClient.create(properties.buildAdminProperties());
        if(client !=null){
            try {
                Collection<NewTopic> newTopics = new ArrayList<>(1);
                newTopics.add(new NewTopic("topic-kl",1,(short) 1));
                client.createTopics(newTopics);
            }catch (Throwable e){
                e.printStackTrace();
            }finally {
                client.close();
            }
        }
    }

}
