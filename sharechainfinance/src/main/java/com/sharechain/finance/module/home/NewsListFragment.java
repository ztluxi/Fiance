package com.sharechain.finance.module.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sharechain.finance.BaseFragment;
import com.sharechain.finance.R;
import com.sharechain.finance.adapter.HomePageAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by ${zhoutao} on 2017/12/13 0013.
 */
public class NewsListFragment extends BaseFragment implements BGABanner.Adapter<ImageView, String>, BGABanner.Delegate<ImageView, String> {

    private static final String NEWS_TYPE_KEY = "NewsTypeKey";

    @BindView(R.id.xrefreshview)
    XRefreshView xrefreshview;

    @BindView(R.id.lv_news_list)
    ListView mLvNewsList;

    private BGABanner mBanner;
    private String mNewsId;
    private HomePageAdapter adviceAdapter;
    private List<String> dataList = new ArrayList<>();

    public static NewsListFragment newInstance(String newsId) {
        NewsListFragment fragment = new NewsListFragment();
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
        return R.layout.fragment_news_list;
    }

    @Override
    protected void initView() {
        // 初始化HeaderView
        View headerView = View.inflate(getActivity(), R.layout.layout_header, null);
        mBanner = headerView.findViewById(R.id.banner);
        mBanner.setAdapter(this);
        mBanner.setDelegate(this);

        initData();
        initListData();
        initXRefreshView(xrefreshview);
        xrefreshview.setPullRefreshEnable(true);
        xrefreshview.setPullLoadEnable(false);
        xrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
                xrefreshview.stopRefresh();
            }

        });

        // 初始化ListView
        mLvNewsList.addHeaderView(headerView);
        adviceAdapter = new HomePageAdapter(getActivity(), R.layout.item_homepage);
        adviceAdapter.setData(dataList);
        mLvNewsList.setAdapter(adviceAdapter);
    }

    public void initData() {
        List<String> image = new ArrayList<>();
        List<String> tips = new ArrayList<>();
        image.add("http://7xk9dj.com1.z0.glb.clouddn.com/banner/imgs/7.png");
        image.add("http://7xk9dj.com1.z0.glb.clouddn.com/banner/imgs/8.png");
        image.add("http://7xk9dj.com1.z0.glb.clouddn.com/banner/imgs/9.png");
        image.add("http://7xk9dj.com1.z0.glb.clouddn.com/banner/imgs/10.png");
        image.add("http://7xk9dj.com1.z0.glb.clouddn.com/banner/imgs/11.png");
        tips.add("第一个Binner");
        tips.add("第二个Binner");
        tips.add("第三个Binner");
        tips.add("第四个Binner");
        tips.add("第五个Binner");
        mBanner.setData(image, tips);
    }

    private void initListData() {
        for (int i = 0; i < 10; i++) {
            dataList.add("item_" + i);
        }
    }

    @Override
    public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
        Glide.with(this)
                .load(model)
                .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).dontAnimate().centerCrop())
                .into(itemView);
    }

    @Override
    public void onBannerItemClick(BGABanner banner, ImageView itemView, String model, int position) {
        Toast.makeText(getActivity(), "点击了第" + (position + 1) + "页", Toast.LENGTH_SHORT).show();
    }

}
