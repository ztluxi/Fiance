package com.sharechain.finance.fragment.main;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.andview.refreshview.XRefreshView;
import com.sharechain.finance.BaseFragment;
import com.sharechain.finance.MyStringCallback;
import com.sharechain.finance.R;
import com.sharechain.finance.SFApplication;
import com.sharechain.finance.adapter.FastMsgAdapter;
import com.sharechain.finance.bean.BaseNotifyBean;
import com.sharechain.finance.bean.FastMsgBean;
import com.sharechain.finance.bean.FastMsgData;
import com.sharechain.finance.bean.MainCacheBean;
import com.sharechain.finance.bean.UrlList;
import com.sharechain.finance.module.home.BaseWebViewActivity;
import com.sharechain.finance.utils.BaseUtils;
import com.sharechain.finance.view.FastMsgDialog;
import com.sharechain.finance.view.MyXRefreshViewHeader;

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
    @BindView(R.id.first_title_tv)
    TextView first_title_tv;


    private FastMsgAdapter adapter;
    private List<FastMsgData> dataList = new ArrayList<>();
    private FastMsgBean.DataBean.ListBean headData;
    private FastMsgBean bean;
    private String time = "";//第一条星期时间

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
        MyXRefreshViewHeader header = new MyXRefreshViewHeader(getActivity());
        header.setBlueBackground(R.color.base_bg_color, R.color.colorBlack);
        xRefreshView.setCustomHeaderView(header);
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
                if (i < dataList.size() && dataList.get(i).getType() == FastMsgData.CHILD_TYPE) {
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
        if (SFApplication.shareData != null) {
            new FastMsgDialog(getActivity(), SFApplication.shareData).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(BaseNotifyBean event) {
        if (event.getType() == BaseNotifyBean.TYPE.TYPE_SHARE_RESULT) {
            //分享回调
        } else if (event.getType() == BaseNotifyBean.TYPE.TYPE_SCROLL_MSG) {
            //弹出分享对话框
            FastMsgData getData = (FastMsgData) event.getObj();
            new FastMsgDialog(getActivity(), getData).show();
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
                        time = bean.getData().get(0).getTime();
                        text_big_news.setText(headData.getText());
                        int type = headData.getType();
                        switch (type) {
                            case 1:
                                first_title_tv.setText(getString(R.string.fastmsg_normal));
                                break;
                            case 2:
                                first_title_tv.setText(getString(R.string.fastmsg_big));
                                break;
                            case 3:
                                first_title_tv.setText(getString(R.string.fastmsg_important));
                                break;
                        }
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
                        childData.setId(parentBean.getList().get(j).getId());
                        childData.setSectionText(sectionText);//日期
                        childData.setType(FastMsgData.CHILD_TYPE);//子item
                        childData.setDataText(content);//内容
                        childData.setMsgType(parentBean.getList().get(j).getType());//消息类型
                        childData.setTitle(parentBean.getList().get(j).getTitle());//标题
                        childData.setHour(parentBean.getList().get(j).getHour());//时间
                        childData.setUrl(parentBean.getList().get(j).getUrl());//url
                        childData.setSource(parentBean.getList().get(j).getSource());//来源
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
            FastMsgData fastMsgData = new FastMsgData();
            fastMsgData.setMsgType(headData.getType());
            fastMsgData.setDataText(headData.getText());
            fastMsgData.setTitle(headData.getTitle());
            fastMsgData.setUrl(headData.getUrl());
            fastMsgData.setId(headData.getId());
            fastMsgData.setHour(headData.getHour());
            fastMsgData.setSectionText(time);

            new FastMsgDialog(getActivity(), fastMsgData).show();
//            Bundle bundle = new Bundle();
//            bundle.putString("web_url", headData.getUrl());
//            BaseUtils.openActivity(getActivity(), BaseWebViewActivity.class, bundle);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
