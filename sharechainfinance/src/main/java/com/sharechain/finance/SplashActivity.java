package com.sharechain.finance;

import com.sharechain.finance.utils.BaseUtils;
import com.sharechain.finance.utils.SharedPreferenceManager;

/**
 * Created by ${zhoutao} on 2017/12/20 0020.
 */

public class SplashActivity extends BaseActivity {
    @Override
    public int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {
        if (SharedPreferenceManager.ifFristOpenSalonAPP(SplashActivity.this)){
            BaseUtils.openActivityBottomAnim(SplashActivity.this, GuideActivity.class, null);
            finish();
        }else {
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    BaseUtils.openActivityBottomAnim(SplashActivity.this, MainActivity.class, null);
                    finish();
                }
            }, 2000);
        }
    }
    @Override
    public void initData() {

    }

}
