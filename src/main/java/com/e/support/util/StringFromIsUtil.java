package com.e.support.util;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by asus on 2017/4/12.
 * 将前台传入的json格式的数据转换为String
 * 传入的参数为（request的InputStream对象）
 * 示例：
 * InputStream is=requset.getInputStream();
 * String json = JsonUtil.getJsonString(is);
 * (需要手动close is对象)
 * 该方法会抛出IOExeption异常，此异常产生的原因是isr或br未能正常close
 */
public class StringFromIsUtil {
    /*
    * 获取请求对象中传输的数据
    * @param is 输入流
    * @param charserName 指定读取数据的编码格式
    * @return String 返回请求对象的传输数据
    * */
    public static String getData(InputStream is,String charserName) throws IOException {
        StringBuffer data = new StringBuffer() ;
        InputStreamReader isr = new InputStreamReader(is,charserName);
        BufferedReader br = new BufferedReader(isr);
        String s = "" ;
        try {
            while((s=br.readLine())!=null){
                data.append(s) ;
            }
        } catch (IOException e) {
            e.getStackTrace();
            Logger logger = Logger.getLogger(StringFromIsUtil.class);
            logger.error("can't get data from requestInputStream location:StringFromIsUtil");
        }finally {
            isr.close();
            br.close();
        }
        String d =data.toString();
        return d;
    }
}
