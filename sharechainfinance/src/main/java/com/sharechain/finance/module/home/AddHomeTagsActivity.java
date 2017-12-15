package com.sharechain.finance.module.home;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.sharechain.finance.BaseActivity;
import com.sharechain.finance.R;
import com.sharechain.finance.adapter.NewsChannelAdapter;
import com.sharechain.finance.adapter.OnItemClickListener;
import com.sharechain.finance.bean.NewsChannelTable;
import com.sharechain.finance.view.ItemDragHelperCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/13.
 */

public class AddHomeTagsActivity extends BaseActivity {

    @BindView(R.id.news_channel_mine_rv)
    RecyclerView mNewsChannelMineRv;
    @BindView(R.id.news_channel_more_rv)
    RecyclerView mNewsChannelMoreRv;

    private NewsChannelAdapter mNewsChannelAdapterMine;
    private NewsChannelAdapter mNewsChannelAdapterMore;

    private List<NewsChannelTable> newsChannelsMine = new ArrayList<>();
    private List<NewsChannelTable> newsChannelsMore = new ArrayList<>();

    @Override
    public int getLayout() {
        return R.layout.activity_add_home_tags;
    }

    @Override
    public void initView() {
        initTitle("未来财经");
    }

    @Override
    public void initData() {
        for (int i = 0; i < 5; i++) {
            NewsChannelTable channelTable = new NewsChannelTable();
            channelTable.setNewsChannelName("首页");
            channelTable.setNewsChannelFixed(false);
            newsChannelsMine.add(channelTable);
        }
        for (int i = 0; i < 10; i++) {
            NewsChannelTable channelTable = new NewsChannelTable();
            channelTable.setNewsChannelName("其它");
            channelTable.setNewsChannelFixed(false);
            newsChannelsMore.add(channelTable);
        }
        initRecyclerViewMineAndMore();
    }

    private void initRecyclerViewMineAndMore() {
        initRecyclerView(mNewsChannelMineRv, newsChannelsMine, true);
        initRecyclerView(mNewsChannelMoreRv, newsChannelsMore, false);
    }

    private void initRecyclerView(RecyclerView recyclerView, List<NewsChannelTable> newsChannels
            , final boolean isChannelMine) {
        // !!!加上这句将不能动态增加列表大小。。。
//        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (isChannelMine) {
            mNewsChannelAdapterMine = new NewsChannelAdapter(this, newsChannels);
            recyclerView.setAdapter(mNewsChannelAdapterMine);
            setChannelMineOnItemClick();

            initItemDragHelper();
        } else {
            mNewsChannelAdapterMore = new NewsChannelAdapter(this, newsChannels);
            recyclerView.setAdapter(mNewsChannelAdapterMore);
            setChannelMoreOnItemClick();
        }

    }

    private void setChannelMineOnItemClick() {
        mNewsChannelAdapterMine.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                NewsChannelTable newsChannel = mNewsChannelAdapterMine.getData().get(position);
                boolean isNewsChannelFixed = newsChannel.getNewsChannelFixed();
                if (!isNewsChannelFixed) {
                    mNewsChannelAdapterMore.add(mNewsChannelAdapterMore.getItemCount(), newsChannel);
                    mNewsChannelAdapterMine.delete(position);
                }
            }
        });
    }

    private void setChannelMoreOnItemClick() {
        mNewsChannelAdapterMore.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                NewsChannelTable newsChannel = mNewsChannelAdapterMore.getData().get(position);
                mNewsChannelAdapterMine.add(mNewsChannelAdapterMine.getItemCount(), newsChannel);
                mNewsChannelAdapterMore.delete(position);

            }
        });
    }

    private void initItemDragHelper() {
        ItemDragHelperCallback itemDragHelperCallback = new ItemDragHelperCallback(mNewsChannelAdapterMine);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragHelperCallback);
        itemTouchHelper.attachToRecyclerView(mNewsChannelMineRv);

        mNewsChannelAdapterMine.setItemDragHelperCallback(itemDragHelperCallback);
    }

}
