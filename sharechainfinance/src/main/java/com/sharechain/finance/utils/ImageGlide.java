package com.sharechain.finance.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.text.Html;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

/**
 * Created by ${zhoutao} on 2018/1/3 0003.
 */

public class ImageGlide implements Html.ImageGetter {
    Context c;
    TextView container;

    public ImageGlide(TextView text, Context c) {
        this.c = c;
        this.container = text;
    }

    public Drawable getDrawable(String source) {
        final LevelListDrawable drawable = new LevelListDrawable();
        Glide.with(c).load(source).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                if (resource != null) {
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(String.valueOf(resource));
                    drawable.addLevel(1, 1, bitmapDrawable);
                    drawable.setBounds(0, 0, resource.getIntrinsicWidth(), resource.getIntrinsicHeight());
                    drawable.setLevel(1);
                    container.invalidate();
                    container.setText(container.getText());
                }
            }
        });
        return drawable;
    }

}