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
}
