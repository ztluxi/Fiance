package com.sharechain.finance.bean;

import java.util.List;

/**
 * Created by Chu on 2018/1/9.
 */

public class TopNewsBean {

    /**
     * success : 1
     * msg : 获取成功
     * data : [{"id":206,"dc_news_id":null,"site_name":"","site_url":"","site_content":"GMO投资管理公司联合创始人杰里米·格兰瑟姆，表示比特币以及差不多整个股市都处于泡沫之中。\u201c这场泡沫可能会在未来6个月到2年内衰退或者进入结束阶段\u201d。","time":"11:45","is_top_text":"GMO投资管理公司联合创始人杰里米·格兰瑟姆，表示比特币以及差不多整个股市都处于泡沫之中。\u201c这场泡沫可能会在未来6个月到2年内衰退或者进入结束阶段\u201d。","is_top":2,"hot_type":2,"create_time":"2018-01-09 11:45:38"},{"id":205,"dc_news_id":null,"site_name":"","site_url":"","site_content":"近日，FSC主席Choi Jong-ku在新闻发布会上透露，韩国金融服务委员会将深化与中国和日本监管机构的合作，以遏制投机性交易。","time":"11:45","is_top_text":"近日，FSC主席Choi Jong-ku在新闻发布会上透露，韩国金融服务委员会将深化与中国和日本监管机构的合作，以遏制投机性交易。","is_top":2,"hot_type":2,"create_time":"2018-01-09 11:45:12"},{"id":194,"dc_news_id":null,"site_name":"","site_url":"","site_content":"coin风险提示：Substratum (SUB)并没有推出新版代币，请用户小心骗子。","time":"17:03","is_top_text":"coin风险提示：Substratum (SUB)并没有推出新版代币，请用户小心骗子。","is_top":2,"hot_type":1,"create_time":"2018-01-08 17:03:41"}]
     * code : 2000
     */

    private int success;
    private String msg;
    private int code;
    private List<DataBean> data;

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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 206
         * dc_news_id : null
         * site_name :
         * site_url :
         * site_content : GMO投资管理公司联合创始人杰里米·格兰瑟姆，表示比特币以及差不多整个股市都处于泡沫之中。“这场泡沫可能会在未来6个月到2年内衰退或者进入结束阶段”。
         * time : 11:45
         * is_top_text : GMO投资管理公司联合创始人杰里米·格兰瑟姆，表示比特币以及差不多整个股市都处于泡沫之中。“这场泡沫可能会在未来6个月到2年内衰退或者进入结束阶段”。
         * is_top : 2
         * hot_type : 2
         * create_time : 2018-01-09 11:45:38
         */

        private int id;
        private Object dc_news_id;
        private String site_name;
        private String site_url;
        private String site_content;
        private String time;
        private String is_top_text;
        private int is_top;
        private int hot_type;
        private String create_time;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getDc_news_id() {
            return dc_news_id;
        }

        public void setDc_news_id(Object dc_news_id) {
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

        public String getIs_top_text() {
            return is_top_text;
        }

        public void setIs_top_text(String is_top_text) {
            this.is_top_text = is_top_text;
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
}
