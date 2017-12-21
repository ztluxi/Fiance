package com.sharechain.finance.module.mine;

import android.view.View;
import android.widget.ImageView;

import com.sharechain.finance.BaseActivity;
import com.sharechain.finance.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ${zhoutao} on 2017/12/19 0013.
 */

public class FeedbackActivity extends BaseActivity {
    @BindView(R.id.image_title_left)
    ImageView back_Image;

    @Override
    public int getLayout() {
        return R.layout.activity_feedback;
    }

    @Override
    public void initView() {
        initTitle(getString((R.string.feedback)));
        back_Image.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.image_title_left})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_title_left:
                finish();
                break;
        }
    }


}
