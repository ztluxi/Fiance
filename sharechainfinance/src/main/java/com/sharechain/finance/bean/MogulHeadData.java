package com.sharechain.finance.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${zhoutao} on 2017/12/22 0013.
 */

public class MogulHeadData {
    private String name;
    private String position;
    private String mogul_image;
    private int focous;

    public int getFocous() {
        return focous;
    }

    public void setFocous(int focous) {
        this.focous = focous;
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

    public String getMogul_image() {
        return mogul_image;
    }

    public void setMogul_image(String mogul_image) {
        this.mogul_image = mogul_image;
    }

}
