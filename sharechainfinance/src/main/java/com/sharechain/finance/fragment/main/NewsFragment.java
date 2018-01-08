package com.sharechain.finance.fragment.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.andview.refreshview.XRefreshView;
import com.sharechain.finance.BaseFragment;
import com.sharechain.finance.MyStringCallback;
import com.sharechain.finance.R;
import com.sharechain.finance.adapter.NewsListAdapter;
import com.sharechain.finance.bean.ArticleListsBean;
import com.sharechain.finance.bean.HomeArticleListBean;
import com.sharechain.finance.bean.HomeIndexBean;
import com.sharechain.finance.bean.UrlList;
import com.sharechain.finance.module.home.ArticleDetailActivity;
import com.sharechain.finance.utils.BaseUtils;
import com.sharechain.finance.view.MyXRefreshViewHeader;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

import static com.sharechain.finance.bean.ArticleListsBean.CACHE_TYPE_RECOMMENT;

/**
 * Created by Administrator on 2017/12/15.
 */

public class NewsFragment extends BaseFragment implements NewsListAdapter.OnNewsListItemClickListener
        , BGARefreshLayout.BGARefreshLayoutDelegate {

    private static final String NEWS_TYPE_KEY = "NewsTypeKey";
    private static final String NEWS_TYPE_POSITION = "NewsTypePosition";

    @BindView(R.id.news_rv)
    RecyclerView mNewsRV;
    @BindView(R.id.xrefreshview)
    XRefreshView xRefreshView;

    private NewsListAdapter mNewsListAdapter;
    private HomeIndexBean homeIndexBean;
    private List<ArticleListsBean> dataList = new ArrayList<>();
    private List<HomeIndexBean.DataBean.BannerBean> imageList = new ArrayList<>();
    private int curPosition;

    public static NewsFragment newInstance(HomeIndexBean homeIndexBean, int pos) {
        NewsFragment fragment = new NewsFragment();
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
            imageList.clear();
            imageList.addAll(homeIndexBean.getData().getBanner());
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initView() {
        mNewsListAdapter = new NewsListAdapter(getActivity(), dataList, homeIndexBean.getData());
        initXRefreshView(xRefreshView);
        MyXRefreshViewHeader header = new MyXRefreshViewHeader(getActivity());
        header.setBlueBackground(R.color.tint_home_color, R.color.white);
        xRefreshView.setCustomHeaderView(header);
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

        mNewsRV.setHasFixedSize(true);
        mNewsRV.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        mNewsRV.setItemAnimator(new DefaultItemAnimator());
        mNewsListAdapter.setOnItemClickListener(this);
        mNewsRV.setAdapter(mNewsListAdapter);

    }

    @Override
    public void initData() {
        List<ArticleListsBean> cacheList = DataSupport.where("cacheType = ?",
                String.valueOf(CACHE_TYPE_RECOMMENT)).find(ArticleListsBean.class);
        dataList.addAll(cacheList);
        mNewsListAdapter.notifyDataSetChanged();
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
                        //缓存第一页数据
                        DataSupport.deleteAll(ArticleListsBean.class, "cacheType = ?", String.valueOf(CACHE_TYPE_RECOMMENT));
                    }
                    //给tagId赋值
                    for (ArticleListsBean tmp : bean.getData().getArticle_lists()) {
                        tmp.setTagId(tmp.getID());
                        if (page == 1) {
                            tmp.save();
                        }
                    }
                    dataList.addAll(bean.getData().getArticle_lists());
                    mNewsListAdapter.notifyDataSetChanged();
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
    public void onItemClick(View view, int position, boolean isPhoto) {
        if (position > 0) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("article", dataList.get(position - 1));
            bundle.putInt("news_type", ArticleDetailActivity.NEWS_TYPE_HOME);
            BaseUtils.openActivity(getActivity(), ArticleDetailActivity.class, bundle);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mNewsListAdapter != null) {
            mNewsListAdapter.stopAll();
        }
    }

    @Override
    protected void onFragmentStopLazy() {
        super.onFragmentStopLazy();
        if (mNewsListAdapter != null) {
            mNewsListAdapter.waitCircleAll();
        }
    }

    @Override
    protected void onFragmentStartLazy() {
        super.onFragmentStartLazy();
        if (mNewsListAdapter != null) {
            mNewsListAdapter.notifyCircleAll();
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        page = 1;
        getListData(homeIndexBean.getData().getArticle_title_lists().get(curPosition).getTerm_taxonomy_id());
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        page++;
        getListData(homeIndexBean.getData().getArticle_title_lists().get(curPosition).getTerm_taxonomy_id());
        return false;
    }

}
