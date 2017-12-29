package com.sharechain.finance.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by Chu on 2017/12/29.
 */

public class MainCacheBean extends DataSupport {
    public static final int TYPE_HOME_NEWS = 0;
    public static final int TYPE_FAST_MSG = 1;
    public static final int TYPE_FRIEND_CIRCLE = 2;
    private int type;
    private String cacheJson;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCacheJson() {
        return cacheJson;
    }

    public void setCacheJson(String cacheJson) {
        this.cacheJson = cacheJson;
    }
}
