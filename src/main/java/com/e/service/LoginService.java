package com.e.service;

import com.alibaba.fastjson.JSON;
import com.e.controller.LoginController;
import com.e.dao.redis.RedisSaveSession;
import com.e.model.ErrorMsg;
import com.e.model.XCXUserSessionInfo;
import com.e.support.util.Create3rdSessionID;
import com.e.support.util.StringFromIsUtil;
import com.e.support.util.json.LoginSessionKeyUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by asus on 2017/10/28.
 */
@Service
public class LoginService {
    @Autowired
    RedisSaveSession saveSession;

    public String login(InputStream inputStream) throws IOException {
        //获取从前端传来的json
        String json1 = StringFromIsUtil.getData(inputStream,"UTF-8");
        //获取json对象中的code属性
        String code = JSON.parseObject(json1).getString("code");
        String json2 = "";
        //从微信服务器返回的用户会话信息对象
        XCXUserSessionInfo userInfo;
        //随机生成的第三方sessionID
        String thirdSessionId = null;
        inputStream.close();
        if (code!=null&&code!=""){
            //使用code从微信服务器换取用户会话信息
            json2 = LoginSessionKeyUtil.getJSONData(code);
            if (JSON.parseObject(json2).getString("openid")!=null) {

                /*在过滤器中还要验证登录,前端会带上3rdsessionid
                还要将openid存入数据库中，判断数据库中是否已有该用户,无则创建*/

                userInfo = JSON.parseObject(json2, XCXUserSessionInfo.class);
                //判断操作系统类型
                String os = System.getProperty("os.name");
                if (os.contains("Windows")){
                    thirdSessionId = Create3rdSessionID.createByOTH();
                }else {
                    thirdSessionId = Create3rdSessionID.createByUnix();
                }
                //存储3rd_session到redis中
                boolean b = saveSession.save(
                        thirdSessionId,
                        userInfo.getSession_key()+","+userInfo.getOpenid(),
                        60*60*24*30);//设置过期时间30天
                if (b==false){
                    return "Can't save into redis location:LoginController";
                }
            }else {
                ErrorMsg errorMsg = JSON.parseObject(json2,ErrorMsg.class);
                Logger logger = Logger.getLogger(LoginController.class);
                logger.error(JSON.toJSONString(errorMsg));
                return "error:"+errorMsg.getErrcode()+" "+"Msg:"+errorMsg.getErrmsg();
            }
        }
        return thirdSessionId;
    }
}
