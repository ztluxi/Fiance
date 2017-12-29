package com.sharechain.finance.bean;

import java.util.List;

/**
 * Created by Chu on 2017/12/26.
 */

public class HomeArticleListBean {

    private int success;
    private String msg;
    private DataBean data;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int article_count;
        private List<ArticleListsBean> article_lists;

        public int getArticle_count() {
            return article_count;
        }

        public void setArticle_count(int article_count) {
            this.article_count = article_count;
        }

        public List<ArticleListsBean> getArticle_lists() {
            return article_lists;
        }

        public void setArticle_lists(List<ArticleListsBean> article_lists) {
            this.article_lists = article_lists;
        }
    }
}
