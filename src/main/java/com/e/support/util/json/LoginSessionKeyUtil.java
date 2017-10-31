package com.e.support.util.json;

import com.e.support.util.RequestForJsonUtil;

import java.util.ResourceBundle;

/**
 * Created by asus on 2017/10/12.
 */
public class LoginSessionKeyUtil{
    /**
    * 使用jscode换取微信服务器的用户会话信息
     * @param jscode 调起登录时前端传来的code
    * @return 微信后台返回的json格式字符
     * */
    public static String getJSONData(String jscode){
        ResourceBundle rb = ResourceBundle.getBundle("weixin");
        String uri = "https://api.weixin.qq.com/sns/jscode2session?" +
                "appid="+rb.getString("appid")+"&" +
                "secret="+rb.getString("appsecret")+"&" +
                "js_code="+jscode+"&" +
                "grant_type=authorization_code";
        String json = RequestForJsonUtil.getJsonByGet(uri);
        return json;
    }
}
