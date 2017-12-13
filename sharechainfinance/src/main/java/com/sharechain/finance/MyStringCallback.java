package com.sharechain.finance;

import android.content.Context;

import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/12/12.
 */

public abstract class MyStringCallback extends StringCallback {
    private Context context;

    public MyStringCallback(Context context) {
        this.context = context;
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        onFailed(e.toString());
    }

    @Override
    public void onResponse(String response, int id) {
        onSuccess(response);
    }

    abstract void onSuccess(String result);

    abstract void onFailed(String errStr);

}
