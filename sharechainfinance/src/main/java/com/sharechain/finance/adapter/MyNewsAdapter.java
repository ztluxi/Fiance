package com.sharechain.finance.adapter;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sharechain.finance.R;
import com.sharechain.finance.bean.HomeData;
import com.sharechain.finance.bean.NewsData;
import com.sharechain.finance.utils.TimeUtil;

import java.util.Date;

import cn.bingoogolapple.baseadapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;

/**
 * Created by ${zhoutao} on 2017/12/15 0013.
 */

public class MyNewsAdapter extends BGAAdapterViewAdapter<NewsData>{

    public MyNewsAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }


    @Override
    protected void setItemChildListener(BGAViewHolderHelper helper) {
        helper.setItemChildClickListener(R.id.my_news_item_rl);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, NewsData model) {
        Date date = TimeUtil.StringToDate(model.getTime());
        helper.setText(R.id.my_news_time,TimeUtil.RelativeDateFormat(date));
        helper.setText(R.id.my_news_title, model.getContent());
    }

}
