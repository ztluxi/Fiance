package com.sharechain.finance.module.mogul;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.sharechain.finance.R;
import com.sharechain.finance.SFApplication;
import com.sharechain.finance.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 查看大图
 * Created by ${zhoutao} on 2018/1/3 0022.
 */

public class DragPhotoActivity extends AppCompatActivity {
    @BindView(R.id.photo_vp)
    CustomViewPager viewPager;
    private PhotoViewPager<String> mAdapter;
    private List<String> mDatas = new ArrayList<>();


    /**
     * viewpagge 指引页面改监听器
     */
    class GuidePageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_photo_gallery);
        ButterKnife.bind(this);
        //获取当前点击图片位置
        final int position = getIntent().getIntExtra("index", 0);
        //获取图片集合
        List<String> data = getIntent().getStringArrayListExtra("datas");
        if (data != null) {
            mDatas.addAll(data);
        }

        mAdapter = new PhotoViewPager<>(this);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(position);
        viewPager.addOnPageChangeListener(new GuidePageChangeListener());
    }

    class PhotoViewPager<T> extends PagerAdapter {
        private Activity mContext;
        private LayoutInflater inflater;

        public PhotoViewPager(Activity context) {
            this.mContext = context;
            inflater = LayoutInflater.from(context);
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
                PhotoView iv = (PhotoView) v.findViewById(R.id.img_pv);
                if (null != iv) {
                    GlideUtils.recyclerviewImageBitmap(iv, DragPhotoActivity.this);
                }
            }
            ((ViewPager) container).removeView((View) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
            if (mDatas.size() <= position) {
                return null;
            }
            View view = inflater.inflate(R.layout.adapter_photo_gallery_item, container, false);
            final PhotoView photoView = view.findViewById(R.id.img_pv);
            FrameLayout parent = view.findViewById(R.id.parent);
            // 获取图片信息
            photoView.enable();
            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.finish();
                }
            });
            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.finish();
                }
            });
            String url = mDatas.get(position);
            final View mView = view;
            Glide.with(DragPhotoActivity.this).asBitmap().load(url).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    float bitmapWidth = resource.getWidth();
                    float bitmapHeight = resource.getHeight();
                    float radius = bitmapWidth / bitmapHeight;
                    int height = (int) (SFApplication.screen_width / radius);

                    photoView.setImageBitmap(resource);
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
                    params.gravity = Gravity.CENTER;
                    photoView.setLayoutParams(params);
                    container.addView(mView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                }

                @Override
                public void onLoadStarted(@Nullable Drawable placeholder) {
                    super.onLoadStarted(placeholder);
                    photoView.setImageResource(R.drawable.home_default);
                }
                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    super.onLoadFailed(errorDrawable);
                    photoView.setImageResource(R.drawable.home_default);
                }
            });
            return view;
        }
    }
}
