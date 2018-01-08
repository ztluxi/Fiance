package com.sharechain.finance.fragment.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.andview.refreshview.XRefreshView;
import com.sharechain.finance.BaseFragment;
import com.sharechain.finance.MyStringCallback;
import com.sharechain.finance.R;
import com.sharechain.finance.adapter.HomeOtherAdapter;
import com.sharechain.finance.bean.ArticleListsBean;
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
 * Created by Chu on 2018/1/8.
 */

public class HomeOtherFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private static final String NEWS_TYPE_KEY = "NewsTypeKey";
    private static final String NEWS_TYPE_POSITION = "NewsTypePosition";

    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.xrefreshview)
    XRefreshView xRefreshView;

    private HomeOtherAdapter answerAdapter;
    private HomeIndexBean homeIndexBean;
    private List<ArticleListsBean> dataList = new ArrayList<>();
    private int curPosition;

    public static HomeOtherFragment newInstance(HomeIndexBean homeIndexBean, int pos) {
        HomeOtherFragment fragment = new HomeOtherFragment();
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
        answerAdapter = new HomeOtherAdapter(getActivity(), R.layout.item_news);
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
        listView.setOnItemClickListener(this);
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
                if (xRefreshView != null) {
                    xRefreshView.stopRefresh();
                    xRefreshView.stopLoadMore();
                }
                HomeArticleListBean bean = JSON.parseObject(result, HomeArticleListBean.class);
                if (bean.getSuccess() == UrlList.CODE_SUCCESS) {
                    if (page == 1) {
                        dataList.clear();
                    }
                    //给tagId赋值
                    for (ArticleListsBean tmp : bean.getData().getArticle_lists()) {
                        tmp.setTagId(tmp.getID());
                    }
                    dataList.addAll(bean.getData().getArticle_lists());
                    answerAdapter.setData(dataList);
                }
            }

            @Override
            protected void onFailed(String errStr) {
                if (xRefreshView != null) {
                    xRefreshView.stopRefresh();
                    xRefreshView.stopLoadMore();
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("article", dataList.get(i));
        bundle.putInt("news_type", ArticleDetailActivity.NEWS_TYPE_HOME);
        BaseUtils.openActivity(getActivity(), ArticleDetailActivity.class, bundle);
    }

}
