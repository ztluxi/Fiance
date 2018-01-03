package com.sharechain.finance.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.sharechain.finance.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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

    //    i:128;s:92:\"http://www.weilaicaijing.com/wp-content/uploads/2017/12/avatar_user_3_1513218915-128x128.jpg\";
    public static String getSubImageUrl(String imageUrl, String firstTag, String lastTag) {
        if (BaseUtils.isEmpty(imageUrl)) {
            return "";
        }
        if (imageUrl.contains(firstTag) && imageUrl.contains(lastTag)) {
            int first = imageUrl.indexOf(firstTag) + firstTag.length();
            int last = imageUrl.indexOf(lastTag);
            String tmp = imageUrl.substring(first, last).replace("\"", "");
            return tmp;
        }
        return "";
    }

    /**
     * 把View绘制到Bitmap上
     *
     * @param comBitmap 需要绘制的View
     * @param width     该View的宽度
     * @param height    该View的高度
     * @return 返回Bitmap对象
     * add by csj 13-11-6
     */
    public static Bitmap getViewBitmap(View comBitmap, int width, int height) {
        Bitmap bitmap = null;
        if (comBitmap != null) {
            comBitmap.clearFocus();
            comBitmap.setPressed(false);

            boolean willNotCache = comBitmap.willNotCacheDrawing();
            comBitmap.setWillNotCacheDrawing(false);

            // Reset the drawing cache background color to fully transparent
            // for the duration of this operation
            int color = comBitmap.getDrawingCacheBackgroundColor();
            comBitmap.setDrawingCacheBackgroundColor(0);
            float alpha = comBitmap.getAlpha();
            comBitmap.setAlpha(1.0f);

            if (color != 0) {
                comBitmap.destroyDrawingCache();
            }

            int widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
            comBitmap.measure(widthSpec, heightSpec);
            comBitmap.layout(0, 0, width, height);

            comBitmap.buildDrawingCache();
            Bitmap cacheBitmap = comBitmap.getDrawingCache();
            if (cacheBitmap == null) {
                Logger.e("view.ProcessImageToBlur", "failed getViewBitmap(" + comBitmap + ")",
                        new RuntimeException());
                return null;
            }
            bitmap = Bitmap.createBitmap(cacheBitmap);
            // Restore the view
            comBitmap.setAlpha(alpha);
            comBitmap.destroyDrawingCache();
            comBitmap.setWillNotCacheDrawing(willNotCache);
            comBitmap.setDrawingCacheBackgroundColor(color);
        }
        return bitmap;
    }

    private static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
        return perm == 0;
    }

    public static File getOwnCacheDirectory(Context context, String cacheDir) {
        File appCacheDir = null;
        if ("mounted".equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            appCacheDir = new File(Environment.getExternalStorageDirectory(), cacheDir);
        }

        if (appCacheDir == null || !appCacheDir.exists() && !appCacheDir.mkdirs()) {
            appCacheDir = context.getCacheDir();
        }

        return appCacheDir;
    }

    /**
     * 保存图片到系统图库
     *
     * @param context
     * @param bmp
     * @return
     */
    public static boolean saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File mFile = getOwnCacheDirectory(context, "shareImage");
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(mFile, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            return isSuccess;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


}
