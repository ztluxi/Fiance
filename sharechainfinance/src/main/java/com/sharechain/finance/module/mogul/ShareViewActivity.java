package com.sharechain.finance.module.mogul;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sharechain.finance.R;
import com.sharechain.finance.bean.FastMsgData;

/**
 * Created by Chu on 2017/12/27.
 */

public class ShareViewActivity extends Activity {
    private FastMsgData fastMsgData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_share_bitmap);
        fastMsgData = (FastMsgData) getIntent().getSerializableExtra("data");
    }

}
