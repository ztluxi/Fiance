package com.sharechain.finance.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.sharechain.finance.R;


/**
 * 加载对话框
 * *Created by ${zhoutao} on 2018/1/2 0013.
 */
public class LoadDialog {

    /**
     * 初始化
     */
    public LoadDialog() {
        super();
    }

    /**
     * 加载进度对话框
     *
     * @return
     */
    public Dialog LoadProgressDialog(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_loading_process, null);
        Dialog dialog = new Dialog(context, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }


}
