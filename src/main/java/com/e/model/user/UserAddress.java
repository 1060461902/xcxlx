package com.e.model.user;

/**
 *
 * @author asus
 * @date 2017/10/29
 * 用户地址信息对象
 */
public class UserAddress {
    private String address_id;//后台自动生成的30位随机字符串
    private String openid;//用户的唯一标识
    private String user_name;//姓名
    private String phone;//电话
    private String address;//地址

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
