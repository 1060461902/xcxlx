package com.e.support.util;

import com.github.wxpay.sdk.WXPayUtil;

import java.util.ResourceBundle;

/**
 * Created by asus on 2017/11/26.
 */
public class PayUtil {
    /**
     * 根据appid,nonceStr,prepay_id,timeStamp,key生成paySign
     * @param appId appid
     * @param nonceStr 随机字符串
     * @param prepay_id 微信服务器返回的预支付订单号
     * @param timeStamp 时间戳
     * @return 生成的paySign
     * */
    public static String getPaySign(String appId,String nonceStr,String prepay_id,String timeStamp) throws Exception {
        ResourceBundle rb = ResourceBundle.getBundle("weixin");
        String signString = "appId="+appId+"&" +
                "nonceStr="+nonceStr+"&" +
                "package=prepay_id="+prepay_id+"&" +
                "signType=MD5&" +
                "timeStamp="+timeStamp+"&" +
                "key="+rb.getString("key");
        return WXPayUtil.MD5(signString);
    }
}
