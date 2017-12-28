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
    private int Facous;

    public int getFacous() {
        return Facous;
    }

    public void setFacous(int facous) {
        Facous = facous;
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
