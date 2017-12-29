package com.sharechain.finance.adapter;

import android.annotation.SuppressLint;
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
import com.sharechain.finance.utils.TimeUtil;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private int tpye;
    private boolean isLike = true;
    public interface MyItemLongClickListener {
        void onTranslateClick(View view, int position, List<MogulData> list);
    }

    public interface MyItemClickListener {
        void onFabulous(View view, int position, List<MogulData> list,boolean isLike);
        void onShare(View view, int position, List<MogulData> list);
    }

    private MyItemLongClickListener mLongClickListener;
    private MyItemClickListener mClickListener;
    public void setOnItemLongClickListener(MyItemLongClickListener listener) {
        this.mLongClickListener = listener;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mClickListener = listener;
    }


    public MogulAdapter(Context context, List<MogulData> postList, List<Translate> trs,int type) {
        super();
        mPostList = postList;
        mInflater = LayoutInflater.from(context);
        mContext = context;
        this.trslist = trs;
        this.tpye = type;
    }

    @Override
    public void onBindViewHolder(final PostViewHolder holder, final int position) {

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
        RequestOptions headOptions = new RequestOptions().placeholder(R.drawable.logo).error(R.drawable.logo).circleCrop();
        GlideUtils.loadUserImage(mContext, mPostList.get(position).getHead(), holder.mogulHeadIvs, headOptions);
        holder.mogulName.setText(mPostList.get(position).getName());

        Date date = TimeUtil.StringToDate(mPostList.get(position).getTime());

        holder.mogulTime.setText(TimeUtil.RelativeDateFormat(date));


        holder.mogulWeibo.setText(mPostList.get(position).getWeibo());
        if (!BaseUtils.isEmpty(mPostList.get(position).getWeibo())){
            holder.mogulWeibo.setVisibility(View.VISIBLE);
        }else {
            holder.mogulWeibo.setVisibility(View.GONE);
        }
        if (!BaseUtils.isEmpty(mPostList.get(position).getPosition())){
            holder.mogulPositionTv.setText(mPostList.get(position).getPosition());
            holder.mogulPositionTv.setVisibility(View.VISIBLE);
        }
        final int like = mPostList.get(position).getFabulous();
        holder.mogulFabulousNumberTv.setText(like+"");

        RichText.from(mPostList.get(position).getContent()).into(holder.mogulContentTv);


        try {
            if (!TextUtils.isEmpty(mPostList.get(position).getTranslate().getTranslations().get(0))) {
                RichText.from(mPostList.get(position).getTranslate().getTranslations().get(0)).into(holder.mogulTranslateTv);
//                holder.mogulTranslateTv.setText(mPostList.get(position).getTranslate().getTranslations().get(0));
                holder.mogulCcontentTranslateLl.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {

        }

        if (tpye==0){
            holder.mogulHeadIvs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, MogulCircleActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", mPostList.get(position).getId());
                    bundle.putString("head",mPostList.get(position).getHead());
                    bundle.putString("name",mPostList.get(position).getName());
                    bundle.putString("position",mPostList.get(position).getPosition());
                    bundle.putInt("focus",mPostList.get(position).getFabulous());
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
        }

        //翻译
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
        //点赞
        if (mClickListener != null) {
            holder.mogulFabulousIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    if (isLike) {
                        GoodView goodView = new GoodView(mContext);
                        goodView.setImage(R.drawable.fabulous_sel);
                        goodView.show(v);
                        holder.mogulFabulousNumberTv.setText(like + 1 + "");
                        holder.mogulFabulousIv.setImageResource(R.drawable.fabulous_sel);
                        holder.mogulFabulousNumberTv.setTextColor(mContext.getResources().getColor(R.color.colorRed));
                        isLike = false;
                    } else {
                        holder.mogulFabulousIv.setImageResource(R.drawable.fabulous_nor);
                        holder.mogulFabulousNumberTv.setText(like+"");
                        holder.mogulFabulousNumberTv.setTextColor(mContext.getResources().getColor(R.color.about_font));
                        isLike = true;
                    }
                    mClickListener.onFabulous(v, pos, mPostList, isLike);
                }
            });
        }

        //分享
        if (mClickListener != null) {
            holder.shareImageIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = holder.getLayoutPosition();
                    mClickListener.onShare(view, pos, mPostList);
                }
            });
        }
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
                RequestOptions headOptions = new RequestOptions().placeholder(R.drawable.logo).error(R.drawable.logo).centerInside();
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
