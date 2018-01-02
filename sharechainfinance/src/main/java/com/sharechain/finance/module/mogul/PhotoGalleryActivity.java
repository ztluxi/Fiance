package com.sharechain.finance.module.mogul;

import android.content.Intent;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sharechain.finance.BaseActivity;
import com.sharechain.finance.R;
import com.sharechain.finance.adapter.PhotoViewPager;
import com.sharechain.finance.utils.BaseUtils;
import com.sharechain.finance.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 查看图片时的 画廊类
 * Created by ${zhoutao} on 2017/12/22 0022.
 */

public class PhotoGalleryActivity extends BaseActivity {
//    @BindView(R.id.image_title_left)
//    ImageView back_Image;
    @BindView(R.id.photo_vp)
    CustomViewPager viewPager;
//    @BindView(R.id.toolbar_ll)
//    LinearLayout toolbar_ll;
    private List<String> mDatas = new ArrayList<>();
    private PhotoViewPager<String> mAdapter;
    private TextView count;// 图片的页数
    private boolean isHide = false;
    /**
     * 点击是否是退出当前图片预览
     */
//    public void show(final View view) {
//        Animation animation = AnimationUtils.loadAnimation(this, R.anim.option_entry_from_top);
//        animation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                if (!PhotoGalleryActivity.this.isFinishing() && view != null) {
//                    view.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//            }
//        });
//        view.startAnimation(animation);
//    }


//    public void hide(final View view) {
//        Animation animation = AnimationUtils.loadAnimation(this, R.anim.option_entry_from_bottom_alpha_0);
//        animation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                if (!PhotoGalleryActivity.this.isFinishing() && view != null) {
//                    view.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//            }
//        });
//        view.startAnimation(animation);
//    }


    /**
     * viewpagge 指引页面改监听器
     */
    class GuidePageChangeListener implements OnPageChangeListener {

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
            if (count != null && mDatas != null) {
                count.setText((position + 1) + "/" + mDatas.size());
            }
        }

    }

    @Override
    public void finish() {
        if (viewPager != null) {
            Intent i = new Intent();
            i.putExtra("index", viewPager.getCurrentItem());
            setResult(RESULT_OK, i);
        }
        super.finish();
    }

    @Override
    protected void onDestroy() {
        if (mDatas != null && !mDatas.isEmpty()) {
            mDatas.clear();
        }
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
            mAdapter.setOnPhotoCallback(null);
        }
        if (viewPager != null) {
//            GlideUtils.recycleMemory(viewPager);
        }
        super.onDestroy();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_photo_gallery;
    }



    @Override
    public void initView() {
//        back_Image.setVisibility(View.VISIBLE);
        String title = getIntent().getStringExtra("title");
        //获取当前点击图片位置
        int position = getIntent().getIntExtra("index", 0);
        if (BaseUtils.isEmpty(title)) {
            title = getString(R.string.picture_preview);
        }
//        initTitle(title);
        //获取图片集合
        List<String> data = getIntent().getStringArrayListExtra("datas");
        isHide = getIntent().getBooleanExtra("view", false);
        if (data != null) {
            mDatas.addAll(data);
        }
//        if (!isHide) {
//            toolbar_ll.setVisibility(View.VISIBLE);
//        }

        mAdapter = new PhotoViewPager<>(this, mDatas);
        viewPager.setAdapter(mAdapter);
//        mAdapter.setOnPhotoCallback(new PhotoViewPager.OnPhotoClick() {
//            @Override
//            public synchronized void onPhotoClick() {
//                if (toolbar_ll.getVisibility() == View.VISIBLE) {
//                    if (!isHide) {
//                        hide(toolbar_ll);
//                    } else {
//                        PhotoGalleryActivity.this.finish();
//                    }
//
//                } else {
//                    if (!isHide) {
//                        show(toolbar_ll);
//                    } else {
//                        PhotoGalleryActivity.this.finish();
//                    }
//                }
//            }
//        });
//        viewPager.setCurrentItem(position);
        viewPager.addOnPageChangeListener(new GuidePageChangeListener());
    }

    @Override
    public void initData() {

    }

//    @OnClick({R.id.image_title_left})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.image_title_left:
//                finish();
//                break;
//        }
//    }
}
