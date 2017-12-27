package com.sharechain.finance.module.mogul;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by ${zhoutao} on 2017/12/22 0022.
 */

public class CustomViewPager extends ViewPager {

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public boolean onInterceptTouchEvent(MotionEvent ev) {
		try {
			return super.onInterceptTouchEvent(ev);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return false;
		}
	}

}
