package com.sharechain.finance.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ${zhoutao} on 2017/12/20 0020.
 */

public class SharedPreferenceManager {
    /**
     * 判断APP是否是第一次登录 如果是则返回true 否则返回false
     *
     * @param context
     * @return
     */
    public static boolean ifFristOpenSalonAPP(Context context) {
        boolean isFirst = true;
        SharedPreferences sp = context.getSharedPreferences("users", 0);
        isFirst = sp.getBoolean("isFirst", true);
        if (isFirst) {
            SharedPreferences.Editor ed = sp.edit();
            ed.putBoolean("isFirst", false);
//            ed.putString("UUID", Utils.getDevideUUID(context));//保存UUID
            ed.apply();
            return true;
        }
        return false;
    }

    /**
     * 保存第一次反馈意见的时间
     * @param context
     * @param time
     */
    public static void saveFeedBackTime(Context context,long time){
        SharedPreferences mSharedPreferences = context.getSharedPreferences("feedBookTime", context.MODE_WORLD_READABLE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong("Time", time);
        editor.commit();
    }
    public static long getFeedBackTime(Context context){
        SharedPreferences share = context.getSharedPreferences("feedBookTime",context.MODE_WORLD_READABLE);
        long feedTime=share.getLong("Time",0);
        return feedTime;
    }
}
