package com.sharechain.finance.fragment.main;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.sharechain.finance.bean.MainCacheBean;
import com.sharechain.finance.bean.MogulCircleBean;
import com.sharechain.finance.bean.MogulData;
import com.sharechain.finance.bean.UrlList;
import com.sharechain.finance.module.mine.MyFollowActivity;
import com.sharechain.finance.utils.BaseUtils;
import com.sharechain.finance.utils.PopOptionUtil;
import com.sharechain.finance.utils.ToastManager;
import com.sharechain.finance.view.dialog.LoadDialog;
import com.sharechain.finance.view.dialog.MogulShareDialog;
import com.youdao.sdk.app.Language;
import com.youdao.sdk.app.LanguageUtils;
import com.youdao.sdk.ydonlinetranslate.Translator;
import com.youdao.sdk.ydtranslate.Translate;
import com.youdao.sdk.ydtranslate.TranslateErrorCode;
import com.youdao.sdk.ydtranslate.TranslateListener;
import com.youdao.sdk.ydtranslate.TranslateParameters;

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

public class FriendCircleFragment extends BaseFragment implements MogulAdapter.MyItemLongClickListener, MogulAdapter.MyItemClickListener, BGARefreshLayout.BGARefreshLayoutDelegate {

    private static final int TRANSLATE_SUCCESS = 1000;
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
    private Translator translator;
    private PopOptionUtil optionUtil;
    private List<Translate> trslist = new ArrayList<Translate>();

    //转圈圈的加载框
    private Dialog mDialog;
    private MogulCircleBean bean;
    /**
     * 是否翻译过
     */
    private boolean isTranslate = false;

    private Handler mHander = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case TRANSLATE_SUCCESS:
                    final List<MogulData> list = (List<MogulData>) message.obj;
                    final int position = message.arg1;

                    break;
            }

            return false;
        }
    });


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
        optionUtil = new PopOptionUtil(SFApplication.get(getActivity()));
    }

    //获取大佬圈列表
    private void getData(int page) {
        mDialog.show();
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
                if (mDialog!=null && mDialog.isShowing()){
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
                if (UrlList.PAGE == 1) {
                    mogulDataList.clear();
                }
                if (bean.getData().getLists().size() > 1) {
                    for (int i = 0; i < bean.getData().getLists().size(); i++) {
                        String user_image = bean.getData().getLists().get(i).getProfile_image_url();
                        String create_time = bean.getData().getLists().get(i).getCreate_at();
                        String mogul_name = bean.getData().getLists().get(i).getFull_name();
                        String weibo_name = bean.getData().getLists().get(i).getScreen_name();
                        String positon = bean.getData().getLists().get(i).getProfessional();
                        String text = bean.getData().getLists().get(i).getText();
                        int fabulous = bean.getData().getLists().get(i).getHits();

                        int mogul_id = bean.getData().getLists().get(i).getId();
                        List<String> imgs = new ArrayList<>();
                        if (bean.getData().getLists().get(i).getImages().size() != 0) {
                            for (int j = 0; j < bean.getData().getLists().get(i).getImages().size(); j++) {
                                imgs.add(bean.getData().getLists().get(i).getImages().get(j).getUrl());
                            }
                        }
                        MogulData mogulData = new MogulData(null);
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
                        mogulDataList.add(mogulData);
                    }
                }
            } else {
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
        getData(UrlList.PAGE);
    }

    @OnClick({R.id.image_title_left, R.id.image_title_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_title_left:
                BaseUtils.openActivity(getActivity(), MyFollowActivity.class, null);
                break;
            case R.id.image_title_right:
                Intent intent = new Intent(getActivity(), MyFollowActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("type", 2);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onTranslateClick(final View view, final int position, final List<MogulData> list) {
        if (list.get(position).getTranslate() != null) {
            isTranslate = true;
            optionUtil.setTextView(getString(R.string.reture));
        } else {
            optionUtil.setTextView(getString(R.string.translate));
            isTranslate = false;
        }
//        view.setBackgroundColor(getResources().getColor(R.color.about_font));
        final String content = list.get(position).getContent();
        optionUtil.setOnPopClickEvent(new PopOptionUtil.PopClickEvent() {
            @Override
            public void onPreClick() {
                BaseUtils.copyComment(content, SFApplication.get(getActivity()));
                optionUtil.dismiss();
                ToastManager.showShort(getActivity(), getString(R.string.copy_success));
            }

            @Override
            public void onNextClick() {
                query(view, list, position, content);
                optionUtil.dismiss();

            }
        });
        optionUtil.show(view);

    }


    private void query(final View view, final List<MogulData> mogulDataList1, final int position, String input) {
        // 源语言或者目标语言其中之一必须为中文,目前只支持中文与其他几个语种的互译
        String to = "中文";
        String from = "英文";
        Language langFrom = LanguageUtils.getLangByName(from);
        // 若设置为自动，则查询自动识别源语言，自动识别不能保证完全正确，最好传源语言类型
        Language langTo = LanguageUtils.getLangByName(to);
        TranslateParameters tps = new TranslateParameters.Builder()
                .source("youdao").from(langFrom).to(langTo).timeout(3000).build();// appkey可以省略
        translator = Translator.getInstance(tps);
        translator.lookup(input, "requestId", new TranslateListener() {
            @Override
            public void onResult(final Translate result, String input, String requestId) {
                mHander.post(new Runnable() {
                    @Override
                    public void run() {
                        view.setBackgroundColor(getResources().getColor(R.color.white));
                        MogulData mogulData = new MogulData(result);
                        mogulData.setHead(mogulDataList1.get(position).getHead());
                        mogulData.setWeibo(mogulDataList1.get(position).getWeibo());
                        mogulData.setTime(mogulDataList1.get(position).getTime());
                        mogulData.setPosition(mogulDataList1.get(position).getPosition());
                        mogulData.setFabulous(mogulDataList1.get(position).getFabulous());
                        mogulData.setUrlList(mogulDataList1.get(position).getUrlList());
                        mogulData.setContent(mogulDataList1.get(position).getContent());
                        mogulData.setName(mogulDataList1.get(position).getName());
                        if (isTranslate) {
                            mogulData.setTranslate(null);

                        } else {
                            mogulData.setTranslate(result);
                        }
                        mogulDataList.set(position, mogulData);
                        updateAdapter(position);
                    }
                });
            }

            @Override
            public void onError(final TranslateErrorCode error, String requestId) {
                mHander.post(new Runnable() {
                    @Override
                    public void run() {

                        ToastManager.showShort(getActivity(), "翻译失败:" + error.name());
                    }
                });
            }
        });
    }

    //点赞
    private void addFabulous(int id) {
        final Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(id));
        requestGet(UrlList.MOGUL_LIKE, params, new MyStringCallback(getActivity()) {
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


    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        UrlList.PAGE = 1;
        getData(UrlList.PAGE);
    }


    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        UrlList.PAGE++;
        getData(UrlList.PAGE);
        return false;
    }

    private void updateAdapter(int position) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        if (mogulAdapter == null) {
            mogulAdapter = new MogulAdapter(getActivity(), mogulDataList, trslist, 0);
            mogulAdapter.setOnItemLongClickListener(this);
            mogulAdapter.setOnItemClickListener(this);
            mRvPostLister.setAdapter(mogulAdapter);
        } else {
            mogulAdapter.notifyItemChanged(position);
        }

    }

    //点赞
    @Override
    public void onFabulous(View view, int position, List<MogulData> list, boolean isLike) {
        MogulData mogulData = new MogulData(null);
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
//        mogulDataList.set(position, mogulData);
//        updateAdapter(position, mogulDataList);
    }

    @Override
    public void onShare(View view, int position, List<MogulData> list) {
        String url = list.get(position).getHead();
        String name = list.get(position).getName();
        String weibo = list.get(position).getWeibo();
        String zhiwei = list.get(position).getPosition();
        String content = list.get(position).getContent();
        MogulShareDialog mogulShareDialog = new MogulShareDialog(getActivity());
        mogulShareDialog.show();
        mogulShareDialog.setContent(getActivity(), url, zhiwei, name, content, weibo);
    }
}
