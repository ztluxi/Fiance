package com.sharechain.finance.module.mine;

import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.utils.LogUtils;
import com.sharechain.finance.BaseActivity;
import com.sharechain.finance.R;
import com.sharechain.finance.adapter.HistoryAdapter;
import com.sharechain.finance.bean.HomeData;
import com.sharechain.finance.utils.ToastManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.baseadapter.BGAOnItemChildClickListener;

/**
 * Created by ${zhoutao} on 2017/12/13 0013.
 */

public class HistoryActivity extends BaseActivity {
    @BindView(R.id.image_title_left)
    ImageView back_Image;
    @BindView(R.id.history_lv)
    ListView historylv;
    @BindView(R.id.xrefreshview_content)
    XRefreshView refreshView;

    private List<HomeData> homeDataList = new ArrayList<>();
    private HistoryAdapter adapter;
    public static long lastRefreshTime;

    @Override
    public int getLayout() {
        return R.layout.activity_history;
    }

    @Override
    public void initView() {
        initData();
        initTitle(getString(R.string.history));
        back_Image.setVisibility(View.VISIBLE);
        initXRefreshView(refreshView);
        refreshView.setPullRefreshEnable(true);
        refreshView.setPullLoadEnable(false);
        refreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
                refreshView.stopRefresh();
            }
        });
        adapter = new HistoryAdapter(this, R.layout.fragment_home_item);
        adapter.setOnItemChildClickListener(new BGAOnItemChildClickListener() {
            @Override
            public void onItemChildClick(ViewGroup parent, View childView, int position) {
                ToastManager.showShort(HistoryActivity.this,"您店里"+position);
            }
        });
        adapter.setData(homeDataList);
        historylv.setAdapter(adapter);
    }
    @Override
    public void initData() {
        for (int i = 0; i < 10; i++) {
            HomeData homeData = new HomeData();
            homeData.setAuthor("索隆");
            homeData.setTitle("A股冰火之歌。。。");
            homeData.setImage("");
            homeDataList.add(homeData);
        }
    }

    @OnClick({R.id.image_title_left})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_title_left:
                finish();
                break;
        }
    }


}
