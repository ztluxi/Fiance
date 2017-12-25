package com.sharechain.finance.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.jaeger.ninegridimageview.ItemImageClickListener;
import com.jaeger.ninegridimageview.ItemImageLongClickListener;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.sharechain.finance.R;
import com.sharechain.finance.SFApplication;
import com.sharechain.finance.bean.MogulData;
import com.sharechain.finance.module.mine.PersonalCenterActivity;
import com.sharechain.finance.module.mogul.MogulCircleActivity;
import com.sharechain.finance.utils.BaseUtils;
import com.sharechain.finance.utils.GlideUtils;
import com.sharechain.finance.utils.PopClickEvent;
import com.sharechain.finance.utils.PopOptionUtil;
import com.sharechain.finance.utils.TimeUtil;
import com.sharechain.finance.utils.ToastManager;
import com.sharechain.finance.view.dialog.MogulShareDialog;
import com.youdao.sdk.app.Language;
import com.youdao.sdk.app.LanguageUtils;
import com.youdao.sdk.ydonlinetranslate.Translator;
import com.youdao.sdk.ydtranslate.Translate;
import com.youdao.sdk.ydtranslate.TranslateErrorCode;
import com.youdao.sdk.ydtranslate.TranslateListener;
import com.youdao.sdk.ydtranslate.TranslateParameters;

import java.util.List;

/**
 * Created by Jaeger on 16/2/24.
 * <p>
 * Email: chjie.jaeger@gmail.com
 * GitHub: https://github.com/laobie
 */
public class MogulAdapter extends RecyclerView.Adapter<MogulAdapter.PostViewHolder> {


    private LayoutInflater mInflater;
    private List<MogulData> mPostList;
    private Context mContext;
    private List<Translate> trslist;
    public interface MyItemLongClickListener {
        void onTranslateClick(View view, int position,List<MogulData> list);
    }
    private MyItemLongClickListener mLongClickListener;
    public void setOnItemLongClickListener(MyItemLongClickListener listener){
        this.mLongClickListener = listener;
    }

    public MogulAdapter(Context context, List<MogulData> postList,List<Translate> trs) {
        super();
        mPostList = postList;
        mInflater = LayoutInflater.from(context);
        mContext = context;
        this.trslist = trs;
    }

    @Override
    public void onBindViewHolder(final PostViewHolder holder, int position) {
        if (mLongClickListener != null ){
            holder.mogulContentTv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mLongClickListener.onTranslateClick(holder.itemView, pos,mPostList);
                    return false;
                }
            });

        }

        holder.mNglContent.setImagesData(mPostList.get(position).getUrlList());
        RequestOptions headOptions = new RequestOptions().placeholder(R.drawable.history).circleCrop();
        GlideUtils.loadUserImage(mContext, mPostList.get(position).getHead(), holder.mogulHeadIvs, headOptions);
        holder.mogulContentTv.setText(mPostList.get(position).getContent());
        holder.mogulName.setText(mPostList.get(position).getName());
        holder.mogulTime.setText(mPostList.get(position).getTime());
        holder.mogulWeibo.setText(mPostList.get(position).getWeibo());
        holder.mogulPositionTv.setText(mPostList.get(position).getPosition());
        holder.mogulFabulousNumberTv.setText(mPostList.get(position).getFabulous());


        try {
            if (!TextUtils.isEmpty(mPostList.get(position).getTranslate().getTranslations().get(0))) {
                holder.mogulTranslateTv.setText(mPostList.get(position).getTranslate().getTranslations().get(0));
                holder.mogulTranslateTv.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {

        }



        holder.mNglContent.setItemImageClickListener(new ItemImageClickListener<String>() {
            @Override
            public void onItemImageClick(Context context, ImageView imageView, int index, List<String> list) {
                Log.d("onItemImageClick", list.get(index));
            }
        });
        holder.mNglContent.setItemImageLongClickListener(new ItemImageLongClickListener<String>() {
            @Override
            public boolean onItemImageLongClick(Context context, ImageView imageView, int index, List<String> list) {
                Log.d("onItemImageLongClick", list.get(index));
                return true;
            }
        });

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
//        holder.mogulContentTv.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                holder.mogulContentTv.setSelected(true);
//                holder.mogulContentTv.setBackgroundColor(mContext.getResources().getColor(R.color.about_font));
//                optionUtil.show(view);
//                return true;
//            }
//        });


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
        private Button mogulContentTv;
        private TextView mogulTranslateTv;
        private TextView mogulPositionTv;
        private TextView mogulFabulousNumberTv;

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
                Toast.makeText(context, "image position is " + index, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected boolean onItemImageLongClick(Context context, ImageView imageView, int index, List<String> list) {
                Toast.makeText(context, "image long click position is " + index, Toast.LENGTH_SHORT).show();
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
            mogulFabulousNumberTv = itemView.findViewById(R.id.mogul_fabulous_number_tv);
            mogulTranslateTv = itemView.findViewById(R.id.mogul_content_translate_tv);
            mNglContent.setAdapter(mAdapter);

        }
    }



}
