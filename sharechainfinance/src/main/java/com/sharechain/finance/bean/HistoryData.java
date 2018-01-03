package com.sharechain.finance.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by Chu on 2018/1/3.
 */

public class HistoryData extends DataSupport {
    public static final int PARENT_TYPE = 0;
    public static final int CHILD_TYPE = 1;

    private int type;//item类型
    private int channel_type;//频道类型
    private String date;//记录日期
    private int tagId;
    private int ID;
    private String post_title;
    private String post_content;
    private String post_date_gmt;
    private int post_view_rand;
    private String user_avatars;
    private int views;
    private String image;
    private String name;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getChannel_type() {
        return channel_type;
    }

    public void setChannel_type(int channel_type) {
        this.channel_type = channel_type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

    public String getPost_date_gmt() {
        return post_date_gmt;
    }

    public void setPost_date_gmt(String post_date_gmt) {
        this.post_date_gmt = post_date_gmt;
    }

    public int getPost_view_rand() {
        return post_view_rand;
    }

    public void setPost_view_rand(int post_view_rand) {
        this.post_view_rand = post_view_rand;
    }

    public String getUser_avatars() {
        return user_avatars;
    }

    public void setUser_avatars(String user_avatars) {
        this.user_avatars = user_avatars;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
