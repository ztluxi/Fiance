package com.sharechain.finance.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Chu on 2017/12/28.
 */

public class UserOperateCallbackViewPager extends ViewPager {
    private IUserOperating mUserOperating;

    public UserOperateCallbackViewPager(Context context) {
        super(context);
    }

    public UserOperateCallbackViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setUserOperating(IUserOperating userOperating) {
        this.mUserOperating = userOperating;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mUserOperating != null) {
                    mUserOperating.userOperating(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mUserOperating != null) {
                    mUserOperating.userOperating(false);
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mUserOperating != null) {
                    mUserOperating.userOperating(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mUserOperating != null) {
                    mUserOperating.userOperating(false);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    public interface IUserOperating {
        void userOperating(boolean isOperating);
    }

}
