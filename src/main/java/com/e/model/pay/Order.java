package com.e.model.pay;

import java.sql.Timestamp;

/**
 * Created by asus on 2017/11/18.
 */
public class Order {
    private String order_id;//商户订单号 openid前5位+时间戳
    private String order_wx_id;//微信订单号
    private String openid;//用户唯一标识
    private String goods_id;//货物ID *
    private int goods_number;//购买的货物数量 *
    private int freight;//运费价格
    private java.sql.Timestamp order_time;//交易时间
    private int order_status;//订单状态 0交易中(支付未成功) 1交易完成（商家处理阶段） 2订单处理完成（完成订单） 3退单 4交易失败的订单 5签名错误导致失败
    private String user_add_message;//用户备注信息 *
    private String address_id;//用户地址ID *
    private String express_company_id;//快递公司ID *

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_wx_id() {
        return order_wx_id;
    }

    public void setOrder_wx_id(String order_wx_id) {
        this.order_wx_id = order_wx_id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public int getGoods_number() {
        return goods_number;
    }

    public void setGoods_number(int goods_number) {
        this.goods_number = goods_number;
    }

    public int getFreight() {
        return freight;
    }

    public void setFreight(int freight) {
        this.freight = freight;
    }

    public Timestamp getOrder_time() {
        return order_time;
    }

    public void setOrder_time(Timestamp order_time) {
        this.order_time = order_time;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public String getUser_add_message() {
        return user_add_message;
    }

    public void setUser_add_message(String user_add_message) {
        this.user_add_message = user_add_message;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getExpress_company_id() {
        return express_company_id;
    }

    public void setExpress_company_id(String express_company_id) {
        this.express_company_id = express_company_id;
    }
}
