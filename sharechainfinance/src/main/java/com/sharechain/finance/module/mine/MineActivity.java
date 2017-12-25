package com.sharechain.finance.module.mine;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.request.RequestOptions;
import com.sharechain.finance.BaseActivity;
import com.sharechain.finance.MyStringCallback;
import com.sharechain.finance.R;
import com.sharechain.finance.SFApplication;
import com.sharechain.finance.bean.BaseNotifyBean;
import com.sharechain.finance.bean.WXAccessBean;
import com.sharechain.finance.bean.WXRefreshBean;
import com.sharechain.finance.utils.BaseUtils;
import com.sharechain.finance.utils.GlideUtils;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/13.
 */

public class MineActivity extends BaseActivity {

    @BindView(R.id.ll_top_info)
    LinearLayout ll_top_info;
    @BindView(R.id.user_image)
    ImageView userImage;
    @BindView(R.id.user_name)
    TextView userName;
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

    private IWXAPI iwxapi;

    @Override
    public int getLayout() {
        return R.layout.activity_mine;
    }

    @Override
    public void initView() {
        iwxapi = WXAPIFactory.createWXAPI(this, SFApplication.WX_APPID);
        mImmersionBar.statusBarColor(android.R.color.transparent).init();
        setTitlePadding(ll_top_info);
        RequestOptions options = new RequestOptions().circleCrop();
        options.placeholder(R.drawable.icon_share_weixin);
        GlideUtils.loadUserImage(this, "http://img4.duitang.com/uploads/item/201208/17/20120817123857_NnPNB.thumb.600_0.jpeg", userImage, options);
    }

    @Override
    public void initData() {

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
                break;
            case R.id.score_tv:
                BaseUtils.openActivity(this, SearchActivity.class, null);
                break;
            case R.id.about_tv:
                BaseUtils.openActivity(this, AboutFinanceActivity.class, null);
                break;
            case R.id.exit_tv:
                break;

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
            }
        }
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


}
