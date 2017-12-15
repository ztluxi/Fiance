package com.sharechain.finance.module.mine;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.andview.refreshview.XRefreshView;
import com.sharechain.finance.BaseActivity;
import com.sharechain.finance.R;
import com.sharechain.finance.adapter.HistoryAdapter;
import com.sharechain.finance.adapter.MyNewsAdapter;
import com.sharechain.finance.bean.HomeData;
import com.sharechain.finance.bean.NewsData;
import com.sharechain.finance.utils.ToastManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.baseadapter.BGAOnItemChildClickListener;

/**
 * Created by ${zhoutao} on 2017/12/13 0013.
 */

public class MyNewsActivity extends BaseActivity {

    @BindView(R.id.image_title_left)
    ImageView back_Image;
    @BindView(R.id.my_news_lv)
    ListView myNewslv;
    @BindView(R.id.xrefreshview_content)
    XRefreshView refreshView;
    private List<NewsData> newsDataList = new ArrayList<>();
    private MyNewsAdapter newsAdapter;
    @Override
    public int getLayout() {
        return R.layout.activity_my_news;
    }

    @Override
    public void initView() {
        initTitle(getString(R.string.my_news));
        back_Image.setVisibility(View.VISIBLE);
        back_Image.setImageResource(R.drawable.add_sel);
        initData();
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
        newsAdapter = new MyNewsAdapter(this, R.layout.adapter_my_news_item);
        newsAdapter.setOnItemChildClickListener(new BGAOnItemChildClickListener() {
            @Override
            public void onItemChildClick(ViewGroup parent, View childView, int position) {
                ToastManager.showShort(MyNewsActivity.this,"您点了"+position);
            }
        });
        newsAdapter.setData(newsDataList);
        myNewslv.setAdapter(newsAdapter);

    }

    @Override
    public void initData() {
        for (int i = 0; i < 10; i++) {
            NewsData newsData = new NewsData();
            newsData.setTime("2017-07-12");
            newsData.setTitle("大佬回复了你的评论");
            newsData.setImage("http://img4.duitang.com/uploads/item/201208/17/20120817123857_NnPNB.thumb.600_0.jpeg");
            newsDataList.add(newsData);
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
