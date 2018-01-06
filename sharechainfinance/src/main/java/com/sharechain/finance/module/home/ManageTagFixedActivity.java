package com.sharechain.finance.module.home;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharechain.finance.BaseActivity;
import com.sharechain.finance.R;
import com.sharechain.finance.adapter.ChannelFixedAdapter;
import com.sharechain.finance.adapter.ChannelFixedOtherAdapter;
import com.sharechain.finance.bean.BaseNotifyBean;
import com.sharechain.finance.bean.NewsChannelTable;
import com.sharechain.finance.view.helper.ItemDragHelperCallback;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.sharechain.finance.bean.BaseNotifyBean.TYPE.TYPE_MANAGE_TAG_RESULT;
import static com.sharechain.finance.bean.BaseNotifyBean.TYPE.TYPE_SELECT_TAG;

/**
 * Created by Chu on 2018/1/5.
 */

public class ManageTagFixedActivity extends BaseActivity {

    @BindView(R.id.rv_selected)
    RecyclerView rv_selected;
    @BindView(R.id.rv_other)
    RecyclerView rv_other;
    @BindView(R.id.text_manage_tip)
    TextView text_manage_tip;
    @BindView(R.id.text_bianji)
    TextView text_bianji;
    @BindView(R.id.image_delete)
    ImageView image_delete;
    @BindView(R.id.layout_other_header)
    View layout_other_header;

    private List<NewsChannelTable> newsChannelsMine = new ArrayList<>();
    private List<NewsChannelTable> newsChannelsMore = new ArrayList<>();
    private ChannelFixedAdapter mineAdapter;
    private ChannelFixedOtherAdapter otherAdapter;
    private int selectPosition = 0;

    @Override
    public int getLayout() {
        return R.layout.activity_manage_tag;
    }

    @Override
    public void initView() {
        initTitle(getString(R.string.title_manage_tag));
        text_title.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
        mImmersionBar.statusBarColor(R.color.colorBlack).init();
        view_status_bar.setVisibility(View.GONE);
        rl_base_layout.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        image_title_left.setVisibility(View.VISIBLE);
        image_title_left.setImageResource(R.drawable.icon_back_black);
        image_title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        image_title_right.setVisibility(View.GONE);
        image_title_right.setImageResource(R.drawable.icon_home_search);
    }

    @Override
    public void initData() {
        List<NewsChannelTable> tmpMineList = DataSupport.where("cacheType = ?", String.valueOf(NewsChannelTable.CACHE_TYPE_MINE)).find(NewsChannelTable.class);
        newsChannelsMine.addAll(tmpMineList);
        List<NewsChannelTable> tmpMoreList = DataSupport.where("cacheType = ?", String.valueOf(NewsChannelTable.CACHE_TYPE_ALL)).find(NewsChannelTable.class);
        newsChannelsMore.addAll(tmpMoreList);
        initRecyclerViewMineAndMore();
        if (newsChannelsMore.size() == 0) {
            layout_other_header.setVisibility(View.GONE);
            rv_other.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.text_bianji)
    void bianji() {
        mineAdapter.startEditMode();
        layout_other_header.setVisibility(View.GONE);
        rv_other.setVisibility(View.GONE);
        text_manage_tip.setText(R.string.manage_tip);
        image_delete.setVisibility(View.VISIBLE);
        text_bianji.setVisibility(View.GONE);
    }

    @OnClick(R.id.image_delete)
    void imageDeleta() {
        mineAdapter.cancelEditMode();
        layout_other_header.setVisibility(View.VISIBLE);
        rv_other.setVisibility(View.VISIBLE);
        text_manage_tip.setText(R.string.search_tag_click);
        image_delete.setVisibility(View.GONE);
        text_bianji.setVisibility(View.VISIBLE);
    }

    private void initRecyclerViewMineAndMore() {
        GridLayoutManager mineManager = new GridLayoutManager(this, 4);
        rv_selected.setLayoutManager(mineManager);
        rv_other.setLayoutManager(new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false));

        ItemDragHelperCallback callback = new ItemDragHelperCallback();
        final ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(rv_selected);

        mineAdapter = new ChannelFixedAdapter(this, helper, newsChannelsMine, newsChannelsMore);
        mineManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = mineAdapter.getItemViewType(position);
                return viewType == ChannelFixedAdapter.TYPE_MY ? 1 : 4;
            }
        });
        rv_selected.setAdapter(mineAdapter);

        mineAdapter.setOnMyChannelItemClickListener(new ChannelFixedAdapter.OnMyChannelItemClickListener() {
            @Override
            public void onItemClick(View v, int position, boolean isManage) {
                if (isManage) {
                    int otherChangePos = newsChannelsMore.size();
                    //其它频道列表添加
                    NewsChannelTable table = newsChannelsMine.get(position);
                    table.setCacheType(NewsChannelTable.CACHE_TYPE_ALL);
                    newsChannelsMore.add(table);
                    //我的频道删除
                    newsChannelsMine.remove(position);
                    mineAdapter.notifyItemRemoved(position);
                    mineAdapter.notifyItemRangeChanged(position, newsChannelsMine.size());
                    //其它频道添加
                    otherAdapter.notifyItemInserted(otherChangePos);
                } else {
                    //已选择的位置
                    selectPosition = position;
                    finish();
                }
            }
        });

        //其它频道
        otherAdapter = new ChannelFixedOtherAdapter(this, newsChannelsMore, new ChannelFixedOtherAdapter.IOtherItemClickListener() {
            @Override
            public void onOtherItemClick(NewsChannelTable otherTable) {
                newsChannelsMine.add(otherTable);
                mineAdapter.notifyDataSetChanged();
                mineAdapter.setDataChanged(true);
            }
        });
        rv_other.setAdapter(otherAdapter);

    }

    private void setResultData() {
        if (mineAdapter != null && mineAdapter.isDataChanged()) {
            List<NewsChannelTable> myList = mineAdapter.getmMyChannelItems();
            DataSupport.deleteAll(NewsChannelTable.class, "cacheType = ?", String.valueOf(NewsChannelTable.CACHE_TYPE_MINE));
            DataSupport.deleteAll(NewsChannelTable.class, "cacheType = ?", String.valueOf(NewsChannelTable.CACHE_TYPE_ALL));
            for (NewsChannelTable tmp : myList) {
                NewsChannelTable mine = new NewsChannelTable();
                mine.setCacheType(NewsChannelTable.CACHE_TYPE_MINE);
                mine.setNewsChannelName(tmp.getNewsChannelName());
                mine.setNewsChannelId(tmp.getNewsChannelId());
                mine.setNewsChannelSelect(tmp.getNewsChannelSelect());
                mine.setNewsChannelFixed(tmp.getNewsChannelFixed());
                mine.save();
            }
            BaseNotifyBean baseNotifyBean = new BaseNotifyBean();
            baseNotifyBean.setType(TYPE_MANAGE_TAG_RESULT);
            baseNotifyBean.setInteger(selectPosition);
            baseNotifyBean.setObj(myList);
            EventBus.getDefault().post(baseNotifyBean);
        } else {
            BaseNotifyBean baseNotifyBean = new BaseNotifyBean();
            baseNotifyBean.setType(TYPE_SELECT_TAG);
            baseNotifyBean.setInteger(selectPosition);
            EventBus.getDefault().post(baseNotifyBean);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResultData();
    }

}