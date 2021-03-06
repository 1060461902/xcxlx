package com.e.dao.mysql.user;

import com.e.model.user.UserAddress;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by asus on 2017/10/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/config.xml"})
public class UserAddressDaoTest {
    @Autowired
    UserAddressDao dao;
    @Test
    public void add() throws Exception {
        UserAddress userAddress = new UserAddress();
        userAddress.setAddress_id("8h321");
        userAddress.setOpenid("123asd");
        userAddress.setPhone("12312312312");
        userAddress.setUser_name("张三");
        userAddress.setAddress("中国");
        assertTrue(dao.add(userAddress));
    }

    @Test
    public void update(){
        UserAddress userAddress = new UserAddress();
        userAddress.setAddress_id("8h321");
        userAddress.setOpenid("123asd");
        userAddress.setPhone("12312312312");
        userAddress.setUser_name("李四");
        userAddress.setAddress("中国");
        assertTrue(dao.update(userAddress));
    }

    @Test
    public void delete(){
        UserAddress userAddress = new UserAddress();
        assertTrue(dao.delete("8h321","123asd"));
    }

    @Test
    public void get(){
        String openid = "12381dasdh1231";
        List<UserAddress> list = dao.get(openid);
        System.out.println(list.get(0).getAddress_id());
    }

    @Test
    public void getTheAddress(){
        UserAddress address = dao.getTheAddress("6f8E1UyHtVV9454doLnm9xnsNkp4KB","12381dasdh1231");
        System.out.println(address.getAddress());
    }
}