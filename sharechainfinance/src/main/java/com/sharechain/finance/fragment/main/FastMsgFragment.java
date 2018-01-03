package com.sharechain.finance.fragment.main;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.andview.refreshview.XRefreshView;
import com.sharechain.finance.BaseFragment;
import com.sharechain.finance.MyStringCallback;
import com.sharechain.finance.R;
import com.sharechain.finance.adapter.FastMsgAdapter;
import com.sharechain.finance.bean.BaseNotifyBean;
import com.sharechain.finance.bean.FastMsgBean;
import com.sharechain.finance.bean.FastMsgData;
import com.sharechain.finance.bean.MainCacheBean;
import com.sharechain.finance.bean.UrlList;
import com.sharechain.finance.module.home.BaseWebViewActivity;
import com.sharechain.finance.module.mine.MineActivity;
import com.sharechain.finance.utils.BaseUtils;
import com.sharechain.finance.utils.ToastManager;
import com.sharechain.finance.view.FastMsgDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.halfbit.pinnedsection.PinnedSectionListView;

/**
 * Created by Administrator on 2017/12/13.
 */

public class FastMsgFragment extends BaseFragment {
    @BindView(R.id.xrefreshview)
    XRefreshView xRefreshView;
    @BindView(R.id.listView)
    PinnedSectionListView listView;
    @BindView(R.id.text_big_news)
    TextView text_big_news;

    private FastMsgAdapter adapter;
    private List<FastMsgData> dataList = new ArrayList<>();
    private FastMsgBean.DataBean.ListBean headData;
    private FastMsgBean bean;

    @Override
    protected int getLayout() {
        return R.layout.fragment_fast_msg;
    }

    @Override
    protected void initView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initXRefreshView(xRefreshView);
        xRefreshView.setPullRefreshEnable(true);
        xRefreshView.setPullLoadEnable(true);
        xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
                page = 1;
                getDetail();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
                page++;
                getDetail();
            }

        });

        adapter = new FastMsgAdapter(getActivity(), dataList);
        adapter.setListData(dataList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i < dataList.size() && dataList.get(i).getType() == FastMsgData.CHILD_TYPE &&
                        !BaseUtils.isEmpty(dataList.get(i).getUrl())) {
                    new FastMsgDialog(getActivity(), dataList.get(i)).show();
                }
            }
        });
    }

    @Override
    public void initData() {
        //json缓存
        List<MainCacheBean> newsCache = DataSupport.where("type = ?", String.valueOf(MainCacheBean.TYPE_FAST_MSG)).find(MainCacheBean.class);
        if (newsCache.size() > 0) {
            String cacheJson = newsCache.get(0).getCacheJson();
            bean = JSON.parseObject(cacheJson, FastMsgBean.class);
        }
        updateView();
        getDetail();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(BaseNotifyBean event) {
        if (event.getType() == BaseNotifyBean.TYPE.TYPE_SHARE_RESULT) {
            Snackbar.make(findViewById(R.id.cl_root), event.getMessage(), Snackbar.LENGTH_SHORT).show();
        }
    }

    private void getDetail() {
        final Map<String, String> params = new HashMap<>();
        params.put(pageParam, String.valueOf(page));
        requestGet(UrlList.MSG_GET_LIST, params, new MyStringCallback(getActivity()) {
            @Override
            protected void onSuccess(String result) {
                xRefreshView.stopRefresh();
                xRefreshView.stopLoadMore();
                bean = JSON.parseObject(result, FastMsgBean.class);
                //保存缓存数据
                DataSupport.deleteAll(MainCacheBean.class, "type = ?", String.valueOf(MainCacheBean.TYPE_FAST_MSG));
                MainCacheBean mainCacheBean = new MainCacheBean();
                mainCacheBean.setType(MainCacheBean.TYPE_FAST_MSG);
                mainCacheBean.setCacheJson(result);
                mainCacheBean.save();
                updateView();
            }

            @Override
            protected void onFailed(String errStr) {
                xRefreshView.stopRefresh();
                xRefreshView.stopLoadMore();
            }
        });
    }

    private void updateView() {
        if (bean != null) {
            if (bean.isSuccess()) {
                if (page == 1) {
                    dataList.clear();
                    //取出头部数据
                    if (bean.getData().size() > 0 && bean.getData().get(0).getList().size() > 0) {
                        headData = bean.getData().get(0).getList().get(0);
                        text_big_news.setText(headData.getText());
                    }
                }
                for (int i = 0; i < bean.getData().size(); i++) {
                    FastMsgBean.DataBean parentBean = bean.getData().get(i);
                    String sectionText = parentBean.getTime();
                    FastMsgData groupData = new FastMsgData();
                    groupData.setType(FastMsgData.PARENT_TYPE);
                    groupData.setSectionText(sectionText);
                    dataList.add(groupData);
                    for (int j = 0; j < parentBean.getList().size(); j++) {
                        if (page == 1 && i == 0 && j == 0) {
                            continue;
                        }
                        String content = parentBean.getList().get(j).getText();
                        FastMsgData childData = new FastMsgData();
                        childData.setSectionText(sectionText);//日期
                        childData.setType(FastMsgData.CHILD_TYPE);//子item
                        childData.setDataText(content);//内容
                        childData.setMsgType(parentBean.getList().get(j).getType());//消息类型
                        childData.setTitle(parentBean.getList().get(j).getTitle());//标题
                        childData.setHour(parentBean.getList().get(j).getHour());//时间
                        childData.setUrl(parentBean.getList().get(j).getUrl());//url
                        dataList.add(childData);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    @OnClick(R.id.btn_view_detail)
    void viewDetail() {
        if (headData != null) {
            Bundle bundle = new Bundle();
            bundle.putString("web_url", headData.getUrl());
            BaseUtils.openActivity(getActivity(), BaseWebViewActivity.class, bundle);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
