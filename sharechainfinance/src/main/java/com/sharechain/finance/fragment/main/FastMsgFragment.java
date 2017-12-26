package com.sharechain.finance.fragment.main;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSON;
import com.andview.refreshview.XRefreshView;
import com.sharechain.finance.BaseFragment;
import com.sharechain.finance.MyStringCallback;
import com.sharechain.finance.R;
import com.sharechain.finance.adapter.FastMsgAdapter;
import com.sharechain.finance.bean.BaseNotifyBean;
import com.sharechain.finance.bean.FastMsgBean;
import com.sharechain.finance.bean.FastMsgData;
import com.sharechain.finance.bean.UrlList;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import de.halfbit.pinnedsection.PinnedSectionListView;

/**
 * Created by Administrator on 2017/12/13.
 */

public class FastMsgFragment extends BaseFragment {
    @BindView(R.id.xrefreshview)
    XRefreshView xRefreshView;
    @BindView(R.id.listView)
    PinnedSectionListView listView;

    private FastMsgAdapter adapter;
    private List<FastMsgData> dataList = new ArrayList<>();

    @Override
    protected int getLayout() {
        return R.layout.fragment_fast_msg;
    }

    @Override
    protected void initView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initTitle(getString(R.string.main_tab_fast_msg));
        rl_base_layout.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.transparent));
        view_status_bar.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.transparent));
        text_title.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        view_line.setVisibility(View.GONE);
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
                if (i < dataList.size() && dataList.get(i).getType() == FastMsgData.CHILD_TYPE) {

                }
            }
        });
    }

    @Override
    public void initData() {
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
                FastMsgBean bean = JSON.parseObject(result, FastMsgBean.class);
                if (bean.isSuccess()) {
                    if (page == 1) {
                        dataList.clear();
                    }
                    for (int i = 0; i < bean.getData().size(); i++) {
                        FastMsgBean.DataBean parentBean = bean.getData().get(i);
                        String sectionText = parentBean.getTime();
                        FastMsgData groupData = new FastMsgData();
                        groupData.setType(FastMsgData.PARENT_TYPE);
                        groupData.setSectionText(sectionText);
                        dataList.add(groupData);
                        for (int j = 0; j < parentBean.getList().size(); j++) {
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

            @Override
            protected void onFailed(String errStr) {
                xRefreshView.stopRefresh();
                xRefreshView.stopLoadMore();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
