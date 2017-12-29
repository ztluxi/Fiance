package com.sharechain.finance.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Chu on 2017/12/28.
 */

public class ArticleListsBean extends DataSupport implements Serializable {
    public static final int CACHE_TYPE_RECOMMENT = 0;
    public static final int CACHE_TYPE_OTHER = 1;
    public static final int CACHE_TYPE_HISTORY = 2;
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
    private int cacheType;

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

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public int getCacheType() {
        return cacheType;
    }

    public void setCacheType(int cacheType) {
        this.cacheType = cacheType;
    }
}
