package com.sharechain.finance.utils;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * *Created by ${zhoutao} on 2017/12/14 0013.
 */
public class ToastManager {
    private static CharSequence oldMsg;
    protected static Toast toast = null;
    private static long oneTime = 0;
    private static long twoTime = 0;

    private ToastManager() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * Toast核心方法 避免相同Toast内容重复出现
     *
     * @param mContext 上下文
     * @param message  弹出信息
     * @param isShort  短时间
     */
    public static void showToast(Context mContext, CharSequence message, boolean isShort) {
        if (null == message || null == mContext || message.length() == 0)
            return;
        if (toast == null) {
            toast = Toast.makeText(mContext, message, isShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (message.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = message;
                toast.setText(message);
                toast.show();
            }
        }
        oneTime = twoTime;
    }

    /**
     * 在handler里面弹出信息
     *
     * @param context 上下文
     * @param handler handler句柄
     * @param msg     内容
     */
    public static void showToast(final Context context, Handler handler, final CharSequence msg) {
        handler.post(new Runnable() {
            public void run() {
                if (context != null) {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * @param mContext 上下文
     * @param resId    内容
     * @param isShort  短时间
     */
    public static void showToast(Context mContext, int resId, boolean isShort) {
        showToast(mContext, mContext.getResources().getString(resId), isShort);
    }

    /**
     * 短时间
     *
     * @param mContext 上下文
     * @param message  内容
     */
    public static void showShort(Context mContext, CharSequence message) {
        showToast(mContext, message, true);
    }

    /**
     * 短时间
     *
     * @param mContext 上下文
     * @param resId    资源string里面的内容
     */
    public static void showShort(Context mContext, int resId) {
        showToast(mContext, resId, true);
    }

    /**
     * 长时间
     *
     * @param mContext 上下文
     * @param message  内容
     */
    public static void showLong(Context mContext, CharSequence message) {
        showToast(mContext, message, false);
    }

    /**
     * 长时间
     *
     * @param mContext 上下文
     * @param resId    资源string里面的内容
     */
    public static void showLong(Context mContext, int resId) {
        showToast(mContext, resId, false);
    }

}
