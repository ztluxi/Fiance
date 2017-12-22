package com.sharechain.finance.adapter;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sharechain.finance.R;
import com.sharechain.finance.bean.HomeData;
import com.sharechain.finance.bean.NewsData;

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
        helper.setText(R.id.my_news_time, model.getTime());
        helper.setText(R.id.my_news_title, model.getTitle());
//        Glide.with(mContext).load(model.getImage()).apply(new RequestOptions().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)).into(helper.getImageView(R.id.my_news_Image_iv));

    }


}
