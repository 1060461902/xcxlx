package com.e.dao.mysql.pay;

import com.alibaba.fastjson.JSON;
import com.e.model.pay.ShowOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by asus on 2017/11/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/config.xml"})
public class ShowOrderDaoTest {
    @Autowired
    ShowOrderDao dao;
    @Test
    public void getAll() throws Exception {
        List<ShowOrder> list = dao.getAll();
        String json = JSON.toJSONString(list);
        System.out.println(json);
    }

    @Test
    public void getOnePersonAll() throws Exception {
        List<ShowOrder> list = dao.getOnePersonAll("12381dasdh1231");
        String json = JSON.toJSONString(list);
        System.out.println(json);
    }

    @Test
    public void getTheStatusAll() throws Exception {
        List<ShowOrder> list = dao.getTheStatusAll(0);
        String json = JSON.toJSONString(list);
        System.out.println(json);
    }

    @Test
    public void getOnePersonTheStatusAll() throws Exception {
        List<ShowOrder> list = dao.getOnePersonTheStatusAll(0,"12381dasdh1231");
        String json = JSON.toJSONString(list);
        System.out.println(json);
    }

}