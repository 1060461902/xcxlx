package com.e.support.util;

import com.e.model.pay.WxPayOrder;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by asus on 2017/11/7.
 */
public class MOUtilTest {
    @Test
    public void mapToObject() throws Exception {
        Map<String,Object> map = new HashMap<>();
        map.put("appid","123jashdk`231");
        map.put("body","123klHSDFljkh");
        WxPayOrder order = (WxPayOrder) MOUtil.mapToObject(map,WxPayOrder.class);
        System.out.println(order.getAppid());
    }

    @Test
    public void objectToMap() throws Exception {
        WxPayOrder order = new WxPayOrder();
        order.setAppid("132123sadf");
        order.setBody("haiwudhuais");
        order.setOut_trade_no("123123123");
        Map<String,String> map = (Map<String, String>) MOUtil.objectToMap(order);
        System.out.println(map.get("appid"));
        System.out.println(map.get("mch_id"));
    }

}