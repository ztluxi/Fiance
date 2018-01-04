package com.sharechain.finance;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sharechain.finance.fragment.main.FastMsgFragment;
import com.sharechain.finance.fragment.main.FriendCircleFragment;
import com.sharechain.finance.fragment.main.HomeFragment;
import com.sharechain.finance.view.FragmentTabHost;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    private static final String TAB_HOME = "TAB_HOME";
    private static final String TAB_FAST_MSG = "TAB_FAST_MSG";
    private static final String TAB_FRIEND_CIRCLE = "TAB_FRIEND_CIRCLE";

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
//        suoFang(tv_tabhost_fast_msg);
        setBottom();
    }

    @OnClick(R.id.tv_tabhost_friend_circle)
    void clickFriendCircle() {
        curItem = BOTTOM_ITEM.FRIEND_CIRCLE;
        setBottom();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabhost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        tabhost.getTabWidget().setVisibility(View.GONE);
        tabhost.setCurrentTabByTag(TAB_HOME);
        tv_tabhost_home.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.icon_tab_home), null, null);
        tv_tabhost_home.setTextColor(getResources().getColor(R.color.color_base_blue));
        tabHostAddTab();
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

    }

    private void tabHostAddTab() {
        tabhost.addTab(tabhost.newTabSpec(TAB_HOME).setIndicator(TAB_HOME), HomeFragment.class, null);
        tabhost.addTab(tabhost.newTabSpec(TAB_FAST_MSG).setIndicator(TAB_FAST_MSG), FastMsgFragment.class, null);
        tabhost.addTab(tabhost.newTabSpec(TAB_FRIEND_CIRCLE).setIndicator(TAB_FRIEND_CIRCLE), FriendCircleFragment.class, null);
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
                tv_tabhost_friend_circle.setTextColor(getResources().getColor(R.color.color_base_blue));
                tv_tabhost_friend_circle.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.icon_tab_big_brother_un), null, null);
                tabhost.setCurrentTabByTag(TAB_FRIEND_CIRCLE);
                break;
        }
    }

    private void unCheckAllBottom() {
        tv_tabhost_home.setTextColor(getResources().getColor(R.color.color_999999));
        tv_tabhost_home.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.icon_tab_home_un), null, null);
        tv_tabhost_friend_circle.setTextColor(getResources().getColor(R.color.color_999999));
        tv_tabhost_friend_circle.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.icon_tab_big_brother), null, null);
    }

    //切换到快讯主页
    public void switchToFastMsg() {
        curItem = BOTTOM_ITEM.FAST_MSG;
        setBottom();
    }

    private void suoFang(ImageView iv) {
        ScaleAnimation animation = new ScaleAnimation(0.8f, 1, 0.8f, 1, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(200);
        animation.setFillAfter(true);
        iv.startAnimation(animation);
    }

}
