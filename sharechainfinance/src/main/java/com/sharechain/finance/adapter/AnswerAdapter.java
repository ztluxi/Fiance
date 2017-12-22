package com.sharechain.finance.adapter;

import android.content.Context;

import cn.bingoogolapple.baseadapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;

/**
 * Created by Chu on 2017/12/22.
 */

public class AnswerAdapter extends BGAAdapterViewAdapter<String> {

    public AnswerAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, String model) {

    }
}
