package com.sharechain.finance.module.home;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.sharechain.finance.BaseActivity;
import com.sharechain.finance.R;
import com.sharechain.finance.adapter.ChannelAdapter;
import com.sharechain.finance.bean.BaseNotifyBean;
import com.sharechain.finance.bean.NewsChannelTable;
import com.sharechain.finance.view.helper.ItemDragHelperCallback;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.sharechain.finance.bean.BaseNotifyBean.TYPE.TYPE_MANAGE_TAG_RESULT;
import static com.sharechain.finance.bean.BaseNotifyBean.TYPE.TYPE_SELECT_TAG;

/**
 * Created by Chu on 2017/12/25.
 */

public class ManageTagActivity extends BaseActivity {

    @BindView(R.id.rv_selected)
    RecyclerView rv_selected;

    private List<NewsChannelTable> newsChannelsMine = new ArrayList<>();
    private List<NewsChannelTable> newsChannelsMore = new ArrayList<>();
    private ChannelAdapter channelAdapter;
    private int selectPosition = 0;

    @Override
    public int getLayout() {
        return R.layout.activity_manage_tag;
    }

    @Override
    public void initView() {
        initTitle("");
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
    }

    private void initRecyclerViewMineAndMore() {
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        rv_selected.setLayoutManager(manager);

        ItemDragHelperCallback callback = new ItemDragHelperCallback();
        final ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(rv_selected);

        channelAdapter = new ChannelAdapter(this, helper, newsChannelsMine, newsChannelsMore);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = channelAdapter.getItemViewType(position);
                return viewType == ChannelAdapter.TYPE_MY || viewType == ChannelAdapter.TYPE_OTHER ? 1 : 4;
            }
        });
        rv_selected.setAdapter(channelAdapter);

        channelAdapter.setOnMyChannelItemClickListener(new ChannelAdapter.OnMyChannelItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                //已选择的位置
                selectPosition = position;
                finish();
            }
        });
    }

    private void setResultData() {
        if (channelAdapter != null && channelAdapter.isDataChanged()) {
            List<NewsChannelTable> myList = channelAdapter.getmMyChannelItems();
            List<NewsChannelTable> otherList = channelAdapter.getmOtherChannelItems();
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
            for (NewsChannelTable tmp : otherList) {
                NewsChannelTable mine = new NewsChannelTable();
                mine.setCacheType(NewsChannelTable.CACHE_TYPE_ALL);
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
