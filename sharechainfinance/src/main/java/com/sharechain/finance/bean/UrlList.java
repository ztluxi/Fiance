package com.sharechain.finance.bean;

/**
 * Created by Chu on 2017/12/25.
 */

public class UrlList {
    private static String base_url = "http://api.weilaicaijing.com";
    public static final int CODE_SUCCESS = 1;
    public static final String PAGE = "1";
    public static final String HOME_INDEX = base_url + "/api_app/Home/index";
    public static final String HOME_ARTICLE_LIST = base_url + "/api_app/Home/article_lists";
    public static final String HOME_ARTICLE_DETAIL = base_url + "/api_app/Home/article_detail";
    public static final String HOME_ARTICLE_PRAISE = base_url + "/api_app/Home/like";
    public static final String MSG_GET_LIST = base_url + "/index/Fastnews/lists";
    public static final String FEEDBOOK = base_url + "/api_app/My/feedback";
    public static final String MOGUL_CIRCLE = base_url + "/api_app/Celebrity/lists";
    public static final String MOGUL_FOLLOW = base_url + "/api_app/Celebrity/focus";
    public static final String MOGUL_LIKE= base_url + "/api_app/Celebrity/like";
    public static final String GET_NEWS= base_url + "/api_app/My/message_lists";
    public static final String GET_MY_FOLLOW= base_url + "/api_app/My/focus_lists";
    public static final String CANCLE_FOLLOW= base_url + "/api_app/My/focus_cancel";




}
