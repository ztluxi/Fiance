package com.sharechain.finance.module.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.andview.refreshview.XRefreshView;
import com.sharechain.finance.BaseActivity;
import com.sharechain.finance.R;
import com.sharechain.finance.adapter.HistoryMineAdapter;
import com.sharechain.finance.bean.ArticleListsBean;
import com.sharechain.finance.bean.HistoryData;
import com.sharechain.finance.module.home.ArticleDetailActivity;
import com.sharechain.finance.utils.BaseUtils;

import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ${zhoutao} on 2017/12/13 0013.
 */

public class HistoryActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.image_title_left)
    ImageView back_Image;
    @BindView(R.id.image_title_right)
    ImageView clearHistoryIv;
    @BindView(R.id.history_lv)
    ListView listView;
    @BindView(R.id.xrefreshview_content)
    XRefreshView xRefreshView;

    private HistoryMineAdapter answerAdapter;
    private List<HistoryData> dataList = new ArrayList<>();

    @Override
    public int getLayout() {
        return R.layout.activity_history;
    }

    @Override
    public void initView() {
        initTitle(getString(R.string.history));
        back_Image.setVisibility(View.VISIBLE);
        clearHistoryIv.setVisibility(View.VISIBLE);
        clearHistoryIv.setImageResource(R.drawable.clear);
        clearHistoryIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataSupport.deleteAll(HistoryData.class);
                dataList.clear();
                answerAdapter.setListData(dataList);
                answerAdapter.notifyDataSetChanged();
                setEmptyView(xRefreshView, true);
            }
        });

        answerAdapter = new HistoryMineAdapter(this, dataList);
        initXRefreshView(xRefreshView);
        initEmptyView(xRefreshView);
        xRefreshView.setPullRefreshEnable(true);
        xRefreshView.setPullLoadEnable(false);
        xRefreshView.setAutoRefresh(false);
        xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
                getHistoryList(true);
            }

        });
        listView.setAdapter(answerAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void initData() {
        getHistoryList(true);
    }

    private void getHistoryList(final boolean isRefresh) {
        DataSupport.findAllAsync(HistoryData.class).listen(new FindMultiCallback() {
            @Override
            public <T> void onFinish(List<T> t) {
                //异步查询数据库
                List<HistoryData> viewedList = (List<HistoryData>) t;
                if (isRefresh) {
                    dataList.clear();
                }
                dataList.addAll(viewedList);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        xRefreshView.stopRefresh();
                        xRefreshView.stopLoadMore();
                        answerAdapter.setListData(dataList);
                        answerAdapter.notifyDataSetChanged();
                        if (dataList.size() == 0) {
                            setEmptyView(xRefreshView, true);
                        } else {
                            setEmptyView(xRefreshView, false);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (dataList.get(i).getType() == HistoryData.CHILD_TYPE) {
            Bundle bundle = new Bundle();
            ArticleListsBean articleListsBean = convertToArticle(dataList.get(i));
            bundle.putSerializable("article", articleListsBean);
            BaseUtils.openActivity(this, ArticleDetailActivity.class, bundle);
        }
    }

}
