package com.sharechain.finance.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sharechain.finance.R;
import com.sharechain.finance.bean.FollowData;
import com.sharechain.finance.bean.MogulData;
import com.sharechain.finance.bean.NewsData;
import com.sharechain.finance.utils.GlideUtils;

import java.util.List;

import cn.bingoogolapple.baseadapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;

/**
 * Created by ${zhoutao} on 2017/12/15 0013.
 */

public class MyFollowAdapter extends BGAAdapterViewAdapter<FollowData> {

    public MyFollowAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    public MyItemClickListener mClickListener;
    public interface MyItemClickListener{
        void cancelFollow(View view, int position,FollowData data);
    }

    public void setMyItemClickLister(MyItemClickListener mClickListener ){
        this.mClickListener = mClickListener;
    }

    @Override
    protected void setItemChildListener(BGAViewHolderHelper helper) {
        helper.setItemChildClickListener(R.id.has_follow_tv);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, final int position, final FollowData model) {
        helper.setText(R.id.my_follow_name, model.getName());
        helper.setText(R.id.my_follow_weibo, model.getWeibo());
        TextView positionTv =  helper.getView(R.id.my_follow_position);
        String professional = model.getPosition();
        if (professional.equals("")) {
            positionTv.setVisibility(View.GONE);
        }else {
            positionTv.setVisibility(View.VISIBLE);
        }
        positionTv.setText(professional+"");

        TextView follow =  helper.getView(R.id.has_follow_tv);
        //如果已关注了显示已关注背景，反正则显示蓝色未关注
        if (model.getFacous()==1){
            follow.setText(R.string.has_follow);
            follow.setBackgroundResource(R.drawable.my_has_follow_bg);
        }else {
            follow.setText(R.string.follow);
            follow.setBackgroundResource(R.drawable.my_no_follow_bg);
        }

        ImageView userImage = helper.getView(R.id.my_follow_Image_iv);
        RequestOptions options = new RequestOptions().circleCrop().placeholder(R.drawable.logo).error(R.drawable.logo);
        GlideUtils.loadUserImage(mContext,model.getImage(),userImage,options);

        helper.getView(R.id.has_follow_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.cancelFollow(view,position,model);
            }
        });
    }


}
