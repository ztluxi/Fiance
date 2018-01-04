package com.sharechain.finance.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.sharechain.finance.R;
import com.sharechain.finance.SFApplication;
import com.sharechain.finance.bean.MogulShareBean;
import com.sharechain.finance.utils.BaseUtils;
import com.sharechain.finance.utils.GlideUtils;
import com.sharechain.finance.utils.MyImageGetter;
import com.sharechain.finance.utils.ShareUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static cn.bingoogolapple.baseadapter.BGABaseAdapterUtil.getColor;

/**
 * *Created by ${zhoutao} on 2017/12/24 0013.
 */

public class MogulShareDialog extends Dialog {
    private Context context;
    @BindView(R.id.text_name)
    TextView text_namet;
    @BindView(R.id.text_position)
    TextView text_position;
    @BindView(R.id.text_weibo)
    TextView text_weibo;
    @BindView(R.id.text_content)
    TextView text_content;

    @BindView(R.id.image_qrcode)
    ImageView image_qrcode;

    @BindView(R.id.mogul_iv)
    ImageView headImage;

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
    }

    @OnClick(R.id.image_circle)
    void gotoShareWeixinCircle() {
        Bitmap bitmap = BaseUtils.getViewBitmap(shareView, SFApplication.screen_width, SFApplication.screen_height);
        if (bitmap != null) {
            shareUtils.shareWithImage(ShareUtils.SHARE_TARGET_TYPE.TYPE_CIRCLE, bitmap);
        }
    }

    @OnClick(R.id.image_download)
    void gotoDownload() {
        Bitmap bitmap = BaseUtils.getViewBitmap(shareView, SFApplication.screen_width, SFApplication.screen_height);
        if (bitmap != null) {
            boolean isSuccess = BaseUtils.saveImageToGallery(context, bitmap);
            if (isSuccess) {
                Toast.makeText(context, "图片保存成功!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "图片保存失败!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public MogulShareDialog(Context context, MogulShareBean mogulShareBean) {
        super(context, R.style.base_dialog_style);
        this.context = context;
        this.mogulShareBean = mogulShareBean;
        shareUtils = new ShareUtils(context);
    }

    private View shareView;
    private MogulShareBean mogulShareBean;
    private ShareUtils shareUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_mogul_share_dialog);
        ButterKnife.bind(this);
        setContent();
        initShareView();
    }

    private void setContent() {
        RequestOptions headOptions = new RequestOptions().placeholder(R.drawable.logo).circleCrop();
        GlideUtils.getInstance().loadUserImage(SFApplication.get(context),mogulShareBean.getUrl(),headImage, headOptions);
        text_namet.setText(mogulShareBean.getName()+"");
        if (mogulShareBean.getPosition().equals("")){
            text_position.setVisibility(View.GONE);
        }else {
            text_position.setText(mogulShareBean.getPosition());
        }
        if (BaseUtils.isEmpty(mogulShareBean.getWeibo())){
            text_weibo.setText(mogulShareBean.getWeibo()+"");
            text_weibo.setVisibility(View.GONE);
        }else {
            text_weibo.setText(mogulShareBean.getWeibo()+"");
            text_weibo.setVisibility(View.VISIBLE);
        }
        MyImageGetter glide = new MyImageGetter(text_content,context);
        text_content.setText(Html.fromHtml(mogulShareBean.getContent(), glide, null));
        text_content.setMovementMethod(LinkMovementMethod.getInstance());
        text_content.setLinkTextColor(getColor(R.color.tint_home_color));
//        RichText.fromHtml(mogulShareBean.getContent()).into(text_content);
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
        shareView = LayoutInflater.from(context).inflate(R.layout.layout_share_bitmap, null);
        ImageView image_avatar = shareView.findViewById(R.id.image_avatar);
        RequestOptions headOptions = new RequestOptions().placeholder(R.drawable.logo).circleCrop();
        GlideUtils.getInstance().loadUserImage(SFApplication.get(context), mogulShareBean.getUrl(), image_avatar, headOptions);
        TextView text_name = shareView.findViewById(R.id.text_name);
        text_name.setText(mogulShareBean.getName());
        TextView text_tag = shareView.findViewById(R.id.text_tag);
        text_tag.setText(mogulShareBean.getPosition());
        TextView text_content = shareView.findViewById(R.id.text_content);
        text_content.setText(mogulShareBean.getContent());
        TextView text_uid = shareView.findViewById(R.id.text_uid);
    }

}
