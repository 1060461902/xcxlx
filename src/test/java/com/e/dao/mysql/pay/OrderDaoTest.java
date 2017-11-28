package com.e.dao.mysql.pay;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by asus on 2017/11/28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/config.xml"})
public class OrderDaoTest {
    @Autowired
    OrderDao orderDao;
    @Test
    public void getOrderIDByPAS() throws Exception {
        List<String> list = orderDao.getOrderIDByPAS(1,"12381dasdh1231");
        for (int i=0;i<list.size();i++){
            System.out.println(list.get(i));
        }
    }
}