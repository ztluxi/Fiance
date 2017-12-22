package com.sharechain.finance.bean;

/**
 * Created by Chu on 2017/12/18.
 */

public class FastMsgData {
    public static final int PARENT_TYPE = 0;
    public static final int CHILD_TYPE = 1;

    private String sectionText;
    private int type;
    private String dataText;

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
}