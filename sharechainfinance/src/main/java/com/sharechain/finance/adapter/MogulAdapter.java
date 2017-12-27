package com.sharechain.finance.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.sharechain.finance.R;
import com.sharechain.finance.bean.MogulData;
import com.sharechain.finance.module.mogul.MogulCircleActivity;
import com.sharechain.finance.module.mogul.PhotoGalleryActivity;
import com.sharechain.finance.utils.BaseUtils;
import com.sharechain.finance.utils.GlideUtils;
import com.sharechain.finance.utils.OkHttpImageDownloader;
import com.sharechain.finance.view.dialog.MogulShareDialog;
import com.sharechain.finance.view.fabulos.GoodView;
import com.youdao.sdk.ydtranslate.Translate;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.RichTextConfig;
import com.zzhoujay.richtext.callback.BitmapStream;
import com.zzhoujay.richtext.callback.Callback;
import com.zzhoujay.richtext.callback.ImageGetter;
import com.zzhoujay.richtext.callback.ImageLoadNotify;
import com.zzhoujay.richtext.callback.OnUrlClickListener;
import com.zzhoujay.richtext.ig.DefaultImageGetter;
import com.zzhoujay.richtext.ig.ImageDownloader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jaeger on 16/2/24.
 * <p>
 * Email: chjie.jaeger@gmail.com
 * GitHub: https://github.com/laobie
 */
public class MogulAdapter extends RecyclerView.Adapter<MogulAdapter.PostViewHolder>{


    private LayoutInflater mInflater;
    private List<MogulData> mPostList;
    private Context mContext;
    private List<Translate> trslist;


    public interface MyItemLongClickListener {
        void onTranslateClick(View view, int position, List<MogulData> list);
    }

    private MyItemLongClickListener mLongClickListener;

    public void setOnItemLongClickListener(MyItemLongClickListener listener) {
        this.mLongClickListener = listener;
    }

    public MogulAdapter(Context context, List<MogulData> postList, List<Translate> trs) {
        super();
        mPostList = postList;
        mInflater = LayoutInflater.from(context);
        mContext = context;
        this.trslist = trs;
    }

    @Override
    public void onBindViewHolder(final PostViewHolder holder, final int position) {
        if (mLongClickListener != null) {
            holder.mogulContentTv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mLongClickListener.onTranslateClick(v, pos, mPostList);
                    return false;
                }
            });
        }
        holder.mogulContentTv.setText("");
        holder.mogulName.setText("");
        holder.mogulTime.setText("");
        holder.mogulWeibo.setText("");
        holder.mogulPositionTv.setText("");
        holder.mogulPositionTv.setVisibility(View.GONE);
        holder.mogulFabulousNumberTv.setText("");
        holder.mogulTranslateTv.setText("");
        holder.mogulHeadIvs.setImageResource(R.drawable.logo);
        holder.mogulCcontentTranslateLl.setVisibility(View.GONE);

        holder.mNglContent.setImagesData(mPostList.get(position).getUrlList());
        RequestOptions headOptions = new RequestOptions().placeholder(R.drawable.history).circleCrop();
        GlideUtils.loadUserImage(mContext, mPostList.get(position).getHead(), holder.mogulHeadIvs, headOptions);
        holder.mogulName.setText(mPostList.get(position).getName());
        holder.mogulTime.setText(mPostList.get(position).getTime());
        holder.mogulWeibo.setText(mPostList.get(position).getWeibo());
        if (!BaseUtils.isEmpty(mPostList.get(position).getPosition())){
            holder.mogulPositionTv.setText(mPostList.get(position).getPosition());
            holder.mogulPositionTv.setVisibility(View.VISIBLE);
        }
        holder.mogulFabulousNumberTv.setText(mPostList.get(position).getFabulous());

        RichText.from(mPostList.get(position).getContent()).urlClick(new OnUrlClickListener() {
            @Override
            public boolean urlClicked(String url) {
                return false;
            }
        }).into(holder.mogulContentTv);

        try {
            if (!TextUtils.isEmpty(mPostList.get(position).getTranslate().getTranslations().get(0))) {
                holder.mogulTranslateTv.setText(mPostList.get(position).getTranslate().getTranslations().get(0));
                holder.mogulCcontentTranslateLl.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {

        }

        holder.shareImageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MogulShareDialog(mContext).show();
            }
        });

        holder.mogulHeadIvs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, MogulCircleActivity.class));
            }
        });
        holder.mogulFabulousIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoodView goodView = new GoodView(mContext);
                goodView.setImage(R.drawable.fabulous_sel);
                goodView.show(view);
                holder.mogulFabulousNumberTv.setText(mPostList.get(position).getFabulous()+1);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }


    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PostViewHolder(mInflater.inflate(R.layout.adapter_my_mogul_item, parent, false));
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        private NineGridImageView<String> mNglContent;
        private ImageView mogulHeadIvs;
        private ImageView shareImageIv;
        private TextView mogulName;
        private TextView mogulTime;
        private TextView mogulWeibo;
        private TextView mogulContentTv;
        private TextView mogulTranslateTv;
        private TextView mogulPositionTv;
        private ImageView mogulFabulousIv;
        private TextView mogulFabulousNumberTv;
        private LinearLayout mogulCcontentTranslateLl;

        private NineGridImageViewAdapter<String> mAdapter = new NineGridImageViewAdapter<String>() {
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
                Intent intent = new Intent(context, PhotoGalleryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("datas", (ArrayList<String>) list);
                bundle.putInt("index", index);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }

            @Override
            protected boolean onItemImageLongClick(Context context, ImageView imageView, int index, List<String> list) {

                return true;
            }
        };

        public PostViewHolder(View itemView) {
            super(itemView);
            mNglContent = (NineGridImageView<String>) itemView.findViewById(R.id.mogul_content_Image_tv);
            mogulHeadIvs = itemView.findViewById(R.id.mogul_head_iv);
            shareImageIv = itemView.findViewById(R.id.share_image_iv);
            mogulName = itemView.findViewById(R.id.mogul_name);
            mogulTime = itemView.findViewById(R.id.mogul_time);
            mogulWeibo = itemView.findViewById(R.id.mogul_weibo);
            mogulContentTv = itemView.findViewById(R.id.mogul_content_tv);
            mogulPositionTv = itemView.findViewById(R.id.mogul_position_tv);
            mogulFabulousIv = itemView.findViewById(R.id.mogul_fabulous_iv);
            mogulFabulousNumberTv = itemView.findViewById(R.id.mogul_fabulous_number_tv);
            mogulTranslateTv = itemView.findViewById(R.id.mogul_content_translate_tv);
            mogulCcontentTranslateLl = itemView.findViewById(R.id.mogul_content_translate_ll);
            mNglContent.setAdapter(mAdapter);

        }
    }


}
