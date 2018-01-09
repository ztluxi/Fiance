package com.sharechain.finance;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.gyf.barlibrary.ImmersionBar;
import com.sharechain.finance.bean.ArticleListsBean;
import com.sharechain.finance.bean.HistoryData;
import com.sharechain.finance.utils.TimeUtil;
import com.sharechain.finance.view.MyXRefreshViewHeader;
import com.sharechain.finance.view.MyXrefreshViewFooter;
import com.zhy.http.okhttp.OkHttpUtils;

import java.lang.reflect.Field;
import java.util.Map;

import butterknife.ButterKnife;
import okhttp3.MediaType;

/**
 * Created by Administrator on 2017/12/12.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected RelativeLayout rl_base_layout;
    protected View view_status_bar;
    protected TextView tv_title_left;
    protected ImageView image_title_left;
    protected TextView tv_title_right;
    protected ImageView image_title_right;
    protected TextView text_title;
    protected ImmersionBar mImmersionBar;
    protected InputMethodManager inputMethodManager;
    protected int page = 1;//分页页码
    protected String pageParam = "page";
    protected LinearLayout ll_load_failed;
    protected TextView text_refresh;
    protected TextView empty_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        setContentView(getLayout());
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarColor(android.R.color.transparent).init();   //所有子类都将继承这些相同的属性

        ButterKnife.bind(this);

        initView();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        }, 100);
    }

    protected void hideSoftKeyboard(EditText editText) {
        if (inputMethodManager != null) {
            // 隐藏软键盘
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0); //强制隐藏键盘
        }
    }

    protected void showSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * 动态设置状态栏的高度
     *
     * @param viewGroup
     */
    protected void setTitlePadding(ViewGroup viewGroup) {
        viewGroup.setPadding(0, getStatusBarHeight(), 0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();  //必须调用该方法，防止内存泄漏，不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
    }

    protected void initTitle(String titleStr) {
        rl_base_layout = findViewById(R.id.rl_base_layout);
        view_status_bar = findViewById(R.id.view_status_bar);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight());
        view_status_bar.setLayoutParams(params);
        tv_title_left = findViewById(R.id.tv_title_left);
        image_title_left = findViewById(R.id.image_title_left);
        tv_title_right = findViewById(R.id.tv_title_right);
        image_title_right = findViewById(R.id.image_title_right);
        text_title = findViewById(R.id.text_title);
        text_title.setText(titleStr);
    }

    //初始化数据为空会或者加载失败的情况
    protected void initEmptyView(XRefreshView xRefreshView) {
        xRefreshView.setEmptyView(R.layout.layout_empty_view);
        ll_load_failed = xRefreshView.getEmptyView().findViewById(R.id.ll_load_failed);
        text_refresh = xRefreshView.getEmptyView().findViewById(R.id.text_refresh);
        empty_view = xRefreshView.getEmptyView().findViewById(R.id.empty_view);
    }

    protected void setEmptyView(XRefreshView xRefreshView, boolean isEnable) {
        ll_load_failed.setVisibility(View.GONE);
        empty_view.setVisibility(View.VISIBLE);
        xRefreshView.enableEmptyView(isEnable);
    }

    protected void setFailedView(XRefreshView xRefreshView, boolean isEnable) {
        ll_load_failed.setVisibility(View.VISIBLE);
        empty_view.setVisibility(View.GONE);
        xRefreshView.enableEmptyView(isEnable);
    }

    protected void initXRefreshView(XRefreshView xRefreshView) {
        xRefreshView.setCustomHeaderView(new MyXRefreshViewHeader(this));
        xRefreshView.setCustomFooterView(new MyXrefreshViewFooter(this));
        xRefreshView.setEmptyView(R.layout.layout_empty_view);
    }

    // 通过反射机制获取手机状态栏高度
    protected int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    protected void requestGet(String url, MyStringCallback callback) {
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(callback);
    }

    protected void requestGet(String url, Map<String, String> params, MyStringCallback callback) {
        OkHttpUtils.get()
                .url(url)
                .params(params)
                .build()
                .execute(callback);
    }

    protected void requestPost(String url, Map<String, String> params, MyStringCallback callback) {
        OkHttpUtils.post()
                .url(url)
                .params(params)
                .build()
                .execute(callback);
    }

    protected void requestPost(String url, String json, MyStringCallback callback) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpUtils.postString()
                .url(url)
                .content(json)
                .mediaType(JSON)
                .build()
                .execute(callback);
    }

    protected HistoryData convertToHistory(ArticleListsBean articleBean, int newsType, boolean isFirst) {
        HistoryData bean = new HistoryData();
        bean.setID(articleBean.getID());
        bean.setImage(articleBean.getImage());
        bean.setName(articleBean.getName());
        bean.setPost_content(articleBean.getPost_content());
        bean.setPost_date_gmt(articleBean.getPost_date_gmt());
        bean.setPost_title(articleBean.getPost_title());
        bean.setPost_view_rand(articleBean.getPost_view_rand());
        bean.setTagId(articleBean.getTagId());
        bean.setUser_avatars(articleBean.getUser_avatars());
        bean.setViews(articleBean.getViews());
        bean.setChannel_type(newsType);
        bean.setDisplay_name(articleBean.getDisplay_name());
        bean.setDate(TimeUtil.getCurrentTime("yyyy-MM-dd"));
        if (isFirst) {
            bean.setType(HistoryData.PARENT_TYPE);
        } else {
            bean.setType(HistoryData.CHILD_TYPE);
        }
        return bean;
    }

    protected ArticleListsBean convertToArticle(HistoryData articleBean) {
        ArticleListsBean bean = new ArticleListsBean();
        bean.setID(articleBean.getID());
        bean.setImage(articleBean.getImage());
        bean.setName(articleBean.getName());
        bean.setDisplay_name(articleBean.getDisplay_name());
        bean.setPost_content(articleBean.getPost_content());
        bean.setPost_date_gmt(articleBean.getPost_date_gmt());
        bean.setPost_title(articleBean.getPost_title());
        bean.setPost_view_rand(articleBean.getPost_view_rand());
        bean.setTagId(articleBean.getTagId());
        bean.setUser_avatars(articleBean.getUser_avatars());
        bean.setViews(articleBean.getViews());
        bean.setDisplay_name(articleBean.getDisplay_name());
        return bean;
    }

    public abstract int getLayout();

    public abstract void initView();

    public abstract void initData();


}
