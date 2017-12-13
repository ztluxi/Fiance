package com.sharechain.finance.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.sharechain.finance.BaseActivity;
import com.sharechain.finance.R;

/**
 * Created by ${zhoutao} on 2017/12/13 0013.
 */

public class AdviceAdapter extends BaseAdapter {

    private Context context;

    public AdviceAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return LinearLayout.inflate(context, R.layout.item_normal,null);
    }
}
