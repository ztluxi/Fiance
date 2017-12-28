package com.sharechain.finance.adapter;

import android.content.Context;

import com.sharechain.finance.R;
import com.sharechain.finance.bean.HistoryBean;

import cn.bingoogolapple.baseadapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;

/**
 * Created by ${zhoutao} on 2017/12/13 0013.
 */

public class HistoryAdapter extends BGAAdapterViewAdapter<HistoryBean> {

    public HistoryAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, HistoryBean model) {
        helper.getTextView(R.id.text_content).setText(model.getPost_title());
        helper.getTextView(R.id.text_comment_num).setText(model.getViews() + mContext.getString(R.string.home_item_comment));
        helper.getTextView(R.id.text_comment_num).setText(model.getPost_view_rand() + mContext.getString(R.string.home_item_praise));
    }

}
