package com.sharechain.finance;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sharechain.finance.fragment.main.FastMsgFragment;
import com.sharechain.finance.fragment.main.FriendCircleFragment;
import com.sharechain.finance.fragment.main.HomeFragment;
import com.sharechain.finance.fragment.main.MineFragment;
import com.sharechain.finance.view.FragmentTabHost;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    private static final String TAB_HOME = "TAB_HOME";
    private static final String TAB_FAST_MSG = "TAB_FAST_MSG";
    private static final String TAB_FRIEND_CIRCLE = "TAB_FRIEND_CIRCLE";
    private static final String TAB_MINE = "TAB_MINE";

    private enum BOTTOM_ITEM {
        HOME, FAST_MSG, FRIEND_CIRCLE, MINE
    }

    private BOTTOM_ITEM curItem = BOTTOM_ITEM.HOME;

    @BindView(android.R.id.tabhost)
    FragmentTabHost tabhost;
    @BindView(R.id.ll_bottom)
    LinearLayout ll_bottom;
    @BindView(R.id.tv_tabhost_home)
    TextView tv_tabhost_home;
    @BindView(R.id.tv_tabhost_fast_msg)
    TextView tv_tabhost_fast_msg;
    @BindView(R.id.tv_tabhost_friend_circle)
    TextView tv_tabhost_friend_circle;
    @BindView(R.id.tv_tabhost_mine)
    TextView tv_tabhost_mine;

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

    @OnClick(R.id.tv_tabhost_mine)
    void clickMine() {
        curItem = BOTTOM_ITEM.MINE;
        setBottom();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabhost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        tabhost.getTabWidget().setVisibility(View.GONE);
        tabhost.setCurrentTabByTag(TAB_HOME);
//        tv_tabhost_home.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.welfare_center), null, null);

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
//        getData();
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
//                tv_tabhost_welfare.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.welfare_center), null, null);
                tabhost.setCurrentTabByTag(TAB_HOME);
                break;
            case FAST_MSG:
//                tv_tabhost_employee_privileges.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.employee_privileges), null, null);
                tabhost.setCurrentTabByTag(TAB_FAST_MSG);
                break;
            case FRIEND_CIRCLE:
//                tv_tabhost_shopping_mall.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.shopping_mall), null, null);
                tabhost.setCurrentTabByTag(TAB_FRIEND_CIRCLE);
                break;
            case MINE:
//                tv_tabhost_shopping_cart.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.home_page_discovery_yellow), null, null);
                tabhost.setCurrentTabByTag(TAB_MINE);
                break;
        }
    }

    private void unCheckAllBottom() {
//        tv_tabhost_welfare.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.welfare_center_gray), null, null);
//        tv_tabhost_employee_privileges.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.employee_privileges_gray), null, null);
//        tv_tabhost_shopping_mall.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.shopping_mall_gray), null, null);
//        tv_tabhost_shopping_cart.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.home_page_discovery), null, null);
//        tv_tabhost_my.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.my_gray), null, null);
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
