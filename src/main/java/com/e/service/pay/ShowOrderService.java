package com.e.service.pay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.e.dao.mysql.pay.ShowOrderDao;
import com.e.dao.redis.RedisDS;
import com.e.model.pay.ShowOrder;
import com.e.service.mail.MailService;
import com.e.service.user.AddressService;
import com.e.support.util.StringFromIsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by asus on 2017/11/18.
 */
@Service
public class ShowOrderService {
    @Autowired
    ShowOrderDao showOrderDao;
    @Autowired
    RedisDS redisDS;
    @Autowired
    MailService mailService;

    /**
     * 获取所有订单
     * */
    public String getAll(){
        List<ShowOrder>showOrderList = showOrderDao.getAll();
        return JSON.toJSONString(showOrderList);
    }
    /**
     * @param request 只需要thirdsessionid
     * @return "fail"重置失败 "lose"3rd_sessionID失效 正常：json格式字符串
     * */
    public String getOnePersonAll(HttpServletRequest request) throws IOException {
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
        return JSON.toJSONString(showOrderDao.getOnePersonAll(openid));
    }
    /**
     * @param request 只需要order_status
     * @return json格式字符串
     * */
    public String getTheStatusAll(HttpServletRequest request) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String json = StringFromIsUtil.getData(request.getInputStream(),"UTF-8");
        JSONObject jsonObject = JSON.parseObject(json);
        int order_status = jsonObject.getInteger("order_status");
        List<ShowOrder>showOrderList = showOrderDao.getTheStatusAll(order_status);
        return JSON.toJSONString(showOrderList);
    }
    /**
     * 获取某人某状态的订单
     * @param request 需要 thirdsessionid,order_status
     * @return "fail"重置失败 "lose"3rd_sessionID失效 正常：json格式字符串
     * */
    public String getOnePersonTheStatusAll(HttpServletRequest request) throws IOException {
        JSONObject result = new JSONObject();
        request.setCharacterEncoding("UTF-8");
        String json = StringFromIsUtil.getData(request.getInputStream(),"UTF-8");
        JSONObject jsonObject = JSON.parseObject(json);
        //获取前端传来的3rd_sessionID
        String thirdSessionID = jsonObject.getString("thirdsessionid");
        int order_status = jsonObject.getInteger("order_status");
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
        List<ShowOrder>showOrders = showOrderDao.getOnePersonTheStatusAll(order_status,openid);
        ShowOrder showOrder = showOrders.get(0);
        result.put("express_company_id",showOrder.getExpress_company_id());
        result.put("address",showOrder.getAddress());
        result.put("freight",showOrder.getFreight());
        result.put("order_id",showOrder.getOrder_id());
        result.put("order_time",showOrder.getOrder_time());
        result.put("order_wx_id",showOrder.getOrder_wx_id());
        result.put("phone",showOrder.getPhone());
        result.put("user_add_message",showOrder.getUser_add_message());
        result.put("user_name",showOrder.getUser_name());
        result.put("order_status",showOrder.getOrder_status());
        JSONArray showOrderArray = new JSONArray();
        for (int i=0;i<showOrders.size();i++){
            ShowOrder indexOrder = showOrders.get(i);
            JSONObject goods_info = new JSONObject();
            goods_info.put("goods_id",indexOrder.getGoods_id());
            goods_info.put("goods_img",indexOrder.getGoods_img());
            goods_info.put("goods_name",indexOrder.getGoods_name());
            goods_info.put("goods_number",indexOrder.getGoods_number());
            goods_info.put("goods_price",indexOrder.getGoods_price());
            showOrderArray.add(goods_info);
        }
        result.put("order_array",showOrderArray);
        System.out.println(result.toJSONString());
        return result.toJSONString();
    }
    /**
     * 获取某个展示型订单对象
     * @param order_id 订单号
     * @return 展示型订单对象
     * */
    public List<ShowOrder> getTheOrder(String order_id){
        return showOrderDao.getTheShowOrder(order_id);
    }
    /**
     * 根据对象发送订单邮件
     * */
    public <T>void sendObjectEmail(List<T> list,Class<T> cls) throws MessagingException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String dt = format.format(date);
        Field[] fields = cls.getDeclaredFields();
        StringBuffer buffer = new StringBuffer();
        buffer.append("<html><body>");
        for (int i=0;i<list.size();i++) {
            T t = list.get(i);
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    buffer.append("<p>" + field.getName() + " : " + field.get(t));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        buffer.append("</body></html>");
        mailService.send("xlxhost@126.com","订单"+dt,buffer.toString());
    }
}
