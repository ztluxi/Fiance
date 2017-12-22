package com.sharechain.finance.module.mine;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.sharechain.finance.BaseActivity;
import com.sharechain.finance.R;
import com.sharechain.finance.utils.BaseUtils;
import com.sharechain.finance.utils.GlideUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/13.
 */

public class MineActivity extends BaseActivity {

    @BindView(R.id.ll_top_info)
    LinearLayout ll_top_info;
    @BindView(R.id.user_image)
    ImageView userImage;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.history_tv)
    TextView historyTv;
    @BindView(R.id.my_news_tv)
    TextView myNewsTv;
    @BindView(R.id.my_follow_tv)
    TextView myFollowTv;
    @BindView(R.id.suggest_tv)
    TextView suggestTv;
    @BindView(R.id.clear_cache_tv)
    TextView clearCacheTv;
    @BindView(R.id.score_tv)
    TextView scoreTv;
    @BindView(R.id.exit_tv)
    TextView exitTv;

    @Override
    public int getLayout() {
        return R.layout.activity_mine;
    }

    @Override
    public void initView() {
        mImmersionBar.statusBarColor(android.R.color.transparent).init();
        setTitlePadding(ll_top_info);
        RequestOptions options = new RequestOptions().circleCrop();
        options.placeholder(R.drawable.history);
        GlideUtils.loadUserImage(this, "http://img4.duitang.com/uploads/item/201208/17/20120817123857_NnPNB.thumb.600_0.jpeg", userImage, options);
    }

    @Override
    public void initData() {

    }


    @OnClick({R.id.back_iv, R.id.user_image, R.id.history_tv, R.id.my_news_tv, R.id.my_follow_tv, R.id.suggest_tv, R.id.clear_cache_tv, R.id.score_tv, R.id.exit_tv, R.id.about_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.user_image:
                BaseUtils.openActivityBottomAnim(this, PersonalCenterActivity.class, null);
                break;
            case R.id.history_tv:
                BaseUtils.openActivityBottomAnim(this, HistoryActivity.class, null);
                break;
            case R.id.my_news_tv:
                BaseUtils.openActivityBottomAnim(this, MyNewsActivity.class, null);
                break;
            case R.id.my_follow_tv:
                break;
            case R.id.suggest_tv:
                BaseUtils.openActivityBottomAnim(this, FeedbackActivity.class, null);
                break;
            case R.id.clear_cache_tv:
                break;
            case R.id.score_tv:
                break;
            case R.id.about_tv:
                BaseUtils.openActivityBottomAnim(this, AboutFinanceActivity.class, null);
                break;
            case R.id.exit_tv:
                break;

        }
    }


}
