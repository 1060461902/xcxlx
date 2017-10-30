package com.e.service.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.e.dao.mysql.user.UserAddressDao;
import com.e.dao.redis.RedisDS;
import com.e.model.user.UserAddress;
import com.e.support.util.Create3rdSessionID;
import com.e.support.util.StringFromIsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by asus on 2017/10/29.
 */
@Service
public class AddressService {
    @Autowired
    UserAddressDao dao;
    @Autowired
    RedisDS redisDS;
    public boolean add(HttpServletRequest request) throws IOException {
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
                return false;
            }
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
        return dao.add(userAddress);
    }
    public boolean delete(HttpServletRequest request) throws IOException {
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
                return false;
            }
        }
        //获取openid
        String openid = sessionValue.split(",")[1];
        String address_id = jsonObject.getString("address_id");
        return dao.delete(address_id,openid);
    }
    public boolean update(HttpServletRequest request) throws IOException {
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
                return false;
            }
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
        return dao.update(userAddress);
    }
}
