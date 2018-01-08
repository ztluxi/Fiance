package com.sharechain.finance.adapter;

import android.content.Context;

import com.sharechain.finance.R;
import com.sharechain.finance.bean.ArticleListsBean;
import com.sharechain.finance.utils.GlideUtils;

import cn.bingoogolapple.baseadapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;

/**
 * Created by Chu on 2017/12/22.
 */

public class AnswerAdapter extends BGAAdapterViewAdapter<ArticleListsBean> {

    public AnswerAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, ArticleListsBean model) {
        helper.getTextView(R.id.text_content).setText(model.getPost_title());
        helper.getTextView(R.id.text_comment_num).setText(model.getViews() + mContext.getString(R.string.home_item_comment));
        helper.getTextView(R.id.text_comment_num).setText(model.getPost_view_rand() + mContext.getString(R.string.home_item_praise));
        GlideUtils.getInstance().loadRoundImage(mContext, model.getImage(), helper.getImageView(R.id.image_pic), 5,
                R.drawable.home_default);
    }

}
