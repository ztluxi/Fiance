package com.sharechain.finance.fragment.main;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.sharechain.finance.BaseFragment;
import com.sharechain.finance.R;
import com.sharechain.finance.adapter.MogulAdapter;
import com.sharechain.finance.bean.MogulData;
import com.sharechain.finance.module.mine.MyFollowActivity;
import com.sharechain.finance.module.mine.SearchActivity;
import com.sharechain.finance.utils.BaseUtils;
import com.sharechain.finance.utils.PopClickEvent;
import com.sharechain.finance.utils.PopOptionUtil;
import com.sharechain.finance.utils.ToastManager;
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
import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/13.
 */

public class FriendCircleFragment extends BaseFragment implements MogulAdapter.MyItemLongClickListener {

    private static final int TRANSLATE_SUCCESS = 1000;
    @BindView(R.id.image_title_left)
    ImageView back_Image;
    @BindView(R.id.image_title_right)
    ImageView searchImage;
    @BindView(R.id.rv_post_list)
    RecyclerView mRvPostLister;
    private MogulAdapter mogulAdapter;
    private List<MogulData> mogulDataList = new ArrayList<>();
    private String[] IMG_URL_LIST = {
            "http://ac-QYgvX1CC.clouddn.com/36f0523ee1888a57.jpg", "http://ac-QYgvX1CC.clouddn.com/07915a0154ac4a64.jpg",
            "http://ac-QYgvX1CC.clouddn.com/9ec4bc44bfaf07ed.jpg", "http://ac-QYgvX1CC.clouddn.com/fa85037f97e8191f.jpg",
            "http://ac-QYgvX1CC.clouddn.com/de13315600ba1cff.jpg", "http://ac-QYgvX1CC.clouddn.com/15c5c50e941ba6b0.jpg",
            "http://ac-QYgvX1CC.clouddn.com/10762c593798466a.jpg", "http://ac-QYgvX1CC.clouddn.com/eaf1c9d55c5f9afd.jpg",
            "http://ac-QYgvX1CC.clouddn.com/ad99de83e1e3f7d4.jpg", "http://ac-QYgvX1CC.clouddn.com/233a5f70512befcc.jpg",
    };

    private Translator translator;

    private PopOptionUtil optionUtil;
    private List<Translate> trslist = new ArrayList<Translate>();
    private Handler mHander = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case TRANSLATE_SUCCESS:
                    final List<MogulData> list = (List<MogulData>) message.obj;
                    final int postion = message.arg1;
                    optionUtil.setOnPopClickEvent(new PopClickEvent() {
                        @Override
                        public void onPreClick() {
                        }

                        @Override
                        public void onNextClick() {
                            String content = list.get(postion).getContent();
                            query(list,postion,content);
                            optionUtil.dismiss();
                        }
                    });
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
        immersionBar.statusBarColor(android.R.color.transparent).statusBarDarkFont(true).init();
        back_Image.setVisibility(View.VISIBLE);
        back_Image.setImageResource(R.drawable.my_follow);
        searchImage.setVisibility(View.VISIBLE);
        searchImage.setImageResource(R.drawable.search_white);

        final LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRvPostLister.setLayoutManager(manager);


        mogulAdapter = new MogulAdapter(getActivity(), mogulDataList,trslist);
        mRvPostLister.setAdapter(mogulAdapter);
        mogulAdapter.setOnItemLongClickListener(this);
        optionUtil = new PopOptionUtil(getActivity());
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && immersionBar != null) {
            immersionBar.statusBarColor(android.R.color.transparent).statusBarDarkFont(true).init();
        }
    }

    @Override
    public void initData() {
        for (int i = 0; i < 10; i++) {
            MogulData newsData = new MogulData(null);
            newsData.setHead("http://img4.duitang.com/uploads/item/201208/17/20120817123857_NnPNB.thumb.600_0.jpeg");
            newsData.setContent("Never give up");
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

    @OnClick({R.id.image_title_left, R.id.image_title_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_title_left:
                BaseUtils.openActivity(getActivity(), MyFollowActivity.class, null);
                break;
            case R.id.image_title_right:
                BaseUtils.openActivity(getActivity(), SearchActivity.class, null);
                break;
        }
    }

    @Override
    public void onTranslateClick(final View view, final int position, final List<MogulData> list) {
        view.setBackgroundColor(getResources().getColor(R.color.about_font));
        optionUtil.show(view);
        Message message = new Message();
        message.obj = list;
        message.arg1 = position;
        message.what = TRANSLATE_SUCCESS;
        mHander.sendMessage(message);

    }


    private void query(final List<MogulData> mogulDataList1, final int position, String input) {
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
                        MogulData mogulData = new MogulData(result);
                        mogulData.setHead(mogulDataList1.get(position).getHead());
                        mogulData.setWeibo(mogulDataList1.get(position).getWeibo());
                        mogulData.setTime(mogulDataList1.get(position).getTime());
                        mogulData.setPosition(mogulDataList1.get(position).getPosition());
                        mogulData.setFabulous(mogulDataList1.get(position).getFabulous());
                        mogulData.setUrlList(mogulDataList1.get(position).getUrlList());
                        mogulData.setContent(mogulDataList1.get(position).getContent());
                        mogulData.setName(mogulDataList1.get(position).getName());
                        mogulData.setTranslate(result);
                        mogulDataList.set(position,mogulData);
                        mogulAdapter.notifyDataSetChanged();
                        mRvPostLister.setScrollingTouchSlop(mogulDataList.size() - position);
                    }
                });
            }

            @Override
            public void onError(final TranslateErrorCode error, String requestId) {
                mHander.post(new Runnable() {
                    @Override
                    public void run() {

                        ToastManager.showShort(getActivity(),"查询错误:" + error.name());
                    }
                });
            }
        });
    }


}
