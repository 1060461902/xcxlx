package com.e.service.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.e.dao.mysql.user.UserAddressDao;
import com.e.dao.redis.RedisDS;
import com.e.model.user.UserAddress;
import com.e.support.util.Create3rdSessionID;
import com.e.support.util.StringFromIsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * Created by asus on 2017/10/29.
 * 用户地址管理界面相关service
 */
@Service
public class AddressService {
    @Autowired
    UserAddressDao dao;
    @Autowired
    RedisDS redisDS;
    public String add(HttpServletRequest request) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String json = StringFromIsUtil.getData(request.getInputStream(),"UTF-8");
        JSONObject jsonObject = JSON.parseObject(json);
        //获取前端传来的3rd_sessionID
        String thirdSessionID = jsonObject.getString("thirdsessionid");
        //获取在redis中存储的用户信息
        String sessionValue = redisDS.getByKey(thirdSessionID);
        if (sessionValue!=null&&!sessionValue.equals("")){
            //重置过期时间30天
            boolean b2 = redisDS.resetExpireTime(thirdSessionID,30*60*60*24);
            //如果重置不成功直接返回false
            if (!b2){
                Logger logger = LoggerFactory.getLogger(AddressService.class);
                logger.error("can't reset time:redis");
                return "fail";
            }
        }else {
            //如果redis中3rd_sessionID失效,直接返回false
            return "lose";
        }
        //获取openid
        String openid = sessionValue.split(",")[1];
        //获取前端传来的用户信息并存入对象
        String user_name = jsonObject.getString("user_name");
        String phone = jsonObject.getString("phone");
        String address = jsonObject.getString("address");
        String address_id = Create3rdSessionID.createRandomNum(30);
        UserAddress userAddress = new UserAddress();
        userAddress.setAddress(address);
        userAddress.setUser_name(user_name);
        userAddress.setOpenid(openid);
        userAddress.setPhone(phone);
        userAddress.setAddress_id(address_id);
        if (dao.add(userAddress)){
            return "suc";
        }else {
            return "fail";
        }
    }
    public String delete(HttpServletRequest request) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String json = StringFromIsUtil.getData(request.getInputStream(),"UTF-8");
        JSONObject jsonObject = JSON.parseObject(json);
        //获取前端传来的3rd_sessionID
        String thirdSessionID = jsonObject.getString("thirdsessionid");
        //获取在redis中存储的用户信息
        String sessionValue = redisDS.getByKey(thirdSessionID);
        if (sessionValue!=null&&!sessionValue.equals("")){
            //重置过期时间30天
            boolean b2 = redisDS.resetExpireTime(thirdSessionID,30*60*60*24);
            //如果重置不成功直接返回false
            if (!b2){
                Logger logger = LoggerFactory.getLogger(AddressService.class);
                logger.error("can't reset time:redis");
                return "fail";
            }
        }else {
            return "lose";
        }
        //获取openid
        String openid = sessionValue.split(",")[1];
        String address_id = jsonObject.getString("address_id");
        if (dao.delete(address_id,openid)){
            return "suc";
        }else {
            return "fail";
        }
    }
    public String update(HttpServletRequest request) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String json = StringFromIsUtil.getData(request.getInputStream(),"UTF-8");
        JSONObject jsonObject = JSON.parseObject(json);
        //获取前端传来的3rd_sessionID
        String thirdSessionID = jsonObject.getString("thirdsessionid");
        //获取在redis中存储的用户信息
        String sessionValue = redisDS.getByKey(thirdSessionID);
        if (sessionValue!=null&&!sessionValue.equals("")){
            //重置过期时间30天
            boolean b2 = redisDS.resetExpireTime(thirdSessionID,30*60*60*24);
            //如果重置不成功直接返回false
            if (!b2){
                Logger logger = LoggerFactory.getLogger(AddressService.class);
                logger.error("can't reset time:redis");
                return "fail";
            }
        }else {
            //如果redis中3rd_sessionID失效,返回"lose"
            return "lose";
        }
        //获取openid
        String openid = sessionValue.split(",")[1];
        //获取前端传来的用户信息并存入对象
        String address_id = jsonObject.getString("address_id");
        String user_name = jsonObject.getString("user_name");
        String phone = jsonObject.getString("phone");
        String address = jsonObject.getString("address");
        UserAddress userAddress = new UserAddress();
        userAddress.setAddress(address);
        userAddress.setUser_name(user_name);
        userAddress.setOpenid(openid);
        userAddress.setPhone(phone);
        userAddress.setAddress_id(address_id);
        if (dao.update(userAddress)){
            return "suc";
        }else {
            return "fail";
        }
    }
    public String get(HttpServletRequest request) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String json = StringFromIsUtil.getData(request.getInputStream(),"UTF-8");
        JSONObject jsonObject = JSON.parseObject(json);
        //获取前端传来的3rd_sessionID
        String thirdSessionID = jsonObject.getString("thirdsessionid");
        //获取在redis中存储的用户信息
        String sessionValue = redisDS.getByKey(thirdSessionID);
        if (sessionValue!=null&&!sessionValue.equals("")){
            //重置过期时间30天
            boolean b2 = redisDS.resetExpireTime(thirdSessionID,30*60*60*24);
            //如果重置不成功直接返回空字符串
            if (!b2){
                org.slf4j.Logger logger = LoggerFactory.getLogger(AddressService.class);
                logger.error("can't reset time:redis");
                return "";
            }
        }else {
            return "lose";
        }
        //获取openid
        String openid = sessionValue.split(",")[1];
        List<UserAddress>list = dao.get(openid);
        //过滤掉openid，不能传给前端
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter(UserAddress.class,"address_id","user_name","phone","address");
        String retjson = JSON.toJSONString(list,filter);
        return retjson;
    }
}
