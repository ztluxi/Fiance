package com.sharechain.finance.fragment.main;

import android.support.v4.content.ContextCompat;

import com.sharechain.finance.BaseFragment;
import com.sharechain.finance.R;

/**
 * Created by Administrator on 2017/12/13.
 */

public class HomeFragment extends BaseFragment {

    @Override
    protected int getLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        initTitle(getString(R.string.main_tab_home));
    }

}
