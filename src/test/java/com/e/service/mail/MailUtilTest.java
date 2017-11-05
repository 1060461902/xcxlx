package com.e.service.mail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by asus on 2017/11/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/config.xml"})
public class MailUtilTest {
    @Autowired
    MailUtil mailUtil;
    @Test
    public void send() throws Exception {
        mailUtil.send("xlxhost@126.com","test12","<html><body><h1>test</h1></body></html>");
    }

}