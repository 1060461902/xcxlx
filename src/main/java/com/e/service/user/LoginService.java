package com.e.service.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.e.controller.user.LoginController;
import com.e.dao.redis.RedisDS;
import com.e.dao.redis.RedisSaveSession;
import com.e.model.ErrorMsg;
import com.e.model.user.XCXUserSessionInfo;
import com.e.support.util.Create3rdSessionID;
import com.e.support.util.StringFromIsUtil;
import com.e.support.util.json.LoginSessionKeyUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author asus
 * @date 2017/10/28
 * 调起登录service
 */
@Service
public class LoginService {
    @Autowired
    RedisSaveSession saveSession;
    @Autowired
    RedisDS redisDS;
    /**
     * 登录
     *
     * */
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
                        //设置过期时间30天
                        60*60*24*30);
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
    /**
     * 保持3rd_sessionID的有效性
     * */
    public boolean keepId(HttpServletRequest request) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String json = StringFromIsUtil.getData(request.getInputStream(),"UTF-8");
        JSONObject jsonObject = JSON.parseObject(json);
        //获取前端传来的3rd_sessionID
        String thirdSessionID = jsonObject.getString("thirdsessionid");
        return redisDS.resetExpireTime(thirdSessionID,30*60*60*24);
    }
}
