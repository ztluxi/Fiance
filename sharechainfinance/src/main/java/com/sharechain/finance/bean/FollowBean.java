package com.sharechain.finance.bean;

import java.util.List;

/**
 * Created by ${zhoutao} on 2017/12/27 0027.
 */

public class FollowBean {


    /**
     * success : 1
     * msg : 获取成功
     * data : [{"id":1369,"state":"1","celebrity_id":111,"profile_image_url":"https://tva4.sinaimg.cn/crop.0.16.210.210.180/005ujckfgw1eems9kxp3aj306509rmxk.jpg","full_name":"李启威","social_type":2,"screen_name":"LitecoinCoblee","professional":"莱特币创始人"},{"id":1368,"state":"1","celebrity_id":110,"profile_image_url":"https://tva4.sinaimg.cn/crop.0.0.1242.1242.180/006u6xlijw8f4dfms11yoj30yi0yi40m.jpg","full_name":"郭宏才","social_type":2,"screen_name":"Bitangel宝二爷","professional":"比特币上帝创始人"},{"id":1367,"state":"1","celebrity_id":109,"profile_image_url":"https://tvax3.sinaimg.cn/crop.0.2.1125.1125.180/7264280dly8fmmged86yvj20v90vdabx.jpg","full_name":"杨林科","social_type":2,"screen_name":"我是杨林科","professional":"ICOCOIN创始人"},{"id":1366,"state":"1","celebrity_id":108,"profile_image_url":"https://tvax3.sinaimg.cn/crop.0.0.996.996.180/006X91Q6ly8flzblw1pe1j30ro0rojtr.jpg","full_name":"徐子敬","social_type":2,"screen_name":"火星人Ryan柯里昂","professional":"超级现金基金会荣誉主席"},{"id":1365,"state":"1","celebrity_id":107,"profile_image_url":"https://tva1.sinaimg.cn/crop.0.0.917.917.180/62d6c63cjw8ec25mj6galj20ph0pi75e.jpg","full_name":"杜均","social_type":2,"screen_name":"杜均","professional":"金色财经CEO"},{"id":1364,"state":"1","celebrity_id":106,"profile_image_url":"https://tva3.sinaimg.cn/crop.0.3.1242.1242.180/62d41719jw8f4k5h2v0fmj20yi0yptbt.jpg","full_name":"赵东 ","social_type":2,"screen_name":"赵乐天","professional":"OTC大咖"},{"id":1363,"state":"1","celebrity_id":105,"profile_image_url":"https://tva1.sinaimg.cn/crop.0.0.180.180.180/6d0b65e7jw1e8qgp5bmzyj2050050aa8.jpg","full_name":"神鱼BTCer","social_type":2,"screen_name":"神鱼BTCer","professional":"F2Pool创始人"},{"id":1362,"state":"1","celebrity_id":104,"profile_image_url":"https://tva3.sinaimg.cn/crop.0.0.180.180.180/53995d60jw1e8qgp5bmzyj2050050aa8.jpg","full_name":"暴走恭亲王","social_type":2,"screen_name":"暴走恭亲王","professional":"比特币基金会成员"},{"id":1361,"state":"1","celebrity_id":103,"profile_image_url":"https://tvax1.sinaimg.cn/crop.0.0.996.996.180/a18a5431ly8fiph3a8o9wj20ro0rowgj.jpg","full_name":"申屠青春","social_type":2,"screen_name":"申屠青春","professional":"投票链创始人"},{"id":1360,"state":"1","celebrity_id":102,"profile_image_url":"https://tvax1.sinaimg.cn/crop.0.0.749.749.180/a2c7d34fly8fmoma73onnj20ku0ktt9q.jpg","full_name":"江恩","social_type":2,"screen_name":"开心-狗狗币","professional":"中国狗狗币协会会长"}]
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
         * id : 1369
         * state : 1
         * celebrity_id : 111
         * profile_image_url : https://tva4.sinaimg.cn/crop.0.16.210.210.180/005ujckfgw1eems9kxp3aj306509rmxk.jpg
         * full_name : 李启威
         * social_type : 2
         * screen_name : LitecoinCoblee
         * professional : 莱特币创始人
         */

        private int id;
        private String state;
        private int celebrity_id;
        private String profile_image_url;
        private String full_name;
        private int social_type;
        private String screen_name;
        private String professional;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public int getCelebrity_id() {
            return celebrity_id;
        }

        public void setCelebrity_id(int celebrity_id) {
            this.celebrity_id = celebrity_id;
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

        public int getSocial_type() {
            return social_type;
        }

        public void setSocial_type(int social_type) {
            this.social_type = social_type;
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
