package com.sharechain.finance.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sharechain.finance.R;
import com.sharechain.finance.bean.NewsChannelTable;

import java.util.List;

/**
 * Created by Chu on 2018/1/6.
 */

public class ChannelFixedOtherAdapter extends RecyclerView.Adapter<ChannelFixedOtherAdapter.OtherViewHolder> {
    private Context context;
    private List<NewsChannelTable> otherList;
    private IOtherItemClickListener iOtherItemClickListener;

    public ChannelFixedOtherAdapter(Context context, List<NewsChannelTable> otherList, IOtherItemClickListener iOtherItemClickListener) {
        this.context = context;
        this.otherList = otherList;
        this.iOtherItemClickListener = iOtherItemClickListener;
    }

    @Override
    public OtherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_manage_tag_other, parent, false);
        final OtherViewHolder otherViewHolder = new OtherViewHolder(view);
        otherViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击从其它频道添加到我的频道
                int position = otherViewHolder.getLayoutPosition();
                NewsChannelTable table = otherList.get(position);
                table.setCacheType(NewsChannelTable.CACHE_TYPE_MINE);
                otherList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, otherList.size());
                if (iOtherItemClickListener != null) {
                    iOtherItemClickListener.onOtherItemClick(table);
                }
            }
        });
        return otherViewHolder;
    }

    @Override
    public void onBindViewHolder(OtherViewHolder holder, int position) {
        holder.textView.setText(otherList.get(position).getNewsChannelName());
    }

    @Override
    public int getItemCount() {
        return otherList.size();
    }

    /**
     * 其他频道
     */
    class OtherViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public OtherViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_item);
        }
    }

    public interface IOtherItemClickListener {
        void onOtherItemClick(NewsChannelTable otherTable);
    }

    public List<NewsChannelTable> getOtherList() {
        return otherList;
    }
}
