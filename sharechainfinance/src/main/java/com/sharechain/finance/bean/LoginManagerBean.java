package com.sharechain.finance.bean;

/**
 * Created by ${zhoutao} on 2018/1/6 0006.
 */

public class LoginManagerBean {

    /**
     * success : 0
     * msg : 登录过期，请重新登录
     * data :
     * code : 1000
     */

    private int success;
    private String msg;
    private String data;
    private int code;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
