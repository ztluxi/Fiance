package com.sharechain.finance.module.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sharechain.finance.BaseActivity;
import com.sharechain.finance.MyStringCallback;
import com.sharechain.finance.R;
import com.sharechain.finance.SFApplication;
import com.sharechain.finance.bean.ArticleDetailBean;
import com.sharechain.finance.bean.ArticleListsBean;
import com.sharechain.finance.bean.HistoryData;
import com.sharechain.finance.bean.LikeBean;
import com.sharechain.finance.bean.UrlList;
import com.sharechain.finance.utils.BaseUtils;
import com.sharechain.finance.utils.TimeUtil;
import com.sharechain.finance.view.fabulos.GoodView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Chu on 2017/12/26.
 */

public class ArticleDetailActivity extends BaseActivity {
    public static final int NEWS_TYPE_HOME = 0;
    public static final int NEWS_TYPE_ANSWER = 1;

    @BindView(R.id.text_article_title)
    TextView text_article_title;
    @BindView(R.id.image_avatar)
    ImageView image_avatar;
    @BindView(R.id.text_name)
    TextView text_name;
    @BindView(R.id.text_time)
    TextView text_time;
    @BindView(R.id.text_view_count)
    TextView text_view_count;
    @BindView(R.id.web_content)
    WebView web_content;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.text_from)
    TextView text_from;
    @BindView(R.id.flow_tags)
    TagFlowLayout flow_tags;
    @BindView(R.id.image_praise)
    ImageView image_praise;
    @BindView(R.id.text_praise_num)
    TextView text_praise_num;

    private boolean isAnimStart = false;
    private int currentProgress;
    private ArticleListsBean articleBean;
    private int newsType = 0;

    @Override
    public int getLayout() {
        return R.layout.activity_article_detail;
    }

    @Override
    public void initView() {
        articleBean = (ArticleListsBean) getIntent().getSerializableExtra("article");
        newsType = getIntent().getIntExtra("news_type", NEWS_TYPE_HOME);
        initTitle(getString(R.string.app_name));
        image_title_left.setImageResource(R.drawable.back);
        image_title_left.setVisibility(View.VISIBLE);
        image_title_right.setImageResource(R.drawable.icon_article_share);
        image_title_right.setVisibility(View.VISIBLE);
        initWebSetting();
        image_title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        image_title_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //分享
            }
        });
        //判断是否点赞
        List<LikeBean> likeList = DataSupport.where("articleId = ?", String.valueOf(articleBean.getTagId())).find(LikeBean.class);
        if (likeList.size() == 0) {
            image_praise.setImageResource(R.drawable.icon_article_praise);
            image_praise.setClickable(true);
        } else {
            image_praise.setImageResource(R.drawable.icon_article_praise_pre);
            image_praise.setClickable(false);
        }

    }

    @Override
    public void initData() {
        getDetail();
    }

    private void initWebSetting() {
        // 设置支持JavaScript脚本
        WebSettings webSettings = web_content.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // 设置可以访问文件
        webSettings.setAllowFileAccess(true);
        // 设置不支持缩放
        webSettings.setBuiltInZoomControls(false);

        webSettings.setDatabaseEnabled(true);
        String dir = getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        webSettings.setDatabasePath(dir);
        web_content.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        // 使用localStorage则必须打开
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setLoadWithOverviewMode(true);

        webSettings.setGeolocationEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        if (Build.VERSION.SDK_INT >= 19) {
            web_content.getSettings().setLoadsImagesAutomatically(true);
        } else {
            web_content.getSettings().setLoadsImagesAutomatically(false);
        }

        web_content.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!web_content.getSettings().getLoadsImagesAutomatically()) {
                    web_content.getSettings().setLoadsImagesAutomatically(true);
                }
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setAlpha(1.0f);
            }

        });

        // 设置WebChromeClient
        web_content.setWebChromeClient(new WebChromeClient() {
            // 处理javascript中的alert
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                // 构建一个Builder来显示网页中的对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(ArticleDetailActivity.this);
                builder.setTitle("Alert");
                builder.setMessage(message);
                builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                builder.setCancelable(false);
                builder.create();
                builder.show();
                return true;
            }

            // 处理javascript中的confirm
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ArticleDetailActivity.this);
                builder.setTitle("confirm");
                builder.setMessage(message);
                builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                });
                builder.setCancelable(false);
                builder.create();
                builder.show();
                return true;
            }

            @Override
            // 设置网页加载的进度条
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                currentProgress = progressBar.getProgress();
                if (newProgress >= 100 && !isAnimStart) {
                    // 防止调用多次动画
                    isAnimStart = true;
                    progressBar.setProgress(newProgress);
                    // 开启属性动画让进度条平滑消失
                    startDismissAnimation(progressBar.getProgress());
                } else {
                    // 开启属性动画让进度条平滑递增
                    startProgressAnimation(newProgress);
                }
            }

            // 设置应用程序的标题title
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            public void onExceededDatabaseQuota(String url, String databaseIdentifier, long currentQuota, long estimatedSize, long totalUsedQuota,
                                                WebStorage.QuotaUpdater quotaUpdater) {
                quotaUpdater.updateQuota(estimatedSize * 2);
            }

            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

            public void onReachedMaxAppCacheSize(long spaceNeeded, long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
                quotaUpdater.updateQuota(spaceNeeded * 2);
            }
        });
    }


    /**
     * progressBar递增动画
     */
    private void startProgressAnimation(int newProgress) {
        ObjectAnimator animator = ObjectAnimator.ofInt(progressBar, "progress", currentProgress, newProgress);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }

    /**
     * progressBar消失动画
     */
    private void startDismissAnimation(final int progress) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(progressBar, "alpha", 1.0f, 0.0f);
        anim.setDuration(1500);  // 动画时长
        anim.setInterpolator(new DecelerateInterpolator());     // 减速
        // 关键, 添加动画进度监听器
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float fraction = valueAnimator.getAnimatedFraction();      // 0.0f ~ 1.0f
                int offset = 100 - progress;
                progressBar.setProgress((int) (progress + offset * fraction));
            }
        });

        anim.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                // 动画结束
                progressBar.setProgress(0);
                progressBar.setVisibility(View.GONE);
                isAnimStart = false;
            }
        });
        anim.start();
    }

    private void updateView(ArticleDetailBean bean) {
        String avatarStr = BaseUtils.getSubImageUrl(bean.getData().getAuthor().getUser_avatars(), "i:64;s:90:", ";i:52;s:90:");
        text_article_title.setText(bean.getData().getArticle().getPost_title());
        RequestOptions options = new RequestOptions().circleCrop();
        Glide.with(SFApplication.get(this)).load(avatarStr).apply(options).into(image_avatar);
        text_name.setText(bean.getData().getAuthor().getDisplay_name());
        text_time.setText(bean.getData().getArticle().getPost_date_gmt());
        text_view_count.setText(String.valueOf(bean.getData().getAuthor().getCount_view()));
        if (bean.getData().getPost_tag().size() == 0) {
            flow_tags.setVisibility(View.GONE);
        }
        flow_tags.setAdapter(new TagAdapter<ArticleDetailBean.DataBean.PostTagBean>(bean.getData().getPost_tag()) {
            @Override
            public View getView(FlowLayout parent, int position, ArticleDetailBean.DataBean.PostTagBean postTagBean) {
                TextView tv = (TextView) LayoutInflater.from(ArticleDetailActivity.this).inflate(R.layout.layout_item_article_tag,
                        flow_tags, false);
                tv.setText(postTagBean.getName());
                if (position % 3 == 0) {
                    tv.setBackgroundDrawable(BaseUtils.createGradientDrawable(0, Color.parseColor("#fd5f5a"),
                            BaseUtils.dip2px(ArticleDetailActivity.this, 5), Color.parseColor("#fd5f5a")));
                } else if (position % 3 == 1) {
                    tv.setBackgroundDrawable(BaseUtils.createGradientDrawable(0, Color.parseColor("#ff8d29"),
                            BaseUtils.dip2px(ArticleDetailActivity.this, 5), Color.parseColor("#ff8d29")));
                } else if (position % 3 == 2) {
                    tv.setBackgroundDrawable(BaseUtils.createGradientDrawable(0, Color.parseColor("#3badfc"),
                            BaseUtils.dip2px(ArticleDetailActivity.this, 5), Color.parseColor("#3badfc")));
                }
                return tv;
            }
        });
        //赞数目
        text_praise_num.setText(String.valueOf(bean.getData().getAuthor().getCount_article()));
        //图文详情
        Document doc = Jsoup.parse(bean.getData().getArticle().getPost_content());
        Elements elements = doc.getElementsByTag("img");
        for (Element element : elements) {
            element.attr("width", "100%").attr("height", "auto");
        }
        web_content.loadDataWithBaseURL(null, doc.toString(), "text/html", "utf-8", null);
    }

    private void getDetail() {
        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(articleBean.getTagId()));
        requestGet(UrlList.HOME_ARTICLE_DETAIL, params, new MyStringCallback(this) {
            @Override
            protected void onSuccess(String result) {
                ArticleDetailBean bean = JSON.parseObject(result, ArticleDetailBean.class);
                if (bean.getSuccess() == UrlList.CODE_SUCCESS) {
                    //获取文章成功，将该文章存储到数据库
                    if (!BaseUtils.isEmpty(articleBean.getPost_title())) {
                        //异步查询数据库
                        String curDate = TimeUtil.getCurrentTime("yyyy-MM-dd");
                        List<HistoryData> tmpList = DataSupport.where("date = ?", curDate).find(HistoryData.class);
                        if (tmpList.size() == 0) {
                            //第一次存储
                            HistoryData parentBean = convertToHistory(articleBean, newsType, true);
                            parentBean.save();
                            HistoryData childBean = convertToHistory(articleBean, newsType, false);
                            childBean.save();
                        } else {
                            //已存在改时间，判断是否已经存储
                            List<HistoryData> saveList = DataSupport.where("tagId = ?", String.valueOf(articleBean.getTagId())).find(HistoryData.class);
                            if (saveList.size() == 0) {
                                HistoryData historyBean = convertToHistory(articleBean, newsType, false);
                                historyBean.save();
                            }
                        }
                    }
                    updateView(bean);
                }
            }

            @Override
            protected void onFailed(String errStr) {

            }
        });
    }

    //点赞
    private void praise() {
        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(articleBean.getTagId()));
        requestGet(UrlList.HOME_ARTICLE_PRAISE, params, new MyStringCallback(this) {
            @Override
            protected void onSuccess(String result) {
                ArticleDetailBean bean = JSON.parseObject(result, ArticleDetailBean.class);
                if (bean.getSuccess() == UrlList.CODE_SUCCESS) {
                    GoodView goodView = new GoodView(ArticleDetailActivity.this);
                    goodView.setImage(R.drawable.icon_article_praise_pre);
                    goodView.show(image_praise);
                    image_praise.setImageResource(R.drawable.icon_article_praise_pre);
                    LikeBean likeBean = new LikeBean();
                    likeBean.setArticleId(articleBean.getTagId());
                    likeBean.save();
                    image_praise.setClickable(false);
                }
            }

            @Override
            protected void onFailed(String errStr) {

            }
        });
    }

    @OnClick(R.id.image_praise)
    void clickPraise() {
        praise();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
