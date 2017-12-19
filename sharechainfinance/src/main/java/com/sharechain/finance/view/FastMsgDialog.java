package com.sharechain.finance.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharechain.finance.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @OnClick(R.id.image_close)
    void closeDialog() {
        dismiss();
    }

    @OnClick(R.id.image_weixin)
    void gotoShareWeixin() {

    }

    @OnClick(R.id.image_circle)
    void gotoShareWeixinCircle() {

    }

    @OnClick(R.id.image_download)
    void gotoDownload() {

    }

    public FastMsgDialog(@NonNull Context context) {
        super(context, R.style.base_dialog_style);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_fast_msg_dialog);
        ButterKnife.bind(this);
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
}
