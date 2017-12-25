package com.sharechain.finance.bean;

/**
 * Created by Chu on 2017/12/19.
 */

public class BaseNotifyBean {
    public enum TYPE {
        TYPE_SHARE_RESULT, TYPE_MANAGE_TAG_RESULT, TYPE_LOGIN_WEIXIN
    }

    private TYPE type;
    private String message;
    private Object obj;

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
