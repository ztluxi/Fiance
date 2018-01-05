package com.sharechain.finance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharechain.finance.R;
import com.sharechain.finance.bean.FastMsgData;
import com.sharechain.finance.bean.HistoryData;
import com.sharechain.finance.module.home.ArticleDetailActivity;
import com.sharechain.finance.utils.GlideUtils;
import com.sharechain.finance.utils.TimeUtil;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.halfbit.pinnedsection.PinnedSectionListView;

/**
 * Created by Chu on 2018/1/3.
 */

public class HistoryMineAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {
    private List<HistoryData> listData;
    private Context context;

    public HistoryMineAdapter(Context context, List<HistoryData> listData) {
        this.context = context;
        this.listData = listData;
    }

    public void setListData(List<HistoryData> listData) {
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
            HistoryMineAdapter.ParentViewHolder parentViewHolder;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item_fast_msg_parent, viewGroup, false);
            }
            parentViewHolder = new HistoryMineAdapter.ParentViewHolder(view);
            parentViewHolder.text_item.setText(listData.get(i).getDate());
        } else if (itemType == FastMsgData.CHILD_TYPE) {
            if (listData.get(i).getChannel_type() == ArticleDetailActivity.NEWS_TYPE_HOME) {
                HistoryMineAdapter.HomeChildViewHolder childViewHolder;
                if (view == null) {
                    view = LayoutInflater.from(context).inflate(R.layout.item_news, viewGroup, false);
                }
                childViewHolder = new HistoryMineAdapter.HomeChildViewHolder(view);
                childViewHolder.text_content.setText(listData.get(i).getPost_title());
                childViewHolder.text_type.setText(listData.get(i).getName());
                Date date = TimeUtil.StringToDate(listData.get(i).getPost_date_gmt());
                childViewHolder.text_time.setText(TimeUtil.RelativeDateFormat(date));
                GlideUtils.getInstance().loadUserImage(context, listData.get(i).getImage(), childViewHolder.image_pic, R.drawable.home_default);
            } else if (listData.get(i).getChannel_type() == ArticleDetailActivity.NEWS_TYPE_ANSWER) {
                HistoryMineAdapter.AnswerChildViewHolder childViewHolder;
                if (view == null) {
                    view = LayoutInflater.from(context).inflate(R.layout.item_answer_fragment, viewGroup, false);
                }
                childViewHolder = new HistoryMineAdapter.AnswerChildViewHolder(view);
                childViewHolder.text_content.setText(listData.get(i).getPost_title());
                childViewHolder.text_comment_num.setText(listData.get(i).getViews() + context.getString(R.string.home_item_comment));
                childViewHolder.text_praise_num.setText(listData.get(i).getPost_view_rand() + context.getString(R.string.home_item_praise));
                GlideUtils.getInstance().loadUserImage(context, listData.get(i).getImage(), childViewHolder.image_pic, R.drawable.home_default);
            }
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

    class AnswerChildViewHolder {
        @BindView(R.id.text_content)
        TextView text_content;
        @BindView(R.id.text_comment_num)
        TextView text_comment_num;
        @BindView(R.id.text_praise_num)
        TextView text_praise_num;
        @BindView(R.id.image_pic)
        ImageView image_pic;

        public AnswerChildViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

    class HomeChildViewHolder {
        @BindView(R.id.text_content)
        TextView text_content;
        @BindView(R.id.text_time)
        TextView text_time;
        @BindView(R.id.text_type)
        TextView text_type;
        @BindView(R.id.image_pic)
        ImageView image_pic;

        public HomeChildViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

}
