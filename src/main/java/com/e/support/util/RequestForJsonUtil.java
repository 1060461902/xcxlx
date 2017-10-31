package com.e.support.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created by asus on 2017/7/6.
 * 请求指定url，返回响应数据字符串
 */
public class RequestForJsonUtil {
    /**
    * 使用GET方法请求指定url并返回请求数据的字符串格式，失败将返回""
    * @param url 需要请求的url
    * @return string 返回的数据字符串
    * */
    public static String getJsonByGet(String url){
        String jsonString;
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet get = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(get);
            HttpEntity httpEntity = httpResponse.getEntity();
            jsonString = EntityUtils.toString(httpEntity,"utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            Logger logger = Logger.getLogger(RequestForJsonUtil.class);
            logger.error("can't get session_key from weChat server location:RequestForJsonUtil");
            return "";
        }
        return jsonString;
    }
}
