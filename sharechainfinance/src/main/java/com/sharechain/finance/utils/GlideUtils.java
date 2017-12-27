package com.sharechain.finance.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sharechain.finance.SFApplication;

/**
 * Created by ${zhoutao} on 2017/12/19 0019.
 */

public class GlideUtils {


    /**
     * 加载图像
     * @param context
     * @param url
     * @param imageView
     * @param options
     */
    public static void loadUserImage(Context context,
                            String url,
                            ImageView imageView,
                            RequestOptions options) {
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }

//    /**
//     * 回收图片资源
//     */
//    public static final void recyclerviewImageBitmap(@NonNull ImageView iv,Context context) {
//        iv.setImageDrawable(null);
//        Glide.with(SFApplication.get(context)).clear(iv);
//    }
//
//    /**
//     * 回收资源
//     */
//    public static void recycleMemory(@NonNull View view) {
//        view.setTag(null);
//        if (view instanceof ViewGroup) {
//            ViewGroup v = (ViewGroup) view;
//            if (v.getChildCount() > 0) {
//                for (int i = 0; i < v.getChildCount(); i++) {
//                    recycleMemory(v.getChildAt(i));
//                }
//            }
//        } else if (view instanceof ImageView) {
//            recyclerviewImageBitmap((ImageView) view);
//        }
//    }

}
