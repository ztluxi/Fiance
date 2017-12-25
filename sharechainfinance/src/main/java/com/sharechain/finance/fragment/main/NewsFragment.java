package com.sharechain.finance.fragment.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.andview.refreshview.XRefreshView;
import com.sharechain.finance.BaseFragment;
import com.sharechain.finance.R;
import com.sharechain.finance.adapter.NewsListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/15.
 */

public class NewsFragment extends BaseFragment implements NewsListAdapter.OnNewsListItemClickListener {

    private static final String NEWS_TYPE_KEY = "NewsTypeKey";

    @BindView(R.id.news_rv)
    RecyclerView mNewsRV;
    @BindView(R.id.xrefreshview)
    XRefreshView xRefreshView;

    private NewsListAdapter mNewsListAdapter;
    private String mNewsId;
    private List<String> dataList = new ArrayList<>();
    private List<String> imageList = new ArrayList<>();
    private List<String> tipList = new ArrayList<>();
    private boolean mIsAllLoaded = false;

    public static NewsFragment newInstance(String newsId) {
        NewsFragment fragment = new NewsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(NEWS_TYPE_KEY, newsId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNewsId = getArguments().getString(NEWS_TYPE_KEY);
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initView() {
        initHeaderData();
        initListData();

        mNewsListAdapter = new NewsListAdapter(getActivity(), dataList, imageList, tipList);
        initXRefreshView(xRefreshView);
        xRefreshView.setPullRefreshEnable(true);
        xRefreshView.setPullLoadEnable(false);
        xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
                xRefreshView.stopRefresh();
            }

        });

        mNewsRV.setHasFixedSize(true);
        mNewsRV.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        mNewsRV.setItemAnimator(new DefaultItemAnimator());
        mNewsListAdapter.setOnItemClickListener(this);
        mNewsRV.setAdapter(mNewsListAdapter);
    }

    @Override
    public void initData() {

    }

    private void initHeaderData() {
        imageList.add("http://7xk9dj.com1.z0.glb.clouddn.com/banner/imgs/7.png");
        imageList.add("http://7xk9dj.com1.z0.glb.clouddn.com/banner/imgs/8.png");
        imageList.add("http://7xk9dj.com1.z0.glb.clouddn.com/banner/imgs/9.png");
        imageList.add("http://7xk9dj.com1.z0.glb.clouddn.com/banner/imgs/10.png");
        imageList.add("http://7xk9dj.com1.z0.glb.clouddn.com/banner/imgs/11.png");
        tipList.add("第一个Binner");
        tipList.add("第二个Binner");
        tipList.add("第三个Binner");
        tipList.add("第四个Binner");
        tipList.add("第五个Binner");
    }

    private void initListData() {
        for (int i = 0; i < 10; i++) {
            dataList.add("item_" + i);
        }
    }

    @Override
    public void onItemClick(View view, int position, boolean isPhoto) {

    }

}
