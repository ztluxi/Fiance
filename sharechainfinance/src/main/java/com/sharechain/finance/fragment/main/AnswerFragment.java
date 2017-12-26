package com.sharechain.finance.fragment.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.andview.refreshview.XRefreshView;
import com.sharechain.finance.BaseFragment;
import com.sharechain.finance.MyStringCallback;
import com.sharechain.finance.R;
import com.sharechain.finance.adapter.AnswerAdapter;
import com.sharechain.finance.adapter.NewsListAdapter;
import com.sharechain.finance.bean.HomeArticleListBean;
import com.sharechain.finance.bean.HomeIndexBean;
import com.sharechain.finance.bean.UrlList;
import com.sharechain.finance.module.home.ArticleDetailActivity;
import com.sharechain.finance.utils.BaseUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 问答界面
 * Created by Chu on 2017/12/22.
 */

public class AnswerFragment extends BaseFragment implements NewsListAdapter.OnNewsListItemClickListener {

    private static final String NEWS_TYPE_KEY = "NewsTypeKey";
    private static final String NEWS_TYPE_POSITION = "NewsTypePosition";

    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.xrefreshview)
    XRefreshView xRefreshView;

    private AnswerAdapter answerAdapter;
    private HomeIndexBean homeIndexBean;
    private List<HomeArticleListBean.DataBean.ArticleListsBean> dataList = new ArrayList<>();
    private int curPosition;

    public static AnswerFragment newInstance(HomeIndexBean homeIndexBean, int pos) {
        AnswerFragment fragment = new AnswerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(NEWS_TYPE_KEY, homeIndexBean);
        bundle.putInt(NEWS_TYPE_POSITION, pos);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            homeIndexBean = (HomeIndexBean) getArguments().getSerializable(NEWS_TYPE_KEY);
            curPosition = getArguments().getInt(NEWS_TYPE_POSITION);
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_answer;
    }

    @Override
    protected void initView() {
        answerAdapter = new AnswerAdapter(getActivity(), R.layout.item_answer_fragment);
        initXRefreshView(xRefreshView);
        xRefreshView.setPullRefreshEnable(true);
        xRefreshView.setPullLoadEnable(true);
        xRefreshView.setAutoRefresh(false);
        xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
                page = 1;
                getListData(homeIndexBean.getData().getArticle_title_lists().get(curPosition).getTerm_taxonomy_id());
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
                page++;
                getListData(homeIndexBean.getData().getArticle_title_lists().get(curPosition).getTerm_taxonomy_id());
            }

        });
        answerAdapter.setData(dataList);
        listView.setAdapter(answerAdapter);
    }

    @Override
    public void initData() {
        getListData(homeIndexBean.getData().getArticle_title_lists().get(curPosition).getTerm_taxonomy_id());
    }

    //根据id获取列表
    private void getListData(int term_taxonomy_id) {
        Map<String, String> params = new HashMap<>();
        if (term_taxonomy_id > 0) {
            params.put("term_taxonomy_id", String.valueOf(term_taxonomy_id));
            params.put(pageParam, String.valueOf(page));
        }
        requestGet(UrlList.HOME_ARTICLE_LIST, params, new MyStringCallback(getActivity()) {
            @Override
            protected void onSuccess(String result) {
                xRefreshView.stopRefresh();
                xRefreshView.stopLoadMore();
                HomeArticleListBean bean = JSON.parseObject(result, HomeArticleListBean.class);
                if (bean.getSuccess() == UrlList.CODE_SUCCESS) {
                    if (page == 1) {
                        dataList.clear();
                    }
                    dataList.addAll(bean.getData().getArticle_lists());
                    answerAdapter.setData(dataList);
                    answerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            protected void onFailed(String errStr) {
                xRefreshView.stopRefresh();
                xRefreshView.stopLoadMore();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position, boolean isPhoto) {
        Bundle bundle = new Bundle();
        bundle.putInt("article_id", dataList.get(position).getID());
        BaseUtils.openActivity(getActivity(), ArticleDetailActivity.class, bundle);
    }

}