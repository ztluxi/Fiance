package com.sharechain.finance.bean;

import com.youdao.sdk.ydtranslate.Translate;

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
    private int fabulous;//点赞
    private int id;
    private String head;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private Translate translate;

    public Translate getTranslate() {
        return translate;
    }

    public void setTranslate(Translate translate) {
        this.translate = translate;
    }

    public MogulData(Translate translate) {
        super();
        this.translate = translate;
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
