package com.sharechain.finance.fragment.main;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.sharechain.finance.BaseFragment;
import com.sharechain.finance.R;
import com.sharechain.finance.module.mine.AboutFinanceActivity;
import com.sharechain.finance.module.mine.FeedbackActivity;
import com.sharechain.finance.module.mine.HistoryActivity;
import com.sharechain.finance.module.mine.MyNewsActivity;
import com.sharechain.finance.utils.GlideUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/13.
 */

public class MineFragment extends BaseFragment {

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
    @BindView(R.id.news_red_tv)
    TextView news_red_tv;

    @Override
    protected int getLayout() {
        return R.layout.activity_mine;
    }

    @Override
    protected void initView() {
        RequestOptions options = new RequestOptions().circleCrop().transform(new GlideUtils.GlideCircleTransform(getActivity(),2,getActivity().getResources().getColor(R.color.white)));
        options.placeholder(R.drawable.logo);
        options.error(R.drawable.logo);
        GlideUtils.getInstance().loadUserImage(getActivity(),"http://img4.duitang.com/uploads/item/201208/17/20120817123857_NnPNB.thumb.600_0.jpeg",userImage,options);

    }

    @Override
    public void initData() {

    }


    @OnClick({R.id.user_image, R.id.history_tv, R.id.my_news_rl, R.id.my_follow_tv, R.id.suggest_tv, R.id.clear_cache_tv, R.id.score_tv, R.id.exit_tv,R.id.about_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_image:
//                startActivity(new Intent(getActivity(), PersonalCenterActivity.class));
                break;
            case R.id.history_tv:
                startActivity(new Intent(getActivity(), HistoryActivity.class));
                break;
            case R.id.my_news_rl:
                startActivity(new Intent(getActivity(), MyNewsActivity.class));
                if (!EventBus.getDefault().isRegistered(this)) {
                    EventBus.getDefault().register(this);
                }
                break;
            case R.id.my_follow_tv:
                break;
            case R.id.suggest_tv:
                startActivity(new Intent(getActivity(), FeedbackActivity.class));
                break;
            case R.id.clear_cache_tv:
                break;
            case R.id.score_tv:
                break;
            case R.id.about_tv:
                startActivity(new Intent(getActivity(), AboutFinanceActivity.class));
                break;
            case R.id.exit_tv:
                break;

        }
    }


}
