package com.sharechain.finance.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.sharechain.finance.R;
import com.sharechain.finance.bean.MogulData;
import com.sharechain.finance.utils.GlideUtils;
import com.sharechain.finance.view.dialog.MogulShareDialog;

import java.util.List;
import java.util.logging.Handler;

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
        helper.setItemChildClickListener(R.id.share_image_iv);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, MogulData model) {
        helper.setText(R.id.mogul_time, model.getTime());
        helper.setText(R.id.mogul_name, model.getName());
        helper.setText(R.id.mogul_position_tv, model.getPosition());
        helper.setText(R.id.mogul_fabulous_number_tv, model.getFabulous());
        helper.setText(R.id.mogul_content_tv, model.getContent());
        helper.setText(R.id.mogul_weibo, model.getWeibo());
        NineGridImageView contentImage = helper.getView(R.id.mogul_content_Image_tv);
        contentImage.setImagesData(model.getUrlList());
        contentImage.setAdapter(mAdapter);

        ImageView head = helper.getView(R.id.mogul_head_iv);
        ImageView share = helper.getView(R.id.share_image_iv);

        RequestOptions headOptions = new RequestOptions().placeholder(R.drawable.user).circleCrop();
        GlideUtils.loadUserImage(mContext, model.getHead(), head, headOptions);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MogulShareDialog(mContext).show();
            }
        });
    }
         NineGridImageViewAdapter<String> mAdapter = new NineGridImageViewAdapter<String>() {
            @Override
            protected void onDisplayImage(Context context, ImageView imageView, String s) {
                RequestOptions headOptions = new RequestOptions().placeholder(R.drawable.history).centerInside();
                GlideUtils.loadUserImage(mContext, s, imageView, headOptions);
            }

            @Override
            protected ImageView generateImageView(Context context) {
                return super.generateImageView(context);
            }

            @Override
            protected void onItemImageClick(Context context, ImageView imageView, int index, List<String> list) {
                Toast.makeText(context, "image position is " + index, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected boolean onItemImageLongClick(Context context, ImageView imageView, int index, List<String> list) {
                Toast.makeText(context, "image long click position is " + index, Toast.LENGTH_SHORT).show();
                return true;
            }
        };


}
