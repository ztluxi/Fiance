package com.sharechain.finance.module.mogul;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.orhanobut.logger.Logger;
import com.sharechain.finance.BaseActivity;
import com.sharechain.finance.MyStringCallback;
import com.sharechain.finance.R;
import com.sharechain.finance.SFApplication;
import com.sharechain.finance.adapter.MogulAdapter;
import com.sharechain.finance.bean.LoginManagerBean;
import com.sharechain.finance.bean.MogulCircleBean;
import com.sharechain.finance.bean.MogulData;
import com.sharechain.finance.bean.MogulLikeBean;
import com.sharechain.finance.bean.MogulShareBean;
import com.sharechain.finance.bean.UrlList;
import com.sharechain.finance.module.mine.MineActivity;
import com.sharechain.finance.utils.BaseUtils;
import com.sharechain.finance.utils.ToastManager;
import com.sharechain.finance.view.FullLinear;
import com.sharechain.finance.view.dialog.LoadDialog;
import com.sharechain.finance.view.dialog.MogulShareDialog;

import org.json.JSONException;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by ${zhoutao} on 2017/12/22 0022.
 */

public class MogulCircleActivity extends BaseActivity implements MogulAdapter.MyItemClickListener, BGARefreshLayout.BGARefreshLayoutDelegate {

    @BindView(R.id.mogul_circle_rl)
    RecyclerView mogulCircleRl;
    @BindView(R.id.rl_recyclerview_refresh)
    BGARefreshLayout mRefreshLayout;
    @BindView(R.id.no_result_rl)
    RelativeLayout no_result_rl;
    private List<MogulData> mogulDataList = new ArrayList<>();
    private MogulAdapter mogulAdapter;

    private Dialog mDialog;

    @Override
    public int getLayout() {
        return R.layout.activity_mogul_cirle;

    }


    public int PAGE = 1;//页数
    /**
     * 是否关注
     */
    private int focus;
    private int id;
    private String mogul_name;
    private String head;
    private String mogulPosition;
    private MogulData headData;

    @Override
    public void initView() {
        //获取大佬ID
        id = getIntent().getIntExtra("id", 0);
        focus = getIntent().getIntExtra("focus", 0);
        head = getIntent().getStringExtra("head");
        mogulPosition = getIntent().getStringExtra("position");
        mogul_name = getIntent().getStringExtra("name");

        headData = new MogulData();
        headData.setFocus(focus);
        headData.setPosition(mogulPosition);
        headData.setName(mogul_name);
        headData.setHead(head);
        headData.setType(0);
        mogulDataList.add(headData);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(SFApplication.get(this), true));

        FullLinear linear = new FullLinear(this);
        linear.setSmoothScrollbarEnabled(true);
        linear.setAutoMeasureEnabled(true);
        mogulCircleRl.setHasFixedSize(true);
        mogulCircleRl.setNestedScrollingEnabled(false);
        mogulCircleRl.setLayoutManager(linear);

        mDialog = new LoadDialog().LoadProgressDialog(this);
        updateAdapter();
        mDialog.show();
        getMogulDetail();
    }

    @Override
    public void initData() {

    }

    //获取大佬个人中心
    private void getMogulDetail() {
        final Map<String, String> params = new HashMap<>();
        params.put(UrlList.MOGUL_SEARCH_ID, String.valueOf(id));
        params.put(UrlList.LIMIT, String.valueOf(10));
        params.put(UrlList.PAGE_STR, String.valueOf(PAGE));
        requestGet(UrlList.MOGUL_CIRCLE, params, new MyStringCallback(this) {
            @Override
            protected void onSuccess(String result) {
                mRefreshLayout.endRefreshing();
                mRefreshLayout.endLoadingMore();
                Logger.d(result);
                if (PAGE == 1) {
                    mogulDataList.clear();
                    mogulDataList.add(headData);
                }
                MogulCircleBean bean = JSON.parseObject(result, MogulCircleBean.class);
                if (bean.getSuccess() == 1 && bean.getData().getLists().size() != 0) {
                    no_result_rl.setVisibility(View.GONE);
                    mogulCircleRl.setVisibility(View.VISIBLE);
                    if (bean.getData().getLists().size() > 0) {
                        for (int i = 0; i < bean.getData().getLists().size(); i++) {
                            String user_image = bean.getData().getLists().get(i).getProfile_image_url();
                            String create_time = bean.getData().getLists().get(i).getCreate_at();
                            String mogul_name = bean.getData().getLists().get(i).getFull_name();
                            String weibo_name = bean.getData().getLists().get(i).getScreen_name();
                            String positon = bean.getData().getLists().get(i).getProfessional();
                            String text = bean.getData().getLists().get(i).getText();
                            String translate = bean.getData().getLists().get(i).getTranslate_content();
                            int mogul_id = bean.getData().getLists().get(i).getCelebrity_id();
                            int circle_id = bean.getData().getLists().get(i).getId();
                            int fabulous = bean.getData().getLists().get(i).getHits();
                            List<String> imgs = new ArrayList<>();
                            if (bean.getData().getLists().get(i).getImages().size() != 0) {
                                for (int j = 0; j < bean.getData().getLists().get(i).getImages().size(); j++) {
                                    imgs.add(bean.getData().getLists().get(i).getImages().get(j).getUrl());
                                }
                            }
                            MogulData mogulData = new MogulData();
                            mogulData.setName(mogul_name);
                            mogulData.setTranslate(translate);
                            mogulData.setContent(text);
                            mogulData.setPosition(positon);
                            mogulData.setUrlList(imgs);
                            mogulData.setHead(user_image);
                            mogulData.setTime(create_time);
                            mogulData.setWeibo(weibo_name);
                            mogulData.setFabulous(fabulous);
                            mogulData.setId(mogul_id);
                            mogulData.setType(1);
                            mogulData.setMogulCircleID(circle_id);
                            List<MogulLikeBean> likeList = DataSupport.where("mogulID = ?", String.valueOf(circle_id)).find(MogulLikeBean.class);
                            if (likeList.size() > 0) {
                                mogulData.setLike(true);
                            } else {
                                mogulData.setLike(false);
                            }
                            mogulDataList.add(mogulData);
                        }
                    }
                } else {
                    if (PAGE == 1) {
                        ToastManager.showShort(MogulCircleActivity.this, getString(R.string.load_no_data));
                    } else {
                        ToastManager.showShort(MogulCircleActivity.this, getString(R.string.nothing_more_data));
                    }
                    no_result_rl.setVisibility(View.VISIBLE);
                }
                updateAdapter();
            }

            @Override
            protected void onFailed(String errStr) {
                mRefreshLayout.endRefreshing();
                mRefreshLayout.endLoadingMore();
                mogulDataList.add(headData);
                updateAdapter();
                Logger.d(errStr);
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        });

    }

    private void updateAdapter() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        if (mogulAdapter == null) {
            mogulAdapter = new MogulAdapter(this, mogulDataList, 1);
            mogulAdapter.setOnItemClickListener(this);
            mogulCircleRl.setAdapter(mogulAdapter);
        } else {
            mogulAdapter.notifyDataSetChanged();
        }

    }


    @Override
    public void onBGARefreshLayoutBeginRefreshing(final BGARefreshLayout refreshLayout) {
        PAGE = 1;
        getMogulDetail();

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        PAGE++;
        getMogulDetail();
        return false;
    }

    @Override
    public void onFabulous(View view, int position, List<MogulData> list, boolean isLike) {

        addFabulous(position,list.get(position).getId(),list.get(position).getMogulCircleID(), isLike);

    }

    @Override
    public void onShare(View view, int position, List<MogulData> list) {
        MogulShareBean mogulShareBean = new MogulShareBean();
        mogulShareBean.setContent(list.get(position).getContent());
        mogulShareBean.setName(list.get(position).getName());
        mogulShareBean.setPosition(list.get(position).getPosition());
        mogulShareBean.setUrl(list.get(position).getHead());
        mogulShareBean.setWeibo(list.get(position).getWeibo());
        MogulShareDialog mogulShareDialog = new MogulShareDialog(this, mogulShareBean);
        mogulShareDialog.show();
    }

    //点赞
    private void addFabulous(final int position, final int id, final int circleID, final boolean isLike) {
        final Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(id));
        requestGet(UrlList.MOGUL_LIKE, params, new MyStringCallback(this) {
            @Override
            protected void onSuccess(String result) {
                LoginManagerBean bean = JSON.parseObject(result, LoginManagerBean.class);
                if (bean.getSuccess() == 1) {
                    MogulLikeBean likeBean = new MogulLikeBean();
                    if (isLike) {
                        likeBean.setLike(false);
                    } else {
                        likeBean.setLike(true);
                    }
                    likeBean.setMogulID(circleID);
                    likeBean.save();
                    MogulData tmp = mogulAdapter.getmPostList().get(position);
                    if (isLike) {
                        tmp.setLike(false);
                        if (tmp.getFabulous()>0){
                            tmp.setFabulous(tmp.getFabulous() - 1);
                        }
                    } else {
                        tmp.setLike(true);
                        tmp.setFabulous(tmp.getFabulous() + 1);
                    }
                    mogulAdapter.getmPostList().set(position, tmp);
                    mogulAdapter.notifyDataSetChanged();
                } else {
                    ToastManager.showShort(MogulCircleActivity.this, bean.getMsg());
                }
                Logger.d(result);
            }
            @Override
            protected void onFailed(String errStr) {
                Logger.d(errStr);
            }
        });

    }

}
