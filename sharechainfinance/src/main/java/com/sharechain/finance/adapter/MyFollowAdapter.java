package com.sharechain.finance.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sharechain.finance.R;
import com.sharechain.finance.bean.FollowData;
import com.sharechain.finance.bean.NewsData;
import com.sharechain.finance.utils.GlideUtils;

import cn.bingoogolapple.baseadapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;

/**
 * Created by ${zhoutao} on 2017/12/15 0013.
 */

public class MyFollowAdapter extends BGAAdapterViewAdapter<FollowData> {

    public MyFollowAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }


    @Override
    protected void setItemChildListener(BGAViewHolderHelper helper) {
        helper.setItemChildClickListener(R.id.has_follow_tv);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, FollowData model) {
        helper.setText(R.id.my_follow_name, model.getName());
        helper.setText(R.id.my_follow_weibo, model.getWeibo());
        helper.setText(R.id.my_follow_position, model.getPosition());
        ImageView view = helper.getView(R.id.my_follow_Image_iv);
        RequestOptions options = new RequestOptions().circleCrop();
        GlideUtils.loadUserImage(mContext,model.getImage(),view,options);

    }


}
