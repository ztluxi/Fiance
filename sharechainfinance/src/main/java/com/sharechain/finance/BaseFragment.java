package com.sharechain.finance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.sharechain.finance.view.MyXRefreshViewHeader;
import com.sharechain.finance.view.MyXrefreshViewFooter;
import com.shizhefei.fragment.LazyFragment;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.MediaType;

/**
 * Created by Administrator on 2017/12/13.
 */

public abstract class BaseFragment extends LazyFragment {
    protected RelativeLayout rl_base_layout;
    protected TextView tv_title_left;
    protected ImageView image_title_left;
    protected TextView tv_title_right;
    protected ImageView image_title_right;
    protected TextView text_title;

    private Unbinder unbinder;

    @Override
    protected View getPreviewLayout(LayoutInflater inflater, ViewGroup container) {
        return super.getPreviewLayout(inflater, container);
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(getLayout());
        unbinder = ButterKnife.bind(this, getContentView());
        initView();
        initData();
    }

    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        unbinder.unbind();
    }

    protected void initTitle(String titleStr) {
        rl_base_layout = (RelativeLayout) findViewById(R.id.rl_base_layout);
        tv_title_left = (TextView) findViewById(R.id.tv_title_left);
        image_title_left = (ImageView) findViewById(R.id.image_title_left);
        tv_title_right = (TextView) findViewById(R.id.tv_title_right);
        image_title_right = (ImageView) findViewById(R.id.image_title_right);
        text_title = (TextView) findViewById(R.id.text_title);
        text_title.setText(titleStr);
    }

    protected void initXRefreshView(XRefreshView xRefreshView) {
        xRefreshView.setCustomHeaderView(new MyXRefreshViewHeader(getActivity()));
        xRefreshView.setCustomFooterView(new MyXrefreshViewFooter(getActivity()));
        xRefreshView.setEmptyView(R.layout.layout_empty_view);
    }

    protected void requestGet(String url, MyStringCallback callback) {
        OkHttpUtils.get()
                .url(url)
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
