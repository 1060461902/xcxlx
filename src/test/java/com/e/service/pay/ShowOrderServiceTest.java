package com.e.service.pay;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by asus on 2017/11/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/config.xml"})
public class ShowOrderServiceTest {
    @Autowired
    ShowOrderService showOrderService;
    @Test
    public void sendObjectEmail() throws Exception {

    }
}