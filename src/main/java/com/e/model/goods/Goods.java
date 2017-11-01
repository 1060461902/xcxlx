package com.e.model.goods;

/**！！！未确定！！！
 * Created by asus on 2017/10/31.
 * 货物信息对象
 */
public class Goods {
    private String goods_id;//货物ID
    private String goods_name;//货物名称
    private int goods_price;//货物价格，单位 分
    private String goods_img;//货物图片地址
    private int goods_num;//货物数量

    public int getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(int goods_num) {
        this.goods_num = goods_num;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public int getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(int goods_price) {
        this.goods_price = goods_price;
    }

    public String getGoods_img() {
        return goods_img;
    }

    public void setGoods_img(String goods_img) {
        this.goods_img = goods_img;
    }
}
