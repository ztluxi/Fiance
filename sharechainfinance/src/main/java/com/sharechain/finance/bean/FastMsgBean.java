package com.sharechain.finance.bean;

import java.util.List;

/**
 * Created by Chu on 2017/12/26.
 */

public class FastMsgBean {

    /**
     * success : true
     * msg : 获取成功
     * data : [{"time":"2017-12-26 周三","list":[{"type":2,"title":"","text":"","hour":"15:22","url":"https://support.okex.com/hc/zh-cn/articles/115003644751-OKEx上线DGB的公告"},{"type":3,"title":"","text":"","hour":"14:59","url":"https://www.huobi.pro/zh-cn/notice_detail/?id=796"}]},{"time":"2017-12-24 周一","list":[{"type":3,"title":"","text":"","hour":"10:28","url":"https://www.zb.com/i/blog?item=66&type="},{"type":1,"title":"","text":"","hour":"10:26","url":"http://www.baidu.com"}]},{"time":"2017-12-22 周六","list":[{"type":1,"title":"","text":"","hour":"15:24","url":"https://www.huobi.pro/zh-cn/notice_detail/?id=774"}]},{"time":"2017-12-21 周五","list":[{"type":1,"title":"","text":"","hour":"14:11","url":"https://bibox.zendesk.com/hc/zh-cn/articles/115004519314--活动-RDN活动结束公告"}]},{"time":"2017-12-20 周四","list":[{"type":1,"title":"","text":"","hour":"18:55","url":"https://support.okex.com/hc/zh-cn/articles/115003545631-OKEx关于开放ETF领取的公告"},{"type":3,"title":"","text":"","hour":"18:15","url":"https://www.uncoinex.com/news/detail/36"},{"type":3,"title":"","text":"","hour":"15:20","url":"https://www.shuzibi.com/notice/443"},{"type":2,"title":"","text":"","hour":"15:15","url":"https://www.shuzibi.com/notice/442"}]}]
     */

    private boolean success;
    private String msg;
    private List<DataBean> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * time : 2017-12-26 周三
         * list : [{"type":2,"title":"","text":"","hour":"15:22","url":"https://support.okex.com/hc/zh-cn/articles/115003644751-OKEx上线DGB的公告"},{"type":3,"title":"","text":"","hour":"14:59","url":"https://www.huobi.pro/zh-cn/notice_detail/?id=796"}]
         */

        private String time;
        private List<ListBean> list;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * type : 2
             * title :
             * text :
             * hour : 15:22
             * url : https://support.okex.com/hc/zh-cn/articles/115003644751-OKEx上线DGB的公告
             */

            private int type;
            private String title;
            private String text;
            private String hour;
            private String url;

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getHour() {
                return hour;
            }

            public void setHour(String hour) {
                this.hour = hour;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
