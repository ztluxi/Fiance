package com.sharechain.finance.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.sharechain.finance.R;

import butterknife.BindView;

/**
 * 点击翻译弹窗
 * Created by ${zhoutao} on 2017/12/25 0025.
 */

public class PopOptionUtil {
    private Button btnCopy;
    private Button btnTranslate;
    private Context mContext;
    private int popupWidth;
    private int popupHeight;
    private PopupWindow popupWindow;

    public interface PopClickEvent {
        void onPreClick();
        void onNextClick();
    }

    private PopClickEvent popClickEvent;

    public void setOnPopClickEvent(PopClickEvent mEvent) {
        this.popClickEvent = mEvent;
    }

    public PopOptionUtil(Context context) {
        mContext = context;
        View popupView = LayoutInflater.from(mContext).inflate(R.layout.pop, null);
        btnTranslate = popupView.findViewById(R.id.btn_translate);
        btnCopy = popupView.findViewById(R.id.btn_copy);
        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWidth = popupView.getMeasuredWidth();
        popupHeight = popupView.getMeasuredHeight();

    }

    public void setTextView(String text) {
        btnTranslate.setText(text);
    }

    public void show(View view) {
        initEvent();
//        if (Build.VERSION.SDK_INT < 24) {
//            popupWindow.showAsDropDown(view);
//        } else {
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            //在控件上方显示
            popupWindow.showAtLocation(view, Gravity.TOP, (location[0]) - popupWidth / 2, location[1] - popupHeight);
//        }
    }

    public void dismiss() {
        popupWindow.dismiss();
    }


    private void initEvent() {

        if (popClickEvent != null) {
            btnCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popClickEvent.onPreClick();
                }
            });
            btnTranslate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popClickEvent.onNextClick();
                }
            });
        }
    }

}
