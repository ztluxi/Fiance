package com.sharechain.finance;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.youdao.sdk.app.YouDaoApplication;
import com.zhy.http.okhttp.OkHttpUtils;

import org.litepal.LitePal;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2017/12/12.
 */

public class SFApplication extends MultiDexApplication {

    public static int screen_width = 0;
    public static int screen_height = 0;
    public static float density = 0;
    /**
     * 维护Activity 的list
     */
    private static List<Activity> mActivitys = Collections
            .synchronizedList(new LinkedList<Activity>());
    public static String WX_APPID = "";
    public static String WX_APPSECRET = "";
    public static String WX_LOGIN_STATE = "wx_login_state";

    public static SFApplication get(Context context) {
        return (SFApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        //litePal数据库
        LitePal.initialize(this);
        WindowManager manager = (WindowManager)
                this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        density = dm.density;
        screen_width = dm.widthPixels;
        screen_height = dm.heightPixels;
        //OkHttpUtils
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS) //链接超时
                .readTimeout(10000L, TimeUnit.MILLISECONDS) //读取超时
                .build(); //其他配置
        OkHttpUtils.initClient(okHttpClient);
        //ActivityLifeLifeCycle
        registerActivityListener();
//        YouDaoApplication.init(this, "3fb99b9987d450cf");//创建应用，每个应用都会有一个Appid，绑定对应的翻译服务实例，即可使用

        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    /**
     * @param activity 作用说明 ：添加一个activity到管理里
     */
    public void pushActivity(Activity activity) {
        mActivitys.add(activity);
        BaseLog.d("activityList:size:" + mActivitys.size());
    }

    /**
     * @param activity 作用说明 ：删除一个activity在管理里
     */
    public void popActivity(Activity activity) {
        mActivitys.remove(activity);
        BaseLog.d("activityList:size:" + mActivitys.size());
    }

    /**
     * get current Activity 获取当前Activity（栈中最后一个压入的）
     */
    public static Activity currentActivity() {
        if (mActivitys == null || mActivitys.isEmpty()) {
            return null;
        }
        Activity activity = mActivitys.get(mActivitys.size() - 1);
        return activity;
    }

    /**
     * 结束指定的Activity
     */
    public static void finishActivity(Activity activity) {
        if (mActivitys == null || mActivitys.isEmpty()) {
            return;
        }
        if (activity != null) {
            mActivitys.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束所有Activity
     */
    public static void finishAllActivity() {
        if (mActivitys == null) {
            return;
        }
        for (Activity activity : mActivitys) {
            activity.finish();
        }
        mActivitys.clear();
    }

    private void registerActivityListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    pushActivity(activity);
                }

                @Override
                public void onActivityStarted(Activity activity) {

                }

                @Override
                public void onActivityResumed(Activity activity) {

                }

                @Override
                public void onActivityPaused(Activity activity) {

                }

                @Override
                public void onActivityStopped(Activity activity) {

                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    if (null == mActivitys && mActivitys.isEmpty()) {
                        return;
                    }
                    if (mActivitys.contains(activity)) {
                        popActivity(activity);
                    }
                }
            });
        }
    }

}
