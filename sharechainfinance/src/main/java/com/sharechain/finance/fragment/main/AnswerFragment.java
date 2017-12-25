package com.sharechain.finance.fragment.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

import com.andview.refreshview.XRefreshView;
import com.sharechain.finance.BaseFragment;
import com.sharechain.finance.R;
import com.sharechain.finance.adapter.AnswerAdapter;
import com.sharechain.finance.adapter.NewsListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 问答界面
 * Created by Chu on 2017/12/22.
 */

public class AnswerFragment extends BaseFragment implements NewsListAdapter.OnNewsListItemClickListener {

    private static final String NEWS_TYPE_KEY = "NewsTypeKey";

    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.xrefreshview)
    XRefreshView xRefreshView;

    private AnswerAdapter answerAdapter;
    private String mNewsId;
    private List<String> dataList = new ArrayList<>();
    private List<String> tipList = new ArrayList<>();
    private boolean mIsAllLoaded = false;

    public static AnswerFragment newInstance(String newsId) {
        AnswerFragment fragment = new AnswerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(NEWS_TYPE_KEY, newsId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNewsId = getArguments().getString(NEWS_TYPE_KEY);
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_answer;
    }

    @Override
    protected void initView() {
        initListData();

        answerAdapter = new AnswerAdapter(getActivity(), R.layout.item_answer_fragment);
        initXRefreshView(xRefreshView);
        xRefreshView.setPullRefreshEnable(true);
        xRefreshView.setPullLoadEnable(false);
        xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
                xRefreshView.stopRefresh();
            }

        });
        answerAdapter.setData(dataList);
        listView.setAdapter(answerAdapter);
    }

    @Override
    public void initData() {

    }

    private void initListData() {
        for (int i = 0; i < 10; i++) {
            dataList.add("item_" + i);
        }
    }

    @Override
    public void onItemClick(View view, int position, boolean isPhoto) {

    }

}