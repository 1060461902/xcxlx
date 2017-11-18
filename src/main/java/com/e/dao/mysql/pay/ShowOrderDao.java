package com.e.dao.mysql.pay;

import com.e.model.pay.ShowOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by asus on 2017/11/18.
 */
public interface ShowOrderDao {
    /**
     * 获取所有订单
     * @return 订单信息列表
     * */
    List<ShowOrder> getAll();
    /**
     * 获取某位用户的所有订单
     * @param openid 用户唯一标识
     * @return 订单信息列表
     * */
    List<ShowOrder> getOnePersonAll(@Param("openid")String openid);
    /**
     *获取特定状态的订单
     * @param order_status 需要获取的状态
     * @return 订单列表
     * */
    List<ShowOrder>getTheStatusAll(@Param("order_status")int order_status);
    /**
     * 获取某人某状态的订单
     * @param order_status 订单状态
     * @param openid 用户唯一标识
     * @return 订单列表
     * */
    List<ShowOrder>getOnePersonTheStatusAll(@Param("order_status")int order_status, @Param("openid") String openid);
}
