/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com | 3772304@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.sharechain.finance.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sharechain.finance.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;

public class NewsListAdapter extends BaseRecyclerViewAdapter<String> {
    private HeaderViewHolder headerViewHolder;
    private List<String> imageList;
    private List<String> tipList;

    public NewsListAdapter(Context context, List<String> list, List<String> imageList, List<String> tipList) {
        super(context, list);
        this.imageList = imageList;
        this.tipList = tipList;
        mIsShowHeader = true;
    }

    public interface OnNewsListItemClickListener extends OnItemClickListener {
        void onItemClick(View view, int position, boolean isPhoto);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view;
        switch (viewType) {
            case TYPE_HEADER:
                view = getView(parent, R.layout.layout_header);
                headerViewHolder = new HeaderViewHolder(view);
                headerViewHolder.banner.setData(imageList, tipList);
                return headerViewHolder;
            case TYPE_FOOTER:
                view = getView(parent, R.layout.item_news_footer);
                return new FooterViewHolder(view);
            case TYPE_ITEM:
                view = getView(parent, R.layout.item_news);
                final ItemViewHolder itemViewHolder = new ItemViewHolder(view);
                setItemOnClickEvent(itemViewHolder, false);
                return itemViewHolder;
            default:
                throw new RuntimeException("there is no type that matches the type " +
                        viewType + " + make sure your using types correctly");
        }
    }

    private void setItemOnClickEvent(final RecyclerView.ViewHolder holder, final boolean isPhoto) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((OnNewsListItemClickListener) mOnItemClickListener).onItemClick(v, holder.getLayoutPosition(), isPhoto);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mIsShowHeader && isHeaderPosition(position)) {
            return TYPE_HEADER;
        } else if (mIsShowFooter && isFooterPosition(position)) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        setBanner();
        setValues(holder, position);
        setItemAppearAnimation(holder, position, R.anim.anim_bottom_in);
    }

    private void setValues(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            setItemValues((ItemViewHolder) holder, position);
        }
    }

    private void setItemValues(ItemViewHolder holder, int position) {

    }

    private void setBanner() {
        if (headerViewHolder != null) {
            headerViewHolder.banner.setDelegate(new BGABanner.Delegate<ImageView, String>() {
                @Override
                public void onBannerItemClick(BGABanner banner, ImageView itemView, String model, int position) {

                }
            });
            headerViewHolder.banner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
                @Override
                public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                    Glide.with(context)
                            .load(model)
                            .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).dontAnimate().centerCrop())
                            .into(itemView);
                }
            });
        }
    }

    public void setBannerData(List<String> imageList, List<String> tipList) {
        this.imageList = imageList;
        this.tipList = tipList;
        if (headerViewHolder != null) {
            headerViewHolder.banner.setData(imageList, tipList);
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (isShowingAnimation(holder)) {
            holder.itemView.clearAnimation();
        }
    }

    private boolean isShowingAnimation(RecyclerView.ViewHolder holder) {
        return holder.itemView.getAnimation() != null && holder.itemView
                .getAnimation().hasStarted();
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.banner)
        BGABanner banner;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.news_summary_photo_iv)
        ImageView mNewsSummaryPhotoIv;
        @BindView(R.id.news_summary_title_tv)
        TextView mNewsSummaryTitleTv;
        @BindView(R.id.news_summary_digest_tv)
        TextView mNewsSummaryDigestTv;
        @BindView(R.id.news_summary_ptime_tv)
        TextView mNewsSummaryPtimeTv;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
