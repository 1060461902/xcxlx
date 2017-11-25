package com.e.service.pay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.e.dao.mysql.pay.OrderDao;
import com.e.support.util.StringFromIsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by asus on 2017/11/24.
 */
@Service
public class OrderService {
    @Autowired
    OrderDao orderDao;
    /**
     * 更新某订单状态
     * */
    public boolean updateOrderStatus(HttpServletRequest request) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String json = StringFromIsUtil.getData(request.getInputStream(),"UTF-8");
        JSONObject jsonObject = JSON.parseObject(json);
        String order_id = jsonObject.getString("order_id");
        int order_status = jsonObject.getInteger("order_status");
        return orderDao.updateTheOrder(order_id,order_status);
    }
    /**
     * 删除订单
     * */
    public boolean deleteOrder(HttpServletRequest request) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String json = StringFromIsUtil.getData(request.getInputStream(),"UTF-8");
        JSONObject jsonObject = JSON.parseObject(json);
        String order_id = jsonObject.getString("order_id");
        return orderDao.delete(order_id);
    }
}
