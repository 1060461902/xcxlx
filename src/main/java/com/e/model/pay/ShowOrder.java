package com.e.model.pay;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 商户订单信息
 *
 * @author asus
 * */
public class ShowOrder {
  @JSONField(serialize = false)
  private String openid;//用户唯一标识
  private String order_id;//商户订单号 openid前5位+时间戳
  private String order_wx_id;//微信订单号
  private String goods_id;//货物ID *
  private int goods_number;//购买的货物数量 *
  private int goods_price;//货物价格
  private String goods_name;//货物名称
  private String goods_img;//货物图片路径
  private int freight;//运费价格
  private java.sql.Timestamp order_time;//交易时间
  private int order_status;//订单状态 0交易中(支付未成功) 1交易完成（商家处理阶段） 2订单处理完成（完成订单） 3退单 4交易失败的订单 5签名错误导致失败
  private String user_add_message;//用户备注信息 *
  private String address;//用户地址
  private String phone;//用户联系号码
  private String user_name;//用户姓名
  private String express_company_id;//订单指定的快递公司ID

  public String getOpenid() {
    return openid;
  }

  public void setOpenid(String openid) {
    this.openid = openid;
  }

  public String getExpress_company_id() {
    return express_company_id;
  }

  public void setExpress_company_id(String express_company_id) {
    this.express_company_id = express_company_id;
  }

  public String getGoods_name() {
    return goods_name;
  }

  public void setGoods_name(String goods_name) {
    this.goods_name = goods_name;
  }

  public String getGoods_img() {
    return goods_img;
  }

  public void setGoods_img(String goods_img) {
    this.goods_img = goods_img;
  }

  public int getGoods_number() {
    return goods_number;
  }

  public void setGoods_number(int goods_number) {
    this.goods_number = goods_number;
  }

  public int getGoods_price() {
    return goods_price;
  }

  public void setGoods_price(int goods_price) {
    this.goods_price = goods_price;
  }

  public int getOrder_status() {
    return order_status;
  }

  public void setOrder_status(int order_status) {
    this.order_status = order_status;
  }

  public int getFreight() {
    return freight;
  }

  public void setFreight(int freight) {
    this.freight = freight;
  }

  public String getOrder_id() {
    return order_id;
  }

  public void setOrder_id(String order_id) {
    this.order_id = order_id;
  }

  public java.sql.Timestamp getOrder_time() {
    return order_time;
  }

  public void setOrder_time(java.sql.Timestamp order_time) {
    this.order_time = order_time;
  }

  public String getUser_add_message() {
    return user_add_message;
  }

  public void setUser_add_message(String user_add_message) {
    this.user_add_message = user_add_message;
  }

  public String getOrder_wx_id() {
    return order_wx_id;
  }

  public void setOrder_wx_id(String order_wx_id) {
    this.order_wx_id = order_wx_id;
  }

  public String getGoods_id() {
    return goods_id;
  }

  public void setGoods_id(String goods_id) {
    this.goods_id = goods_id;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getUser_name() {
    return user_name;
  }

  public void setUser_name(String user_name) {
    this.user_name = user_name;
  }
}
