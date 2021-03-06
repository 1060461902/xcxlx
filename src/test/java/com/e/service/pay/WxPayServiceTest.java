package com.e.service.pay;

import com.e.model.pay.Freight;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

/**
 * Created by asus on 2017/11/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/config.xml"})
public class WxPayServiceTest {
    @Autowired
    WxPayService service;
    @Test
    public void createOrder() throws Exception {

    }

    @Test
    public void getOrder() throws Exception {

    }

    @Test
    public void calculation() throws Exception {
        Freight freight = new Freight();
        freight.setFirst_weight(Double.valueOf(5));
        freight.setContinue_weight(Double.valueOf(1));
        System.out.println(service.calculation(10.7,freight,1).multiply(new BigDecimal("100")).setScale(0));
    }

    @Test
    public void getFrightPrice()throws Exception{
        String address = "浙江省杭州市江干区";
        System.out.println(service.getFreightPrice(address,1.5,"SF"));
        String address1 = "黑龙江省贺州市";
        System.out.println(service.getFreightPrice(address1,1.2,"SF"));
        String address2 = "上海市闵行区";
        System.out.println(service.getFreightPrice(address2,2.0,"SF"));
        String address3 = "内蒙古自治区呼伦贝尔市";
        System.out.println(service.getFreightPrice(address3,3.0,"SF"));
    }

}