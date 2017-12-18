package com.sharechain.finance.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sharechain.finance.R;
import com.sharechain.finance.bean.FastMsgData;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
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
            childViewHolder.text_content.setText(listData.get(i).getDataText());
            childViewHolder.card_fast_msg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new FastMsgDialog(context).show();
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
        @BindView(R.id.text_content)
        TextView text_content;
        @BindView(R.id.card_fast_msg)
        CardView card_fast_msg;

        public ChildViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

}
