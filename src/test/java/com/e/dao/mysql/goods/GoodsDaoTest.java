package com.e.dao.mysql.goods;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.e.model.goods.Goods;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by asus on 2017/11/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/config.xml"})
public class GoodsDaoTest {
    @Autowired
    GoodsDao dao;
    @Test
    public void addORUpdate() throws Exception {
        List<Goods>list = new ArrayList<>();
        Goods goods = new Goods();
        goods.setGoods_id("12983h13hasd");
        goods.setGoods_name("1233123safasd");
        goods.setGoods_num(131);
        goods.setGoods_price(9000);
        Goods goods2 = new Goods();
        goods2.setGoods_id("13jffs3hasd");
        goods2.setGoods_name("123312312dasdd");
        goods2.setGoods_price(9900);
        goods2.setGoods_img("123123saddvz1323/e123123");
        list.add(goods);
        list.add(goods2);
        assertTrue(dao.addORUpdate(list));
    }

    @Test
    public void delete() throws Exception {
        assertTrue(dao.delete("13jffs3hasd"));
    }

    @Test
    public void getAll() throws Exception {
        System.out.println(JSON.toJSONString(dao.getAll(), SerializerFeature.WriteMapNullValue));
    }

    @Test
    public void update(){
        Goods goods = new Goods();
        goods.setGoods_id("12983h13hasd");
        goods.setGoods_name("1233123safasd");
        goods.setGoods_num(130);
        goods.setGoods_price(9000);
        assertTrue(dao.update(goods));
    }
}