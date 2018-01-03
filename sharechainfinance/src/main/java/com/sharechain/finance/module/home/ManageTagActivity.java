package com.sharechain.finance.module.home;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.sharechain.finance.BaseActivity;
import com.sharechain.finance.R;
import com.sharechain.finance.adapter.ChannelAdapter;
import com.sharechain.finance.bean.NewsChannelTable;
import com.sharechain.finance.view.helper.ItemDragHelperCallback;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Chu on 2017/12/25.
 */

public class ManageTagActivity extends BaseActivity {

    @BindView(R.id.rv_selected)
    RecyclerView rv_selected;

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

        final ChannelAdapter adapter = new ChannelAdapter(this, helper, newsChannelsMine, newsChannelsMore);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = adapter.getItemViewType(position);
                return viewType == ChannelAdapter.TYPE_MY || viewType == ChannelAdapter.TYPE_OTHER ? 1 : 4;
            }
        });
        rv_selected.setAdapter(adapter);

        adapter.setOnMyChannelItemClickListener(new ChannelAdapter.OnMyChannelItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Toast.makeText(ManageTagActivity.this, newsChannelsMine.get(position).getNewsChannelName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    @OnClick(R.id.text_bianji)
//    void manage() {
//        isManage = true;
//        for (int i = 0; i < newsChannelsMine.size(); i++) {
//            if (i == 0) {
//                newsChannelsMine.get(i).setNewsChannelFixed(true);
//                newsChannelsMine.get(i).setNewsChannelSelect(false);
//            } else {
//                newsChannelsMine.get(i).setNewsChannelFixed(false);
//                newsChannelsMine.get(i).setNewsChannelSelect(true);
//            }
//        }
//    }
//
//    @OnClick(R.id.image_delete)
//    void closeManage() {
//        isManage = false;
//        for (int i = 0; i < newsChannelsMine.size(); i++) {
//            if (i == 0) {
//                newsChannelsMine.get(i).setNewsChannelFixed(true);
//                newsChannelsMine.get(i).setNewsChannelSelect(false);
//            } else {
//                newsChannelsMine.get(i).setNewsChannelFixed(false);
//                newsChannelsMine.get(i).setNewsChannelSelect(false);
//            }
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (isDataChanged) {
//            //编辑后先删除所有标签，然后再添加编辑后的标签
//            DataSupport.deleteAll(NewsChannelTable.class, "cacheType = ?", String.valueOf(NewsChannelTable.CACHE_TYPE_MINE));
//            List<NewsChannelTable> resultList = mNewsChannelAdapterMine.getData();
//            for (NewsChannelTable tmp : resultList) {
//                NewsChannelTable mine = new NewsChannelTable();
//                mine.setCacheType(NewsChannelTable.CACHE_TYPE_MINE);
//                mine.setNewsChannelName(tmp.getNewsChannelName());
//                mine.setNewsChannelId(tmp.getNewsChannelId());
//                mine.setNewsChannelSelect(tmp.getNewsChannelSelect());
//                mine.setNewsChannelFixed(tmp.getNewsChannelFixed());
//                mine.save();
//            }
//            BaseNotifyBean baseNotifyBean = new BaseNotifyBean();
//            baseNotifyBean.setType(TYPE_MANAGE_TAG_RESULT);
//            baseNotifyBean.setObj(resultList);
//            EventBus.getDefault().post(baseNotifyBean);
//        }
    }

}
