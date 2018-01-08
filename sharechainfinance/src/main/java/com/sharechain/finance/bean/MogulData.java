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
    private String translate;
    private int Type;
    private int fabulous;//点赞
    private int id;//大佬id
    private String head;
    private int focus;
    private int mogulCircleID;//内容id

    public int getMogulCircleID() {
        return mogulCircleID;
    }

    public void setMogulCircleID(int mogulCircleID) {
        this.mogulCircleID = mogulCircleID;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    private boolean isLike;

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }
    public int getFocus() {
        return focus;
    }

    public void setFocus(int focus) {
        this.focus = focus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


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

    public int getFabulous() {
        return fabulous;
    }

    public void setFabulous(int fabulous) {
        this.fabulous = fabulous;
    }
}
