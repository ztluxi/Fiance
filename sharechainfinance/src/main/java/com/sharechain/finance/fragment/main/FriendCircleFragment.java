package com.sharechain.finance.fragment.main;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.orhanobut.logger.Logger;
import com.sharechain.finance.BaseFragment;
import com.sharechain.finance.MyStringCallback;
import com.sharechain.finance.R;
import com.sharechain.finance.SFApplication;
import com.sharechain.finance.adapter.MogulAdapter;
import com.sharechain.finance.bean.FastMsgBean;
import com.sharechain.finance.bean.MogulCircleBean;
import com.sharechain.finance.bean.MogulData;
import com.sharechain.finance.bean.UrlList;
import com.sharechain.finance.module.mine.MyFollowActivity;
import com.sharechain.finance.module.mogul.MogulSearchActivity;
import com.sharechain.finance.utils.BaseUtils;
import com.sharechain.finance.utils.PopOptionUtil;
import com.sharechain.finance.utils.ToastManager;
import com.sharechain.finance.view.dialog.MogulShareDialog;
import com.sharechain.finance.view.fabulos.GoodView;
import com.youdao.sdk.app.Language;
import com.youdao.sdk.app.LanguageUtils;
import com.youdao.sdk.ydonlinetranslate.Translator;
import com.youdao.sdk.ydtranslate.Translate;
import com.youdao.sdk.ydtranslate.TranslateErrorCode;
import com.youdao.sdk.ydtranslate.TranslateListener;
import com.youdao.sdk.ydtranslate.TranslateParameters;

import java.util.ArrayList;
import java.util.Arrays;
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
    /**
     * 是否翻译过
     */
    private boolean isTranslate = false;

    private int mNewPageNumber = 1;
    private int mMorePageNumber = 0;

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

        initLister();
    }

    private void initLister() {
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(SFApplication.get(getActivity()), true));
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRvPostLister.setLayoutManager(manager);

    }

    //获取大佬圈列表
    private void getData() {
        final Map<String, String> params = new HashMap<>();
        params.put(pageParam, UrlList.PAGE);
        requestGet(UrlList.MOGUL_CIRCLE, params, new MyStringCallback(getActivity()) {
            @Override
            protected void onSuccess(String result) {
                Logger.d(result);
                MogulCircleBean bean = JSON.parseObject(result, MogulCircleBean.class);
                if (bean.getSuccess() == 1) {
                    if (bean.getData().size() > 1) {
                        for (int i = 0; i < bean.getData().size(); i++) {
                            String user_image = bean.getData().get(i).getProfile_image_url();
                            String create_time = bean.getData().get(i).getCreate_at();
                            String mogul_name = bean.getData().get(i).getFull_name();
                            String weibo_name = bean.getData().get(i).getScreen_name();
                            String positon = bean.getData().get(i).getProfessional();
                            String text = bean.getData().get(i).getText();
                            int mogul_id = bean.getData().get(i).getId();
//                            List<String> imgs = new ArrayList<>();
//                            imgs.add(user_image);
//                            imgs.add(user_image);
                            MogulData mogulData = new MogulData(null);
                            mogulData.setName(mogul_name);
                            mogulData.setTranslate(null);
                            mogulData.setContent(text);
                            mogulData.setPosition(positon);
                            mogulData.setUrlList(null);
                            mogulData.setHead(user_image);
                            mogulData.setTime(create_time);
                            mogulData.setWeibo(weibo_name);
                            mogulData.setFabulous(1002);
                            mogulData.setId(mogul_id);
                            mogulDataList.add(mogulData);
                        }
                    }
                }
            }

            @Override
            protected void onFailed(String errStr) {

            }
        });
    }

    @Override
    public void initData() {
        getData();
        mogulAdapter = new MogulAdapter(getActivity(), mogulDataList, trslist,0);
        mRvPostLister.setAdapter(mogulAdapter);
        mogulAdapter.setOnItemLongClickListener(this);
        mogulAdapter.setOnItemClickListener(this);
        optionUtil = new PopOptionUtil(SFApplication.get(getActivity()));

    }

    @OnClick({R.id.image_title_left, R.id.image_title_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_title_left:
                BaseUtils.openActivity(getActivity(), MyFollowActivity.class, null);
                break;
            case R.id.image_title_right:
                BaseUtils.openActivity(getActivity(), MogulSearchActivity.class, null);
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
        view.setBackgroundColor(getResources().getColor(R.color.white));
        final String content = list.get(position).getContent();
        optionUtil.setOnPopClickEvent(new PopOptionUtil.PopClickEvent() {
            @Override
            public void onPreClick() {
                BaseUtils.copyComment(content, SFApplication.get(getActivity()));
                optionUtil.dismiss();
                view.setBackgroundColor(getResources().getColor(R.color.white));
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

    private void loadMoreData() {

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
                        updateAdapter(position, mogulDataList);
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
    //
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
        mRefreshLayout.beginRefreshing();
        mHander.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.endRefreshing();
            }
        }, 3000);
    }

    private void updateAdapter(int position, List<MogulData> mogulData) {
        mogulAdapter.notifyDataSetChanged();
//        mRvPostLister.setScrollingTouchSlop(mogulData.size() - position);

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {

        return false;
    }

    @Override
    public void onFabulous(View view, int position, List<MogulData> list) {
        GoodView goodView = new GoodView(getActivity());
        goodView.setImage(R.drawable.fabulous_sel);
        goodView.show(view);
        MogulData mogulData = new MogulData(null);
        mogulData.setHead(list.get(position).getHead());
        mogulData.setWeibo(list.get(position).getWeibo());
        mogulData.setTime(list.get(position).getTime());
        mogulData.setPosition(list.get(position).getPosition());
        mogulData.setFabulous(list.get(position).getFabulous()+1);
        mogulData.setUrlList(list.get(position).getUrlList());
        mogulData.setContent(list.get(position).getContent());
        mogulData.setName(list.get(position).getName());
        mogulData.setTranslate(list.get(position).getTranslate());
        addFabulous(list.get(position).getId());
        mogulDataList.set(position, mogulData);
        updateAdapter(position, mogulDataList);

    }

    @Override
    public void onShare(View view, int position, List<MogulData> list) {
//        MogulShareDialog mogulShareDialog = new MogulShareDialog(SFApplication.get(getActivity()));
//        mogulShareDialog.setHead(SFApplication.get(getActivity()),list.get(position).getHead());
//        mogulShareDialog.show();
    }
}
