package com.sharechain.finance.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sharechain.finance.R;
import com.sharechain.finance.bean.HomeData;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.baseadapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;

/**
 * Created by ${zhoutao} on 2017/12/13 0013.
 */

public class HistoryAdapter extends BGAAdapterViewAdapter<HomeData>{

    public HistoryAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }


    @Override
    protected void setItemChildListener(BGAViewHolderHelper helper) {
        helper.setItemChildClickListener(R.id.author_tv);

    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, HomeData model) {
        helper.setText(R.id.author_tv, model.getAuthor());
        helper.setText(R.id.title_tv, model.getTitle());
//        Glide.with(mContext).load(model.getImage()).apply(new RequestOptions().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)).into(helper.getImageView(R.id.image_iv));

    }


}
