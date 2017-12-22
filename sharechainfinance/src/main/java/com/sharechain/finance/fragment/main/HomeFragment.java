package com.sharechain.finance.fragment.main;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sharechain.finance.BaseFragment;
import com.sharechain.finance.R;
import com.sharechain.finance.adapter.ViewPagerAdapter;
import com.sharechain.finance.module.mine.MineActivity;
import com.sharechain.finance.utils.BaseUtils;
import com.sharechain.finance.view.ScaleTransitionPagerTitleView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/13.
 */

public class HomeFragment extends BaseFragment {
    private List<String> typelist = new ArrayList<>();
    @BindView(R.id.magic_indicator)
    MagicIndicator magic_indicator;

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.iv_add_channel)
    ImageView add_channel;
    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    private ViewPagerAdapter mPagerAdapter;
    private int needShowPosition = -1;

    @Override
    protected int getLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        immersionBar.statusBarColor(R.color.tint_home_color)
                .init();
        setTitlePadding(rl_top, BaseUtils.dip2px(getActivity(), 10));
    }

    @Override
    public void initData() {
        typelist.add("首页");
        typelist.add("内讯");
        typelist.add("外讯");
        typelist.add("解析");
        typelist.add("区块链");
        typelist.add("政策");
        typelist.add("内讯");
        typelist.add("外讯");
        typelist.add("解析");
        typelist.add("区块链");
        typelist.add("政策");
        loadData();
    }

    private void initMagicIndicator() {
        magic_indicator.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.tint_home_color));
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        commonNavigator.setScrollPivotX(0.8f);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return typelist == null ? 0 : typelist.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                simplePagerTitleView.setText(typelist.get(index));
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
        for (int i = 0; i < typelist.size(); i++) {
            fragments.add(NewsFragment.newInstance(typelist.get(i)));
        }
        mPagerAdapter.setItems(fragments, typelist);
        viewpager.setAdapter(mPagerAdapter);
        viewpager.setOffscreenPageLimit(typelist.size());

        initMagicIndicator();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && immersionBar != null) {
            immersionBar.statusBarColor(R.color.tint_home_color)
                    .init();
        }

    }

    @OnClick(R.id.iv_add_channel)
    public void onViewClicked() {
//        getActivity().startActivity(new Intent());
        BaseUtils.openActivityBottomAnim(getActivity(), MineActivity.class, null);
    }

}
