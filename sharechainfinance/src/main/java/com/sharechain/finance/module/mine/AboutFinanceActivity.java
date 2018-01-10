package com.sharechain.finance.module.mine;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharechain.finance.BaseActivity;
import com.sharechain.finance.R;
import com.sharechain.finance.utils.BaseUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ${zhoutao} on 2017/12/19 0013.
 */

public class AboutFinanceActivity extends BaseActivity {
    @BindView(R.id.image_title_left)
    ImageView back_Image;
    @BindView(R.id.version_tv)
    TextView version_tv;

    @Override
    public int getLayout() {
        return R.layout.activity_about_finance;
    }

    @Override
    public void initView() {
        initData();
        initTitle(getString(R.string.app_name));
        back_Image.setVisibility(View.VISIBLE);
        version_tv.setText(BaseUtils.getVersionName(this));
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
