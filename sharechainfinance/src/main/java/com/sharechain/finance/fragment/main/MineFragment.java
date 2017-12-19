package com.sharechain.finance.fragment.main;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sharechain.finance.BaseFragment;
import com.sharechain.finance.R;
import com.sharechain.finance.module.mine.AboutFinanceActivity;
import com.sharechain.finance.module.mine.HistoryActivity;
import com.sharechain.finance.module.mine.MyNewsActivity;
import com.sharechain.finance.module.mine.PersonalCenterActivity;
import com.sharechain.finance.utils.GlideUtils;

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


    @Override
    protected int getLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {
        RequestOptions options = new RequestOptions().circleCrop();
        options.placeholder(R.drawable.history);
        GlideUtils.loadUserImage(getActivity(),"http://img4.duitang.com/uploads/item/201208/17/20120817123857_NnPNB.thumb.600_0.jpeg",userImage,options);

    }

    @Override
    public void initData() {

    }


    @OnClick({R.id.user_image, R.id.history_tv, R.id.my_news_tv, R.id.my_follow_tv, R.id.suggest_tv, R.id.clear_cache_tv, R.id.score_tv, R.id.exit_tv,R.id.center_ll,R.id.about_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.center_ll:
                startActivity(new Intent(getActivity(), PersonalCenterActivity.class));
                break;
            case R.id.user_image:

                break;
            case R.id.history_tv:
                startActivity(new Intent(getActivity(), HistoryActivity.class));
                break;
            case R.id.my_news_tv:
                startActivity(new Intent(getActivity(), MyNewsActivity.class));
                break;
            case R.id.my_follow_tv:
                break;
            case R.id.suggest_tv:
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
