package com.sharechain.finance.module.mine;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.andview.refreshview.XRefreshView;
import com.orhanobut.logger.Logger;
import com.sharechain.finance.BaseActivity;
import com.sharechain.finance.MyStringCallback;
import com.sharechain.finance.R;
import com.sharechain.finance.adapter.MyNewsAdapter;
import com.sharechain.finance.bean.MyNewsBean;
import com.sharechain.finance.bean.NewsData;
import com.sharechain.finance.bean.UrlList;
import com.sharechain.finance.utils.ToastManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.baseadapter.BGAOnItemChildClickListener;

/**
 * Created by ${zhoutao} on 2017/12/13 0013.
 */

public class MyNewsActivity extends BaseActivity {

    @BindView(R.id.image_title_left)
    ImageView back_Image;
    @BindView(R.id.my_news_lv)
    ListView myNewslv;
    @BindView(R.id.xrefreshview_content)
    XRefreshView refreshView;
    private List<NewsData> newsDataList = new ArrayList<>();
    private String page="1";//页数
    private MyNewsAdapter newsAdapter;

    @Override
    public int getLayout() {
        return R.layout.activity_my_news;
    }

    @Override
    public void initView() {
        initTitle(getString(R.string.my_news));
        back_Image.setVisibility(View.VISIBLE);
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


    }

    @Override
    public void initData() {
        getNews();
    }

    //获取我的消息列表
    private void getNews() {
        final Map<String, String> params = new HashMap<>();
        params.put(pageParam, String.valueOf(UrlList.PAGE));
        requestGet(UrlList.GET_NEWS, params, new MyStringCallback(this) {
            @Override
            protected void onSuccess(String result) {
                Logger.d(result);
                MyNewsBean bean = JSON.parseObject(result, MyNewsBean.class);
                if (bean.getSuccess() == 1) {
                    for (int i = 0; i < bean.getData().size(); i++) {
                        String content = bean.getData().get(i).getContent();
                        String time = bean.getData().get(i).getCreate_time();
                        int id = bean.getData().get(i).getId();

                        NewsData newsData = new NewsData();
                        newsData.setContent(content);
                        newsData.setTime(time);
                        newsData.setId(id);
                        newsDataList.add(newsData);
                    }
                }
                updateAdapter(newsDataList);
            }
            @Override
            protected void onFailed(String errStr) {
                Logger.d(errStr);
            }
        });

    }

    private void updateAdapter(List<NewsData> newsDataList) {
        if (newsAdapter==null){
            newsAdapter = new MyNewsAdapter(this, R.layout.adapter_my_news_item);
            newsAdapter.setData(newsDataList);
            myNewslv.setAdapter(newsAdapter);
            newsAdapter.setOnItemChildClickListener(new BGAOnItemChildClickListener() {
                @Override
                public void onItemChildClick(ViewGroup parent, View childView, int position) {
                    ToastManager.showShort(MyNewsActivity.this, "您点了" + position);
                }
            });

        }else {
            newsAdapter.notifyDataSetChanged();
        }
    }


    @OnClick({R.id.image_title_left})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_title_left:
                finish();
                break;
        }
    }
}
