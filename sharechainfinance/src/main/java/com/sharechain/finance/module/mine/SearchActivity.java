package com.sharechain.finance.module.mine;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sharechain.finance.BaseActivity;
import com.sharechain.finance.R;
import com.sharechain.finance.bean.FlowLayoutBean;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.flow_hot)
    TagFlowLayout flow_hot;
    @BindView(R.id.flow_history)
    TagFlowLayout flow_history;
    @BindView(R.id.search_result_lv)
    ListView searchResultLv;
    @BindView(R.id.search_history_bg_ll)
    LinearLayout search_history_bg_ll;
    @BindView(R.id.search_bg_ll)
    LinearLayout search_bg_ll;
    @BindView(R.id.search_et)
    EditText search_et;
    @BindView(R.id.no_result_rl)
    RelativeLayout no_result_rl;
    @BindView(R.id.text_cancel)
    TextView text_cancel;

    private String[] HotSearch = new String[]
            {"Hello", "Android", "Weclome Hi ", "Button", "TextView", "Hello",
                    "Android"};
    private List<FlowLayoutBean> list = new ArrayList<>();
    private Unregistrar unregistrar;

    @Override
    public int getLayout() {
        return R.layout.activity_search;
    }

    @Override
    public void initView() {
        mImmersionBar.statusBarColor(R.color.colorBlack).init();
        initData();
        final LayoutInflater mInflater = LayoutInflater.from(SearchActivity.this);
        flow_hot.setAdapter(new TagAdapter<String>(HotSearch) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.layout_search_item,
                        flow_hot, false);
                tv.setText(s);
                return tv;
            }
        });
        flow_hot.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Toast.makeText(SearchActivity.this, HotSearch[position], Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        flow_history.setAdapter(new TagAdapter<FlowLayoutBean>(list) {
            @Override
            public View getView(FlowLayout parent, int position, FlowLayoutBean flowLayoutBean) {
                TextView tv = (TextView) mInflater.inflate(R.layout.layout_search_item,
                        flow_history, false);
                tv.setText(flowLayoutBean.getTv());
                return tv;
            }
        });
        flow_history.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                return true;
            }
        });

        search();
    }

    private void search() {
        //键盘状态监听
        unregistrar = KeyboardVisibilityEvent.registerEventListener(this, new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean isOpen) {

            }
        });
        search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    text_cancel.setText(getString(R.string.search));
                } else {
                    text_cancel.setText(getString(R.string.cancel));
                }
            }
        });
    }

    //提供数据源
    @Override
    public void initData() {
        FlowLayoutBean tv = null;
        for (int i = 0; i < HotSearch.length; i++) {
            tv = new FlowLayoutBean();
            tv.setTv(HotSearch[i]);
            list.add(tv);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.no_result_rl:
                break;
            case R.id.search_page_delete:
                search_history_bg_ll.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregistrar.unregister();
    }

    @OnClick(R.id.text_cancel)
    void cancel() {
        hideSoftKeyboard(search_et);
        if (search_et.getText().toString().length() > 0) {
            //搜索
        } else {
            //取消
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 10);
        }
    }

}
