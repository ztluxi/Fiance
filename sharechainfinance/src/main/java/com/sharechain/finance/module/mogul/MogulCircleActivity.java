package com.sharechain.finance.module.mogul;

import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.andview.refreshview.XRefreshView;
import com.orhanobut.logger.Logger;
import com.sharechain.finance.BaseActivity;
import com.sharechain.finance.R;
import com.sharechain.finance.SFApplication;
import com.sharechain.finance.adapter.MogulAdapter;
import com.sharechain.finance.bean.MogulData;
import com.sharechain.finance.utils.BaseUtils;
import com.sharechain.finance.utils.PopOptionUtil;
import com.sharechain.finance.utils.ToastManager;
import com.sharechain.finance.view.FullLinear;
import com.youdao.sdk.app.Language;
import com.youdao.sdk.app.LanguageUtils;
import com.youdao.sdk.ydonlinetranslate.Translator;
import com.youdao.sdk.ydtranslate.Translate;
import com.youdao.sdk.ydtranslate.TranslateErrorCode;
import com.youdao.sdk.ydtranslate.TranslateListener;
import com.youdao.sdk.ydtranslate.TranslateParameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by ${zhoutao} on 2017/12/22 0022.
 */

public class MogulCircleActivity extends BaseActivity implements  MogulAdapter.MyItemLongClickListener , BGARefreshLayout.BGARefreshLayoutDelegate{
    private static final int TRANSLATE_SUCCESS = 1000;
    @BindView(R.id.back_iv)
    ImageView back_Image;
    @BindView(R.id.mogul_circle_rl)
    RecyclerView mogulCircleRl;
    @BindView(R.id.rl_recyclerview_refresh)
    BGARefreshLayout mRefreshLayout;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsing_toolbar_layout;

    private List<MogulData> mogulDataList = new ArrayList<>();
    private MogulAdapter mogulAdapter;
    private String[] IMG_URL_LIST = {
            "http://ac-QYgvX1CC.clouddn.com/36f0523ee1888a57.jpg", "http://ac-QYgvX1CC.clouddn.com/07915a0154ac4a64.jpg",
            "http://ac-QYgvX1CC.clouddn.com/9ec4bc44bfaf07ed.jpg", "http://ac-QYgvX1CC.clouddn.com/fa85037f97e8191f.jpg",
            "http://ac-QYgvX1CC.clouddn.com/de13315600ba1cff.jpg", "http://ac-QYgvX1CC.clouddn.com/15c5c50e941ba6b0.jpg",
            "http://ac-QYgvX1CC.clouddn.com/10762c593798466a.jpg", "http://ac-QYgvX1CC.clouddn.com/eaf1c9d55c5f9afd.jpg",
            "http://ac-QYgvX1CC.clouddn.com/ad99de83e1e3f7d4.jpg", "http://ac-QYgvX1CC.clouddn.com/233a5f70512befcc.jpg",
    };

    @Override
    public int getLayout() {
        return R.layout.activity_mogul_cirle;
    }
    /**
     * 是否翻译过
     */
    private boolean isTranslate = false;
    private Translator translator;
    private PopOptionUtil optionUtil;
    private List<Translate> trslist = new ArrayList<Translate>();


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
    public void initView() {
        collapsing_toolbar_layout.setTitleEnabled(false);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(SFApplication.get(this), true));
        FullLinear linearLayoutManager = new FullLinear(this);
        mogulCircleRl.setNestedScrollingEnabled(false);
        //设置布局管理器
        mogulCircleRl.setLayoutManager(linearLayoutManager);
        mogulAdapter = new MogulAdapter(this, mogulDataList,null);
        mogulCircleRl.setAdapter(mogulAdapter);
        optionUtil = new PopOptionUtil(SFApplication.get(this));
    }

    @Override
    public void initData() {
        for (int i = 0; i < 10; i++) {
            MogulData newsData = new MogulData(null);
            newsData.setHead("http://img4.duitang.com/uploads/item/201208/17/20120817123857_NnPNB.thumb.600_0.jpeg");
            newsData.setContent("Never give up" + i);
            newsData.setFabulous("20090" + i);
            newsData.setName("大哥大" + i);
            newsData.setPosition("未来财经ceo");
            newsData.setTime("2017-12-22");
            newsData.setWeibo("黑猫警长");
            List<String> imgUrls = new ArrayList<>();
            imgUrls.addAll(Arrays.asList(IMG_URL_LIST).subList(0, i % 9));
            newsData.setUrlList(imgUrls);
            mogulDataList.add(newsData);
        }

    }

    @OnClick({R.id.back_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
        }
    }

    @Override
    public void onTranslateClick(final View view, final int position, final List<MogulData> list) {
        if (list.get(position).getTranslate() != null) {
            isTranslate = true;
            optionUtil.setTextView(getString(R.string.reture));
        }else {
            optionUtil.setTextView(getString(R.string.translate));
            isTranslate = false;
        }
        view.setBackgroundColor(getResources().getColor(R.color.about_font));
        final String content = list.get(position).getContent();
        optionUtil.setOnPopClickEvent(new PopOptionUtil.PopClickEvent() {
            @Override
            public void onPreClick() {
                BaseUtils.copyComment(content, SFApplication.get(MogulCircleActivity.this));
                optionUtil.dismiss();
                view.setBackgroundColor(getResources().getColor(R.color.white));
                ToastManager.showShort(MogulCircleActivity.this, getString(R.string.copy_success));
            }

            @Override
            public void onNextClick() {
                query(view, list, position, content);
                optionUtil.dismiss();
            }
        });
        optionUtil.show(view);
        view.setBackgroundColor(getResources().getColor(R.color.white));



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
                        mogulAdapter.notifyDataSetChanged();
                        mogulCircleRl.setScrollingTouchSlop(mogulDataList.size() - position);
                    }
                });
            }

            @Override
            public void onError(final TranslateErrorCode error, String requestId) {
                mHander.post(new Runnable() {
                    @Override
                    public void run() {

                        ToastManager.showShort(MogulCircleActivity.this, "翻译失败:" + error.name());
                    }
                });
            }
        });
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
