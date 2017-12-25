package com.sharechain.finance.fragment.main;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.andview.refreshview.XRefreshView;
import com.sharechain.finance.BaseFragment;
import com.sharechain.finance.R;
import com.sharechain.finance.adapter.FastMsgAdapter;
import com.sharechain.finance.bean.BaseNotifyBean;
import com.sharechain.finance.bean.FastMsgData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

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
        xRefreshView.setPullLoadEnable(false);
        xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
                xRefreshView.stopRefresh();
            }

        });

        adapter = new FastMsgAdapter(getActivity(), dataList);
        adapter.setListData(dataList);
        listView.setAdapter(adapter);
    }

    @Override
    public void initData() {
        for (int i = 0; i < 5; i++) {
            String sectionText = "2017-12-18";
            FastMsgData groupData = new FastMsgData();
            groupData.setType(FastMsgData.PARENT_TYPE);
            groupData.setSectionText(sectionText);
            dataList.add(groupData);
            for (int j = 0; j < 10; j++) {
                String content = "比特币价格突破19000美元，最高刷新至19500美元....................";
                FastMsgData childData = new FastMsgData();
                childData.setType(FastMsgData.CHILD_TYPE);
                childData.setDataText(content);
                dataList.add(childData);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(BaseNotifyBean event) {
        if (event.getType() == BaseNotifyBean.TYPE.TYPE_SHARE_RESULT) {
            Snackbar.make(findViewById(R.id.cl_root), event.getMessage(), Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
