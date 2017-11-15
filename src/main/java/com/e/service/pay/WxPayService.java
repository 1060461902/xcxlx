package com.e.service.pay;

import com.e.model.pay.WxPayOrder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by asus on 2017/11/6.
 */
@Service
public class WxPayService {
    public void createOrder(WxPayOrder order) {
    }

    public WxPayOrder getOrder(String out_trade_no) {
        return null;
    }

    public String calculation(double weight){
        String w = Double.toString(weight);
        BigDecimal rd = null;//需要返回的rd
        BigDecimal d1 = new BigDecimal(w);

        d1.setScale(1);//保留一位小数
        return rd.toString();
    }
}
