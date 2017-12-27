package com.sharechain.finance.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.lang.ref.WeakReference;

/**
 * Created by Chu on 2017/12/27.
 */

public class MyBannerViewPager extends ViewPager {
    private AutoPlayTask mAutoPlayTask;
    private int mAutoPlayInterval = 3000;
    private boolean mAutoPlayAble = true;
    private int viewSize = 1;

    public MyBannerViewPager(@NonNull Context context) {
        super(context);
        mAutoPlayTask = new AutoPlayTask(this);
    }

    public MyBannerViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mAutoPlayTask = new AutoPlayTask(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mAutoPlayAble) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    stopAutoPlay();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    startAutoPlay();
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setViewSize(int viewSize) {
        this.viewSize = viewSize;
    }

    private static class AutoPlayTask implements Runnable {
        private final WeakReference<MyBannerViewPager> mBanner;

        private AutoPlayTask(MyBannerViewPager banner) {
            mBanner = new WeakReference<>(banner);
        }

        @Override
        public void run() {
            MyBannerViewPager banner = mBanner.get();
            if (banner != null) {
                banner.switchToNextPage();
                banner.startAutoPlay();
            }
        }
    }

    /**
     * 切换到下一页
     */
    private void switchToNextPage() {
        setCurrentItem(getRealCurrentItem() + 1);
    }

    public int getRealCurrentItem() {
        return getCurrentItem() % viewSize;
    }

    public void stopAutoPlay() {
        if (mAutoPlayTask != null) {
            removeCallbacks(mAutoPlayTask);
        }
    }

    public void startAutoPlay() {
        stopAutoPlay();
        if (mAutoPlayAble) {
            postDelayed(mAutoPlayTask, mAutoPlayInterval);
        }
    }

}
