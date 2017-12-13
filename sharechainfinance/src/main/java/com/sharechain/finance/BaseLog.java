package com.sharechain.finance;

import android.util.Log;

/**
 * Created by chu on 2017/6/1.
 */

public class BaseLog {

    private static boolean isShowLog = true;

    public static void d(String tag, String str) {
        if (isShowLog) {
            Log.d(tag, str);
        }
    }

    public static void d(String str) {
        if (isShowLog) {
            Log.d("chu", str);
        }
    }

}
