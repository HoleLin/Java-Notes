package com.holelin.sundry.test;

import com.holelin.sundry.SundryApplication;
import com.holelin.sundry.utils.MailUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SundryApplication.class)
public class MailTest {

    @Autowired
    private MailUtils mailUtils;

    @Test
    public void test() throws MessagingException {
        Set<String> recipients = new HashSet<>();
        recipients.add("HoleLin@163.com");
        mailUtils.send(recipients);
    }
}
