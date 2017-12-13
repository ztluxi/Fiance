package com.sharechain.finance.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sharechain.finance.R;

/**
 * Created by ${zhoutao} on 2017/12/13 0013.
 */

public class BaseUtils {

    private static final String NEWS_ITEM_SPECIAL = "special";
    private static final String NEWS_ITEM_PHOTO_SET = "photoset";
    /**
     * @param skipType
     * @return
     */
    public static boolean isNewsSpecial(String skipType) {
        return NEWS_ITEM_SPECIAL.equals(skipType);
    }

    public static boolean isNewsPhotoSet(String skipType) {
        return NEWS_ITEM_PHOTO_SET.equals(skipType);
    }

    /**
     * 带参数的页面跳转
     *
     * @param context
     * @param pClass
     * @param pBundle
     * @author Jeff
     */
    public static void openActivity(Activity context, Class<?> pClass, Bundle pBundle) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
    }

    /**
     * 带参数的页面跳转
     *
     * @param context
     * @param pClass
     * @param pBundle
     * @author Jeff
     */
    public static void openActivityBottomAnim(Activity context, Class<?> pClass, Bundle pBundle) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.fragment_slide_bottom_enter, R.anim.fragment_slide_bottom_exit);
    }

}
