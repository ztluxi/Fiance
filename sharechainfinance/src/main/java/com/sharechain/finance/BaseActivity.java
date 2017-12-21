package com.sharechain.finance;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.sharechain.finance.utils.BaseUtils;
import com.sharechain.finance.view.MyXRefreshViewHeader;
import com.sharechain.finance.view.MyXrefreshViewFooter;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.Map;

import butterknife.ButterKnife;
import butterknife.internal.Utils;
import okhttp3.MediaType;

/**
 * Created by Administrator on 2017/12/12.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected RelativeLayout rl_base_layout;
    protected TextView tv_title_left;
    protected ImageView image_title_left;
    protected TextView tv_title_right;
    protected ImageView image_title_right;
    protected TextView text_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        // 4.4及以上版本开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        setTintDrawable(R.drawable.common_mine_title_bg);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    protected void initTitle(String titleStr) {
        rl_base_layout = findViewById(R.id.rl_base_layout);
        tv_title_left = findViewById(R.id.tv_title_left);
        image_title_left = findViewById(R.id.image_title_left);
        tv_title_right = findViewById(R.id.tv_title_right);
        image_title_right = findViewById(R.id.image_title_right);
        text_title = findViewById(R.id.text_title);
        text_title.setText(titleStr);
    }

    protected void initXRefreshView(XRefreshView xRefreshView) {
        xRefreshView.setCustomHeaderView(new MyXRefreshViewHeader(this));
        xRefreshView.setCustomFooterView(new MyXrefreshViewFooter(this));
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

    /**
     * 设置状态栏背景
     *
     * @param res
     */
    protected void setTintDrawable(int res) {
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintDrawable(ContextCompat.getDrawable(this, res));
    }

    public abstract int getLayout();

    public abstract void initView();

    public abstract void initData();


}
