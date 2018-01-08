package com.sharechain.finance.bean;

import org.litepal.crud.DataSupport;

/**
 * 大佬圈点赞缓存
 * Created by ${zhoutao} on 2018/1/8 0008.
 */

public class MogulLikeBean extends DataSupport {
    private long mogulID;
    private boolean isLike;

    public long getMogulID() {
        return mogulID;
    }

    public void setMogulID(long mogulID) {
        this.mogulID = mogulID;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }
}
