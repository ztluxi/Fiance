package com.sharechain.finance.fragment.main;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.sharechain.finance.BaseFragment;
import com.sharechain.finance.R;

import com.sharechain.finance.adapter.ViewPagerAdapter;
import com.sharechain.finance.module.home.AddHomeTagsActivity;
import com.sharechain.finance.module.home.NewsListFragment;
import com.sharechain.finance.utils.BaseUtils;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/13.
 */

public class HomeFragment extends BaseFragment {
    private List<String> typelist = new ArrayList<>();
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.iv_add_channel)
    ImageView add_channel;
    private ViewPagerAdapter mPagerAdapter;
    private int needShowPosition = -1;

    @Override
    protected int getLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        initTitle(getString(R.string.main_tab_home));
    }

    @Override
    public void initData() {
        typelist.add("首页");
        typelist.add("内讯");
        typelist.add("外讯");
        typelist.add("解析");
        typelist.add("区块链");
        typelist.add("政策");
        loadData();
    }

    private void loadData() {
        mPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < typelist.size(); i++) {
            fragments.add(NewsListFragment.newInstance(typelist.get(i)));
        }
        mPagerAdapter.setItems(fragments, typelist);
        viewpager.setAdapter(mPagerAdapter);
        tab.setupWithViewPager(viewpager);
    }

    @OnClick(R.id.iv_add_channel)
    public void onViewClicked() {
        BaseUtils.openActivityBottomAnim(getActivity(), AddHomeTagsActivity.class, null);
    }

}
