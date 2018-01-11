package com.sharechain.finance;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sharechain.finance.utils.BaseUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by ${zhoutao} on 2017/12/20 0020.
 */

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
//        if (SharedPreferenceManager.ifFristOpenSalonAPP(SplashActivity.this)) {
//            BaseUtils.openActivity(SplashActivity.this, GuideActivity.class, null);
//            finish();
//        } else {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BaseUtils.openActivity(SplashActivity.this, MainActivity.class, null);
                finish();
            }
        }, 2000);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
