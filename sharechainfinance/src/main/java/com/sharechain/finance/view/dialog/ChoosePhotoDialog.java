package com.sharechain.finance.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.sharechain.finance.R;
import com.sharechain.finance.utils.ScreenUtil;

/**
 * Created by ${zhoutao} on 2017/12/18 0018.
 */

public class ChoosePhotoDialog extends Dialog{

    public ChoosePhotoDialog(@NonNull Context context) {
        super(context);
    }

    /**
     * 创建底部弹出对话框。(这里用于提示用户登录和上传照片选择两个地方)
     *
     * @param context
     * @param resId
     * @return
     */
    public  Dialog createBottomDialog(Context context, int resId) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resId, null);
        final Dialog dialog = new Dialog(context, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.picker_photo_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        // wl.y = window.getWindowManager().getDefaultDisplay().getHeight();
        wl.y = ScreenUtil.getScreenHeight(context);
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

}
