package com.sharechain.finance.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

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

}
