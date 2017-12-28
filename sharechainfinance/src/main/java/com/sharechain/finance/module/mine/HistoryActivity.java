package com.sharechain.finance.module.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.andview.refreshview.XRefreshView;
import com.sharechain.finance.BaseActivity;
import com.sharechain.finance.R;
import com.sharechain.finance.adapter.HistoryAdapter;
import com.sharechain.finance.bean.ArticleListsBean;
import com.sharechain.finance.bean.HistoryBean;
import com.sharechain.finance.module.home.ArticleDetailActivity;
import com.sharechain.finance.utils.BaseUtils;

import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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

    private HistoryAdapter answerAdapter;
    private List<HistoryBean> dataList = new ArrayList<>();

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
                DataSupport.deleteAll(HistoryBean.class);
                dataList.clear();
                answerAdapter.setData(dataList);
                setEmptyView(xRefreshView, true);
            }
        });

        answerAdapter = new HistoryAdapter(this, R.layout.item_answer_fragment);
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

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
                getHistoryList(true);
            }

        });
        answerAdapter.setData(dataList);
        listView.setAdapter(answerAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void initData() {
        getHistoryList(true);
    }

    private void getHistoryList(final boolean isRefresh) {
        DataSupport.findAllAsync(HistoryBean.class).listen(new FindMultiCallback() {
            @Override
            public <T> void onFinish(List<T> t) {
                //异步查询数据库
                List<HistoryBean> viewedList = (List<HistoryBean>) t;
                if (isRefresh) {
                    dataList.clear();
                }
                dataList.addAll(viewedList);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        xRefreshView.stopRefresh();
                        xRefreshView.stopLoadMore();
                        answerAdapter.setData(dataList);
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

    @OnClick({R.id.image_title_left, R.id.image_title_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_title_left:
                finish();
                break;
            case R.id.image_title_right:
                dataList.clear();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Bundle bundle = new Bundle();
        ArticleListsBean articleListsBean = convertToArticle(dataList.get(i));
        bundle.putSerializable("article", articleListsBean);
        BaseUtils.openActivity(this, ArticleDetailActivity.class, bundle);
    }
}
