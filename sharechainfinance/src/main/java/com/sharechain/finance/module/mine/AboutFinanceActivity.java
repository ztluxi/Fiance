package com.sharechain.finance.module.mine;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.andview.refreshview.XRefreshView;
import com.sharechain.finance.BaseActivity;
import com.sharechain.finance.R;
import com.sharechain.finance.adapter.HistoryAdapter;
import com.sharechain.finance.bean.HomeData;
import com.sharechain.finance.utils.ToastManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.baseadapter.BGAOnItemChildClickListener;

/**
 * Created by ${zhoutao} on 2017/12/19 0013.
 */

public class AboutFinanceActivity extends BaseActivity {
    @BindView(R.id.image_title_left)
    ImageView back_Image;

    @Override
    public int getLayout() {
        return R.layout.activity_about_finance;
    }

    @Override
    public void initView() {
        initData();
        initTitle(getString(R.string.app_name));
        back_Image.setVisibility(View.VISIBLE);
        back_Image.setImageResource(R.drawable.back);

    }
    @Override
    public void initData() {

    }
    @OnClick({R.id.image_title_left})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_title_left:
                finish();
                break;
        }
    }



}
