package com.sharechain.finance.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import com.sharechain.finance.R;

import java.io.ByteArrayOutputStream;

/**
 * Created by ${zhoutao} on 2017/12/13 0013.
 */

public class BaseUtils {

    private static final String NEWS_ITEM_SPECIAL = "special";
    private static final String NEWS_ITEM_PHOTO_SET = "photoset";


    /**
     * 复制评论
     *
     * @param content 文本内容
     * @param context 资源管理器
     */
    public static void copyComment(String content, Context context) {
        // 得到剪贴板管理器
        ClipData clipData = ClipData.newPlainText("text", content);
        ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clip.setPrimaryClip(clipData);
    }



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

    /**
     * Tests if a code point is "whitespace" as defined in the HTML spec.
     *
     * @param c code point to test
     * @return true if code point is whitespace, false otherwise
     */
    public static boolean isWhitespace(int c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\f' || c == '\r';
    }

    /**
     * Tests if a string is blank: null, emtpy, or only whitespace (" ", \r\n, \t, etc)
     *
     * @param string string to test
     * @return if string is blank
     */
    public static boolean isEmpty(String string) {
        if (string == null || string.length() == 0)
            return true;

        int l = string.length();
        for (int i = 0; i < l; i++) {
            if (!isWhitespace(string.codePointAt(i)))
                return false;
        }
        return true;
    }


    /**
     * 正则校验 验证表单
     *
     * @param value
     * @param reg
     * @return
     */
    public static boolean valid(String value, String reg) {
        if (value == null || reg == null) {
            return false;
        }
        return value.matches(reg);
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * dip单位转换成像素单位
     *
     * @param context  上下文对象
     * @param dipValue 需要转换的值
     * @return
     * @author Jeff
     */
    public static int dip2px(Context context, float dipValue) {
        if (context == null) {
            return 1;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 创建控件半园形效果
     *
     * @param strokeWidth 边框宽
     * @param storkeColor 边框辨色
     * @param roundRadius 圆角半径
     * @param fillColor   填充颜色
     * @return
     * @author Aaron
     */
    public static Drawable createGradientDrawable(int strokeWidth, int storkeColor, int roundRadius, int fillColor) {
        GradientDrawable drawable = new GradientDrawable();//创建drawable
        drawable.setColor(fillColor);
        drawable.setCornerRadius(roundRadius);
        drawable.setStroke(strokeWidth, storkeColor);
        return drawable;
    }

    public static Drawable createRectDrawable(int fillColor, int roundRadius) {
        return createGradientDrawable(0, fillColor, roundRadius, fillColor);
    }

}
