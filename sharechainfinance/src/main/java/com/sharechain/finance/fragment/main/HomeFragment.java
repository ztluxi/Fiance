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
import com.sharechain.finance.bean.MainCacheBean;
import com.sharechain.finance.bean.NewsChannelTable;
import com.sharechain.finance.bean.UrlList;
import com.sharechain.finance.module.home.ManageTagActivity;
import com.sharechain.finance.module.mine.HistoryActivity;
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
import org.litepal.crud.DataSupport;

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
    //频道列表
    private List<String> titleList = new ArrayList<>();
    //首页数据
    private HomeIndexBean homeIndexBean;

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
        //json缓存
        List<MainCacheBean> newsCache = DataSupport.where("type = ?", String.valueOf(MainCacheBean.TYPE_HOME_NEWS)).find(MainCacheBean.class);
        if (newsCache.size() > 0) {
            String cacheJson = newsCache.get(0).getCacheJson();
            homeIndexBean = JSON.parseObject(cacheJson, HomeIndexBean.class);
        }
        //标签缓存
        List<NewsChannelTable> tmpMineList = DataSupport.where("cacheType = ?", String.valueOf(NewsChannelTable.CACHE_TYPE_MINE)).find(NewsChannelTable.class);
        typeList.addAll(tmpMineList);//从数据库获取
        titleList.clear();
        for (NewsChannelTable table : typeList) {
            titleList.add(table.getNewsChannelName());
        }
        //获取标签，轮播图，热讯数据
        getHomeIndexData();
        //加载轮播图，热讯控件
        loadData();
    }

    private void initMagicIndicator() {
        magic_indicator.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.tint_home_color));
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        commonNavigator.setEnablePivotScroll(true);
        commonNavigator.setSkimOver(true);
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
        if (homeIndexBean != null) {
            mPagerAdapter = new ViewPagerAdapter(getFragmentManager());
            List<Fragment> fragments = new ArrayList<>();
            for (int i = 0; i < typeList.size(); i++) {
                if (i == 0) {
                    fragments.add(NewsFragment.newInstance(homeIndexBean, i));
                } else {
                    fragments.add(AnswerFragment.newInstance(homeIndexBean, i));
                }
            }
            mPagerAdapter.setItems(fragments, titleList);
            viewpager.setAdapter(mPagerAdapter);
            viewpager.setOffscreenPageLimit(typeList.size());
            initMagicIndicator();
        }
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

    @OnClick(R.id.image_history)
    void getHistory() {
        BaseUtils.openActivity(getActivity(), HistoryActivity.class, null);
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

    //获取频道列表，banner列表，快讯数据
    private void getHomeIndexData() {
        requestGet(UrlList.HOME_INDEX, new MyStringCallback(getActivity()) {
            @Override
            protected void onSuccess(String result) {
                HomeIndexBean bean = JSON.parseObject(result, HomeIndexBean.class);
                if (bean.getSuccess() == UrlList.CODE_SUCCESS) {
                    homeIndexBean = bean;
                    //保存首页缓存数据
                    DataSupport.deleteAll(MainCacheBean.class, "type = ?", String.valueOf(MainCacheBean.TYPE_HOME_NEWS));
                    MainCacheBean mainCacheBean = new MainCacheBean();
                    mainCacheBean.setType(MainCacheBean.TYPE_HOME_NEWS);
                    mainCacheBean.setCacheJson(result);
                    mainCacheBean.save();
                    if (typeList.size() == 0) {
                        //第一次进入app
                        for (int i = 0; i < bean.getData().getArticle_title_lists().size(); i++) {
                            HomeIndexBean.DataBean.ArticleTitleListsBean titleListsBean = bean.getData().getArticle_title_lists().get(i);
                            NewsChannelTable allTable = getCacheAllTable(titleListsBean, i);
                            NewsChannelTable mineTable = getCacheMineTable(titleListsBean, i);
                            allTable.save();
                            mineTable.save();
                            //第一次进入
                            typeList.add(mineTable);
                            titleList.add(titleListsBean.getName());
                        }
                    } else {
                        titleList.clear();
                        for (NewsChannelTable table : typeList) {
                            titleList.add(table.getNewsChannelName());
                        }
                    }
                    //返回成功
                    loadData();
                }
            }

            @Override
            protected void onFailed(String errStr) {

            }
        });
    }

    private NewsChannelTable getCacheAllTable(HomeIndexBean.DataBean.ArticleTitleListsBean titleListsBean, int i) {
        NewsChannelTable table = new NewsChannelTable();
        table.setNewsChannelId(String.valueOf(titleListsBean.getTerm_taxonomy_id()));
        table.setNewsChannelName(titleListsBean.getName());
        table.setCacheType(NewsChannelTable.CACHE_TYPE_ALL);//设置缓存类型为全部
        if (i == 0) {
            table.setNewsChannelFixed(true);
            table.setNewsChannelSelect(false);
        } else {
            table.setNewsChannelFixed(false);
            table.setNewsChannelSelect(false);
        }
        return table;
    }

    private NewsChannelTable getCacheMineTable(HomeIndexBean.DataBean.ArticleTitleListsBean titleListsBean, int i) {
        NewsChannelTable table = new NewsChannelTable();
        table.setNewsChannelId(String.valueOf(titleListsBean.getTerm_taxonomy_id()));
        table.setNewsChannelName(titleListsBean.getName());
        table.setCacheType(NewsChannelTable.CACHE_TYPE_MINE);//设置缓存类型为全部
        if (i == 0) {
            table.setNewsChannelFixed(true);
            table.setNewsChannelSelect(false);
        } else {
            table.setNewsChannelFixed(false);
            table.setNewsChannelSelect(false);
        }
        return table;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
