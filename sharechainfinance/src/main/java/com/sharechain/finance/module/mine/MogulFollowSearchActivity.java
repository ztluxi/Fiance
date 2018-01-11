package com.sharechain.finance.module.mine;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.orhanobut.logger.Logger;
import com.sharechain.finance.BaseActivity;
import com.sharechain.finance.MyStringCallback;
import com.sharechain.finance.R;
import com.sharechain.finance.SFApplication;
import com.sharechain.finance.adapter.MogulAdapter;
import com.sharechain.finance.adapter.MyFollowAdapter;
import com.sharechain.finance.bean.FollowBean;
import com.sharechain.finance.bean.FollowData;
import com.sharechain.finance.bean.LoginManagerBean;
import com.sharechain.finance.bean.MogulCircleBean;
import com.sharechain.finance.bean.MogulData;
import com.sharechain.finance.bean.UrlList;
import com.sharechain.finance.module.mogul.MogulCircleFixedActivity;
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

/**
 * 我的关注列表
 * Created by ${zhoutao} on 2017/12/21 0013.
 */

public class MogulFollowSearchActivity extends BaseActivity implements MyFollowAdapter.MyItemClickListener {

    @BindView(R.id.image_title_left)
    ImageView back_Image;
    @BindView(R.id.my_news_lv)
    ListView myNewslv;
    @BindView(R.id.text_cancel)
    TextView text_cancel;
    @BindView(R.id.search_et)
    EditText search_et;
    @BindView(R.id.empty_view)
    TextView empty_view;
    @BindView(R.id.delete_tv)
    TextView deleteTv;
    @BindView(R.id.rv_post_list)
    RecyclerView mRvPostLister;

    private List<FollowData> followDataList = new ArrayList<>();
    private List<MogulData> mogulDataList = new ArrayList<>();
    private int type;//1表示我的关注  2表示大佬搜索

    private MyFollowAdapter followAdapter;
    private MogulAdapter mogulAdapter;

    private Dialog mDialog;
    //默认不搜索
    private boolean isSearch = false;
    public int PAGE = 1;//页数

    @Override
    public int getLayout() {
        return R.layout.activity_mogul_follow_search;
    }

    @Override
    public void initView() {
        type = getIntent().getIntExtra("type", 0);
        if (type == 2) {
            initTitle(getString(R.string.mogul_search));
        } else {
            initTitle(getString(R.string.my_follow));
        }
        back_Image.setVisibility(View.VISIBLE);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvPostLister.setLayoutManager(linearLayoutManager);
        mDialog = new LoadDialog().LoadProgressDialog(this);
        initListen();
    }

    private void updateAdapter(int type) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        if (type == 1) {
            myNewslv.setVisibility(View.VISIBLE);
            mRvPostLister.setVisibility(View.GONE);
            if (followAdapter == null) {
                followAdapter = new MyFollowAdapter(this, R.layout.adapter_my_follow_item);
                followAdapter.setMyItemClickLister(this);
                followAdapter.setData(followDataList);
                myNewslv.setAdapter(followAdapter);
            } else {
                followAdapter.notifyDataSetChanged();
            }
        } else {
            myNewslv.setVisibility(View.GONE);
            mRvPostLister.setVisibility(View.VISIBLE);
            if (mogulAdapter == null) {
                mogulAdapter = new MogulAdapter(this, mogulDataList, 3);
                mRvPostLister.setAdapter(mogulAdapter);
            } else {
                mogulAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public void initData() {
        showSoftKeyboard();
    }

    private void initListen() {
        search_et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    //点击搜索才出来内容
                    isSearch = true;
                    if (search_et.getText().toString().equals("")) {
                        ToastManager.showShort(MogulFollowSearchActivity.this, getString(R.string.please_enter_keyword));
                    } else {
                        searchMogul(search_et.getText().toString());
                    }
                    return true;
                }
                return false;
            }
        });
        search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    deleteTv.setVisibility(View.VISIBLE);
                } else {
                    deleteTv.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        myNewslv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", followDataList.get(i).getMogulID());
                bundle.putString("head", followDataList.get(i).getImage());
                bundle.putString("name", followDataList.get(i).getName());
                bundle.putString("position", followDataList.get(i).getPosition());
                bundle.putInt("focus", followDataList.get(i).getState());
                BaseUtils.openActivity(MogulFollowSearchActivity.this, MogulCircleFixedActivity.class, bundle);
            }
        });
        deleteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_et.getText().clear();
                followDataList.clear();
                mogulDataList.clear();
                updateAdapter(type);
            }
        });
    }

    //2搜索大佬圈 1 搜索我的关注
    private void searchMogul(String search_str) {

        mDialog.show();
        String url = null;
        final Map<String, String> params = new HashMap<>();
        params.put("search_str", search_str);
        params.put(UrlList.PAGE_STR, String.valueOf(PAGE));
        params.put(UrlList.LIMIT, String.valueOf(10));
        if (type == 2) {
            url = UrlList.MOGUL_CIRCLE;
        } else {
            if (SFApplication.loginDataBean != null) {
                String token = SFApplication.loginDataBean.getToken();
                params.put(UrlList.TOKEN, token);
                url = UrlList.GET_MY_FOLLOW;
            } else {
                ToastManager.showShort(this, getString(R.string.please_login));
                startActivity(new Intent(this, MineActivity.class));
                return;
            }
        }
        requestGet(url, params, new MyStringCallback(this) {
            @Override
            protected void onSuccess(String result) {
                Logger.d(result);
                followDataList.clear();
                if (type == 1) {
                    FollowBean myFollowBean = JSON.parseObject(result, FollowBean.class);
                    if (myFollowBean.getData().size() == 0) {
                        empty_view.setVisibility(View.VISIBLE);
                        myNewslv.setVisibility(View.GONE);
                        mRvPostLister.setVisibility(View.GONE);
                    } else {
                        empty_view.setVisibility(View.GONE);
                        myNewslv.setVisibility(View.VISIBLE);
                        mRvPostLister.setVisibility(View.GONE);
                        if (myFollowBean.getData().size() != 0) {
                            for (int i = 0; i < myFollowBean.getData().size(); i++) {
                                FollowData followData = new FollowData();
                                followData.setPosition(myFollowBean.getData().get(i).getProfessional());
                                followData.setWeibo(myFollowBean.getData().get(i).getScreen_name());
                                followData.setName(myFollowBean.getData().get(i).getFull_name());
                                followData.setImage(myFollowBean.getData().get(i).getProfile_image_url());
                                followData.setMogulID(myFollowBean.getData().get(i).getCelebrity_id());
                                followData.setState(Integer.parseInt(myFollowBean.getData().get(i).getState()));
                                followData.setFollowID(myFollowBean.getData().get(i).getId());
                                followDataList.add(followData);
                            }
                        }
                    }
                } else {
                    MogulCircleBean bean = JSON.parseObject(result, MogulCircleBean.class);
                    if (bean.getData().getLists().size() == 0) {
                        empty_view.setVisibility(View.VISIBLE);
                        myNewslv.setVisibility(View.GONE);
                        mRvPostLister.setVisibility(View.GONE);
                    } else {
                        empty_view.setVisibility(View.GONE);
                        myNewslv.setVisibility(View.GONE);
                        mRvPostLister.setVisibility(View.VISIBLE);
                        if (bean.getSuccess() == 1) {
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
                                    mogulDataList.add(mogulData);
                                }
                            }
                        }
                    }
                }
                updateAdapter(type);
            }

            @Override
            protected void onFailed(String errStr) {
                Logger.d(errStr);
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        });

    }

    //取消大佬关注
    private void cancelMogulFollow(int followID,int MogulID, final int position, final FollowData data) {
        if (SFApplication.loginDataBean != null) {
            String token = SFApplication.loginDataBean.getToken();
            mDialog.show();
            final Map<String, String> params = new HashMap<>();
            params.put(UrlList.TOKEN, token);
            if (type == 2) {
                params.put(UrlList.MOGUL_SEARCH_ID, String.valueOf(MogulID));
            } else {
                params.put(UrlList.MOGUL_CANCLE_FOLLOW_ID, String.valueOf(followID));
            }
            requestGet(UrlList.CANCLE_FOLLOW, params, new MyStringCallback(this) {
                @Override
                protected void onSuccess(String result) {
                    LoginManagerBean bean = JSON.parseObject(result, LoginManagerBean.class);
                    if (bean.getSuccess() == 1 && bean.getCode() == 2000) {
                        FollowData data1 = new FollowData();
                        data1.setState(2);
                        data1.setMogulID(data.getMogulID());
                        data1.setFollowID(data.getFollowID());
                        data1.setImage(data.getImage());
                        data1.setName(data.getName());
                        data1.setPosition(data.getPosition());
                        data1.setWeibo(data.getWeibo());
                        followAdapter.setItem(position, data1);
                        updateAdapter(1);
                        ToastManager.showShort(MogulFollowSearchActivity.this, getString(R.string.you_cancel) + data.getName() + getString(R.string.de_follow));
                    }
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
    private void addMogulFollow(int followID, int mogulID,final int position, final FollowData data) {
        if (SFApplication.loginDataBean != null) {
            String token = SFApplication.loginDataBean.getToken();
            mDialog.show();
            final Map<String, String> params = new HashMap<>();
            params.put(UrlList.TOKEN, token);
            if (type == 2) {
                params.put(UrlList.MOGUL_SEARCH_ID, String.valueOf(mogulID));
            } else {
                params.put(UrlList.MOGUL_CANCLE_FOLLOW_ID, String.valueOf(followID));
            }
            requestGet(UrlList.MOGUL_FOLLOW, params, new MyStringCallback(this) {
                @Override
                protected void onSuccess(String result) {
                    LoginManagerBean bean = JSON.parseObject(result, LoginManagerBean.class);
                    if (bean.getSuccess() == 1 && bean.getCode() == 2000) {
                        FollowData data1 = new FollowData();
                        data1.setState(1);
                        data1.setMogulID(data.getMogulID());
                        data1.setFollowID(data.getFollowID());
                        data1.setImage(data.getImage());
                        data1.setName(data.getName());
                        data1.setPosition(data.getPosition());
                        data1.setWeibo(data.getWeibo());
                        followAdapter.setItem(position, data1);
                        updateAdapter(1);
                        ToastManager.showShort(MogulFollowSearchActivity.this, getString(R.string.you_follow) + data.getName());
                        Logger.d(result);
                    }

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


    @OnClick({R.id.image_title_left})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_title_left:
                hideSoftKeyboard(search_et);
                //取消
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 10);
                break;

        }
    }

    @Override
    public void cancelFollow(View view, int position, FollowData data) {
        if (data.getState() == 1) {
            cancelMogulFollow(data.getFollowID(),data.getMogulID(), position, data);
        } else {
            addMogulFollow(data.getFollowID(),data.getMogulID(), position, data);
        }
    }
}
