package com.sharechain.finance.module.mogul;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.RequestOptions;
import com.sharechain.finance.R;
import com.sharechain.finance.utils.GlideUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
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
    private PhotoView photoView;


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
        private LinkedList<View> mViewCache;

        public PhotoViewPager(Activity context) {
            this.mContext = context;
            inflater = LayoutInflater.from(context);
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
                PhotoView iv = (PhotoView) v.findViewById(R.id.img_pv);
                if (null != iv) {
                    GlideUtils.recyclerviewImageBitmap(iv,DragPhotoActivity.this);
                }
                this.mViewCache.add(v);
            }
            ((ViewPager) container).removeView((View) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
            if (mDatas.size() <= position) {
                return null;
            }
            View view = null;
            if (mViewCache.isEmpty()) {
                view = inflater.inflate(R.layout.adapter_photo_gallery_item, container, false);
                photoView = view.findViewById(R.id.img_pv);
            }else {
                view = mViewCache.removeFirst();
                photoView = view.findViewById(R.id.img_pv);
            }

            // 获取图片信息
            photoView.enable();
            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.finish();
                }
            });

            String url = mDatas.get(position);
            RequestOptions options = new RequestOptions().placeholder(R.drawable.home_default).error(R.drawable.home_default).centerCrop();
			GlideUtils.getInstance().loadUserImage(DragPhotoActivity.this, url, photoView, options);
            container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return view;
        }
    }
}
