package com.sharechain.finance.fragment.main;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.orhanobut.logger.Logger;
import com.sharechain.finance.BaseFragment;
import com.sharechain.finance.MyStringCallback;
import com.sharechain.finance.R;
import com.sharechain.finance.SFApplication;
import com.sharechain.finance.adapter.MogulAdapter;
import com.sharechain.finance.bean.LoginManagerBean;
import com.sharechain.finance.bean.MainCacheBean;
import com.sharechain.finance.bean.MogulCircleBean;
import com.sharechain.finance.bean.MogulData;
import com.sharechain.finance.bean.MogulLikeBean;
import com.sharechain.finance.bean.MogulShareBean;
import com.sharechain.finance.bean.UrlList;
import com.sharechain.finance.module.mine.MogulFollowSearchActivity;
import com.sharechain.finance.module.mine.MyFollowActivity;
import com.sharechain.finance.utils.BaseUtils;
import com.sharechain.finance.utils.ToastManager;
import com.sharechain.finance.view.dialog.LoadDialog;
import com.sharechain.finance.view.dialog.MogulShareDialog;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by ${zhoutao} on 2017/12/27 0027.
 */

public class FriendCircleFragment extends BaseFragment implements MogulAdapter.MyItemClickListener, BGARefreshLayout.BGARefreshLayoutDelegate {

    @BindView(R.id.image_title_left)
    ImageView back_Image;
    @BindView(R.id.empty_view)
    TextView empty_view;
    @BindView(R.id.image_title_right)
    ImageView searchImage;
    @BindView(R.id.rv_post_list)
    RecyclerView mRvPostLister;
    @BindView(R.id.rl_recyclerview_refresh)
    BGARefreshLayout mRefreshLayout;
    private MogulAdapter mogulAdapter;
    private List<MogulData> mogulDataList = new ArrayList<>();
    public int PAGE = 1;//页数
    //转圈圈的加载框
    private Dialog mDialog;
    private MogulCircleBean bean;

    @Override
    protected int getLayout() {
        return R.layout.fragment_mogul_cirle;
    }

    @Override
    protected void initView() {
        initTitle(getString(R.string.main_tab_friend_circle));

        back_Image.setVisibility(View.VISIBLE);
        back_Image.setImageResource(R.drawable.my_follow);
        searchImage.setVisibility(View.VISIBLE);
        searchImage.setImageResource(R.drawable.search_white);

        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(SFApplication.get(getActivity()), true));
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRvPostLister.setLayoutManager(manager);

        mDialog = new LoadDialog().LoadProgressDialog(getActivity());
    }

    //获取大佬圈列表
    private void getData(int page) {
        final Map<String, String> params = new HashMap<>();
        params.put(UrlList.PAGE_STR, String.valueOf(page));
        params.put(UrlList.LIMIT, String.valueOf(10));
        requestGet(UrlList.MOGUL_CIRCLE, params, new MyStringCallback(getActivity()) {
            @Override
            protected void onSuccess(String result) {
                mRefreshLayout.endRefreshing();
                mRefreshLayout.endLoadingMore();
                bean = JSON.parseObject(result, MogulCircleBean.class);
                //保存首页缓存数据
                DataSupport.deleteAll(MainCacheBean.class, "type = ?", String.valueOf(MainCacheBean.TYPE_FRIEND_CIRCLE));
                MainCacheBean mainCacheBean = new MainCacheBean();
                mainCacheBean.setType(MainCacheBean.TYPE_FRIEND_CIRCLE);
                mainCacheBean.setCacheJson(result);
                mainCacheBean.save();
                updateView();
            }

            @Override
            protected void onFailed(String errStr) {
                mRefreshLayout.endRefreshing();
                mRefreshLayout.endLoadingMore();
                Logger.d(errStr);
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        });
    }

    private void updateView() {
        if (bean != null) {
            if (bean.getSuccess() == 1 && bean.getData().getLists().size() != 0) {
                empty_view.setVisibility(View.GONE);
                mRefreshLayout.setVisibility(View.VISIBLE);
                if (PAGE == 1) {
                    mogulDataList.clear();
                }
                if (bean.getData().getLists().size() > 0) {
                    for (int i = 0; i < bean.getData().getLists().size(); i++) {
                        String user_image = bean.getData().getLists().get(i).getProfile_image_url();
                        String create_time = bean.getData().getLists().get(i).getCreate_at();
                        String mogul_name = bean.getData().getLists().get(i).getFull_name();
                        String weibo_name = bean.getData().getLists().get(i).getScreen_name();
                        String positon = bean.getData().getLists().get(i).getProfessional();
                        String text = bean.getData().getLists().get(i).getText();
                        String translate = bean.getData().getLists().get(i).getTranslate_content();
                        int fabulous = bean.getData().getLists().get(i).getHits();
                        int circleID = bean.getData().getLists().get(i).getId();

                        int mogul_id = bean.getData().getLists().get(i).getCelebrity_id();
                        List<String> imgs = new ArrayList<>();
                        if (bean.getData().getLists().get(i).getImages().size() != 0) {
                            for (int j = 0; j < bean.getData().getLists().get(i).getImages().size(); j++) {
                                imgs.add(bean.getData().getLists().get(i).getImages().get(j).getUrl());
                            }
                        }
                        MogulData mogulData = new MogulData();
                        mogulData.setName(mogul_name);
                        mogulData.setTranslate(null);
                        mogulData.setContent(text);
                        mogulData.setPosition(positon);
                        mogulData.setUrlList(null);
                        mogulData.setHead(user_image);
                        mogulData.setTime(create_time);
                        mogulData.setWeibo(weibo_name);
                        mogulData.setFabulous(fabulous);
                        mogulData.setId(mogul_id);
                        mogulData.setUrlList(imgs);
                        mogulData.setTranslate(translate);
                        mogulData.setType(1);
                        mogulData.setMogulCircleID(circleID);
                        List<MogulLikeBean> likeList = DataSupport.where("mogulID = ?", String.valueOf(circleID)).find(MogulLikeBean.class);
                        if (likeList.size() > 0) {
                            mogulData.setLike(true);
                        } else {
                            mogulData.setLike(false);
                        }
                        mogulDataList.add(mogulData);

                    }
                }
            } else {
                if (PAGE != 1) {
                    ToastManager.showShort(getActivity(), getString(R.string.nothing_more_data));
                }
                empty_view.setVisibility(View.VISIBLE);
                mRefreshLayout.setVisibility(View.GONE);
            }
        }
        updateAdapter(mogulDataList.size());

    }

    @Override
    public void initData() {
        //json缓存
        List<MainCacheBean> newsCache = DataSupport.where("type = ?", String.valueOf(MainCacheBean.TYPE_FRIEND_CIRCLE)).find(MainCacheBean.class);
        if (newsCache.size() > 0) {
            String cacheJson = newsCache.get(0).getCacheJson();
            bean = JSON.parseObject(cacheJson, MogulCircleBean.class);
        }
        updateView();
        mDialog.show();
        getData(PAGE);
    }

    @OnClick({R.id.image_title_left, R.id.image_title_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_title_left:
                BaseUtils.openActivity(getActivity(), MyFollowActivity.class, null);
                break;
            case R.id.image_title_right:
                Intent intentMogul = new Intent(getActivity(), MogulFollowSearchActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putInt("type", 2);
                intentMogul.putExtras(bundle1);
                BaseUtils.openActivity(getActivity(), MogulFollowSearchActivity.class, bundle1);
                break;
        }
    }

    //点赞
    private void addFabulous(final int id, final int circleID, final boolean isLike, final int position) {
        final Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(id));
        requestGet(UrlList.MOGUL_LIKE, params, new MyStringCallback(getActivity()) {
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
                    boolean isSave = likeBean.save();
                    MogulData tmp = mogulAdapter.getmPostList().get(position);
                    if (isLike) {
                        tmp.setLike(false);
                        tmp.setFabulous(tmp.getFabulous() - 1);
                    } else {
                        tmp.setLike(true);
                        tmp.setFabulous(tmp.getFabulous() + 1);
                    }
                    mogulAdapter.getmPostList().set(position, tmp);
                    mogulAdapter.notifyDataSetChanged();
                } else {
                    ToastManager.showShort(getActivity(), bean.getMsg());
                }
                Logger.d(result);
            }

            @Override
            protected void onFailed(String errStr) {
                Logger.d(errStr);
            }
        });

    }


    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        PAGE = 1;
        getData(PAGE);
    }


    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        PAGE++;
        getData(PAGE);
        return false;
    }

    private void updateAdapter(int position) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        if (mogulAdapter == null) {
            mogulAdapter = new MogulAdapter(getActivity(), mogulDataList, 0);
            mogulAdapter.setOnItemClickListener(this);
            mRvPostLister.setAdapter(mogulAdapter);
        } else {
            mogulAdapter.notifyItemChanged(position);
        }

    }

    //点赞
    @Override
    public void onFabulous(View view, int position, List<MogulData> list, boolean isLike) {

        addFabulous(list.get(position).getId(), list.get(position).getMogulCircleID(), isLike, position);

    }

    @Override
    public void onShare(View view, int position, List<MogulData> list) {
        MogulShareBean mogulShareBean = new MogulShareBean();
        mogulShareBean.setContent(list.get(position).getContent());
        mogulShareBean.setName(list.get(position).getName());
        mogulShareBean.setPosition(list.get(position).getPosition());
        mogulShareBean.setUrl(list.get(position).getHead());
        mogulShareBean.setWeibo(list.get(position).getWeibo());
        MogulShareDialog mogulShareDialog = new MogulShareDialog(getActivity(), mogulShareBean);
        mogulShareDialog.show();
    }

}
