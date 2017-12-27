package com.sharechain.finance.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sharechain.finance.R;
import com.sharechain.finance.SFApplication;
import com.sharechain.finance.utils.GlideUtils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by ${zhoutao} on 2017/12/26 0026.
 */

public class PhotoViewPager<T> extends PagerAdapter {
    private List<String> mDatas;
    private Activity mContext;
    private OnPhotoClick onPhotoClick;
    private LayoutInflater inflater;

    private Drawable drawable;

    private LinkedList<View> mViewCache;

    public void setOnPhotoCallback(OnPhotoClick callback) {
        this.onPhotoClick = callback;
    }

    public PhotoViewPager(Activity context, List<String> datas) {
        this.mDatas = datas;
        this.mContext = context;
        inflater = LayoutInflater.from(context);
        drawable = context.getResources().getDrawable(R.drawable.logo);
        mViewCache = new LinkedList<>();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if (null != object && object instanceof View) {
            View v = (View) object;
            PhotoView iv = (PhotoView) v.findViewById(R.id.icon_pv);
            if (null != iv) {
//                GlideUtils.recyclerviewImageBitmap(iv);
            }
            this.mViewCache.add(v);
        }
        (container).removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if (mDatas.size() <= position) {
            return null;
        }
        View view = null;
        PhotoView photoView = null;
        ProgressBar pb = null;
        if (mViewCache.isEmpty()) {
            view = inflater.inflate(R.layout.adapter_photo_gallery_item, container, false);
            photoView = (PhotoView) view.findViewById(R.id.icon_pv);
            pb = (ProgressBar) view.findViewById(R.id.progress_bar);
        } else {
            view = mViewCache.removeFirst();
            photoView = (PhotoView) view.findViewById(R.id.icon_pv);
            pb = (ProgressBar) view.findViewById(R.id.progress_bar);
        }
        photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float v, float v1) {

            }

            @Override
            public void onOutsidePhotoTap() {

            }
        });
        String url = mDatas.get(position);
        RequestOptions options = new RequestOptions().placeholder(R.drawable.logo).error(R.drawable.logo).centerInside();
        GlideUtils.loadUserImage(SFApplication.get(mContext), url, photoView, options);
        container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return view;
    }

    public interface OnPhotoClick {
        void onPhotoClick();
    }

    interface ProgressListener {
        void update(long bytesRead, long contentLength, boolean done);
    }
}
