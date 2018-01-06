package com.sharechain.finance.bean;

import java.util.List;

/**
 * Created by ${zhoutao} on 2017/12/27 0027.
 */

public class FollowBean {


    /**
     * success : 1
     * msg : 获取成功
     * data : [{"id":6,"uid":2,"profile_image_url":"http://api.weilaicaijing.com/images/17sPVVyx_normal.jpg","full_name":"Fred Ehrsam","screen_name":"FEhrsam","professional":"Coinbase联合创始人"}]
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
         * id : 6
         * uid : 2
         * profile_image_url : http://api.weilaicaijing.com/images/17sPVVyx_normal.jpg
         * full_name : Fred Ehrsam
         * screen_name : FEhrsam
         * professional : Coinbase联合创始人
         */

        private int celebrity_id;
        private int uid;
        private String profile_image_url;
        private String full_name;
        private String screen_name;
        private String professional;
        private int state;

        public int getCelebrity_id() {
            return celebrity_id;
        }

        public void setCelebrity_id(int celebrity_id) {
            this.celebrity_id = celebrity_id;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getProfile_image_url() {
            return profile_image_url;
        }

        public void setProfile_image_url(String profile_image_url) {
            this.profile_image_url = profile_image_url;
        }

        public String getFull_name() {
            return full_name;
        }

        public void setFull_name(String full_name) {
            this.full_name = full_name;
        }

        public String getScreen_name() {
            return screen_name;
        }

        public void setScreen_name(String screen_name) {
            this.screen_name = screen_name;
        }

        public String getProfessional() {
            return professional;
        }

        public void setProfessional(String professional) {
            this.professional = professional;
        }
    }
}
