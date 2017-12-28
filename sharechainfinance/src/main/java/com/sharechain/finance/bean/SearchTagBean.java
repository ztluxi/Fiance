package com.sharechain.finance.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by Chu on 2017/12/28.
 */

public class SearchTagBean extends DataSupport {
    private String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
