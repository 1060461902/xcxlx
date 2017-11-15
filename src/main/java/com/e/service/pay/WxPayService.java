package com.e.service.pay;

import com.e.model.pay.Freight;
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

    public BigDecimal calculation(double weight, Freight freight){
        BigDecimal rd = null;//需要返回的rd

        if (weight<=1){ //小于1kg按首重
            rd = new BigDecimal(freight.getFirst_weight().toString());
        }else {
            BigDecimal firestWeightPrice = new BigDecimal(freight.getFirst_weight().toString());
            firestWeightPrice.setScale(1);
            BigDecimal continueWeightPrice = new BigDecimal(freight.getContinue_weight().toString());
            continueWeightPrice.setScale(1);
            weight = weight - 1;
            String s = Double.toString(weight);
            BigDecimal wgt = new BigDecimal(s);
            wgt.setScale(1);//重量保留一位小数
        }
        rd.setScale(1);//保留一位小数
        return rd;
    }
}
