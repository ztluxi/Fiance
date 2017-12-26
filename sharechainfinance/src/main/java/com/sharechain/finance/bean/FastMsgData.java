package com.sharechain.finance.bean;

/**
 * Created by Chu on 2017/12/18.
 */

public class FastMsgData {
    public static final int PARENT_TYPE = 0;
    public static final int CHILD_TYPE = 1;

    private String sectionText;
    private int type;
    private String dataText;//content
    private int msgType;//消息热度
    private String title;//标题
    private String hour;//时间
    private String url;

    public String getSectionText() {
        return sectionText;
    }

    public void setSectionText(String sectionText) {
        this.sectionText = sectionText;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDataText() {
        return dataText;
    }

    public void setDataText(String dataText) {
        this.dataText = dataText;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
