package com.sharechain.finance.adapter;

import android.content.Context;

import com.sharechain.finance.R;
import com.sharechain.finance.bean.ArticleListsBean;
import com.sharechain.finance.utils.GlideUtils;
import com.sharechain.finance.utils.TimeUtil;

import java.util.Date;

import cn.bingoogolapple.baseadapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;

/**
 * Created by Chu on 2018/1/8.
 */

public class HomeOtherAdapter extends BGAAdapterViewAdapter<ArticleListsBean> {

    public HomeOtherAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, ArticleListsBean model) {
        helper.getTextView(R.id.text_type).setText(model.getName());
        Date date = TimeUtil.StringToDate(model.getPost_date_gmt());
        helper.getTextView(R.id.text_time).setText(TimeUtil.RelativeDateFormat(date));
        helper.getTextView(R.id.text_views).setText(String.format("%1$d阅读", model.getViews()));
        helper.getTextView(R.id.text_content).setText(model.getPost_title());
        GlideUtils.getInstance().loadRoundImage(mContext, model.getImage(), helper.getImageView(R.id.image_pic), 10, R.drawable.home_default);
    }

}
