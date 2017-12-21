package com.sharechain.finance.module.mine;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.andview.refreshview.XRefreshView;
import com.sharechain.finance.BaseActivity;
import com.sharechain.finance.R;
import com.sharechain.finance.adapter.MyFollowAdapter;
import com.sharechain.finance.adapter.MyNewsAdapter;
import com.sharechain.finance.bean.FollowData;
import com.sharechain.finance.bean.NewsData;
import com.sharechain.finance.utils.ToastManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.baseadapter.BGAOnItemChildClickListener;

/**
 * Created by ${zhoutao} on 2017/12/21 0013.
 */

public class MyFollowActivity extends BaseActivity {

    @BindView(R.id.image_title_left)
    ImageView back_Image;
    @BindView(R.id.my_news_lv)
    ListView myNewslv;
    @BindView(R.id.xrefreshview_content)
    XRefreshView refreshView;
    private List<FollowData> followDataList = new ArrayList<>();


    private MyFollowAdapter followAdapter;
    @Override
    public int getLayout() {
        return R.layout.activity_my_follow;
    }

    @Override
    public void initView() {
        initTitle(getString(R.string.my_follow));
        back_Image.setVisibility(View.VISIBLE);
        initXRefreshView(refreshView);
        refreshView.setPullRefreshEnable(true);
        refreshView.setPullLoadEnable(false);
        refreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
                refreshView.stopRefresh();
            }
        });
        followAdapter = new MyFollowAdapter(this, R.layout.adapter_my_follow_item);
        followAdapter.setOnItemChildClickListener(new BGAOnItemChildClickListener() {
            @Override
            public void onItemChildClick(ViewGroup parent, View childView, int position) {
                ToastManager.showShort(MyFollowActivity.this,"您取消了"+followDataList.get(position).getName()+"的关注");
                followAdapter.removeItem(position);
            }
        });

        followAdapter.setData(followDataList);
        myNewslv.setAdapter(followAdapter);

    }

    @Override
    public void initData() {
        for (int i = 0; i < 10; i++) {
            FollowData followData = new FollowData();
            followData.setPosition("比特币分析师"+i);
            followData.setFans(i+1+"万");
            followData.setName("我是大佬"+i);
            followData.setImage("http://img4.duitang.com/uploads/item/201208/17/20120817123857_NnPNB.thumb.600_0.jpeg");
            followDataList.add(followData);
        }

    }

    @OnClick({R.id.image_title_left})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_title_left:
                finish();
                break;
        }
    }
}
