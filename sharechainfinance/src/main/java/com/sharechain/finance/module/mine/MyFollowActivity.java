package com.sharechain.finance.module.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import com.sharechain.finance.adapter.MyFollowAdapter;
import com.sharechain.finance.adapter.MyNewsAdapter;
import com.sharechain.finance.bean.FollowBean;
import com.sharechain.finance.bean.FollowData;
import com.sharechain.finance.bean.MogulCircleBean;
import com.sharechain.finance.bean.MyFollowBean;
import com.sharechain.finance.bean.MyNewsBean;
import com.sharechain.finance.bean.NewsData;
import com.sharechain.finance.bean.UrlList;
import com.sharechain.finance.utils.ToastManager;

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
    @BindView(R.id.text_cancel)
    TextView text_cancel;
    @BindView(R.id.search_et)
    EditText search_et;
    @BindView(R.id.follow_search_ll)
    LinearLayout followSearchLL;
    @BindView(R.id.empty_view)
    TextView empty_view;
    @BindView(R.id.xrefreshview_content)
    XRefreshView refreshView;
    private List<FollowData> followDataList = new ArrayList<>();
    private int type;//1表示我的关注  2表示大佬搜索

    private MyFollowAdapter followAdapter;

    @Override
    public int getLayout() {
        return R.layout.activity_my_follow;
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
        searchIV.setVisibility(View.VISIBLE);
        searchIV.setImageResource(R.drawable.search_white);

        initXRefreshView(refreshView);
        refreshView.setPullRefreshEnable(true);
        refreshView.setPullLoadEnable(true);
        refreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
                UrlList.PAGE = 1;
                getFollow(type);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
                UrlList.PAGE++;
                getFollow(type);
            }
        });
        initListen();
    }

    private void updateAdapter() {
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
        getFollow(type);
    }


    private void initListen() {
        search_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND || (keyEvent != null && keyEvent.getKeyCode() == keyEvent.KEYCODE_ENTER)) {
                    searchMogul(search_et.getText().toString());
                    return true;
                }
                return false;
            }
        });

    }

    //2搜索大佬圈 1 搜索我的关注
    private void searchMogul(String search_str) {
        String url = null;
        final Map<String, String> params = new HashMap<>();
        params.put("search_str", search_str);
        if (type == 2) {
            url = UrlList.MOGUL_CIRCLE;
        } else {
            url = UrlList.GET_MY_FOLLOW;
        }
        requestGet(url, params, new MyStringCallback(this) {
            @Override
            protected void onSuccess(String result) {
                Logger.d(result);
                followDataList.clear();
//                {"success":1,"msg":"获取成功","data":[]}
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(result);
                    String data = jsonObject.getString("data");
                    if (data == null) {
                        empty_view.setVisibility(View.VISIBLE);
                        refreshView.setVisibility(View.GONE);
                    } else {


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                MogulCircleBean bean = JSON.parseObject(result, MogulCircleBean.class);
//                int focus = bean.getData().getIs_focus();
//                if (bean.getSuccess() == 1) {
//                    //如果返回数据为空
//                    if (bean.getData()==null||bean.getData().equals("")){
//                        no_result_rl.setVisibility(View.VISIBLE);
//                        refreshView.setVisibility(View.GONE);
//                        return;
//                    }
//                    if (bean.getData().getLists().size() != 0) {
//                        for (int i = 0; i < bean.getData().getLists().size(); i++) {
//                            FollowData followData = new FollowData();
//                            followData.setPosition(bean.getData().getLists().get(i).getProfessional());
//                            followData.setWeibo(bean.getData().getLists().get(i).getScreen_name());
//                            followData.setName(bean.getData().getLists().get(i).getFull_name());
//                            followData.setImage(bean.getData().getLists().get(i).getProfile_image_url());
//                            followData.setFacous(focus);
//                            followDataList.add(followData);
//                        }
//                    }
//                    updateAdapter(followDataList);
//                }
            }

            @Override
            protected void onFailed(String errStr) {
                Logger.d(errStr);
            }
        });

    }

    //获取我的关注列表
    private void getFollow(final int type) {
        String url = null;
        final Map<String, String> params = new HashMap<>();
        if (type == 0) {
            url = UrlList.GET_MY_FOLLOW;
        } else {
            url = UrlList.MOGUL_CIRCLE;
        }
        params.put(UrlList.PAGE_STR, String.valueOf(UrlList.PAGE));
        requestGet(url, params, new MyStringCallback(this) {
            @Override
            protected void onSuccess(String result) {
                refreshView.stopRefresh();
                refreshView.stopLoadMore();
                Logger.d(result);
                if (type == 0) {
                    FollowBean myFollowBean = JSON.parseObject(result, FollowBean.class);
                    if (myFollowBean.getSuccess() == 1 && myFollowBean.getData() != null) {
                        empty_view.setVisibility(View.GONE);
                        refreshView.setVisibility(View.VISIBLE);
                        if (UrlList.PAGE == 1) {
                            followDataList.clear();
                        }
                        if (myFollowBean.getData().size() != 0) {
                            for (int i = 0; i < myFollowBean.getData().size(); i++) {
                                FollowData followData = new FollowData();
                                followData.setPosition(myFollowBean.getData().get(i).getProfessional());
                                followData.setWeibo(myFollowBean.getData().get(i).getScreen_name());
                                followData.setName(myFollowBean.getData().get(i).getFull_name());
                                followData.setImage(myFollowBean.getData().get(i).getProfile_image_url());
                                followData.setMogulID(myFollowBean.getData().get(i).getId());
                                followData.setFacous(1);
                                followDataList.add(followData);
                            }
                        }
                    } else {
                        empty_view.setVisibility(View.VISIBLE);
                        refreshView.setVisibility(View.GONE);
                    }
                } else {
                    MogulCircleBean bean = JSON.parseObject(result, MogulCircleBean.class);
                    if (bean.getSuccess() == 1 && bean.getData().getLists() != null) {
                        empty_view.setVisibility(View.GONE);
                        refreshView.setVisibility(View.VISIBLE);
                        if (UrlList.PAGE == 1) {
                            followDataList.clear();
                        }
                        if (bean.getData().getLists().size() != 0) {
                            for (int i = 0; i < bean.getData().getLists().size(); i++) {
                                FollowData followData = new FollowData();
                                followData.setPosition(bean.getData().getLists().get(i).getProfessional());
                                followData.setWeibo(bean.getData().getLists().get(i).getScreen_name());
                                followData.setName(bean.getData().getLists().get(i).getFull_name());
                                followData.setImage(bean.getData().getLists().get(i).getProfile_image_url());
                                followData.setMogulID(bean.getData().getLists().get(i).getId());
                                followData.setFacous(0);
                                followDataList.add(followData);
                            }
                        }
                    } else {
                        empty_view.setVisibility(View.VISIBLE);
                        refreshView.setVisibility(View.GONE);
                    }
                }
                updateAdapter();
            }

            @Override
            protected void onFailed(String errStr) {
                refreshView.stopRefresh();
                refreshView.stopLoadMore();
                Logger.d(errStr);
            }
        });

    }

    //取消大佬关注
    private void cancelMogulFollow(int mogulID, final int position, final FollowData data) {
        final Map<String, String> params = new HashMap<>();
        if (type == 2) {
            params.put(UrlList.MOGUL_SEARCH_ID, String.valueOf(mogulID));
        } else {
            params.put(UrlList.MOGUL_CANCLE_FOLLOW_ID, String.valueOf(mogulID));
        }
        requestGet(UrlList.CANCLE_FOLLOW, params, new MyStringCallback(this) {
            @Override
            protected void onSuccess(String result) {
                followDataList.remove(position);
                followAdapter.notifyDataSetChanged();
                ToastManager.showShort(MyFollowActivity.this, getString(R.string.you_cancel) + data.getName() + getString(R.string.de_follow));
                Logger.d(result);
            }

            @Override
            protected void onFailed(String errStr) {
                Logger.d(errStr);
            }
        });
    }

    //关注大佬
    private void addMogulFollow(int mogulID, final int position, final FollowData data) {
        final Map<String, String> params = new HashMap<>();
        if (type == 2) {
            params.put(UrlList.MOGUL_SEARCH_ID, String.valueOf(mogulID));
        } else {
            params.put(UrlList.MOGUL_CANCLE_FOLLOW_ID, String.valueOf(mogulID));
        }
        requestGet(UrlList.MOGUL_FOLLOW, params, new MyStringCallback(this) {
            @Override
            protected void onSuccess(String result) {
//                {"success":0,"msg":"请登录","data":""}
                try {
                    org.json.JSONObject object = new org.json.JSONObject(result);
                    int success = object.getInt("success");
                    String messg = object.getString("msg");
                    if (success == 0) {
                        ToastManager.showShort(MyFollowActivity.this, messg);
                    } else {
                        followDataList.remove(position);
                        followAdapter.notifyDataSetChanged();
                        ToastManager.showShort(MyFollowActivity.this, getString(R.string.you_follow) + data.getName());
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
    }


    @OnClick({R.id.image_title_left, R.id.image_title_right, R.id.text_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_title_left:
                finish();
                break;
            case R.id.image_title_right:
                followSearchLL.setVisibility(View.VISIBLE);
                break;
            case R.id.text_cancel:
                search_et.getText().clear();
                break;
        }
    }

    @Override
    public void cancelFollow(View view, int position, FollowData data) {
        if (data.getFacous() == 1) {
            cancelMogulFollow(data.getMogulID(), position, data);
        } else {
            addMogulFollow(data.getMogulID(), position, data);
        }
    }
}
