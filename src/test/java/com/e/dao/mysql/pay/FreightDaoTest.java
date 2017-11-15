package com.e.dao.mysql.pay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.e.model.pay.Freight;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by asus on 2017/11/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/config.xml"})
public class FreightDaoTest {
    @Autowired
    FreightDao dao;
    @Test
    public void getFreight() throws Exception {
        Freight freight = dao.getFreight("浙江","杭州");
        String firstweight = freight.getFirst_weight().toString();
        BigDecimal decimal = new BigDecimal(firstweight);
        decimal.setScale(1);
        System.out.println(decimal);
    }

    @Test
    public void insert(){
        Freight freight = new Freight();
        freight.setFreight_id("1892371982");
        freight.setProvince("黑龙江");
        freight.setFirst_weight(Double.valueOf(25));
        freight.setContinue_weight(Double.valueOf(20));
        assertTrue(dao.insert(freight));
    }

    @Test
    public void update(){
        Freight freight = new Freight();
        freight.setFreight_id("123");
        freight.setProvince("浙江");
        freight.setFlow_place("杭州,湖州,嘉兴,宁波,绍兴,台州,温州,衢州,丽水,金华,舟山");
        freight.setFirst_weight(Double.valueOf(23));
        freight.setContinue_weight(Double.valueOf(13));
        assertTrue(dao.update(freight));
    }

    @Test
    public void addORUpdate(){
        List<Freight>freights = new ArrayList<>();
        Freight freight1 = new Freight();
        freight1.setFreight_id("123");
        freight1.setProvince("浙江");
        freight1.setFlow_place("杭州,湖州,嘉兴,宁波,绍兴,台州,温州,衢州,丽水,金华,舟山");
        freight1.setFirst_weight(Double.valueOf(23));
        freight1.setContinue_weight(Double.valueOf(13));
        freights.add(freight1);
        Freight freight2 = new Freight();
        freight2.setFreight_id("12asd");
        freight2.setProvince("内蒙古");
        freight2.setFlow_place("呼伦贝尔,兴安盟");
        freight2.setFirst_weight(Double.valueOf(25));
        freight2.setContinue_weight(Double.valueOf(20));
        freights.add(freight2);
        assertTrue(dao.addORUpdate(freights));
    }
    @Test
    public void delete(){
        assertTrue(dao.delete("asd"));
    }
    @Test
    public void getAll(){
        List<Freight>list = dao.getAll();
        System.out.println(JSON.toJSONString(list,SerializerFeature.WriteNullStringAsEmpty));
    }
}