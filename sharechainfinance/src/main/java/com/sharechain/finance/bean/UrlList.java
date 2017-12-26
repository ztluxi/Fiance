package com.sharechain.finance.bean;

/**
 * Created by Chu on 2017/12/25.
 */

public class UrlList {
    private static String base_url = "http://api.weilaicaijing.com";
    public static final int CODE_SUCCESS = 1;
    public static final String HOME_INDEX = base_url + "/api_app/Home/index";
    public static final String HOME_ARTICLE_LIST = base_url + "/api_app/Home/article_lists";
    public static final String HOME_ARTICLE_DETAIL = base_url + "/api_app/Home/article_detail";
    public static final String HOME_ARTICLE_PRAISE = base_url + "/api_app/Home/like";
    public static final String MSG_GET_LIST = base_url + "/index/Fastnews/lists";
}
