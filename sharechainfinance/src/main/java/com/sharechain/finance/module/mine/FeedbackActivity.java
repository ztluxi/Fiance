package com.sharechain.finance.module.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.andview.refreshview.utils.Utils;
import com.sharechain.finance.BaseActivity;
import com.sharechain.finance.R;
import com.sharechain.finance.utils.BaseUtils;
import com.sharechain.finance.utils.NetworkManager;
import com.sharechain.finance.utils.ToastManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ${zhoutao} on 2017/12/19 0013.
 */

public class FeedbackActivity extends BaseActivity {
    @BindView(R.id.image_title_left)
    ImageView back_Image;
    @BindView(R.id.write_feedback_et)
    EditText writeFeedbackEt;
    @BindView(R.id.phone_et)
    EditText phoneEt;

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

    @OnClick({R.id.image_title_left,R.id.submit_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_title_left:
                finish();
                break;
            case R.id.submit_btn:
                String content = writeFeedbackEt.getText().toString();
                String phone = phoneEt.getText().toString();
                if (BaseUtils.isEmpty(content)){
                    ToastManager.showShort(this,getString(R.string.please_write_your_suggest));
                    return;
                }
                if (BaseUtils.isEmpty(phone)){
                    ToastManager.showShort(this,getString(R.string.please_write_your_phone_number));
                    return;
                }

                if (!NetworkManager.isConnnected(this)){
                    ToastManager.showShort(this,getString(R.string.please_check_network));
                    return;
                }

                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
