package com.sharechain.finance.fragment.main;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.andview.refreshview.XRefreshView;
import com.sharechain.finance.BaseFragment;
import com.sharechain.finance.R;
import com.sharechain.finance.adapter.MogulAdapter;
import com.sharechain.finance.bean.MogulData;
import com.sharechain.finance.module.mine.MyFollowActivity;
import com.sharechain.finance.module.mine.SearchActivity;
import com.sharechain.finance.module.mogul.MogulCircleActivity;
import com.sharechain.finance.utils.BaseUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.baseadapter.BGAOnItemChildClickListener;

/**
 * Created by Administrator on 2017/12/13.
 */

public class FriendCircleFragment extends BaseFragment {

    @BindView(R.id.image_title_left)
    ImageView back_Image;
    @BindView(R.id.image_title_right)
    ImageView searchImage;
    @BindView(R.id.mogul_lv)
    ListView mogulLv;
    @BindView(R.id.xrefreshview_content)
    XRefreshView refreshView;
    private List<MogulData> mogulDataList = new ArrayList<>();


    private MogulAdapter mogulAdapter;
    @Override
    protected int getLayout() {
        return R.layout.fragment_mogul_cirle;
    }

    @Override
    protected void initView() {
        initTitle(getString(R.string.main_tab_friend_circle));
        immersionBar.statusBarColor(android.R.color.transparent).statusBarDarkFont(true).init();
        back_Image.setVisibility(View.VISIBLE);
        back_Image.setImageResource(R.drawable.my_follow);
        searchImage.setVisibility(View.VISIBLE);
        searchImage.setImageResource(R.drawable.search_white);
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
        mogulAdapter = new MogulAdapter(getActivity(), R.layout.adapter_my_mogul_item);
        mogulAdapter.setOnItemChildClickListener(new BGAOnItemChildClickListener() {
            @Override
            public void onItemChildClick(ViewGroup parent, View childView, int position) {
                BaseUtils.openActivity(getActivity(), MogulCircleActivity.class,null);
//                ToastManager.showShort(getActivity(), "您点了" + position);
            }
        });
        mogulAdapter.setData(mogulDataList);
        mogulLv.setAdapter(mogulAdapter);

    }
    private String[] IMG_URL_LIST = {
            "http://ac-QYgvX1CC.clouddn.com/36f0523ee1888a57.jpg", "http://ac-QYgvX1CC.clouddn.com/07915a0154ac4a64.jpg",
            "http://ac-QYgvX1CC.clouddn.com/9ec4bc44bfaf07ed.jpg", "http://ac-QYgvX1CC.clouddn.com/fa85037f97e8191f.jpg",
            "http://ac-QYgvX1CC.clouddn.com/de13315600ba1cff.jpg", "http://ac-QYgvX1CC.clouddn.com/15c5c50e941ba6b0.jpg",
            "http://ac-QYgvX1CC.clouddn.com/10762c593798466a.jpg", "http://ac-QYgvX1CC.clouddn.com/eaf1c9d55c5f9afd.jpg",
            "http://ac-QYgvX1CC.clouddn.com/ad99de83e1e3f7d4.jpg", "http://ac-QYgvX1CC.clouddn.com/233a5f70512befcc.jpg",
    };
    List<String> imgUrls = new ArrayList<>();
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && immersionBar != null) {
            immersionBar.statusBarColor(android.R.color.transparent).statusBarDarkFont(true).init();
        }

    }


    @Override
    public void initData() {
        for (int i = 0; i < 10; i++) {
            MogulData newsData = new MogulData();
            newsData.setHead("http://img4.duitang.com/uploads/item/201208/17/20120817123857_NnPNB.thumb.600_0.jpeg");
            newsData.setContent("Never give up");
            newsData.setFabulous("20090");
            newsData.setName("大哥大");
            newsData.setPosition("未来财经ceo");
            newsData.setTime("2017-12-22");
            newsData.setWeibo("黑猫警长");
             imgUrls.add(IMG_URL_LIST[2]);
             imgUrls.add(IMG_URL_LIST[4]);
            newsData.setUrlList(imgUrls);
            mogulDataList.add(newsData);
        }

    }
    @OnClick({R.id.image_title_left,R.id.image_title_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_title_left:
                BaseUtils.openActivity(getActivity(), MyFollowActivity.class,null);
                break;
            case R.id.image_title_right:
                BaseUtils.openActivity(getActivity(), SearchActivity.class,null);
                break;
        }
    }

}
