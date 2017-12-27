package com.sharechain.finance;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.gyf.barlibrary.ImmersionBar;
import com.sharechain.finance.view.MyXRefreshViewHeader;
import com.sharechain.finance.view.MyXrefreshViewFooter;
import com.shizhefei.fragment.LazyFragment;
import com.zhy.http.okhttp.OkHttpUtils;

import java.lang.reflect.Field;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.MediaType;

/**
 * Created by Administrator on 2017/12/13.
 */

public abstract class BaseFragment extends LazyFragment {
    protected RelativeLayout rl_base_layout;
    protected View view_status_bar;
    protected TextView tv_title_left;
    protected ImageView image_title_left;
    protected TextView tv_title_right;
    protected ImageView image_title_right;
    protected TextView text_title;
    protected View view_line;

    private Unbinder unbinder;
    protected ImmersionBar immersionBar;
    protected int page = 1;//分页页码
    protected String pageParam = "page";

    @Override
    protected View getPreviewLayout(LayoutInflater inflater, ViewGroup container) {
        return super.getPreviewLayout(inflater, container);
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(getLayout());
        immersionBar = ImmersionBar.with(getActivity());
        unbinder = ButterKnife.bind(this, getContentView());
        initView();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        }, 50);
    }

    /**
     * 动态设置状态栏的高度
     *
     * @param viewGroup
     * @param paddinfBottom 底部距离
     */
    protected void setTitlePadding(ViewGroup viewGroup, int paddinfBottom) {
        viewGroup.setPadding(0, getStatusBarHeight(), 0, paddinfBottom);
    }

    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        unbinder.unbind();
        if (immersionBar != null)
            immersionBar.destroy();
    }

    protected void initTitle(String titleStr) {
        rl_base_layout = (RelativeLayout) findViewById(R.id.rl_base_layout);
        view_status_bar = findViewById(R.id.view_status_bar);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight());
        view_status_bar.setLayoutParams(params);
        tv_title_left = (TextView) findViewById(R.id.tv_title_left);
        image_title_left = (ImageView) findViewById(R.id.image_title_left);
        tv_title_right = (TextView) findViewById(R.id.tv_title_right);
        image_title_right = (ImageView) findViewById(R.id.image_title_right);
        text_title = (TextView) findViewById(R.id.text_title);
        text_title.setText(titleStr);
        view_line = findViewById(R.id.view_line);
    }

    protected void initXRefreshView(XRefreshView xRefreshView) {
        xRefreshView.setCustomHeaderView(new MyXRefreshViewHeader(getActivity()));
        xRefreshView.setCustomFooterView(new MyXrefreshViewFooter(getActivity()));
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

    protected abstract int getLayout();

    protected abstract void initView();

    public abstract void initData();

}
