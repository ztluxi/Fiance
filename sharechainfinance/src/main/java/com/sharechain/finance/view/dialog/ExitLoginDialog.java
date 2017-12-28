package com.sharechain.finance.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sharechain.finance.R;


/**
 * Created by ${zhoutao} on 2017/12/28 0027.
 */
public class ExitLoginDialog extends Dialog {

	private Button positiveButton;
	private Button negativeButton;
	/***
	 * 自定义对话框
	 * @param context  上下文s
	 * @param title  对话框标题
	 * @param leftBtn  左边按钮显示的字
	 * @param rightBtn 右边按钮显示的字
	 */
	public ExitLoginDialog(Context context, String title, String leftBtn, String rightBtn) {
		super(context, R.style.progress_dialog);
		setCustomDialog(title, leftBtn, rightBtn);
	}
	private void setCustomDialog(String title , String leftBtn, String rightBtn) {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_exit, null);
        positiveButton = (Button) mView.findViewById(R.id.positiveButton);
        negativeButton = (Button) mView.findViewById(R.id.negativeButton);
        if(leftBtn!=""){
	        positiveButton.setText(leftBtn);
        }
        if(rightBtn!=""){
            negativeButton.setText(rightBtn);
        }
        TextView titleTV = (TextView) mView.findViewById(R.id.title_tv);
        titleTV.setText(title);
        super.setContentView(mView);
    }
	/**
     * 确定键监听器
     * @param listener
     */ 
    public void setOnPositiveListener(View.OnClickListener listener){
        positiveButton.setOnClickListener(listener); 
    } 
    /**
     * 取消键监听器
     * @param listener
     */ 
    public void setOnNegativeListener(View.OnClickListener listener){
        negativeButton.setOnClickListener(listener); 
    }
}
