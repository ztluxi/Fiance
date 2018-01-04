package com.sharechain.finance.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by Chu on 2018/1/4.
 */

public class LoginDataBean extends DataSupport {
    /**
     * nick_name : 凌烟
     * sex : men
     * head_img : http://wx.qlogo.cn/mmopen/vi_32/XD19ZZns59nnicCNeNu0zCHRwAAibLxQWBuMINVeHPRIkq3NT1jicpOxMtsE7nic90jezfYLg9rlXDiaYdpxGX0WicgA/0
     * token : 188bad8e3ab0c7c8c1f82f490d666033
     */

    private String nick_name;
    private String sex;
    private String head_img;
    private String token;

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
