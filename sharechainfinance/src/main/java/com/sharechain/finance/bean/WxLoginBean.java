package com.sharechain.finance.bean;

/**
 * Created by Chu on 2018/1/4.
 */

public class WxLoginBean {

    /**
     * success : true
     * msg : 登录成功
     * data : {"nick_name":"凌烟","sex":"men","head_img":"http://wx.qlogo.cn/mmopen/vi_32/XD19ZZns59nnicCNeNu0zCHRwAAibLxQWBuMINVeHPRIkq3NT1jicpOxMtsE7nic90jezfYLg9rlXDiaYdpxGX0WicgA/0","token":"188bad8e3ab0c7c8c1f82f490d666033"}
     */

    private boolean success;
    private String msg;
    private LoginDataBean data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public LoginDataBean getData() {
        return data;
    }

    public void setData(LoginDataBean data) {
        this.data = data;
    }

}
