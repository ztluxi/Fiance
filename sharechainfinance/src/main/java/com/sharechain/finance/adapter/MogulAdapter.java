package com.sharechain.finance.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.request.RequestOptions;
import com.sharechain.finance.R;
import com.sharechain.finance.bean.MogulData;
import com.sharechain.finance.bean.NewsData;
import com.sharechain.finance.utils.GlideUtils;

import cn.bingoogolapple.baseadapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;

/**
 * Created by ${zhoutao} on 2017/12/22 0013.
 */

public class MogulAdapter extends BGAAdapterViewAdapter<MogulData>{

    public MogulAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }


    @Override
    protected void setItemChildListener(BGAViewHolderHelper helper) {
        helper.setItemChildClickListener(R.id.my_mogul_item_rl);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, MogulData model) {
        helper.setText(R.id.mogul_time, model.getTime());
        helper.setText(R.id.mogul_name, model.getName());
        helper.setText(R.id.mogul_position_tv, model.getPosition());
        helper.setText(R.id.mogul_fabulous_number_tv, model.getFabulous());
        helper.setText(R.id.mogul_content_tv, model.getContent());
        helper.setText(R.id.mogul_weibo, model.getWeibo());


        ImageView head = helper.getView(R.id.mogul_head_iv);
        ImageView contentImage = helper.getView(R.id.mogul_content_Image_tv);
        RequestOptions headOptions = new RequestOptions().circleCrop();
        RequestOptions contentOptions = new RequestOptions().centerInside();
        GlideUtils.loadUserImage(mContext,model.getHead(),head,headOptions);
        GlideUtils.loadUserImage(mContext,model.getImage(),contentImage,contentOptions);
    }


}
