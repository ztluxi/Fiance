package com.sharechain.finance;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sharechain.finance.fragment.main.FastMsgFragment;
import com.sharechain.finance.fragment.main.FriendCircleFragment;
import com.sharechain.finance.fragment.main.HomeFragment;
import com.sharechain.finance.fragment.main.MineFragment;
import com.sharechain.finance.view.FragmentTabHost;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    private static final String TAB_HOME = "TAB_HOME";
    private static final String TAB_FAST_MSG = "TAB_FAST_MSG";
    private static final String TAB_FRIEND_CIRCLE = "TAB_FRIEND_CIRCLE";
    private static final String TAB_MINE = "TAB_MINE";

    private enum BOTTOM_ITEM {
        HOME, FAST_MSG, FRIEND_CIRCLE
    }

    private BOTTOM_ITEM curItem = BOTTOM_ITEM.HOME;

    @BindView(android.R.id.tabhost)
    FragmentTabHost tabhost;
    @BindView(R.id.ll_bottom)
    LinearLayout ll_bottom;
    @BindView(R.id.tv_tabhost_home)
    TextView tv_tabhost_home;
    @BindView(R.id.tv_tabhost_fast_msg)
    ImageView tv_tabhost_fast_msg;
    @BindView(R.id.tv_tabhost_friend_circle)
    TextView tv_tabhost_friend_circle;

    @OnClick(R.id.tv_tabhost_home)
    void clickHome() {
        curItem = BOTTOM_ITEM.HOME;
        setBottom();
    }

    @OnClick(R.id.tv_tabhost_fast_msg)
    void clickFastMsg() {
        curItem = BOTTOM_ITEM.FAST_MSG;
        setBottom();
    }

    @OnClick(R.id.tv_tabhost_friend_circle)
    void clickFriendCircle() {
        curItem = BOTTOM_ITEM.FRIEND_CIRCLE;
        setBottom();
    }

    public IWXAPI iwxapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTintDrawable(R.drawable.common_home_tint_color_bg);
        tabhost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        tabhost.getTabWidget().setVisibility(View.GONE);
        tabhost.setCurrentTabByTag(TAB_HOME);
        tv_tabhost_home.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.icon_tab_home), null, null);
        tv_tabhost_home.setTextColor(getResources().getColor(R.color.color_base_blue));
        tabHostAddTab();
        registerToWX();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
//        getData();
    }

    private void registerToWX() {
        iwxapi = WXAPIFactory.createWXAPI(this, SFApplication.WX_APPID, true);
        //将应用注册到微信
        iwxapi.registerApp(SFApplication.WX_APPID);
    }

    private void tabHostAddTab() {
        tabhost.addTab(tabhost.newTabSpec(TAB_HOME).setIndicator(TAB_HOME), HomeFragment.class, null);
        tabhost.addTab(tabhost.newTabSpec(TAB_FAST_MSG).setIndicator(TAB_FAST_MSG), FastMsgFragment.class, null);
        tabhost.addTab(tabhost.newTabSpec(TAB_FRIEND_CIRCLE).setIndicator(TAB_FRIEND_CIRCLE), FriendCircleFragment.class, null);
        tabhost.addTab(tabhost.newTabSpec(TAB_MINE).setIndicator(TAB_MINE), MineFragment.class, null);
    }

    private void setBottom() {
        unCheckAllBottom();
        switch (curItem) {
            case HOME:
                tv_tabhost_home.setTextColor(getResources().getColor(R.color.color_base_blue));
                tv_tabhost_home.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.icon_tab_home), null, null);
                tabhost.setCurrentTabByTag(TAB_HOME);
                break;
            case FAST_MSG:
                tabhost.setCurrentTabByTag(TAB_FAST_MSG);
                break;
            case FRIEND_CIRCLE:
                tv_tabhost_friend_circle.setTextColor(getResources().getColor(R.color.color_999999));
                tv_tabhost_friend_circle.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.icon_tab_big_brother), null, null);
                tabhost.setCurrentTabByTag(TAB_FRIEND_CIRCLE);
                break;
        }
    }

    private void unCheckAllBottom() {
        tv_tabhost_home.setTextColor(getResources().getColor(R.color.color_base_blue));
        tv_tabhost_home.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.icon_tab_home), null, null);
        tv_tabhost_friend_circle.setTextColor(getResources().getColor(R.color.color_999999));
        tv_tabhost_friend_circle.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.icon_tab_big_brother), null, null);
    }

    private void getData() {
        requestGet("https://www.baidu.com", new MyStringCallback(this) {

            @Override
            void onSuccess(String result) {
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
            }

            @Override
            void onFailed(String errStr) {

            }
        });
    }

}
