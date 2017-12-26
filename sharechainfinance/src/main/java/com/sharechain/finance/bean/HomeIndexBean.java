package com.sharechain.finance.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Chu on 2017/12/25.
 */

public class HomeIndexBean implements Serializable {

    /**
     * success : 1
     * msg : 获取成功
     * data : {"banner":[{"url":"http://www.weilaicaijing.com/wp-content/uploads/2017/11/20171114163444044404.jpg","pid":736},{"url":"http://www.weilaicaijing.com/wp-content/uploads/2017/11/20171115151130543054.jpg","pid":738},{"url":"http://www.weilaicaijing.com/wp-content/uploads/2017/11/20171116150574247424.jpg","pid":744}],"top_news":[{"id":3,"dc_news_id":3496,"site_name":"[火币Pro]","site_url":"https://www.huobi.pro/zh-cn/notice_detail/?id=774","site_content":"火币全球专业站12月19日14:00上线HSR","time":"15:24","is_top":1,"hot_type":1,"create_time":"2017-12-20 15:24:42"},{"id":61,"dc_news_id":7953,"site_name":"[火币Pro]","site_url":"https://www.huobi.pro/zh-cn/notice_detail/?id=779","site_content":"【100万元BTM等你拿】12月20日14:00上线比原链(BTM)","time":"14:00","is_top":1,"hot_type":1,"create_time":"2017-12-25 17:59:46"},{"id":1,"dc_news_id":3494,"site_name":"[OKEX]","site_url":"https://support.okex.com/hc/zh-cn/articles/115003511291-OKEx上线QVT-YOYOW的公告","site_content":"根据活动规则，奖励将于本周五，即2017年12月22日全部发放完毕，请所有符合活动条件的用户查看资产管理\u2014我的账单。","time":"11:58","is_top":1,"hot_type":1,"create_time":"2017-12-20 11:58:40"}],"article_title_lists":[{"term_taxonomy_id":1,"name":"金融科技"},{"term_taxonomy_id":8,"name":"区块链"},{"term_taxonomy_id":9,"name":"数字货币"}]}
     */

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

    public static class DataBean implements Serializable {
        private List<BannerBean> banner;
        private List<TopNewsBean> top_news;
        private List<ArticleTitleListsBean> article_title_lists;

        public List<BannerBean> getBanner() {
            return banner;
        }

        public void setBanner(List<BannerBean> banner) {
            this.banner = banner;
        }

        public List<TopNewsBean> getTop_news() {
            return top_news;
        }

        public void setTop_news(List<TopNewsBean> top_news) {
            this.top_news = top_news;
        }

        public List<ArticleTitleListsBean> getArticle_title_lists() {
            return article_title_lists;
        }

        public void setArticle_title_lists(List<ArticleTitleListsBean> article_title_lists) {
            this.article_title_lists = article_title_lists;
        }

        public static class BannerBean implements Serializable {
            /**
             * url : http://www.weilaicaijing.com/wp-content/uploads/2017/11/20171114163444044404.jpg
             * pid : 736
             */

            private String url;
            private int pid;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getPid() {
                return pid;
            }

            public void setPid(int pid) {
                this.pid = pid;
            }
        }

        public static class TopNewsBean implements Serializable {
            /**
             * id : 3
             * dc_news_id : 3496
             * site_name : [火币Pro]
             * site_url : https://www.huobi.pro/zh-cn/notice_detail/?id=774
             * site_content : 火币全球专业站12月19日14:00上线HSR
             * time : 15:24
             * is_top : 1
             * hot_type : 1
             * create_time : 2017-12-20 15:24:42
             */

            private int id;
            private int dc_news_id;
            private String site_name;
            private String site_url;
            private String site_content;
            private String time;
            private int is_top;
            private int hot_type;
            private String create_time;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getDc_news_id() {
                return dc_news_id;
            }

            public void setDc_news_id(int dc_news_id) {
                this.dc_news_id = dc_news_id;
            }

            public String getSite_name() {
                return site_name;
            }

            public void setSite_name(String site_name) {
                this.site_name = site_name;
            }

            public String getSite_url() {
                return site_url;
            }

            public void setSite_url(String site_url) {
                this.site_url = site_url;
            }

            public String getSite_content() {
                return site_content;
            }

            public void setSite_content(String site_content) {
                this.site_content = site_content;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public int getIs_top() {
                return is_top;
            }

            public void setIs_top(int is_top) {
                this.is_top = is_top;
            }

            public int getHot_type() {
                return hot_type;
            }

            public void setHot_type(int hot_type) {
                this.hot_type = hot_type;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }
        }

        public static class ArticleTitleListsBean implements Serializable {
            /**
             * term_taxonomy_id : 1
             * name : 金融科技
             */

            private int term_taxonomy_id;
            private String name;

            public int getTerm_taxonomy_id() {
                return term_taxonomy_id;
            }

            public void setTerm_taxonomy_id(int term_taxonomy_id) {
                this.term_taxonomy_id = term_taxonomy_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
