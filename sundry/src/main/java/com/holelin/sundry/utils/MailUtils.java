package com.holelin.sundry.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Set;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/8/12 14:42
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/8/12 14:42
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Component
public class MailUtils {

    public static final String FROM = "junlin.chen@dianei-ai.com";
    public static final String SUBJECT = "测试主题";

    @Autowired
    private JavaMailSenderImpl sender;

    public void send(Set<String> recipients) throws MessagingException {
        final MimeMessage mimeMessage = sender.createMimeMessage();
        final MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, false);
        messageHelper.setFrom(FROM);
        messageHelper.setSubject(SUBJECT);
        messageHelper.setText("content", false);
        for (String recipient : recipients) {
            messageHelper.addTo(recipient);
        }
        sender.send(messageHelper.getMimeMessage());
    }

}
