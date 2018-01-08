package com.sharechain.finance.module.mine;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.andview.refreshview.XRefreshView;
import com.orhanobut.logger.Logger;
import com.sharechain.finance.BaseActivity;
import com.sharechain.finance.MyStringCallback;
import com.sharechain.finance.R;
import com.sharechain.finance.SFApplication;
import com.sharechain.finance.adapter.MyFollowAdapter;
import com.sharechain.finance.adapter.MyNewsAdapter;
import com.sharechain.finance.bean.FollowBean;
import com.sharechain.finance.bean.FollowData;
import com.sharechain.finance.bean.MogulCircleBean;
import com.sharechain.finance.bean.MyFollowBean;
import com.sharechain.finance.bean.MyNewsBean;
import com.sharechain.finance.bean.NewsData;
import com.sharechain.finance.bean.UrlList;
import com.sharechain.finance.module.home.BaseWebViewActivity;
import com.sharechain.finance.module.mogul.MogulCircleActivity;
import com.sharechain.finance.utils.BaseUtils;
import com.sharechain.finance.utils.ToastManager;
import com.sharechain.finance.view.dialog.LoadDialog;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.baseadapter.BGAOnItemChildClickListener;

/**
 * 我的关注列表
 * Created by ${zhoutao} on 2017/12/21 0013.
 */

public class MyFollowActivity extends BaseActivity implements MyFollowAdapter.MyItemClickListener {

    @BindView(R.id.image_title_left)
    ImageView back_Image;
    @BindView(R.id.image_title_right)
    ImageView searchIV;
    @BindView(R.id.my_news_lv)
    ListView myNewslv;
    @BindView(R.id.empty_view)
    TextView empty_view;
    @BindView(R.id.xrefreshview_content)
    XRefreshView refreshView;
    private List<FollowData> followDataList = new ArrayList<>();
    private MyFollowAdapter followAdapter;

    private Dialog mDialog;
    public int PAGE = 1;//页数

    @Override
    public int getLayout() {
        return R.layout.activity_my_follow;
    }

    @Override
    public void initView() {
        initTitle(getString(R.string.my_follow));
        back_Image.setVisibility(View.VISIBLE);
        searchIV.setVisibility(View.VISIBLE);
        searchIV.setImageResource(R.drawable.search_white);

        initXRefreshView(refreshView);
        refreshView.setPullRefreshEnable(true);
        refreshView.setPullLoadEnable(true);

        refreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
                PAGE = 1;
                getFollow(PAGE);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
                PAGE++;
                getFollow(PAGE);
            }
        });

        mDialog = new LoadDialog().LoadProgressDialog(this);
        initListen();
    }

    private void updateAdapter(int position) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        if (followAdapter == null) {
            followAdapter = new MyFollowAdapter(this, R.layout.adapter_my_follow_item);
            followAdapter.setMyItemClickLister(this);
            followAdapter.setData(followDataList);
            myNewslv.setAdapter(followAdapter);
        } else {
            followAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void initData() {
        mDialog.show();
        getFollow(PAGE);
    }


    private void initListen() {

        myNewslv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", followDataList.get(i).getMogulID());
                bundle.putString("head", followDataList.get(i).getImage());
                bundle.putString("name", followDataList.get(i).getName());
                bundle.putString("position", followDataList.get(i).getPosition());
                bundle.putInt("focus", followDataList.get(i).getState());
                BaseUtils.openActivity(MyFollowActivity.this, MogulCircleActivity.class, bundle);
            }
        });

    }


    //获取我的关注列表
    private void getFollow(int page) {
        if (SFApplication.loginDataBean != null) {
            String token = SFApplication.loginDataBean.getToken();
            final Map<String, String> params = new HashMap<>();
            params.put(UrlList.TOKEN, token);
            params.put(UrlList.PAGE_STR, String.valueOf(page));
            requestGet(UrlList.GET_MY_FOLLOW, params, new MyStringCallback(this) {
                @Override
                protected void onSuccess(String result) {
                    refreshView.stopRefresh();
                    refreshView.stopLoadMore();
                    Logger.d(result);
                    FollowBean myFollowBean = JSON.parseObject(result, FollowBean.class);
                    if (myFollowBean.getSuccess() == 1 && myFollowBean.getData().size() != 0) {
                        empty_view.setVisibility(View.GONE);
                        refreshView.setVisibility(View.VISIBLE);
                        if (PAGE == 1) {
                            followDataList.clear();
                        }
                        if (myFollowBean.getData().size() != 0) {
                            for (int i = 0; i < myFollowBean.getData().size(); i++) {
                                FollowData followData = new FollowData();
                                followData.setPosition(myFollowBean.getData().get(i).getProfessional());
                                followData.setWeibo(myFollowBean.getData().get(i).getScreen_name());
                                followData.setName(myFollowBean.getData().get(i).getFull_name());
                                followData.setImage(myFollowBean.getData().get(i).getProfile_image_url());
                                followData.setMogulID(myFollowBean.getData().get(i).getCelebrity_id());
                                followData.setState(myFollowBean.getData().get(i).getState());
                                followDataList.add(followData);
                            }
                        }
                    } else {
                        if (PAGE != 1) {
                            ToastManager.showShort(MyFollowActivity.this, getString(R.string.nothing_more_data));
                        }
                        empty_view.setVisibility(View.VISIBLE);
                        refreshView.setVisibility(View.GONE);
                    }
                    updateAdapter(0);
                }

                @Override
                protected void onFailed(String errStr) {
                    refreshView.stopRefresh();
                    refreshView.stopLoadMore();
                    Logger.d(errStr);
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                }
            });
        } else {
            refreshView.stopRefresh();
            refreshView.stopLoadMore();
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
            ToastManager.showShort(this, getString(R.string.please_login));
            startActivity(new Intent(this, MineActivity.class));
        }
    }

    //取消大佬关注

    private void cancelMogulFollow(int mogulID, final int position, final FollowData data) {
        if (SFApplication.loginDataBean != null) {
            String token = SFApplication.loginDataBean.getToken();
            mDialog.show();
            final Map<String, String> params = new HashMap<>();
            params.put(UrlList.TOKEN, token);
            params.put(UrlList.MOGUL_CANCLE_FOLLOW_ID, String.valueOf(mogulID));
            requestGet(UrlList.CANCLE_FOLLOW, params, new MyStringCallback(this) {
                @Override
                protected void onSuccess(String result) {
                    FollowData data1 = new FollowData();
                    data1.setState(2);
                    data1.setMogulID(data.getMogulID());
                    data1.setImage(data.getImage());
                    data1.setName(data.getName());
                    data1.setPosition(data.getPosition());
                    data1.setWeibo(data.getWeibo());
                    followDataList.set(position, data1);
                    updateAdapter(position);
                    ToastManager.showShort(MyFollowActivity.this, getString(R.string.you_cancel) + data.getName() + getString(R.string.de_follow));
                    Logger.d(result);
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                }

                @Override
                protected void onFailed(String errStr) {
                    Logger.d(errStr);
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                }
            });
        } else {
            ToastManager.showShort(this, getString(R.string.please_login));
            startActivity(new Intent(this, MineActivity.class));
        }
    }

    //关注大佬
    private void addMogulFollow(int mogulID, final int position, final FollowData data) {
        if (SFApplication.loginDataBean != null) {
            String token = SFApplication.loginDataBean.getToken();
            mDialog.show();
            final Map<String, String> params = new HashMap<>();
            params.put(UrlList.TOKEN, token);
            params.put(UrlList.MOGUL_CANCLE_FOLLOW_ID, String.valueOf(mogulID));
            requestGet(UrlList.MOGUL_FOLLOW, params, new MyStringCallback(this) {
                @Override
                protected void onSuccess(String result) {
//                {"success":0,"msg":"请登录","data":""}
//                    try {
//                        org.json.JSONObject object = new org.json.JSONObject(result);
//                        int success = object.getInt("success");
//                        String messg = object.getString("msg");
//                        if (success == 0) {
//                            ToastManager.showShort(MyFollowActivity.this, messg);
//                        } else {
                    FollowData data1 = new FollowData();
                    data1.setState(1);
                    data1.setMogulID(data.getMogulID());
                    data1.setImage(data.getImage());
                    data1.setName(data.getName());
                    data1.setPosition(data.getPosition());
                    data1.setWeibo(data.getWeibo());
                    followDataList.set(position, data1);
                    updateAdapter(position);
                    ToastManager.showShort(MyFollowActivity.this, getString(R.string.you_follow) + data.getName());
                    Logger.d(result);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                }

                @Override
                protected void onFailed(String errStr) {
                    Logger.d(errStr);
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                }
            });
        } else {
            ToastManager.showShort(this, getString(R.string.please_login));
            startActivity(new Intent(this, MineActivity.class));
        }
    }


    @OnClick({R.id.image_title_left, R.id.image_title_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_title_left:
                finish();
                break;
            case R.id.image_title_right:
                Intent intent = new Intent(this, MogulFollowSearchActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("type", 1);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void cancelFollow(View view, int position, FollowData data) {
        if (data.getState() == 1) {
            cancelMogulFollow(data.getMogulID(), position, data);
        } else {
            addMogulFollow(data.getMogulID(), position, data);
        }
    }
}
