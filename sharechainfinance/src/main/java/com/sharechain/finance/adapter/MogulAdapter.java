package com.sharechain.finance.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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
import com.sharechain.finance.SFApplication;
import com.sharechain.finance.bean.MogulData;
import com.sharechain.finance.bean.MogulHeadData;
import com.sharechain.finance.module.mogul.DragPhotoActivity;
import com.sharechain.finance.module.mogul.MogulCircleActivity;
import com.sharechain.finance.utils.BaseUtils;
import com.sharechain.finance.utils.GlideUtils;
import com.sharechain.finance.utils.MyImageGetter;
import com.sharechain.finance.utils.TimeUtil;
import com.sharechain.finance.view.fabulos.GoodView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static cn.bingoogolapple.baseadapter.BGABaseAdapterUtil.getColor;


/**
 * Created by ${zhoutao} on 2017/12/21 0013.
 */

public class MogulAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEAD = 0;
    private static final int TYPE_BODY = 1;


    private LayoutInflater mInflater;
    private List<MogulData> mPostList;
    private Context mContext;
    private int tpye;

    public interface MyItemClickListener {
        void onFabulous(View view, int position, List<MogulData> list, boolean isLike);

        void onShare(View view, int position, List<MogulData> list);

    }

    public List<MogulData> getmPostList() {
        return mPostList;
    }

    private MyItemClickListener mClickListener;

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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeadViewHolder) {
            ((HeadViewHolder) holder).mogulHeadTopNameTv.setText(mPostList.get(position).getName() + "");
            ((HeadViewHolder) holder).mogulHeadNameTv.setText(mPostList.get(position).getName() + "");
            ((HeadViewHolder) holder).mogulHeadPositionTv.setText(mPostList.get(position).getPosition() + "");
            ((HeadViewHolder) holder).mogulHeadWeiboTv.setText(mPostList.get(position).getPosition() + "");


//            if (mPostList.get(position).getFocus() == 1) {
//                ((HeadViewHolder) holder).mogulHeadFollowTv.setBackgroundResource(R.drawable.my_has_follow_bg);
//                ((HeadViewHolder) holder).mogulHeadFollowTv.setText(R.string.has_follow);
//            } else {
//                ((HeadViewHolder) holder).mogulHeadFollowTv.setBackgroundResource(R.drawable.my_no_follow_bg);
//                ((HeadViewHolder) holder).mogulHeadFollowTv.setText(R.string.follow);
//            }


            if (!BaseUtils.isEmpty(mPostList.get(position).getWeibo())) {
                ((HeadViewHolder) holder).mogulHeadWeiboTv.setVisibility(View.VISIBLE);
                ((HeadViewHolder) holder).mogulHeadWeiboTv.setText(mPostList.get(position).getWeibo() + "");
            } else {
                ((HeadViewHolder) holder).mogulHeadWeiboTv.setVisibility(View.GONE);
                ((HeadViewHolder) holder).mogulHeadWeiboTv.setText(mPostList.get(position).getWeibo() + "");
            }
            if (!BaseUtils.isEmpty(mPostList.get(position).getPosition())) {
                ((HeadViewHolder) holder).mogulHeadPositionTv.setVisibility(View.VISIBLE);
                ((HeadViewHolder) holder).mogulHeadPositionTv.setText(mPostList.get(position).getPosition() + "");
            } else {
                ((HeadViewHolder) holder).mogulHeadPositionTv.setVisibility(View.GONE);
                ((HeadViewHolder) holder).mogulHeadPositionTv.setText(mPostList.get(position).getPosition() + "");
            }


            //大佬图像
            RequestOptions headOptions = new RequestOptions().placeholder(R.drawable.mogul_default).error(R.drawable.mogul_default).circleCrop().diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            GlideUtils.getInstance().loadUserImage(mContext, mPostList.get(position).getHead(), ((HeadViewHolder) holder).mogulHeadImageTv, headOptions);

            //返回
            ((HeadViewHolder) holder).mogulHeadBackTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Activity activity = (Activity) mContext;
                    activity.finish();
                }
            });

        }

        if (holder instanceof PostViewHolder) {
            final MogulData mogulData = mPostList.get(position);
            holder.itemView.setTag(mogulData);

            //9宫格图片
            ((PostViewHolder) holder).mNglContent.setImagesData(mogulData.getUrlList());
            ((PostViewHolder) holder).mNglContent.setShowStyle(NineGridImageView.STYLE_FILL);
            //大佬图像
            RequestOptions headOptions = new RequestOptions().placeholder(R.drawable.mogul_default).error(R.drawable.mogul_default).circleCrop().diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            GlideUtils.getInstance().loadUserImage(mContext, mogulData.getHead(), ((PostViewHolder) holder).mogulHeadIvs, headOptions);
            //大佬名称
            ((PostViewHolder) holder).mogulName.setText(mogulData.getName());
            //发布时间
            Date date = TimeUtil.StringToDate(mogulData.getTime());
            ((PostViewHolder) holder).mogulTime.setText(TimeUtil.RelativeDateFormat(date));
            //大佬微博
            ((PostViewHolder) holder).mogulWeibo.setText(mogulData.getWeibo());
            if (!BaseUtils.isEmpty(mogulData.getWeibo())) {
                ((PostViewHolder) holder).mogulWeibo.setVisibility(View.VISIBLE);
            } else {
                ((PostViewHolder) holder).mogulWeibo.setVisibility(View.GONE);
            }
            if (!BaseUtils.isEmpty(mogulData.getPosition())) {
                ((PostViewHolder) holder).mogulPositionTv.setText(mogulData.getPosition());
                ((PostViewHolder) holder).mogulPositionTv.setVisibility(View.VISIBLE);
            }
            //评论数
            final int like = mogulData.getFabulous();
            ((PostViewHolder) holder).mogulFabulousNumberTv.setText(like + "");
            MyImageGetter glide = new MyImageGetter(((PostViewHolder) holder).mogulContentTv, mContext);
            ((PostViewHolder) holder).mogulContentTv.setText(Html.fromHtml(mogulData.getContent(), glide, null));
            //超链接跳转
            ((PostViewHolder) holder).mogulContentTv.setMovementMethod(LinkMovementMethod.getInstance());
            ((PostViewHolder) holder).mogulContentTv.setLinkTextColor(getColor(R.color.tint_home_color));

            if (!BaseUtils.isEmpty(mogulData.getTranslate())) {
                ((PostViewHolder) holder).mogulTranslateTv.setText(mogulData.getTranslate());
                ((PostViewHolder) holder).mogulContentTranslateLl.setVisibility(View.VISIBLE);
            } else {
                ((PostViewHolder) holder).mogulContentTranslateLl.setVisibility(View.GONE);
            }

            if (mPostList.get(position).isLike()) {
                if (mPostList.get(position).getFabulous() == 0) {
                    ((PostViewHolder) holder).mogulFabulousNumberTv.setText(like + 1+"");
                } else {
                    ((PostViewHolder) holder).mogulFabulousNumberTv.setText(like + "");
                }
                ((PostViewHolder) holder).mogulFabulousIv.setImageResource(R.drawable.fabulous_sel);
                ((PostViewHolder) holder).mogulFabulousNumberTv.setTextColor(mContext.getResources().getColor(R.color.colorRed));
            } else {
                ((PostViewHolder) holder).mogulFabulousIv.setImageResource(R.drawable.fabulous_nor);
                ((PostViewHolder) holder).mogulFabulousNumberTv.setText(like + "");
                ((PostViewHolder) holder).mogulFabulousNumberTv.setTextColor(mContext.getResources().getColor(R.color.about_font));
            }

            if (tpye == 0) {
                ((PostViewHolder) holder).mogulHeadIvs.setOnClickListener(new View.OnClickListener() {
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
            if (mClickListener != null) {
                ((PostViewHolder) holder).mogulFocusLl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = ((PostViewHolder) holder).getLayoutPosition();
                        if (!mPostList.get(position).isLike()) {
                            GoodView goodView = new GoodView(mContext);
                            goodView.setImage(R.drawable.fabulous_sel);
                            goodView.show(v);
                        }
                        mClickListener.onFabulous(v, pos, mPostList, mPostList.get(position).isLike());
                    }
                });
            }

            //分享
            if (mClickListener != null) {
                ((PostViewHolder) holder).mogulShareLl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = holder.getLayoutPosition();
                        mClickListener.onShare(view, pos, mPostList);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEAD) {
            return new HeadViewHolder(mInflater.inflate(R.layout.activity_mogul_cirle_head, parent, false));
        }
        if (viewType == TYPE_BODY) {
            return new PostViewHolder(mInflater.inflate(R.layout.adapter_my_mogul_item, parent, false));
        }
        return null;
    }


    public class PostViewHolder extends RecyclerView.ViewHolder {
        public NineGridImageView<String> mNglContent;
        public ImageView mogulHeadIvs;
        public ImageView shareImageIv;
        public TextView mogulName;
        public TextView mogulTime;
        public TextView mogulWeibo;
        public TextView mogulContentTv;
        public TextView mogulTranslateTv;
        public TextView mogulPositionTv;
        public ImageView mogulFabulousIv;
        public TextView mogulFabulousNumberTv;
        public LinearLayout mogulContentTranslateLl;
        public LinearLayout mogulFocusLl;
        public LinearLayout mogulShareLl;

        public NineGridImageViewAdapter<String> mAdapter = new NineGridImageViewAdapter<String>() {
            @Override
            protected void onDisplayImage(Context context, ImageView imageView, String s) {
                RequestOptions headOptions = new RequestOptions().placeholder(R.drawable.home_default).error(R.drawable.home_default).centerCrop().diskCacheStrategy(DiskCacheStrategy.RESOURCE);
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

    public class HeadViewHolder extends RecyclerView.ViewHolder {
        public TextView mogulHeadTopNameTv;
        public TextView mogulHeadNameTv;
        public TextView mogulHeadWeiboTv;
        public TextView mogulHeadPositionTv;
        public ImageView mogulHeadImageTv;
        public ImageView mogulHeadBackTv;


        public HeadViewHolder(View inflate) {
            super(inflate);
            mogulHeadTopNameTv = inflate.findViewById(R.id.mogul_top_name_tv);
            mogulHeadNameTv = inflate.findViewById(R.id.mogul_head_name_tv);
            mogulHeadWeiboTv = inflate.findViewById(R.id.mogul_head_weibo_tv);
            mogulHeadPositionTv = inflate.findViewById(R.id.mogul_head_position_tv);
            mogulHeadImageTv = inflate.findViewById(R.id.mogul_head_center_iv);
            mogulHeadBackTv = inflate.findViewById(R.id.back_iv);

        }
    }

    @Override
    public int getItemViewType(int position) {
        return mPostList.get(position).getType();
    }
}
