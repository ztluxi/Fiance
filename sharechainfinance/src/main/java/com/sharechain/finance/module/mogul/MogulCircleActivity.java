package com.sharechain.finance.module.mogul;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.andview.refreshview.XRefreshView;
import com.sharechain.finance.BaseActivity;
import com.sharechain.finance.R;
import com.sharechain.finance.adapter.MogulAdapter;
import com.sharechain.finance.bean.MogulData;
import com.sharechain.finance.utils.ToastManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.baseadapter.BGAOnItemChildClickListener;

/**
 * Created by ${zhoutao} on 2017/12/22 0022.
 */

public class MogulCircleActivity extends BaseActivity {

    @BindView(R.id.back_iv)
    ImageView back_Image;
    @BindView(R.id.mogul_lv)
    ListView mogulLv;
    @BindView(R.id.xrefreshview_content)
    XRefreshView refreshView;
    private List<MogulData> mogulDataList = new ArrayList<>();


    private MogulAdapter mogulAdapter;

    @Override
    public int getLayout() {
        return R.layout.activity_mogul_cirle;
    }

    @Override
    public void initView() {
        initData();
        initXRefreshView(refreshView);
        refreshView.setPullRefreshEnable(true);
        refreshView.setPullLoadEnable(false);
        refreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
                refreshView.stopRefresh();
            }
        });
        mogulAdapter = new MogulAdapter(this, R.layout.adapter_my_mogul_item);
        mogulAdapter.setOnItemChildClickListener(new BGAOnItemChildClickListener() {
            @Override
            public void onItemChildClick(ViewGroup parent, View childView, int position) {
                ToastManager.showShort(MogulCircleActivity.this, "您点了" + position);
            }
        });

        mogulAdapter.setData(mogulDataList);
        mogulLv.setAdapter(mogulAdapter);

    }

    @Override
    public void initData() {
        for (int i = 0; i < 10; i++) {
            MogulData newsData = new MogulData();
            newsData.setImage("http://img4.duitang.com/uploads/item/201208/17/20120817123857_NnPNB.thumb.600_0.jpeg");
            newsData.setHead("http://img4.duitang.com/uploads/item/201208/17/20120817123857_NnPNB.thumb.600_0.jpeg");
            newsData.setContent("Never give up");
            newsData.setFabulous("20090");
            newsData.setName("大哥大");
            newsData.setPosition("未来财经ceo");
            newsData.setTime("2017-12-22");
            newsData.setWeibo("黑猫警长");
            mogulDataList.add(newsData);
        }

    }

    @OnClick({R.id.back_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
        }
    }
}
