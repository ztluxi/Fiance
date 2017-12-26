package com.sharechain.finance.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharechain.finance.R;
import com.sharechain.finance.bean.FastMsgData;
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
            childViewHolder.text_title.setText(listData.get(i).getTitle());
            childViewHolder.text_time.setText(listData.get(i).getHour());
            childViewHolder.text_content.setText(listData.get(i).getDataText());
            if (listData.get(i).getMsgType() == 1) {
                //一般消息
                childViewHolder.text_msg_type.setBackgroundResource(R.drawable.common_blue_msg_bg);
                childViewHolder.text_msg_type.setText(context.getString(R.string.fastmsg_normal));
            } else if (listData.get(i).getMsgType() == 2) {
                //重要
                childViewHolder.text_msg_type.setBackgroundResource(R.drawable.common_orange_msg_bg);
                childViewHolder.text_msg_type.setText(context.getString(R.string.fastmsg_important));
            } else if (listData.get(i).getMsgType() == 3) {
                //非常重要
                childViewHolder.text_msg_type.setBackgroundResource(R.drawable.common_red_msg_bg);
                childViewHolder.text_msg_type.setText(context.getString(R.string.fastmsg_important));
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
        @BindView(R.id.text_title)
        TextView text_title;
        @BindView(R.id.text_msg_type)
        TextView text_msg_type;
        @BindView(R.id.text_time)
        TextView text_time;
        @BindView(R.id.text_content)
        TextView text_content;
        @BindView(R.id.text_view_article)
        TextView text_view_article;
        @BindView(R.id.image_share)
        ImageView image_share;
        @BindView(R.id.card_fast_msg)
        CardView card_fast_msg;

        public ChildViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

}
