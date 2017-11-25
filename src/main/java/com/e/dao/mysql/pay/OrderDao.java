package com.e.dao.mysql.pay;

import com.e.model.pay.Order;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by asus on 2017/11/18.
 */
@Repository
@MapperScan
public interface OrderDao {
    /**
     * 创建订单
     * @param order 订单信息对象
     * @return 是否创建成功
     * */
    boolean insert(Order order);
    /**
     * 更新订单
     * @param order 订单信息
     * @return 是否更新成功
     * */
    @Deprecated
    boolean update(Order order);
    /**
     * 删除订单
     * @param order_id 订单号
     * @return 是否删除成功
     * */
    boolean delete(@Param("order_id") String order_id);
    /**
     * 获取所有订单
     * @return 订单信息列表
     * */
    List<Order> getAll();
    /**
     * 获取某位用户的所有订单
     * @param openid 用户唯一标识
     * @return 订单信息列表
     * */
    @Deprecated
    List<Order> getOnePersonAll(@Param("openid")String openid);
    /**
     *获取特定状态的订单
     * @param status 需要获取的状态
     * @return 订单列表
     * */
    @Deprecated
    List<Order>getTheStatusAll(@Param("status")int status);
    /**
     * 获取某人某状态的订单
     * @param status 订单状态
     * @param openid 用户唯一标识
     * @return 订单列表
     * */
    @Deprecated
    List<Order>getOnePersonTheStatusAll(@Param("status")int status, @Param("openid") String openid);
    /**
     * 更新某订单状态
     * @param order_id 订单ID
     * @return 是否成功
     * */
    boolean updateTheOrder(@Param("order_id")String order_id,@Param("status")int status);
}
