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
import com.sharechain.finance.utils.BaseUtils;
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
        ImageView mogulHeadIv = helper.getImageView(R.id.my_follow_Image_iv);
        TextView mogulNameTV = helper.getTextView(R.id.my_follow_name);
        TextView mogulWeiboTV = helper.getTextView(R.id.my_follow_weibo);
        TextView positionTv =  helper.getView(R.id.my_follow_position);
        TextView followTv =  helper.getView(R.id.has_follow_tv);

        TextView follow =  helper.getView(R.id.has_follow_tv);
        mogulHeadIv.setImageResource(R.drawable.logo);
        mogulNameTV.setText("");
        mogulWeiboTV.setText("");
        positionTv.setText("");

        mogulNameTV.setText(model.getName());
        mogulWeiboTV.setText(model.getWeibo());
        if (!BaseUtils.isEmpty(model.getWeibo())){
            mogulWeiboTV.setVisibility(View.VISIBLE);
        }else {
            mogulWeiboTV.setVisibility(View.GONE);
        }
        mogulNameTV.setText(model.getName());
        String professional = model.getPosition();
        if (professional.equals("")) {
            positionTv.setVisibility(View.GONE);
        }else {
            positionTv.setVisibility(View.VISIBLE);
        }
        positionTv.setText(professional+"");
        //如果已关注了显示已关注背景，反正则显示蓝色未关注
        if (model.getFacous()==1){
            follow.setText(R.string.has_follow);
            follow.setBackgroundResource(R.drawable.my_has_follow_bg);
        }else {
            follow.setText(R.string.follow);
            follow.setBackgroundResource(R.drawable.my_no_follow_bg);
        }
        RequestOptions options = new RequestOptions().circleCrop().placeholder(R.drawable.mogul_default).error(R.drawable.mogul_default);
        GlideUtils.getInstance().loadUserImage(mContext,model.getImage(),mogulHeadIv,options);

        followTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.cancelFollow(view,position,model);
            }
        });
    }


}
