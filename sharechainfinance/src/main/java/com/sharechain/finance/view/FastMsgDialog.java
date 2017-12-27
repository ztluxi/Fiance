package com.sharechain.finance.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharechain.finance.R;
import com.sharechain.finance.bean.FastMsgData;

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

    private FastMsgData fastMsgData;

    public FastMsgDialog(@NonNull Context context, FastMsgData fastMsgData) {
        super(context, R.style.base_dialog_style);
        this.context = context;
        this.fastMsgData = fastMsgData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_fast_msg_dialog, null);
        setContentView(view);
        ButterKnife.bind(this, view);
        initViews();
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
        text_date.setText(fastMsgData.getSectionText() + "  " + fastMsgData.getHour());
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
}
