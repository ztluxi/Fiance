package com.sharechain.finance.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${zhoutao} on 2017/12/22 0013.
 */

public class MogulData {
    private String name;
    private String weibo;
    private String content;
    private String position;
    private String time;
    private String fabulous;//点赞
    private String head;
    public List<String> urlList = new ArrayList<>();

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFabulous() {
        return fabulous;
    }

    public void setFabulous(String fabulous) {
        this.fabulous = fabulous;
    }


}
