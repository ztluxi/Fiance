package com.sharechain.finance;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sharechain.finance.bean.BaseNotifyBean;
import com.sharechain.finance.fragment.main.FastMsgFragment;
import com.sharechain.finance.fragment.main.FriendCircleFragment;
import com.sharechain.finance.fragment.main.HomeFragment;
import com.sharechain.finance.utils.ToastManager;
import com.sharechain.finance.view.FragmentTabHost;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    private static final String TAB_HOME = "TAB_HOME";
    private static final String TAB_FAST_MSG = "TAB_FAST_MSG";
    private static final String TAB_FRIEND_CIRCLE = "TAB_FRIEND_CIRCLE";

    private enum BOTTOM_ITEM {
        HOME, FAST_MSG, FRIEND_CIRCLE
    }

    private long backTime; // 记录用户点击的时间
    private BOTTOM_ITEM curItem = BOTTOM_ITEM.HOME;
    private boolean isSwitchFastMsg = false;//是否切换到快讯界面

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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isSwitchFastMsg = getIntent().getIntExtra("type", 0) == 2;
        tabhost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        tabhost.getTabWidget().setVisibility(View.GONE);
        tabhost.setCurrentTabByTag(TAB_HOME);
        tv_tabhost_home.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.icon_tab_home), null, null);
        tv_tabhost_home.setTextColor(getResources().getColor(R.color.color_base_blue));
        tabHostAddTab();
        tv_tabhost_fast_msg.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchDown(tv_tabhost_fast_msg);
                        break;
                    case MotionEvent.ACTION_UP:
                        touchUp(tv_tabhost_fast_msg);
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        isSwitchFastMsg = intent.getIntExtra("type", 0) == 2;
        if (isSwitchFastMsg) {
            curItem = BOTTOM_ITEM.FAST_MSG;
            unCheckAllBottom();
            tabhost.setCurrentTabByTag(TAB_FAST_MSG);
            //EventBus发送消息
            BaseNotifyBean bean = new BaseNotifyBean();
            bean.setType(BaseNotifyBean.TYPE.TYPE_REFRESH_FASTMSG_DATA);
            EventBus.getDefault().post(bean);
        } else {
            tabhost.setCurrentTabByTag(TAB_HOME);
        }
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
        if (isSwitchFastMsg) {
            curItem = BOTTOM_ITEM.FAST_MSG;
            setBottom();
        }
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

    private void touchDown(ImageView iv) {
        ScaleAnimation animation = new ScaleAnimation(1, 0.9f, 1, 0.9f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(100);
        animation.setFillAfter(true);
        iv.startAnimation(animation);
    }

    private void touchUp(ImageView iv) {
        ScaleAnimation animation = new ScaleAnimation(0.8f, 1, 0.8f, 1, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(100);
        animation.setFillAfter(true);
        iv.startAnimation(animation);
    }

    //是否是从推送消息打开
    public boolean isSwitchFastMsg() {
        return isSwitchFastMsg;
    }

    // 添加系统监听的返回
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - backTime) > 2000) {
                backTime = System.currentTimeMillis();
                ToastManager.showShort(this, getString(R.string.again_exit));
            } else if ((System.currentTimeMillis() - backTime) < 2000) {
                backTime = System.currentTimeMillis();
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
