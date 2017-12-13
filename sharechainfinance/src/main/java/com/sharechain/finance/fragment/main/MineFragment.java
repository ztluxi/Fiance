package com.sharechain.finance.fragment.main;

import com.sharechain.finance.BaseFragment;
import com.sharechain.finance.R;

/**
 * Created by Administrator on 2017/12/13.
 */

public class MineFragment extends BaseFragment {
    @Override
    protected int getLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        initTitle(getString(R.string.main_tab_mine));
    }
}
