package com.sharechain.finance.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.sharechain.finance.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Chu on 2017/12/18.
 */

public class FastMsgDialog extends Dialog {
    private Context context;

    @OnClick(R.id.image_close)
    void closeDialog() {
        dismiss();
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
