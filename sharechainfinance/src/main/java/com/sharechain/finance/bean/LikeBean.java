package com.sharechain.finance.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by Chu on 2017/12/28.
 */

public class LikeBean extends DataSupport {
    private int articleId;

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }
}
