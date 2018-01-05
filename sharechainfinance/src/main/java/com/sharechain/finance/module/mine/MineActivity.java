package com.sharechain.finance.module.mine;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.sharechain.finance.BaseActivity;
import com.sharechain.finance.MyStringCallback;
import com.sharechain.finance.R;
import com.sharechain.finance.SFApplication;
import com.sharechain.finance.bean.BaseNotifyBean;
import com.sharechain.finance.bean.LoginDataBean;
import com.sharechain.finance.bean.NewsEven;
import com.sharechain.finance.bean.UrlList;
import com.sharechain.finance.bean.WXAccessBean;
import com.sharechain.finance.bean.WXRefreshBean;
import com.sharechain.finance.bean.WxLoginBean;
import com.sharechain.finance.utils.BaseUtils;
import com.sharechain.finance.utils.GlideUtils;
import com.sharechain.finance.view.dialog.ExitLoginDialog;
import com.sharechain.finance.view.dialog.LoadDialog;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/13.
 */

public class MineActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.ll_top_info)
    LinearLayout ll_top_info;
    @BindView(R.id.user_image)
    ImageView userImage;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.user_uid)
    TextView user_uid;
    @BindView(R.id.history_tv)
    TextView historyTv;
    @BindView(R.id.my_news_tv)
    TextView myNewsTv;
    @BindView(R.id.my_follow_tv)
    TextView myFollowTv;
    @BindView(R.id.suggest_tv)
    TextView suggestTv;
    @BindView(R.id.clear_cache_tv)
    TextView clearCacheTv;
    @BindView(R.id.score_tv)
    TextView scoreTv;
    @BindView(R.id.exit_tv)
    TextView exitTv;
    @BindView(R.id.news_red_tv)
    TextView news_red_tv;
    @BindView(R.id.ll_user_info)
    LinearLayout ll_user_info;
    @BindView(R.id.user_login)
    TextView user_login;

    private IWXAPI iwxapi;
    private String size;//glide缓存大小；

    @Override
    public int getLayout() {
        return R.layout.activity_mine;
    }
    private ExitLoginDialog mDialog;
    private Dialog dialog;

    @Override
    public void initView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        iwxapi = WXAPIFactory.createWXAPI(this, SFApplication.WX_APPID, true);
        //将应用注册到微信
        iwxapi.registerApp(SFApplication.WX_APPID);
        mImmersionBar.statusBarColor(android.R.color.transparent).init();
        setTitlePadding(ll_top_info);
        updateView();
        dialog = new LoadDialog().LoadProgressDialog(this);
    }

    @Override
    public void initData() {
        size = GlideUtils.getInstance().getCacheSize(this);
        clearCacheTv.setText(size + "");
    }

    private void updateView() {
        if (SFApplication.loginDataBean != null) {
            //已登录
            ll_user_info.setVisibility(View.VISIBLE);
            user_login.setVisibility(View.GONE);
            exitTv.setVisibility(View.VISIBLE);
            userName.setText(SFApplication.loginDataBean.getNick_name());
            RequestOptions options = new RequestOptions().circleCrop().placeholder(R.drawable.user_center_default).diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            GlideUtils.getInstance().loadUserImage(MineActivity.this.getApplicationContext(), SFApplication.loginDataBean.getHead_img(), userImage, options);
        } else {
            //未登录
            ll_user_info.setVisibility(View.GONE);
            user_login.setVisibility(View.VISIBLE);
            userImage.setImageResource(R.drawable.user_center_default);
            exitTv.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.back_iv, R.id.user_image, R.id.history_tv, R.id.my_news_tv, R.id.my_follow_tv, R.id.suggest_tv, R.id.clear_cache_tv, R.id.score_tv, R.id.exit_tv, R.id.about_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.user_image:
//                BaseUtils.openActivity(this, PersonalCenterActivity.class, null);
                break;
            case R.id.history_tv:
                BaseUtils.openActivity(this, HistoryActivity.class, null);
                break;
            case R.id.my_news_tv:
                BaseUtils.openActivity(this, MyNewsActivity.class, null);
                break;
            case R.id.my_follow_tv:
                BaseUtils.openActivity(this, MyFollowActivity.class, null);
                break;
            case R.id.suggest_tv:
                BaseUtils.openActivity(this, FeedbackActivity.class, null);
                break;
            case R.id.clear_cache_tv:
                GlideUtils.getInstance().clearImageAllCache(this);
                clearCacheTv.setText("");
                break;
            case R.id.score_tv:
//                BaseUtils.openActivity(this, SearchActivity.class, null);
                break;
            case R.id.about_tv:
                BaseUtils.openActivity(this, AboutFinanceActivity.class, null);
                break;
            case R.id.exit_tv:
                displayDialog(this,getString(R.string.exit_login),getString(R.string.exit_yes),getString(R.string.exit_no));
                break;

        }
    }

    /**
     * 显示对话框
     *
     * @param ctx     上下文
     * @param title   标题
     * @param cancel  左边显示的文本
     * @param confirm 右边显示的文本
     */
    private void displayDialog(Context ctx, String title, String cancel, String confirm) {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null; // 对话框的dismiss的资源释放但内容不会变
        }
        if (mDialog == null) {
            mDialog = new ExitLoginDialog(ctx, title, cancel, confirm);
            mDialog.setCanceledOnTouchOutside(true);
            mDialog.setOnNegativeListener(this);
            mDialog.setOnPositiveListener(this);
            mDialog.show();
        }
    }

    @OnClick(R.id.ll_user_login)
    void login() {
        if (SFApplication.loginDataBean == null) {
            //未登录，点击进入登录
            sendWxLogin();
        }
    }

    /**
     * 调起微信登录
     */
    private void sendWxLogin() {
        // send oauth request
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = SFApplication.WX_LOGIN_STATE;
        iwxapi.sendReq(req);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(BaseNotifyBean event) {
        if (event.getType() == BaseNotifyBean.TYPE.TYPE_LOGIN_WEIXIN) {
            if (!BaseUtils.isEmpty(event.getMessage())) {
                //登录回调
                loginWX(event.getMessage());
            }
        }
    }

    //消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewsEvent(NewsEven event) {
        if (event.getNews() == 0) {
            news_red_tv.setVisibility(View.GONE);
        } else {
            news_red_tv.setVisibility(View.VISIBLE);
        }
    }

    private void loginWX(String code) {
        HashMap<String, String> params = new HashMap<>();
        params.put("code", code);
        requestGet(UrlList.WX_LOGIN, params, new MyStringCallback(this) {
            @Override
            protected void onSuccess(String result) {
                WxLoginBean bean = JSON.parseObject(result, WxLoginBean.class);
                if (bean.isSuccess()) {
                    //先删除用户数据
                    SFApplication.loginDataBean = bean.getData();
                    updateView();
                    DataSupport.deleteAll(LoginDataBean.class);
                    LoginDataBean loginDataBean = bean.getData();
                    //存储用户数据
                    loginDataBean.save();
                }
            }

            @Override
            protected void onFailed(String errStr) {

            }
        });
    }

    /**
     * 获取微信登录accessToken(第一次调用获取token和refreshToken)
     */
    private void getWxAccessToken(String code) {
        StringBuffer sb = new StringBuffer();
        sb.append("https://api.weixin.qq.com/sns/oauth2/access_token");
        sb.append("?appid=" + SFApplication.WX_APPID);
        sb.append("&secret=" + SFApplication.WX_APPSECRET);
        sb.append("&code=" + code);
        sb.append("&grant_type=authorization_code");
        requestGet(sb.toString(), new MyStringCallback(this) {
            @Override
            protected void onSuccess(String result) {
                WXAccessBean wxAccessBean = JSON.parseObject(result, WXAccessBean.class);
            }

            @Override
            protected void onFailed(String errStr) {

            }
        });
    }

    /**
     * 根据refreshToken刷新accessToken(refreshToken有效期40天，先调用此方法刷新token)
     */
    private void refreshWxAccessToken(String refresh_token) {
        StringBuffer sb = new StringBuffer();
        sb.append("https://api.weixin.qq.com/sns/oauth2/refresh_token");
        sb.append("?appid=" + SFApplication.WX_APPID);
        sb.append("&grant_type=refresh_token");
        sb.append("&refresh_token" + refresh_token);
        requestGet(sb.toString(), new MyStringCallback(this) {
            @Override
            protected void onSuccess(String result) {
                WXRefreshBean wxRefreshBean = JSON.parseObject(result, WXRefreshBean.class);
            }

            @Override
            protected void onFailed(String errStr) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.negativeButton:// 对话框右边按钮监听
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                exitTv.setVisibility(View.GONE);
                //退出登录
                SFApplication.loginDataBean = null;
                DataSupport.deleteAll(LoginDataBean.class);
                updateView();
                break;
            case R.id.positiveButton: // 对话框左边按钮监听
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                break;
        }
    }

}
