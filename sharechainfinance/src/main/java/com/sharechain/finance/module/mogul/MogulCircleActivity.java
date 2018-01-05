package com.sharechain.finance.module.mogul;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.orhanobut.logger.Logger;
import com.sharechain.finance.BaseActivity;
import com.sharechain.finance.MyStringCallback;
import com.sharechain.finance.R;
import com.sharechain.finance.SFApplication;
import com.sharechain.finance.adapter.MogulAdapter;
import com.sharechain.finance.bean.MogulCircleBean;
import com.sharechain.finance.bean.MogulData;
import com.sharechain.finance.bean.MogulShareBean;
import com.sharechain.finance.bean.UrlList;
import com.sharechain.finance.module.mine.MineActivity;
import com.sharechain.finance.utils.BaseUtils;
import com.sharechain.finance.utils.ToastManager;
import com.sharechain.finance.view.FullLinear;
import com.sharechain.finance.view.dialog.LoadDialog;
import com.sharechain.finance.view.dialog.MogulShareDialog;

import org.json.JSONException;

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
    //    @BindView(R.id.back_iv)
//    ImageView back_Image;
//    @BindView(R.id.mogul_top_name_tv)
//    TextView mogul_top_name_tv;
//    @BindView(R.id.mogul_name_tv)
//    TextView mogul_name_tv;
//    @BindView(R.id.mogul_position_tv)
//    TextView mogul_position_tv;
//    @BindView(R.id.mogul_follow_tv)
//    TextView mogul_follow_tv;
//    @BindView(R.id.mogul_center_iv)
//    ImageView mogul_center_iv;
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
        getMogulDetail(id);
    }

    @Override
    public void initData() {

    }

    //获取大佬个人中心
    private void getMogulDetail(int page) {
        final Map<String, String> params = new HashMap<>();
        params.put(UrlList.MOGUL_SEARCH_ID, String.valueOf(id));
        params.put(UrlList.LIMIT, String.valueOf(10));
        params.put(UrlList.PAGE_STR, String.valueOf(page));
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
                    int focus = bean.getData().getIs_focus();
                    if (bean.getData().getLists().size() > 0) {
                        for (int i = 0; i < bean.getData().getLists().size(); i++) {
                            String user_image = bean.getData().getLists().get(i).getProfile_image_url();
                            String create_time = bean.getData().getLists().get(i).getCreate_at();
                            String mogul_name = bean.getData().getLists().get(i).getFull_name();
                            String weibo_name = bean.getData().getLists().get(i).getScreen_name();
                            String positon = bean.getData().getLists().get(i).getProfessional();
                            String text = bean.getData().getLists().get(i).getText();
                            int mogul_id = bean.getData().getLists().get(i).getId();
                            int fabulous = bean.getData().getLists().get(i).getHits();

                            List<String> imgs = new ArrayList<>();
                            imgs.add(user_image);
                            imgs.add(user_image);
                            MogulData mogulData = new MogulData();
                            mogulData.setName(mogul_name);
                            mogulData.setTranslate(null);
                            mogulData.setContent(text);
                            mogulData.setPosition(positon);
                            mogulData.setUrlList(imgs);
                            mogulData.setHead(user_image);
                            mogulData.setTime(create_time);
                            mogulData.setWeibo(weibo_name);
                            mogulData.setFabulous(fabulous);
                            mogulData.setId(mogul_id);
                            mogulData.setFocus(focus);
                            mogulData.setType(1);
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
//                    mogulCircleRl.setVisibility(View.GONE);
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


    //取消大佬关注
    private void cancelMogulFollow(int mogulID) {
        if (SFApplication.loginDataBean != null) {
            String token = SFApplication.loginDataBean.getToken();
            final Map<String, String> params = new HashMap<>();
            params.put(UrlList.TOKEN, token);
            params.put(UrlList.MOGUL_SEARCH_ID, String.valueOf(mogulID));
            requestGet(UrlList.CANCLE_FOLLOW, params, new MyStringCallback(this) {
                @Override
                protected void onSuccess(String result) {
                    ToastManager.showShort(MogulCircleActivity.this, getString(R.string.you_cancel) + mogul_name + getString(R.string.de_follow));
                    Logger.d(result);
                }

                @Override
                protected void onFailed(String errStr) {
                    Logger.d(errStr);
                }
            });
        } else {
            ToastManager.showShort(this, getString(R.string.please_login));
            startActivity(new Intent(this, MineActivity.class));
        }
    }

    //关注大佬
    private void addMogulFollow(int mogulID) {
        if (SFApplication.loginDataBean != null) {
            String token = SFApplication.loginDataBean.getToken();
            final Map<String, String> params = new HashMap<>();
            params.put(UrlList.TOKEN, token);
            params.put(UrlList.MOGUL_SEARCH_ID, String.valueOf(mogulID));
            requestGet(UrlList.MOGUL_FOLLOW, params, new MyStringCallback(this) {
                @Override
                protected void onSuccess(String result) {
//                {"success":0,"msg":"请登录","data":""}
                    try {
                        org.json.JSONObject object = new org.json.JSONObject(result);
                        int success = object.getInt("success");
                        String messg = object.getString("msg");
                        if (success == 0) {
                            ToastManager.showShort(MogulCircleActivity.this, messg);
                        } else {
                            ToastManager.showShort(MogulCircleActivity.this, getString(R.string.you_follow) + mogul_name);
                            Logger.d(result);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                protected void onFailed(String errStr) {
                    Logger.d(errStr);
                }
            });
        } else {
            ToastManager.showShort(this, getString(R.string.please_login));
            startActivity(new Intent(this, MineActivity.class));
        }
    }


    @Override
    public void onBGARefreshLayoutBeginRefreshing(final BGARefreshLayout refreshLayout) {
        PAGE = 1;
        getMogulDetail(PAGE);

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        PAGE++;
        getMogulDetail(PAGE);
        return false;
    }

    @Override
    public void onFabulous(View view, int position, List<MogulData> list, boolean isLike) {
        MogulData mogulData = new MogulData();
        mogulData.setHead(list.get(position).getHead());
        mogulData.setWeibo(list.get(position).getWeibo());
        mogulData.setTime(list.get(position).getTime());
        mogulData.setPosition(list.get(position).getPosition());
        if (isLike) {
            mogulData.setFabulous(list.get(position).getFabulous() + 1);
        } else {
            mogulData.setFabulous(list.get(position).getFabulous());
        }
        mogulData.setUrlList(list.get(position).getUrlList());
        mogulData.setContent(list.get(position).getContent());
        mogulData.setName(list.get(position).getName());
        mogulData.setTranslate(list.get(position).getTranslate());

        addFabulous(list.get(position).getId());
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


    //关注
    @Override
    public void onFollow(View view, int position, List<MogulData> list) {
        if (list.get(position).getFocus() == 1) {
            cancelMogulFollow(list.get(position).getId());
        } else {
            addMogulFollow(list.get(position).getId());
        }
    }

    //点赞
    private void addFabulous(int id) {
        final Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(id));
        requestGet(UrlList.MOGUL_LIKE, params, new MyStringCallback(this) {
            @Override
            protected void onSuccess(String result) {
                Logger.d(result);
            }

            @Override
            protected void onFailed(String errStr) {
                Logger.d(errStr);
            }
        });

    }

}
