package com.sharechain.finance.module.mine;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class SearchActivity extends BaseActivity implements View.OnClickListener {
    private TagFlowLayout searchPageFlowlayout;
    private ListView searchPageLv,searchResultLv;
    private RelativeLayout searchPageRl;
    private LinearLayout search_history_bg_ll,search_bg_ll;
    private EditText search_et;
    private TextView search_tv;
    private SearchHistoryAdapter historyAdapter;
    private String[] HotSearch = new String[]
            {"Hello", "Android", "Weclome Hi ", "Button", "TextView", "Hello",
                    "Android"};
    private List<FlowLayoutBean> list = new ArrayList<>();

    @Override
    public int getLayout() {
        return R.layout.activity_search;
    }

    @Override
    public void initView() {
        search_et = findViewById(R.id.search_et);
        search_tv = findViewById(R.id.search_tv);
        searchPageFlowlayout = findViewById(R.id.search_page_flowlayout);
        searchResultLv = findViewById(R.id.search_result_lv);
        search_bg_ll = findViewById(R.id.search_bg_ll);
        searchPageLv = findViewById(R.id.search_page_lv);
        search_history_bg_ll = findViewById(R.id.search_history_bg_ll);
        searchPageRl = findViewById(R.id.no_result_rl);
        final LayoutInflater mInflater = LayoutInflater.from(SearchActivity.this);
        searchPageFlowlayout.setAdapter(new TagAdapter<String>(HotSearch) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.search_page_flowlayout_tv,
                        searchPageFlowlayout, false);
                tv.setText(s);
                return tv;
            }
        });
        searchPageFlowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Toast.makeText(SearchActivity.this, HotSearch[position], Toast.LENGTH_SHORT).show();
                view.setVisibility(View.GONE);
                return true;
            }
        });
        searchPageFlowlayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                SearchActivity.this.setTitle("choose:" + selectPosSet.toString());
            }
        });


        initData();
        historyAdapter = new SearchHistoryAdapter(SearchActivity.this, list);
        searchPageLv.setAdapter(historyAdapter);


        Search();
    }


    private void Search() {
        search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    search_tv.setText(getString(R.string.search));
                }else {
                    search_tv.setText(getString(R.string.cancel));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        search_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchResultLv.setVisibility(View.VISIBLE);
                searchResultLv.setAdapter(historyAdapter);
                search_bg_ll.setVisibility(View.GONE);
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
            case R.id.search_page_flowlayout:
                break;
            case R.id.search_page_lv:
                break;
            case R.id.no_result_rl:
                break;
            case R.id.search_page_delete:
                search_history_bg_ll.setVisibility(View.GONE);
                searchPageLv.setVisibility(View.GONE);
                break;
        }
    }

    class SearchHistoryAdapter extends BaseAdapter {
        private Context context;
        private List<FlowLayoutBean> list;

        public SearchHistoryAdapter(Context context, List<FlowLayoutBean> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            FlowLayoutBean tv = (FlowLayoutBean) getItem(i);
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.search_page_flowlayout_tv, null);
                viewHolder = new ViewHolder();
                viewHolder.flowlayout_tv = (TextView) view.findViewById(R.id.flowlayout_tv);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.flowlayout_tv.setText(tv.getTv());

            return view;
        }

        //创建ViewHolder类
        class ViewHolder {
            TextView flowlayout_tv;
        }
    }
}
