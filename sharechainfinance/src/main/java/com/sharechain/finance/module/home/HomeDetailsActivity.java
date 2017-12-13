package com.sharechain.finance.module.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.sharechain.finance.BaseActivity;

/**
 * Created by ${zhoutao} on 2017/12/13 0013.
 */

public class HomeDetailsActivity  extends BaseActivity{

    private static final String PHOTO_SET_KEY = "PhotoSetKey";
    public static void launch(Context context, String photoId) {
        Intent intent = new Intent(context, HomeDetailsActivity.class);
        intent.putExtra(PHOTO_SET_KEY, photoId);
        context.startActivity(intent);
//        ((Activity)context).overridePendingTransition(R.anim.slide_right_entry, R.anim.hold);
    }


    @Override
    public int getLayout() {
        return 0;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
