package com.sharechain.finance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.sharechain.finance.bean.FollowBean;
import com.sharechain.finance.bean.LoginDataBean;
import com.sharechain.finance.bean.LoginManagerBean;
import com.sharechain.finance.module.mine.MineActivity;
import com.sharechain.finance.utils.ToastManager;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

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
        try {
            JSONObject object = new JSONObject(response);
            if (object.has("code") && object.optInt("code") == 1000) {
                ToastManager.showShort(context, object.optString("msg"));
                context.startActivity(new Intent(context, MineActivity.class));
                Activity activity = (Activity) context;
                activity.finish();
            } else {
                onSuccess(response);
            }
        } catch (JSONException e) {
            onSuccess(response);
            e.printStackTrace();
        }
    }

    protected abstract void onSuccess(String result);

    protected abstract void onFailed(String errStr);

}
