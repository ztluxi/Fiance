package com.sharechain.finance.module.mogul;

import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.request.RequestOptions;
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
import com.sharechain.finance.utils.BaseUtils;
import com.sharechain.finance.utils.GlideUtils;
import com.sharechain.finance.utils.PopOptionUtil;
import com.sharechain.finance.utils.ToastManager;
import com.sharechain.finance.view.FullLinear;
import com.sharechain.finance.view.dialog.LoadDialog;
import com.sharechain.finance.view.dialog.MogulShareDialog;
import com.youdao.sdk.app.Language;
import com.youdao.sdk.app.LanguageUtils;
import com.youdao.sdk.ydonlinetranslate.Translator;
import com.youdao.sdk.ydtranslate.Translate;
import com.youdao.sdk.ydtranslate.TranslateErrorCode;
import com.youdao.sdk.ydtranslate.TranslateListener;
import com.youdao.sdk.ydtranslate.TranslateParameters;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by ${zhoutao} on 2017/12/22 0022.
 */

public class MogulCircleActivity extends BaseActivity implements MogulAdapter.MyItemClickListener,BGARefreshLayout.BGARefreshLayoutDelegate{
    @BindView(R.id.back_iv)
    ImageView back_Image;
    @BindView(R.id.mogul_top_name_tv)
    TextView mogul_top_name_tv;
    @BindView(R.id.mogul_name_tv)
    TextView mogul_name_tv;
    @BindView(R.id.mogul_position_tv)
    TextView mogul_position_tv;
    @BindView(R.id.mogul_follow_tv)
    TextView mogul_follow_tv;
    @BindView(R.id.mogul_center_iv)
    ImageView mogul_center_iv;
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

    /**
     * 是否翻译过
     */
    private boolean isTranslate = false;
    /**
     * 是否关注
     */
    private int focus;
    private int id;
    private String mogul_name;
    private String head;
    private String mogulPosition;


    private Translator translator;
    private PopOptionUtil optionUtil;
    private List<Translate> trslist = new ArrayList<Translate>();
    private Handler mHander = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
            }
            return false;
        }
    });

    @Override
    public void initView() {
//        collapsing_toolbar_layout.setTitleEnabled(false);
        //获取大佬ID
        id = getIntent().getIntExtra("id", 0);
        focus = getIntent().getIntExtra("focus", 0);
        head = getIntent().getStringExtra("head");
        mogulPosition = getIntent().getStringExtra("position");
        mogul_name = getIntent().getStringExtra("name");
        mogul_top_name_tv.setText(mogul_name);
        mogul_name_tv.setText(mogul_name);
        mogul_position_tv.setText(mogulPosition);
        if (focus == 1) {
            mogul_follow_tv.setBackgroundResource(R.drawable.my_has_follow_bg);
            mogul_follow_tv.setText(R.string.has_follow);
        } else {
            mogul_follow_tv.setBackgroundResource(R.drawable.my_no_follow_bg);
            mogul_follow_tv.setText(R.string.follow);
        }
        RequestOptions options = new RequestOptions().circleCrop();
        options.placeholder(R.drawable.logo);
        options.error(R.drawable.logo);
        GlideUtils.getInstance().loadUserImage(this, head, mogul_center_iv, options);


        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(SFApplication.get(this), true));
        mogulCircleRl.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                mRefreshLayout.setEnabled(topRowVerticalPosition >= 0);

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        mogulCircleRl.setNestedScrollingEnabled(false);
        //设置布局管理器
        mogulCircleRl.setLayoutManager(new FullLinear(this, LinearLayoutManager.VERTICAL, true));
        optionUtil = new PopOptionUtil(SFApplication.get(this));
        mDialog = new LoadDialog().LoadProgressDialog(this);
    }

    @Override
    public void initData() {

        getMogulDetail(id);
    }

    //获取大佬个人中心
    private void getMogulDetail(int mogulId) {
        mDialog.show();
        final Map<String, String> params = new HashMap<>();
        params.put(UrlList.MOGUL_SEARCH_ID, String.valueOf(mogulId));
        params.put(UrlList.LIMIT, String.valueOf(50));
        requestGet(UrlList.MOGUL_CIRCLE, params, new MyStringCallback(this) {
            @Override
            protected void onSuccess(String result) {
                mRefreshLayout.endRefreshing();
                mRefreshLayout.endLoadingMore();
                Logger.d(result);

                if (UrlList.PAGE == 1) {
                    mogulDataList.clear();
                }
                MogulCircleBean bean = JSON.parseObject(result, MogulCircleBean.class);
                if (bean.getSuccess() == 1 && bean.getData().getLists().size()!=0) {
                    no_result_rl.setVisibility(View.GONE);
                    mogulCircleRl.setVisibility(View.VISIBLE);
                    int focus = bean.getData().getIs_focus();
                    if (bean.getData().getLists().size() > 1) {
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
                            mogulData.setUrlList(null);
                            mogulData.setHead(user_image);
                            mogulData.setTime(create_time);
                            mogulData.setWeibo(weibo_name);
                            mogulData.setFabulous(fabulous);
                            mogulData.setId(mogul_id);
                            mogulData.setFocus(focus);
                            mogulDataList.add(mogulData);
                        }
                    }
                } else {
                    no_result_rl.setVisibility(View.VISIBLE);
                    mogulCircleRl.setVisibility(View.GONE);
                }
                updateAdapter();
            }
            @Override
            protected void onFailed(String errStr) {
                mRefreshLayout.endRefreshing();
                mRefreshLayout.endLoadingMore();
                Logger.d(errStr);
                if (mDialog!=null&&mDialog.isShowing()){
                    mDialog.dismiss();
                }
            }
        });

    }

    private void updateAdapter() {
        if (mDialog!=null&&mDialog.isShowing()){
            mDialog.dismiss();
        }
        if (mogulAdapter == null) {
            mogulAdapter = new MogulAdapter(this, mogulDataList, 1);
//            mogulAdapter.setOnItemLongClickListener(this);
            mogulAdapter.setOnItemClickListener(this);
            mogulCircleRl.setAdapter(mogulAdapter);
        } else {
            mogulAdapter.notifyDataSetChanged();
        }

    }

    @OnClick({R.id.back_iv, R.id.mogul_follow_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.mogul_follow_tv:
                if (focus == 1) {
                    cancelMogulFollow(id);
                } else {
                    addMogulFollow(id);
                }
                break;
        }
    }


    //取消大佬关注
    private void cancelMogulFollow(int mogulID) {
        final Map<String, String> params = new HashMap<>();
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
    }

    //关注大佬
    private void addMogulFollow(int mogulID) {
        final Map<String, String> params = new HashMap<>();
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
    }

//    @Override
//    public void onTranslateClick(final View view, final int position, final List<MogulData> list) {
//        if (list.get(position).getTranslate() != null) {
//            isTranslate = true;
//            optionUtil.setTextView(getString(R.string.reture));
//        } else {
//            optionUtil.setTextView(getString(R.string.translate));
//            isTranslate = false;
//        }
//        view.setBackgroundColor(getResources().getColor(R.color.about_font));
//        final String content = list.get(position).getContent();
//        optionUtil.setOnPopClickEvent(new PopOptionUtil.PopClickEvent() {
//            @Override
//            public void onPreClick() {
//                BaseUtils.copyComment(content, SFApplication.get(MogulCircleActivity.this));
//                optionUtil.dismiss();
//                view.setBackgroundColor(getResources().getColor(R.color.white));
//                ToastManager.showShort(MogulCircleActivity.this, getString(R.string.copy_success));
//            }
//
//            @Override
//            public void onNextClick() {
//                query(view, list, position, content);
//                optionUtil.dismiss();
//            }
//        });
//        optionUtil.show(view);
//        view.setBackgroundColor(getResources().getColor(R.color.white));
//
//
//    }
//
//    private void query(final View view, final List<MogulData> mogulDataList1, final int position, String input) {
//        // 源语言或者目标语言其中之一必须为中文,目前只支持中文与其他几个语种的互译
//        String to = "中文";
//        String from = "英文";
//        Language langFrom = LanguageUtils.getLangByName(from);
//        // 若设置为自动，则查询自动识别源语言，自动识别不能保证完全正确，最好传源语言类型
//        Language langTo = LanguageUtils.getLangByName(to);
//        TranslateParameters tps = new TranslateParameters.Builder()
//                .source("youdao").from(langFrom).to(langTo).timeout(3000).build();// appkey可以省略
//        translator = Translator.getInstance(tps);
//        translator.lookup(input, "requestId", new TranslateListener() {
//            @Override
//            public void onResult(final Translate result, String input, String requestId) {
//                mHander.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        view.setBackgroundColor(getResources().getColor(R.color.white));
//                        MogulData mogulData = new MogulData(result);
//                        mogulData.setHead(mogulDataList1.get(position).getHead());
//                        mogulData.setWeibo(mogulDataList1.get(position).getWeibo());
//                        mogulData.setTime(mogulDataList1.get(position).getTime());
//                        mogulData.setPosition(mogulDataList1.get(position).getPosition());
//                        mogulData.setFabulous(mogulDataList1.get(position).getFabulous());
//                        mogulData.setUrlList(mogulDataList1.get(position).getUrlList());
//                        mogulData.setContent(mogulDataList1.get(position).getContent());
//                        mogulData.setName(mogulDataList1.get(position).getName());
//                        if (isTranslate) {
//                            mogulData.setTranslate(null);
//
//                        } else {
//                            mogulData.setTranslate(result);
//                        }
//                        mogulDataList.set(position, mogulData);
//                        mogulAdapter.notifyDataSetChanged();
//                        mogulCircleRl.setScrollingTouchSlop(mogulDataList.size() - position);
//                    }
//                });
//            }
//
//            @Override
//            public void onError(final TranslateErrorCode error, String requestId) {
//                mHander.post(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        ToastManager.showShort(MogulCircleActivity.this, "翻译失败:" + error.name());
//                    }
//                });
//            }
//        });
//    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(final BGARefreshLayout refreshLayout) {
        UrlList.PAGE = 1;
        getMogulDetail(UrlList.PAGE);

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        UrlList.PAGE++;
        getMogulDetail(UrlList.PAGE);
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
