package com.sharechain.finance.bean;

/**
 * Created by ${zhoutao} on 2017/12/15 0013.
 */

public class FollowData {
    private String name;
    private String weibo;
    private String position;
    private String image;
    private int MogulID;
    private int FollowID;
    private int state;

    public int getFollowID() {
        return FollowID;
    }

    public void setFollowID(int followID) {
        FollowID = followID;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getMogulID() {
        return MogulID;
    }

    public void setMogulID(int mogulID) {
        MogulID = mogulID;
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
