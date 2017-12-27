package com.sharechain.finance.bean;

import java.util.List;

/**
 * Created by ${zhoutao} on 2017/12/27 0027.
 */
public class MyNewsBean {


    /**
     * success : 1
     * msg : 获取成功
     * data : [{"id":3,"content":"gdsfgsdfsdfsdfdddszzzzzzzzzzzz","create_time":"2017-12-26 20:44:50"},{"id":1,"content":"123","create_time":"2017-12-26 20:27:08"}]
     */

    private int success;
    private String msg;
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 3
         * content : gdsfgsdfsdfsdfdddszzzzzzzzzzzz
         * create_time : 2017-12-26 20:44:50
         */

        private int id;
        private String content;
        private String create_time;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
    }
}
