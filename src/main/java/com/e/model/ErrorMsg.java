package com.e.model;

/**
 * Created by asus on 2017/10/12.
 * 微信后台返回的错误信息对象
 */
public class ErrorMsg {
    private String errcode;

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    private String errmsg;

}
