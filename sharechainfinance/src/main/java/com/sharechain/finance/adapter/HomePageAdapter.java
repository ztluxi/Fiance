package com.sharechain.finance.adapter;

import android.content.Context;

import com.sharechain.finance.R;

import cn.bingoogolapple.baseadapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;

/**
 * Created by Administrator on 2017/12/13.
 */

public class HomePageAdapter extends BGAAdapterViewAdapter<String> {

    public HomePageAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, String model) {
        helper.setText(R.id.text_item, model);
    }
}
