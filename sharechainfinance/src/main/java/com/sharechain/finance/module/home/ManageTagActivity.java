package com.sharechain.finance.module.home;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharechain.finance.BaseActivity;
import com.sharechain.finance.R;
import com.sharechain.finance.adapter.NewsChannelAdapter;
import com.sharechain.finance.adapter.OnItemClickListener;
import com.sharechain.finance.bean.BaseNotifyBean;
import com.sharechain.finance.bean.NewsChannelTable;
import com.sharechain.finance.view.ItemDragHelperCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.sharechain.finance.bean.BaseNotifyBean.TYPE.TYPE_MANAGE_TAG_RESULT;

/**
 * Created by Chu on 2017/12/25.
 */

public class ManageTagActivity extends BaseActivity {

    @BindView(R.id.rv_selected)
    RecyclerView rv_selected;
    @BindView(R.id.rv_other)
    RecyclerView rv_other;
    @BindView(R.id.text_bianji)
    TextView text_bianji;
    @BindView(R.id.image_delete)
    ImageView image_delete;

    private NewsChannelAdapter mNewsChannelAdapterMine;
    private NewsChannelAdapter mNewsChannelAdapterMore;

    private List<NewsChannelTable> newsChannelsMine = new ArrayList<>();
    private List<NewsChannelTable> newsChannelsMore = new ArrayList<>();
    private boolean isManage = false;
    //标签是否编辑
    private boolean isDataChanged = false;

    @Override
    public int getLayout() {
        return R.layout.activity_manage_tag;
    }

    @Override
    public void initView() {
        initTitle("");
        List<NewsChannelTable> getList = (List<NewsChannelTable>) getIntent().getSerializableExtra("list");
        newsChannelsMine.addAll(getList);
        mImmersionBar.statusBarColor(R.color.colorBlack).init();
        view_status_bar.setVisibility(View.GONE);
        rl_base_layout.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        image_title_right.setVisibility(View.VISIBLE);
        image_title_right.setImageResource(R.drawable.icon_home_search);
    }

    @Override
    public void initData() {
        for (int i = 0; i < newsChannelsMine.size(); i++) {
            if (!newsChannelsMine.get(i).getNewsChannelName().equals("推荐")) {
                NewsChannelTable channelTable = new NewsChannelTable();
                channelTable.setNewsChannelId(String.valueOf(newsChannelsMine.get(i).getNewsChannelId()));
                channelTable.setNewsChannelName(newsChannelsMine.get(i).getNewsChannelName());
                channelTable.setNewsChannelFixed(false);
                channelTable.setNewsChannelSelect(false);
                newsChannelsMore.add(channelTable);
            }
        }
        initRecyclerViewMineAndMore();
    }

    private void initRecyclerViewMineAndMore() {
        initRecyclerView(rv_selected, newsChannelsMine, true);
        initRecyclerView(rv_other, newsChannelsMore, false);
    }

    private void initRecyclerView(RecyclerView recyclerView, List<NewsChannelTable> newsChannels
            , final boolean isChannelMine) {
        // !!!加上这句将不能动态增加列表大小。。。
        // recyclerView.setHasFixedSize(true);
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

    //我的频道点击
    private void setChannelMineOnItemClick() {
        mNewsChannelAdapterMine.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, boolean isItem) {
                if (!isItem) {
                    mNewsChannelAdapterMine.delete(position);
                    isDataChanged = true;
                }
            }
        });
        mNewsChannelAdapterMine.setiOnItemMoveListener(new NewsChannelAdapter.IOnItemMoveListener() {
            @Override
            public void onItemMove() {
                //item移动监听
                isDataChanged = true;
            }
        });
    }

    //更多item点击
    private void setChannelMoreOnItemClick() {
        mNewsChannelAdapterMore.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, boolean isItem) {
                closeManage();
                NewsChannelTable newsChannel = mNewsChannelAdapterMore.getData().get(position);
                for (NewsChannelTable table : newsChannelsMine) {
                    if (table.getNewsChannelName().equals(newsChannel.getNewsChannelName())) {
                        //已经添加
                        return;
                    }
                }
                mNewsChannelAdapterMine.add(mNewsChannelAdapterMine.getItemCount(), newsChannel);
                isDataChanged = true;
            }
        });
    }

    private void initItemDragHelper() {
        ItemDragHelperCallback itemDragHelperCallback = new ItemDragHelperCallback(mNewsChannelAdapterMine);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragHelperCallback);
        itemTouchHelper.attachToRecyclerView(rv_selected);
        mNewsChannelAdapterMine.setItemDragHelperCallback(itemDragHelperCallback);
    }

    @OnClick(R.id.text_bianji)
    void manage() {
        isManage = true;
        text_bianji.setVisibility(View.GONE);
        image_delete.setVisibility(View.VISIBLE);
        for (int i = 0; i < newsChannelsMine.size(); i++) {
            if (i == 0) {
                newsChannelsMine.get(i).setNewsChannelFixed(true);
                newsChannelsMine.get(i).setNewsChannelSelect(false);
            } else {
                newsChannelsMine.get(i).setNewsChannelFixed(false);
                newsChannelsMine.get(i).setNewsChannelSelect(true);
            }
        }
        mNewsChannelAdapterMine.notifyDataSetChanged();
    }

    @OnClick(R.id.image_delete)
    void closeManage() {
        isManage = false;
        text_bianji.setVisibility(View.VISIBLE);
        image_delete.setVisibility(View.GONE);
        for (int i = 0; i < newsChannelsMine.size(); i++) {
            if (i == 0) {
                newsChannelsMine.get(i).setNewsChannelFixed(true);
                newsChannelsMine.get(i).setNewsChannelSelect(false);
            } else {
                newsChannelsMine.get(i).setNewsChannelFixed(false);
                newsChannelsMine.get(i).setNewsChannelSelect(false);
            }
        }
        mNewsChannelAdapterMine.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isDataChanged) {
            List<NewsChannelTable> resultList = mNewsChannelAdapterMine.getData();
            BaseNotifyBean baseNotifyBean = new BaseNotifyBean();
            baseNotifyBean.setType(TYPE_MANAGE_TAG_RESULT);
            baseNotifyBean.setObj(resultList);
            EventBus.getDefault().post(baseNotifyBean);
        }
    }

}
