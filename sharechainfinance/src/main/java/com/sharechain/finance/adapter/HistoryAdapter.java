package com.sharechain.finance.adapter;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.sharechain.finance.R;
import com.sharechain.finance.bean.HistoryData;
import com.sharechain.finance.utils.GlideUtils;

import cn.bingoogolapple.baseadapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;

/**
 * Created by ${zhoutao} on 2017/12/13 0013.
 */

public class HistoryAdapter extends BGAAdapterViewAdapter<HistoryData> {

    public HistoryAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, HistoryData model) {
        helper.getTextView(R.id.text_content).setText(model.getPost_title());
        helper.getTextView(R.id.text_comment_num).setText(model.getViews() + mContext.getString(R.string.home_item_comment));
        helper.getTextView(R.id.text_praise_num).setText(model.getPost_view_rand() + mContext.getString(R.string.home_item_praise));
        RequestOptions headOptions = new RequestOptions().placeholder(R.drawable.home_default).error(R.drawable.home_default).centerInside().diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        GlideUtils.getInstance().loadUserImage(mContext, model.getUser_avatars(), helper.getImageView(R.id.image_pic), headOptions);
    }

}
