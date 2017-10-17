package com.e.support.util;

import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * Created by asus on 2017/10/14.
 * 生成随机3rd_sessionID
 */
public class Create3rdSessionID {
    /*
    * @param cmd需要执行的操作系统命令
    * @return 返回执行后的信息
    * */
    public static String create(String cmd){
        String wxSessionkey = "err";
        StringBuffer sb = new StringBuffer();
        try {
            String[] cmdA = { "/bin/sh", "-c", cmd };
            Process process = Runtime.getRuntime().exec(cmdA);
            LineNumberReader br = new LineNumberReader(new InputStreamReader(
                    process.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            //如果本地测试，会报空指针异常，所以为了不让报错，索性返回有值即可
            sb.append(wxSessionkey);
        }
        return sb.toString();
    }
    /*
    * 类Unix系统上生成168位随机数
    * */
    public static String createByUnix(){
        return create("head -n 80 /dev/urandom | tr -dc A-Za-z0-9 | head -c 168");
    }
}
