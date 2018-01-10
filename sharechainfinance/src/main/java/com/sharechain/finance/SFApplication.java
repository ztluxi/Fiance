package com.sharechain.finance;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.sharechain.finance.bean.FastMsgData;
import com.sharechain.finance.bean.LoginDataBean;
import com.sharechain.finance.module.mine.UpgradeActivity;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.tencent.bugly.beta.upgrade.UpgradeListener;
import com.umeng.commonsdk.UMConfigure;
import com.zhy.http.okhttp.OkHttpUtils;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.BasicPushNotificationBuilder;
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
     * 设置我的主页消息小红点
     */
    public static boolean mineNews = false;
    /**
     * 维护Activity 的list
     */
    private static List<Activity> mActivitys = Collections
            .synchronizedList(new LinkedList<Activity>());
    public static String WX_APPID = "wx07fd5363e3b104ef";
    public static String WX_APPSECRET = "683296feadec6cfb9768e38271430238";
    public static String WX_LOGIN_STATE = "wx_login_state";
    public static FastMsgData shareData;//首页热讯分享数据
    public static LoginDataBean loginDataBean;//登录数据

    public static SFApplication get(Context context) {
        return (SFApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Bugly

        initBugly();

        initJPush();
        initUmeng();
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
//        YouDaoApplication.init(this, "3fb99b9987d450cf");//创建应用，每个应 //判断是否登录
        List<LoginDataBean> users = DataSupport.findAll(LoginDataBean.class);
        if (users.size() > 0) {
            //已登录
            loginDataBean = users.get(0);
        }
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    private void initJPush() {
        //极光推送
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(this);
        builder.statusBarDrawable = R.drawable.jpush_notification_icon;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
                | Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
        builder.notificationDefaults = Notification.DEFAULT_SOUND
                | Notification.DEFAULT_VIBRATE
                | Notification.DEFAULT_LIGHTS;  // 设置为铃声、震动、呼吸灯闪烁都要
        JPushInterface.setPushNotificationBuilder(1, builder);
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
    }

    private void initBugly() {

        Bugly.init(getApplicationContext(), "a01e9ba003", false);

        /**** Beta高级设置*****/
        /**
         * true表示app启动自动初始化升级模块；
         * false不好自动初始化
         * 开发者如果担心sdk初始化影响app启动速度，可以设置为false
         * 在后面某个时刻手动调用
         */
        Beta.autoInit = true;

        /**
         * true表示初始化时自动检查升级
         * false表示不会自动检查升级，需要手动调用Beta.checkUpgrade()方法
         */
        Beta.autoCheckUpgrade = true;

        /**
         * 设置升级周期为60s（默认检查周期为0s），60s内SDK不重复向后台请求策略
         */
        Beta.initDelay = 1 * 1000;

        /**
         * 设置通知栏大图标，largeIconId为项目中的图片资源；
         */
        Beta.largeIconId = R.drawable.logo;

        /**
         * 设置状态栏小图标，smallIconId为项目中的图片资源id;
         */
        Beta.smallIconId = R.drawable.logo;


        /**
         * 设置更新弹窗默认展示的banner，defaultBannerId为项目中的图片资源Id;
         * 当后台配置的banner拉取失败时显示此banner，默认不设置则展示“loading“;
         */
        Beta.defaultBannerId = R.drawable.logo;

        /**
         * 设置sd卡的Download为更新资源保存目录;
         * 后续更新资源会保存在此目录，需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;
         */
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        /**
         * 点击过确认的弹窗在APP下次启动自动检查更新时会再次显示;
         */
        Beta.showInterruptedStrategy = false;

        /**
         * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗;
         * 不设置会默认所有activity都可以显示弹窗;
         */
        Beta.canShowUpgradeActs.add(MainActivity.class);


//        /**
//         *  设置自定义升级对话框UI布局
//         *  注意：因为要保持接口统一，需要用户在指定控件按照以下方式设置tag，否则会影响您的正常使用：
//         *  标题：beta_title，如：android:tag="beta_title"
//         *  升级信息：beta_upgrade_info  如： android:tag="beta_upgrade_info"
//         *  更新属性：beta_upgrade_feature 如： android:tag="beta_upgrade_feature"
//         *  取消按钮：beta_cancel_button 如：android:tag="beta_cancel_button"
//         *  确定按钮：beta_confirm_button 如：android:tag="beta_confirm_button"
//         *  详见layout/upgrade_dialog.xml
//         */
//        Beta.upgradeDialogLayoutId = R.layout.dialog_update;

        /**
         * 自定义Activity参考，通过回调接口来跳转到你自定义的Actiivty中。
         */
        Beta.upgradeListener = new UpgradeListener() {
            @Override
            public void onUpgrade(int ret, UpgradeInfo strategy, boolean isManual, boolean isSilence) {
                if (strategy != null) {
                    Intent i = new Intent();
                    i.setClass(getApplicationContext(), UpgradeActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "没有更新", Toast.LENGTH_SHORT).show();
                }
            }
        };

    }

    private void initUmeng() {
        /**
         * 初始化common库
         * 参数1:上下文，不能为空
         * 参数2:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
         * 参数3:Push推送业务的secret
         */
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "");
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
