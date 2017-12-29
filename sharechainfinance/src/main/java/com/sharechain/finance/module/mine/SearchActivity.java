package com.sharechain.finance.module.mine;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.andview.refreshview.XRefreshView;
import com.sharechain.finance.BaseActivity;
import com.sharechain.finance.MyStringCallback;
import com.sharechain.finance.R;
import com.sharechain.finance.adapter.AnswerAdapter;
import com.sharechain.finance.bean.ArticleListsBean;
import com.sharechain.finance.bean.HomeArticleListBean;
import com.sharechain.finance.bean.SearchTagBean;
import com.sharechain.finance.bean.UrlList;
import com.sharechain.finance.module.home.ArticleDetailActivity;
import com.sharechain.finance.utils.BaseUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar;

import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.flow_hot)
    TagFlowLayout flow_hot;
    @BindView(R.id.flow_history)
    TagFlowLayout flow_history;
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
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.xrefreshview)
    XRefreshView xRefreshView;
    private Unregistrar unregistrar;
    private LayoutInflater mInflater;

    //搜索结果
    private List<ArticleListsBean> dataList = new ArrayList<>();
    private AnswerAdapter answerAdapter;
    private String searchText = "";
    //搜索记录
    private List<SearchTagBean> searchTagList = new ArrayList<>();

    @Override
    public int getLayout() {
        return R.layout.activity_search;
    }

    @Override
    public void initView() {
        mImmersionBar.statusBarColor(R.color.colorBlack).init();
        mInflater = LayoutInflater.from(SearchActivity.this);

        flow_history.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (position < searchTagList.size()) {
                    searchText = searchTagList.get(position).getTag();
                    doSearch();
                }
                return true;
            }
        });

        answerAdapter = new AnswerAdapter(this, R.layout.item_answer_fragment);
        initXRefreshView(xRefreshView);
        xRefreshView.setPullRefreshEnable(true);
        xRefreshView.setPullLoadEnable(true);
        xRefreshView.setAutoRefresh(false);
        xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
                page = 1;
                doSearch();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
                page++;
                doSearch();
            }

        });
        answerAdapter.setData(dataList);
        listView.setAdapter(answerAdapter);
        listView.setOnItemClickListener(this);

        initEditText();
    }

    private void initEditText() {
        //键盘状态监听
        unregistrar = KeyboardVisibilityEvent.registerEventListener(this, new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean isOpen) {

            }
        });
        search_et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    searchText = search_et.getText().toString().trim();
                    doSearch();
                }
                return false;
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
                if (BaseUtils.isEmpty(editable.toString())) {
                    searchText = "";
                    showTop();
                    dataList.clear();
                    answerAdapter.setData(dataList);
                }
            }
        });
    }

    //提供数据源
    @Override
    public void initData() {
        DataSupport.findAllAsync(SearchTagBean.class).listen(new FindMultiCallback() {
            @Override
            public <T> void onFinish(List<T> t) {
                searchTagList.addAll((List<SearchTagBean>) t);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (searchTagList.size() == 0) {
                            search_history_bg_ll.setVisibility(View.GONE);
                        } else {
                            search_history_bg_ll.setVisibility(View.VISIBLE);
                            flow_history.setAdapter(new TagAdapter<SearchTagBean>(searchTagList) {
                                @Override
                                public View getView(FlowLayout parent, int position, SearchTagBean searchTagBean) {
                                    TextView tv = (TextView) mInflater.inflate(R.layout.layout_search_item,
                                            flow_history, false);
                                    tv.setText(searchTagBean.getTag());
                                    return tv;
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    //根据id获取列表
    private void doSearch() {
        if (BaseUtils.isEmpty(searchText)) {
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("search_str", searchText);
        params.put(pageParam, String.valueOf(page));
        requestGet(UrlList.HOME_ARTICLE_LIST, params, new MyStringCallback(this) {
            @Override
            protected void onSuccess(String result) {
                hideSoftKeyboard(search_et);
                if (xRefreshView != null) {
                    xRefreshView.stopRefresh();
                    xRefreshView.stopLoadMore();
                }
                HomeArticleListBean bean = JSON.parseObject(result, HomeArticleListBean.class);
                if (bean.getSuccess() == UrlList.CODE_SUCCESS) {
                    showListView();
                    //保存搜索历史
                    List<SearchTagBean> dbTags = DataSupport.where("tag = ?", searchText).find(SearchTagBean.class);
                    if (dbTags.size() == 0) {
                        SearchTagBean searchTagBean = new SearchTagBean();
                        searchTagBean.setTag(searchText);
                        searchTagBean.save();
                    }
                    if (page == 1) {
                        dataList.clear();
                    }
                    //给tagId赋值
                    for (ArticleListsBean tmp : bean.getData().getArticle_lists()) {
                        tmp.setTagId(tmp.getID());
                        tmp.setUser_avatars(BaseUtils.getSubImageUrl(tmp.getUser_avatars(), "i:128;s:92:", ";i:64;s:90:"));
                    }
                    dataList.addAll(bean.getData().getArticle_lists());
                    answerAdapter.setData(dataList);
                }
            }

            @Override
            protected void onFailed(String errStr) {
                hideSoftKeyboard(search_et);
                if (xRefreshView != null) {
                    xRefreshView.stopRefresh();
                    xRefreshView.stopLoadMore();
                }
            }
        });
    }

    private void showListView() {
        search_bg_ll.setVisibility(View.GONE);
        xRefreshView.setVisibility(View.VISIBLE);
    }

    private void showTop() {
        search_bg_ll.setVisibility(View.VISIBLE);
        xRefreshView.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregistrar.unregister();
    }

    @OnClick(R.id.text_cancel)
    void cancel() {
        hideSoftKeyboard(search_et);
        //取消
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 10);
    }

    @OnClick(R.id.search_page_delete)
    void deleteHistory() {
        DataSupport.deleteAll(SearchTagBean.class);
        searchTagList.clear();
        search_history_bg_ll.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("article", dataList.get(i));
        BaseUtils.openActivity(this, ArticleDetailActivity.class, bundle);
    }

}
