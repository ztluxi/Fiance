package com.sharechain.finance.module.mine;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;
import com.sharechain.finance.BaseActivity;
import com.sharechain.finance.MyStringCallback;
import com.sharechain.finance.R;
import com.sharechain.finance.bean.UrlList;
import com.sharechain.finance.utils.AppRegex;
import com.sharechain.finance.utils.BaseUtils;
import com.sharechain.finance.utils.NetworkManager;
import com.sharechain.finance.utils.SharedPreferenceManager;
import com.sharechain.finance.utils.ToastManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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

    //记录第一次进来成功反馈意见时间
    private long firstTime = 0;

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

    @OnClick({R.id.image_title_left, R.id.submit_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_title_left:
                finish();
                break;
            case R.id.submit_btn:
                Long nowTime = System.currentTimeMillis();
                firstTime = SharedPreferenceManager.getFeedBackTime(FeedbackActivity.this);
                if (firstTime == 0) {
                    String content = writeFeedbackEt.getText().toString();
                    String phone = phoneEt.getText().toString();
                    if (BaseUtils.isEmpty(content)) {
                        ToastManager.showShort(this, getString(R.string.please_write_your_suggest));
                        return;
                    }
                    if (!NetworkManager.isConnnected(this)) {
                        ToastManager.showShort(this, getString(R.string.please_check_network));
                        return;
                    }
                    if (phone.equals("")){
                        phone = "未填写";
                    }
                    putFeedBack(content, phone);

                } else {
                    //如果时间小于半小时内就不能提交
                    if ((nowTime - firstTime) < 1800000) {
                        ToastManager.showShort(this, getString(R.string.only_once));
                    }
                }
                break;
        }
    }

    //反馈意见
    private void putFeedBack(String content, String contact) {
        Map<String, String> map = new HashMap<>();
        map.put("content", content);
        map.put("contact", contact);
        requestPost(UrlList.FEEDBOOK, map, new MyStringCallback(this) {
            @Override
            protected void onSuccess(String result) {
                String success = "";
                String msg = "";
                try {
                    JSONObject object = new JSONObject(result);
                    success = object.getString("success");
                    msg = object.getString("msg");
                    if (success.equals("1")) {
                        ToastManager.showShort(FeedbackActivity.this, msg);
                        firstTime = System.currentTimeMillis();
                        SharedPreferenceManager.saveFeedBackTime(FeedbackActivity.this, firstTime);
                        finish();
                    } else {
                        ToastManager.showShort(FeedbackActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected void onFailed(String errStr) {
                ToastManager.showShort(FeedbackActivity.this, errStr);
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
