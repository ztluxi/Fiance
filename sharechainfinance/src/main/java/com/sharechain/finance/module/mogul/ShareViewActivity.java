package com.sharechain.finance.module.mogul;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sharechain.finance.R;
import com.sharechain.finance.SFApplication;
import com.sharechain.finance.bean.FastMsgData;
import com.sharechain.finance.utils.BaseUtils;
import com.sharechain.finance.utils.ShareUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Chu on 2017/12/27.
 */

public class ShareViewActivity extends Activity {
    @BindView(R.id.image_avatar)
    ImageView image_avatar;
    @BindView(R.id.text_name)
    TextView text_name;
    @BindView(R.id.text_tag)
    TextView text_tag;
    @BindView(R.id.text_content)
    TextView text_content;
    private FastMsgData fastMsgData;
    private ShareUtils shareUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_share_bitmap);
        ButterKnife.bind(this);
        fastMsgData = (FastMsgData) getIntent().getSerializableExtra("data");
        text_content.setText(fastMsgData.getDataText());
        shareUtils = new ShareUtils(this);
    }

    @OnClick(R.id.image_weixin)
    void image_weixin() {
        Bitmap bitmap = BaseUtils.getViewBitmap(getContentView(), SFApplication.screen_width, SFApplication.screen_height);
        if (bitmap != null) {
            shareUtils.shareWithImage(ShareUtils.SHARE_TARGET_TYPE.TYPE_FRIEND, bitmap);
        }
    }

    @OnClick(R.id.image_circle)
    void image_circle() {

    }

    @OnClick(R.id.image_download)
    void image_download() {

    }

    private View getContentView() {
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        LinearLayout content = (LinearLayout) view.getChildAt(0);
        return content;
    }

}
