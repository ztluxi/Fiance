package com.sharechain.finance.fragment.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.sharechain.finance.BaseFragment;
import com.sharechain.finance.MyStringCallback;
import com.sharechain.finance.R;
import com.sharechain.finance.adapter.ViewPagerAdapter;
import com.sharechain.finance.bean.BaseNotifyBean;
import com.sharechain.finance.bean.HomeIndexBean;
import com.sharechain.finance.bean.NewsChannelTable;
import com.sharechain.finance.bean.UrlList;
import com.sharechain.finance.module.home.ManageTagActivity;
import com.sharechain.finance.module.mine.MineActivity;
import com.sharechain.finance.module.mine.SearchActivity;
import com.sharechain.finance.utils.BaseUtils;
import com.sharechain.finance.view.ScaleTransitionPagerTitleView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/13.
 */

public class HomeFragment extends BaseFragment {
    private List<NewsChannelTable> typeList = new ArrayList<>();
    @BindView(R.id.magic_indicator)
    MagicIndicator magic_indicator;

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.iv_add_channel)
    ImageView add_channel;
    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    private ViewPagerAdapter mPagerAdapter;
    private List<String> titleList = new ArrayList<>();

    @Override
    protected int getLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        setTitlePadding(rl_top, BaseUtils.dip2px(getActivity(), 10));
    }

    @Override
    public void initData() {
        getHomeIndexData();
        loadData();
    }

    private void initMagicIndicator() {
        magic_indicator.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.tint_home_color));
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        commonNavigator.setScrollPivotX(0.8f);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return typeList == null ? 0 : typeList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                simplePagerTitleView.setText(typeList.get(index).getNewsChannelName());
                simplePagerTitleView.setTextSize(18);
                simplePagerTitleView.setNormalColor(ContextCompat.getColor(getActivity(), R.color.white));
                simplePagerTitleView.setSelectedColor(ContextCompat.getColor(getActivity(), R.color.white));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewpager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }

            @Override
            public float getTitleWeight(Context context, int index) {
                if (index == 0) {
                    return 2.0f;
                } else if (index == 1) {
                    return 1.5f;
                } else {
                    return 1.5f;
                }
            }

        });
        magic_indicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magic_indicator, viewpager);
    }

    private void loadData() {
        mPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < typeList.size(); i++) {
            if (i == 0) {
                fragments.add(NewsFragment.newInstance(typeList.get(i).getNewsChannelId()));
            } else {
                fragments.add(AnswerFragment.newInstance(typeList.get(i).getNewsChannelId()));
            }
        }
        mPagerAdapter.setItems(fragments, titleList);
        viewpager.setAdapter(mPagerAdapter);
        viewpager.setOffscreenPageLimit(typeList.size());
        initMagicIndicator();
    }

    @OnClick(R.id.iv_add_channel)
    public void onViewClicked() {
        BaseUtils.openActivity(getActivity(), MineActivity.class, null);
    }

    @OnClick(R.id.image_add_tag)
    void addTag() {
        Bundle data = new Bundle();
        data.putSerializable("list", (Serializable) typeList);
        BaseUtils.openActivityBottomAnim(getActivity(), ManageTagActivity.class, data);
    }

    @OnClick(R.id.text_search)
    void search() {
        BaseUtils.openActivity(getActivity(), SearchActivity.class, null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(BaseNotifyBean event) {
        if (event.getType() == BaseNotifyBean.TYPE.TYPE_MANAGE_TAG_RESULT) {
            typeList.clear();
            titleList.clear();
            typeList.addAll((List<NewsChannelTable>) event.getObj());
            for (NewsChannelTable table : typeList) {
                titleList.add(table.getNewsChannelName());
            }
            loadData();
        }
    }

    private void getHomeIndexData() {
        requestGet(UrlList.HOME_INDEX, new MyStringCallback(getActivity()) {
            @Override
            protected void onSuccess(String result) {
                HomeIndexBean bean = JSON.parseObject(result, HomeIndexBean.class);
                if (bean.getSuccess() == UrlList.CODE_SUCCESS) {
                    //返回成功
                    for (int i = 0; i < bean.getData().getArticle_title_lists().size(); i++) {
                        HomeIndexBean.DataBean.ArticleTitleListsBean titleListsBean = bean.getData().getArticle_title_lists().get(i);
                        NewsChannelTable table = new NewsChannelTable();
                        table.setNewsChannelId(String.valueOf(titleListsBean.getTerm_taxonomy_id()));
                        table.setNewsChannelName(titleListsBean.getName());
                        if (i == 0) {
                            table.setNewsChannelFixed(true);
                            table.setNewsChannelSelect(false);
                        } else {
                            table.setNewsChannelFixed(false);
                            table.setNewsChannelSelect(false);
                        }
                        typeList.add(table);
                        titleList.add(titleListsBean.getName());
                        loadData();
                    }
                }
            }

            @Override
            protected void onFailed(String errStr) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
