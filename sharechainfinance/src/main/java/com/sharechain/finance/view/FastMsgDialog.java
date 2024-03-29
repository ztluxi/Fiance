package com.sharechain.finance.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sharechain.finance.R;
import com.sharechain.finance.SFApplication;
import com.sharechain.finance.bean.FastMsgData;
import com.sharechain.finance.utils.BaseUtils;
import com.sharechain.finance.utils.ShareUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import github.nisrulz.screenshott.ScreenShott;

/**
 * Created by Chu on 2017/12/18.
 */

public class FastMsgDialog extends Dialog {
    private Context context;
    @BindView(R.id.text_title)
    TextView text_title;
    @BindView(R.id.text_date)
    TextView text_date;
    @BindView(R.id.text_content)
    TextView text_content;
    @BindView(R.id.text_from)
    TextView text_from;
    @BindView(R.id.image_qrcode)
    ImageView image_qrcode;
    @BindView(R.id.text_uid)
    TextView text_uid;
    @BindView(R.id.text_hour)
    TextView text_hour;
    @BindView(R.id.text_week)
    TextView text_week;
    private ShareUtils shareUtils;

    @OnClick(R.id.image_close)
    void closeDialog() {
        dismiss();
    }

    @OnClick(R.id.image_weixin)
    void gotoShareWeixin() {
        Bitmap bitmap = BaseUtils.getViewBitmap(shareView, SFApplication.screen_width, SFApplication.screen_height);
        if (bitmap != null) {
            shareUtils.shareWithImage(ShareUtils.SHARE_TARGET_TYPE.TYPE_FRIEND, bitmap);
        }
        dismiss();
    }

    @OnClick(R.id.image_circle)
    void gotoShareWeixinCircle() {
        Bitmap bitmap = BaseUtils.getViewBitmap(shareView, SFApplication.screen_width, SFApplication.screen_height);
        if (bitmap != null) {
            shareUtils.shareWithImage(ShareUtils.SHARE_TARGET_TYPE.TYPE_CIRCLE, bitmap);
        }
        dismiss();
    }

    @OnClick(R.id.image_download)
    void gotoDownload() {
//        Bitmap bitmap = BaseUtils.getViewBitmap(shareView, SFApplication.screen_width, SFApplication.screen_height);
        Bitmap bitmap = ScreenShott.getInstance().takeScreenShotOfJustView(shareView);
        if (bitmap != null) {
            boolean isSuccess = BaseUtils.saveImageToGallery(context, bitmap);
            if (isSuccess) {
                Toast.makeText(context, "图片保存成功!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "图片保存失败!", Toast.LENGTH_SHORT).show();
            }
        }
        dismiss();
    }

    private FastMsgData fastMsgData;
    private View contentView;
    private View shareView;
    private boolean isWeek;

    public FastMsgDialog(@NonNull Context context, FastMsgData fastMsgData, boolean isWeek) {
        super(context, R.style.base_dialog_style);
        this.context = context;
        this.fastMsgData = fastMsgData;
        shareUtils = new ShareUtils(context);
        this.isWeek = isWeek;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView = LayoutInflater.from(context).inflate(R.layout.dialog_fast_msg_dialog, null);
        setContentView(contentView);
        ButterKnife.bind(this, contentView);
        initViews();
        initShareView();
    }

    private void initViews() {
        if (fastMsgData.getMsgType() == 1) {
            //一般消息
            text_title.setBackgroundResource(R.drawable.icon_share_blue_bg);
            text_title.setText(context.getString(R.string.fastmsg_normal));
        } else if (fastMsgData.getMsgType() == 2) {
            //重要
            text_title.setBackgroundResource(R.drawable.icon_share_orange_bg);
            text_title.setText(context.getString(R.string.fastmsg_important));
        } else if (fastMsgData.getMsgType() == 3) {
            //非常重要
            text_title.setBackgroundResource(R.drawable.icon_share_red_bg);
            text_title.setText(context.getString(R.string.fastmsg_important));
        }
        text_hour.setText(fastMsgData.getHour());

        String result = fastMsgData.getSectionText();
        String[] date = result.split(" ");
        if (isWeek) {
            if (date.length > 1) {
                text_date.setText(date[0]);
                text_week.setText(date[1]);
            } else {
                text_date.setText(result);
            }
        } else {
            text_date.setText(date[0]);
        }
        text_content.setText(fastMsgData.getDataText());
    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
    }

    private void initShareView() {
        shareView = LayoutInflater.from(context).inflate(R.layout.layout_share_fastmsg, null);
        TextView share_title = shareView.findViewById(R.id.text_title);
        TextView share_date = shareView.findViewById(R.id.text_date);
        TextView share_hour = shareView.findViewById(R.id.text_hour);
        TextView share_week = shareView.findViewById(R.id.text_week);
        TextView share_content = shareView.findViewById(R.id.text_content);
        if (fastMsgData.getMsgType() == 1) {
            //一般消息
            share_title.setBackgroundResource(R.drawable.icon_share_blue_bg_rect);
            share_title.setText(context.getString(R.string.fastmsg_normal));
        } else if (fastMsgData.getMsgType() == 2) {
            //重要
            share_title.setBackgroundResource(R.drawable.icon_share_orange_bg_rect);
            share_title.setText(context.getString(R.string.fastmsg_big));
        } else if (fastMsgData.getMsgType() == 3) {
            //非常重要
            share_title.setBackgroundResource(R.drawable.icon_share_red_bg_rect);
            share_title.setText(context.getString(R.string.fastmsg_important));
        }
        String result = fastMsgData.getSectionText();
        share_hour.setText(fastMsgData.getHour());
        String[] date = result.split(" ");
        if (date.length > 1) {
            share_date.setText(date[0]);
            share_week.setText(date[1]);
        } else {
            share_date.setText(result);
        }

        share_content.setText(fastMsgData.getDataText());
    }

    //    计算view高度
    private int getMeasureHeight() {
        if (shareView != null) {
            int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            shareView.measure(width, height);
            int viewHeight = shareView.getMeasuredHeight();
            return viewHeight;
        }
        return SFApplication.screen_height;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        contentView = null;
        shareView = null;
        SFApplication.shareData = null;
    }

}
