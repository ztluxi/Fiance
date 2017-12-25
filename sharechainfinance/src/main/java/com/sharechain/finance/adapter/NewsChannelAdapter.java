/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com>
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
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharechain.finance.R;
import com.sharechain.finance.bean.NewsChannelTable;
import com.sharechain.finance.utils.ClickUtil;
import com.sharechain.finance.view.ItemDragHelperCallback;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author ckm
 */
public class NewsChannelAdapter extends BaseRecyclerViewAdapter<NewsChannelTable> implements
        ItemDragHelperCallback.OnItemMoveListener {
    private static final int TYPE_CHANNEL_FIXED = 0;
    private static final int TYPE_CHANNEL_NO_FIXED = 1;

    private ItemDragHelperCallback mItemDragHelperCallback;
    private OnItemClickListener mOnItemClickListener;
    private IOnItemMoveListener iOnItemMoveListener;

    public NewsChannelAdapter(Context context, List<NewsChannelTable> list) {
        super(context, list);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setItemDragHelperCallback(ItemDragHelperCallback itemDragHelperCallback) {
        mItemDragHelperCallback = itemDragHelperCallback;
    }

    public void setiOnItemMoveListener(IOnItemMoveListener iOnItemMoveListener) {
        this.iOnItemMoveListener = iOnItemMoveListener;
    }

    public List<NewsChannelTable> getData() {
        return mList;
    }

    @Override
    public NewsChannelViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_channel, parent, false);
        final NewsChannelViewHolder newsChannelViewHolder = new NewsChannelViewHolder(view);
        handleLongPress(newsChannelViewHolder);
        handleOnClick(newsChannelViewHolder);
        return newsChannelViewHolder;
    }

    private void handleLongPress(final NewsChannelViewHolder newsChannelViewHolder) {
        if (mItemDragHelperCallback != null) {
            newsChannelViewHolder.itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    NewsChannelTable newsChannel = mList.get(newsChannelViewHolder.getLayoutPosition());
                    boolean isChannelFixed = newsChannel.getNewsChannelFixed();
                    if (isChannelFixed) {
                        mItemDragHelperCallback.setLongPressEnabled(false);
                    } else {
                        mItemDragHelperCallback.setLongPressEnabled(true);
                    }
                    return false;
                }
            });
        }
    }

    private void handleOnClick(final NewsChannelViewHolder newsChannelViewHolder) {
        if (mOnItemClickListener != null) {
            newsChannelViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //item click
                    if (!ClickUtil.isFastDoubleClick()) {
                        mOnItemClickListener.onItemClick(v, newsChannelViewHolder.getLayoutPosition(), true);
                    }
                }
            });
            newsChannelViewHolder.image_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //image click
                    if (!ClickUtil.isFastDoubleClick()) {
                        mOnItemClickListener.onItemClick(view, newsChannelViewHolder.getLayoutPosition(), false);
                    }
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final NewsChannelTable newsChannel = mList.get(position);
        String newsChannelName = newsChannel.getNewsChannelName();
        NewsChannelViewHolder viewHolder = (NewsChannelViewHolder) holder;
        viewHolder.text_item.setText(newsChannelName);
        if (newsChannel.getNewsChannelFixed()) {
            viewHolder.text_item.setBackgroundResource(R.drawable.common_transparent_bg);
            viewHolder.text_item.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            viewHolder.image_delete.setVisibility(View.GONE);
        } else {
            viewHolder.text_item.setBackgroundResource(R.drawable.search_bg);
            viewHolder.text_item.setTextColor(ContextCompat.getColor(context, R.color.about_font));
            if (newsChannel.getNewsChannelSelect()) {
                viewHolder.image_delete.setVisibility(View.VISIBLE);
            } else {
                viewHolder.image_delete.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        Boolean isFixed = mList.get(position).getNewsChannelFixed();
        if (isFixed) {
            return TYPE_CHANNEL_FIXED;
        } else {
            return TYPE_CHANNEL_NO_FIXED;
        }
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (isChannelFixed(fromPosition, toPosition)) {
            return false;
        }
        Collections.swap(mList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        if (iOnItemMoveListener != null) {
            iOnItemMoveListener.onItemMove();
        }
        return true;
    }

    private boolean isChannelFixed(int fromPosition, int toPosition) {
        return mList.get(fromPosition).getNewsChannelFixed() ||
                mList.get(toPosition).getNewsChannelFixed();
    }

    class NewsChannelViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_item)
        TextView text_item;
        @BindView(R.id.image_delete)
        ImageView image_delete;

        public NewsChannelViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface IOnItemMoveListener {
        void onItemMove();
    }

}
