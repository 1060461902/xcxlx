package com.e.model.pay;

public class Order {
  private String order_id;
  private int goods_number;
  private int goods_price;
  private java.sql.Timestamp order_time;
  private int order_status;
  private String user_add_message;
  private String order_wx_id;
  private String goods_id;
  private String user_address;
  private String user_phone;
  private String user_name;
  private int freight;

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

  public String getUser_address() {
    return user_address;
  }

  public void setUser_address(String user_address) {
    this.user_address = user_address;
  }

  public String getUser_phone() {
    return user_phone;
  }

  public void setUser_phone(String user_phone) {
    this.user_phone = user_phone;
  }

  public String getUser_name() {
    return user_name;
  }

  public void setUser_name(String user_name) {
    this.user_name = user_name;
  }
}
