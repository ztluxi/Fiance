package com.sharechain.finance.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.sharechain.finance.R;
import com.sharechain.finance.bean.MogulData;
import com.sharechain.finance.module.mogul.DragPhotoActivity;
import com.sharechain.finance.module.mogul.MogulCircleActivity;
import com.sharechain.finance.utils.BaseUtils;
import com.sharechain.finance.utils.GlideUtils;
import com.sharechain.finance.utils.TimeUtil;
import com.sharechain.finance.view.fabulos.GoodView;
import com.youdao.sdk.ydtranslate.Translate;
import com.zzhoujay.richtext.RichText;

import java.util.ArrayList;
import java.util.Date;
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
    private int tpye;
    private boolean isLike = true;

//    public interface MyItemLongClickListener {
//        void onTranslateClick(View view, int position, List<MogulData> list);
//    }

    public interface MyItemClickListener {
        void onFabulous(View view, int position, List<MogulData> list, boolean isLike);

        void onShare(View view, int position, List<MogulData> list);
    }

//    private MyItemLongClickListener mLongClickListener;
    private MyItemClickListener mClickListener;

//    public void setOnItemLongClickListener(MyItemLongClickListener listener) {
//        this.mLongClickListener = listener;
//    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mClickListener = listener;
    }


    public MogulAdapter(Context context, List<MogulData> postList, int type) {
        super();
        mPostList = postList;
        mInflater = LayoutInflater.from(context);
        mContext = context;
        this.tpye = type;
    }

    @Override
    public void onBindViewHolder(final PostViewHolder holder, final int position) {
        final MogulData mogulData = mPostList.get(position);
        holder.itemView.setTag(mogulData);

        holder.mogulContentTv.setText("");
        holder.mogulName.setText("");
        holder.mogulTime.setText("");
        holder.mogulWeibo.setText("");
        holder.mogulPositionTv.setText("");
        holder.mogulPositionTv.setVisibility(View.GONE);
        holder.mogulTranslateTv.setText("");
        holder.mogulHeadIvs.setImageResource(R.drawable.logo);
        holder.mogulContentTranslateLl.setVisibility(View.GONE);
        holder.mogulFabulousNumberTv.setText("");
        holder.mogulFabulousIv.setImageResource(R.drawable.fabulous_nor);
        holder.mogulFabulousNumberTv.setTextColor(mContext.getResources().getColor(R.color.about_font));


        //9宫格图片
        holder.mNglContent.setImagesData(mogulData.getUrlList());
        holder.mNglContent.setShowStyle(NineGridImageView.STYLE_FILL);
        //大佬图像
        RequestOptions headOptions = new RequestOptions().placeholder(R.drawable.logo).error(R.drawable.logo).circleCrop().diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        GlideUtils.getInstance().loadUserImage(mContext, mogulData.getHead(), holder.mogulHeadIvs, headOptions);
        //大佬名称
        holder.mogulName.setText(mogulData.getName());
        //发布时间
        Date date = TimeUtil.StringToDate(mogulData.getTime());
        holder.mogulTime.setText(TimeUtil.RelativeDateFormat(date));
        //大佬微博
        holder.mogulWeibo.setText(mogulData.getWeibo());
        if (!BaseUtils.isEmpty(mogulData.getWeibo())) {
            holder.mogulWeibo.setVisibility(View.VISIBLE);
        } else {
            holder.mogulWeibo.setVisibility(View.GONE);
        }
        if (!BaseUtils.isEmpty(mogulData.getPosition())) {
            holder.mogulPositionTv.setText(mogulData.getPosition());
            holder.mogulPositionTv.setVisibility(View.VISIBLE);
        }
        //评论数
        final int like = mogulData.getFabulous();
        holder.mogulFabulousNumberTv.setText(like + "");
        //发布内容
        RichText.fromHtml(mogulData.getContent()).noImage(true).into(holder.mogulContentTv);

        if (!BaseUtils.isEmpty(mogulData.getTranslate())){
            holder.mogulTranslateTv.setText(mogulData.getTranslate());
            holder.mogulContentTranslateLl.setVisibility(View.VISIBLE);
        }else{
            holder.mogulContentTranslateLl.setVisibility(View.GONE);
        }
//        try {
//            if (!TextUtils.isEmpty(mogulData.getTranslate().getTranslations().get(0))) {
//                //翻译内容
//                RichText.from(mogulData.getTranslate().getTranslations().get(0)).into(holder.mogulTranslateTv);
////                holder.mogulTranslateTv.setText(mogulData.getTranslate().getTranslations().get(0));
//                holder.mogulContentTranslateLl.setVisibility(View.VISIBLE);
//            }
//        } catch (Exception e) {
//
//        }

        if (tpye == 0) {
            holder.mogulHeadIvs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, MogulCircleActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", mPostList.get(position).getId());
                    bundle.putString("head", mPostList.get(position).getHead());
                    bundle.putString("name", mPostList.get(position).getName());
                    bundle.putString("position", mPostList.get(position).getPosition());
                    bundle.putInt("focus", mPostList.get(position).getFocus());
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
        }

//        //翻译
//        if (mLongClickListener != null) {
//            holder.mogulContentTv.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    int pos = holder.getLayoutPosition();
//                    mLongClickListener.onTranslateClick(v, pos, mPostList);
//                    return false;
//                }
//            });
//        }
        //点赞
        if (mClickListener != null) {
            holder.mogulFocusLl.setOnClickListener(new View.OnClickListener() {
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
                        holder.mogulFabulousNumberTv.setText(like + "");
                        holder.mogulFabulousNumberTv.setTextColor(mContext.getResources().getColor(R.color.about_font));
                        isLike = true;
                    }

                    mClickListener.onFabulous(v, pos, mPostList, isLike);
                }
            });
        }

        //分享
        if (mClickListener != null) {
            holder.mogulShareLl.setOnClickListener(new View.OnClickListener() {
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
        private LinearLayout mogulContentTranslateLl;
        private LinearLayout mogulFocusLl;
        private LinearLayout mogulShareLl;

        private NineGridImageViewAdapter<String> mAdapter = new NineGridImageViewAdapter<String>() {
            @Override
            protected void onDisplayImage(Context context, ImageView imageView, String s) {
                RequestOptions headOptions = new RequestOptions().placeholder(R.drawable.logo).error(R.drawable.logo).centerInside().diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                GlideUtils.getInstance().loadUserImage(mContext, s, imageView, headOptions);
            }

            @Override
            protected ImageView generateImageView(Context context) {
                return super.generateImageView(context);
            }

            @Override
            protected void onItemImageClick(Context context, ImageView imageView, int index, List<String> list) {
                Intent intent = new Intent(context, DragPhotoActivity.class);
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
            mogulContentTranslateLl = itemView.findViewById(R.id.mogul_content_translate_ll);
            mogulFocusLl = itemView.findViewById(R.id.focus_ll);
            mogulShareLl = itemView.findViewById(R.id.share_ll);

            mNglContent.setAdapter(mAdapter);

        }
    }

}
