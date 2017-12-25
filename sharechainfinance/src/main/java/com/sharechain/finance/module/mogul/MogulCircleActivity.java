package com.sharechain.finance.module.mogul;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.andview.refreshview.XRefreshView;
import com.sharechain.finance.BaseActivity;
import com.sharechain.finance.R;
import com.sharechain.finance.adapter.MogulAdapter;
import com.sharechain.finance.bean.MogulData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ${zhoutao} on 2017/12/22 0022.
 */

public class MogulCircleActivity extends BaseActivity {

    @BindView(R.id.back_iv)
    ImageView back_Image;
    @BindView(R.id.mogul_circle_rl)
    RecyclerView mogulCircleRl;
    private List<MogulData> mogulDataList = new ArrayList<>();
    private MogulAdapter mogulAdapter;
    private String[] IMG_URL_LIST = {
            "http://ac-QYgvX1CC.clouddn.com/36f0523ee1888a57.jpg", "http://ac-QYgvX1CC.clouddn.com/07915a0154ac4a64.jpg",
            "http://ac-QYgvX1CC.clouddn.com/9ec4bc44bfaf07ed.jpg", "http://ac-QYgvX1CC.clouddn.com/fa85037f97e8191f.jpg",
            "http://ac-QYgvX1CC.clouddn.com/de13315600ba1cff.jpg", "http://ac-QYgvX1CC.clouddn.com/15c5c50e941ba6b0.jpg",
            "http://ac-QYgvX1CC.clouddn.com/10762c593798466a.jpg", "http://ac-QYgvX1CC.clouddn.com/eaf1c9d55c5f9afd.jpg",
            "http://ac-QYgvX1CC.clouddn.com/ad99de83e1e3f7d4.jpg", "http://ac-QYgvX1CC.clouddn.com/233a5f70512befcc.jpg",
    };

    @Override
    public int getLayout() {
        return R.layout.activity_mogul_cirle;
    }

    @Override
    public void initView() {

        final LinearLayoutManager manager = new LinearLayoutManager(this);
        mogulCircleRl.setLayoutManager(manager);

        mogulAdapter = new MogulAdapter(this, mogulDataList,null);
        mogulCircleRl.setAdapter(mogulAdapter);

    }

    @Override
    public void initData() {
        for (int i = 0; i < 10; i++) {
            MogulData newsData = new MogulData(null);
            newsData.setHead("http://img4.duitang.com/uploads/item/201208/17/20120817123857_NnPNB.thumb.600_0.jpeg");
            newsData.setContent("Never give up" + i);
            newsData.setFabulous("20090" + i);
            newsData.setName("大哥大" + i);
            newsData.setPosition("未来财经ceo");
            newsData.setTime("2017-12-22");
            newsData.setWeibo("黑猫警长");
            List<String> imgUrls = new ArrayList<>();
            imgUrls.addAll(Arrays.asList(IMG_URL_LIST).subList(0, i % 9));
            newsData.setUrlList(imgUrls);
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
