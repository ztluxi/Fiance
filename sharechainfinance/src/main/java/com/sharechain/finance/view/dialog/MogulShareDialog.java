package com.sharechain.finance.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.sharechain.finance.R;
import com.sharechain.finance.SFApplication;
import com.sharechain.finance.utils.GlideUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * *Created by ${zhoutao} on 2017/12/24 0013.
 */

public class MogulShareDialog extends Dialog {
    private Context context;
    @BindView(R.id.text_name)
    TextView text_namet;
    @BindView(R.id.text_position)
    TextView text_position;
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

    public MogulShareDialog(@NonNull Context context) {
        super(context, R.style.base_dialog_style);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_mogul_share_dialog);
        ButterKnife.bind(this);
    }

    public void setHead(Context context,String url){
        ImageView imageView = findViewById(R.id.mogul_iv);

        RequestOptions headOptions = new RequestOptions().placeholder(R.drawable.history).circleCrop();
        GlideUtils.loadUserImage(SFApplication.get(context),url,imageView, headOptions);

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
