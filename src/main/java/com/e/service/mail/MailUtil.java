package com.e.service.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Created by asus on 2017/11/5.
 */
@Service
public class MailUtil {
    /*@Autowired
    private MailSender mailSender;*/
    @Autowired
    private SimpleMailMessage simpleMailMessage;
    @Autowired
    private JavaMailSender javaMailSender;
    /*//Spring 依赖注入
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    //Spring 依赖注入
    public void setSimpleMailMessage(SimpleMailMessage simpleMailMessage) {
        this.simpleMailMessage = simpleMailMessage;
    }*/
    /**
     * 单发
     *
     * @param recipient 收件人
     * @param subject 主题
     * @param content 内容
     */
    public void send(String recipient,String subject,String content) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,true);
        messageHelper.setFrom(simpleMailMessage.getFrom());
        messageHelper.setTo(recipient);
        messageHelper.setSubject(subject);
        messageHelper.setText(content,true);
        javaMailSender.send(mimeMessage);
    }
    /**
     * 群发
     *
     * @param recipients 收件人
     * @param subject 主题
     * @param content 内容
     */
    /*public void send(List<String> recipients,String subject,String content){
        simpleMailMessage.setTo(recipients.toArray(new String[recipients.size()]));
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(content);
        mailSender.send(simpleMailMessage);
    }*/
}
