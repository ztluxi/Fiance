package com.sharechain.finance.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sharechain.finance.R;
import com.sharechain.finance.bean.FastMsgData;
import com.sharechain.finance.module.home.BaseWebViewActivity;
import com.sharechain.finance.utils.BaseUtils;
import com.sharechain.finance.view.FastMsgDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.halfbit.pinnedsection.PinnedSectionListView;

/**
 * Created by Chu on 2017/12/18.
 */

public class FastMsgAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {
    private List<FastMsgData> listData;
    private Context context;

    public FastMsgAdapter(Context context, List<FastMsgData> listData) {
        this.context = context;
        this.listData = listData;
    }

    public void setListData(List<FastMsgData> listData) {
        this.listData = listData;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int i) {
        return listData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        int itemType = getItemViewType(i);
        if (itemType == FastMsgData.PARENT_TYPE) {
            ParentViewHolder parentViewHolder;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item_fast_msg_parent, viewGroup, false);
            }
            parentViewHolder = new ParentViewHolder(view);
            parentViewHolder.text_item.setText(listData.get(i).getSectionText());
        } else if (itemType == FastMsgData.CHILD_TYPE) {
            ChildViewHolder childViewHolder;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item_fastmsg_child, viewGroup, false);
            }
            childViewHolder = new ChildViewHolder(view);
            childViewHolder.text_time.setText(listData.get(i).getHour());
            String content = "";
            if (BaseUtils.isEmpty(listData.get(i).getSource())) {
                content = listData.get(i).getDataText();
            } else {
                content = context.getString(R.string.str_fastmsg_content, listData.get(i).getSource()) + listData.get(i).getDataText();
            }
            childViewHolder.text_content.setText(content);
            if (listData.get(i).getMsgType() == 1) {
                //一般消息
                childViewHolder.ll_red_fire.setVisibility(View.GONE);
                childViewHolder.image_orange_fire.setVisibility(View.GONE);
                childViewHolder.image_pos.setImageResource(R.drawable.image_bule_cricle);
            } else if (listData.get(i).getMsgType() == 2) {
                //重要
                childViewHolder.ll_red_fire.setVisibility(View.GONE);
                childViewHolder.image_orange_fire.setVisibility(View.VISIBLE);
                childViewHolder.image_pos.setImageResource(R.drawable.image_orange_cricle);
            } else if (listData.get(i).getMsgType() == 3) {
                //非常重要
                childViewHolder.ll_red_fire.setVisibility(View.VISIBLE);
                childViewHolder.image_orange_fire.setVisibility(View.GONE);
                childViewHolder.image_pos.setImageResource(R.drawable.image_red_circle);
            }
            if (BaseUtils.isEmpty(listData.get(i).getUrl())) {
                childViewHolder.text_view_article.setVisibility(View.GONE);
            } else {
                childViewHolder.text_view_article.setVisibility(View.VISIBLE);
            }
            childViewHolder.image_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new FastMsgDialog(context, listData.get(i)).show();
                }
            });
            childViewHolder.text_view_article.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("web_url", listData.get(i).getUrl());
                    BaseUtils.openActivity((Activity) context, BaseWebViewActivity.class, bundle);
                }
            });
        }
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return listData.get(position).getType();
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == FastMsgData.PARENT_TYPE;
    }

    class ParentViewHolder {
        @BindView(R.id.text_item)
        TextView text_item;

        public ParentViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

    class ChildViewHolder {
        @BindView(R.id.text_time)
        TextView text_time;
        @BindView(R.id.text_content)
        TextView text_content;
        @BindView(R.id.text_view_article)
        ImageView text_view_article;
        @BindView(R.id.image_share)
        ImageView image_share;
        @BindView(R.id.card_fast_msg)
        CardView card_fast_msg;
        @BindView(R.id.ll_red_fire)
        LinearLayout ll_red_fire;
        @BindView(R.id.image_orange_fire)
        ImageView image_orange_fire;
        @BindView(R.id.image_pos)
        ImageView image_pos;


        public ChildViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

}
