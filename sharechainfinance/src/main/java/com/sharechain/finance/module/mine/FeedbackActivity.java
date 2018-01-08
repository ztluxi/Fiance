package com.sharechain.finance.module.mine;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

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
    @BindView(R.id.feedback_rl)
    RelativeLayout feedbackRl;
    @BindView(R.id.submit_btn)
    Button submitBtn;

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
        initListen();
    }

    private void initListen() {
        phoneEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLayoutListener(feedbackRl,submitBtn);
            }
        });

        phoneEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    addLayoutListener(feedbackRl,submitBtn);
                }
            }
        });
    }


    /**
     *  1、获取main在窗体的可视区域
     *  2、获取main在窗体的不可视区域高度
     *  3、判断不可视区域高度
     *      1、大于100：键盘显示  获取Scroll的窗体坐标
     *                           算出main需要滚动的高度，使scroll显示。
     *      2、小于100：键盘隐藏
     *
     * @param main 根布局
     * @param scroll 需要显示的最下方View
     */
    public void addLayoutListener(final View main, final View scroll) {
        main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                main.getWindowVisibleDisplayFrame(rect);
                int mainInvisibleHeight = main.getRootView().getHeight() - rect.bottom;
                if (mainInvisibleHeight > 100) {
                    int[] location = new int[2];
                    scroll.getLocationInWindow(location);
                    int srollHeight = (location[1] + scroll.getHeight()) - rect.bottom;
                    main.scrollTo(0, srollHeight);
                } else {
                    main.scrollTo(0, 0);
                }
            }
        });
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
                if (firstTime == 0) {
                    putFeedBack(content, phone);
                } else {
                    long result =  nowTime - firstTime;
                    //如果时间小于半小时内就不能提交
//                    1800000
                    if (result  < 1800000) {
                        ToastManager.showShort(this, getString(R.string.only_once));
                    }else {
                        putFeedBack(content, phone);
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
