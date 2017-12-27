/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com | 3772304@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.sharechain.finance.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sharechain.finance.R;
import com.sharechain.finance.bean.HomeArticleListBean;
import com.sharechain.finance.bean.HomeIndexBean;
import com.sharechain.finance.utils.BaseUtils;
import com.sharechain.finance.view.ScaleInTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class NewsListAdapter extends BaseRecyclerViewAdapter<HomeArticleListBean.DataBean.ArticleListsBean> {
    private HeaderViewHolder headerViewHolder;
    private PagerAdapter pagerAdapter;
    private HomeIndexBean.DataBean headerBean;
    private volatile int switchIndex = 0;
    private volatile int switchBannerIndex = 0;
    private int oldPosition = 0;//记录上一次点的位置
    private int currentItem; //当前页面
    private ScheduledExecutorService scheduledExecutorService;
    private List<View> bannerViews = new ArrayList<>();

    public NewsListAdapter(Context context, List<HomeArticleListBean.DataBean.ArticleListsBean> list,
                           HomeIndexBean.DataBean headerBean) {
        super(context, list);
        this.headerBean = headerBean;
        mIsShowHeader = true;
    }

    public interface OnNewsListItemClickListener extends OnItemClickListener {
        void onItemClick(View view, int position, boolean isPhoto);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view;
        switch (viewType) {
            case TYPE_HEADER:
                view = getView(parent, R.layout.layout_header);
                headerViewHolder = new HeaderViewHolder(view);
//                initViewPager();
                initPoints();
                setHeaderHotData();
                return headerViewHolder;
            case TYPE_FOOTER:
                view = getView(parent, R.layout.item_news_footer);
                return new FooterViewHolder(view);
            case TYPE_ITEM:
                view = getView(parent, R.layout.item_news);
                final ItemViewHolder itemViewHolder = new ItemViewHolder(view);
                setItemOnClickEvent(itemViewHolder, false);
                return itemViewHolder;
            default:
                throw new RuntimeException("there is no type that matches the type " +
                        viewType + " + make sure your using types correctly");
        }
    }

    private void setItemOnClickEvent(final RecyclerView.ViewHolder holder, final boolean isPhoto) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((OnNewsListItemClickListener) mOnItemClickListener).onItemClick(v, holder.getLayoutPosition(), isPhoto);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mIsShowHeader && isHeaderPosition(position)) {
            return TYPE_HEADER;
        } else if (mIsShowFooter && isFooterPosition(position)) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        setValues(holder, position);
//        setItemAppearAnimation(holder, position, R.anim.anim_bottom_in);
    }

    private void setValues(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            setItemValues((ItemViewHolder) holder, position);
        }
    }

    private void setItemValues(ItemViewHolder holder, int position) {
        if (position > 0) {
            HomeArticleListBean.DataBean.ArticleListsBean bean = mList.get(position - 1);
            holder.text_content.setText(bean.getPost_title());
            holder.text_time.setText(bean.getPost_date_gmt() + "  " + bean.getName());
        }
    }

//    private void initViewPager() {
//        if (headerViewHolder != null) {
//            headerViewHolder.viewpager.setPageMargin(BaseUtils.dip2px(context, 7));
//            headerViewHolder.viewpager.setAdapter(pagerAdapter = new PagerAdapter() {
//                @Override
//                public Object instantiateItem(final ViewGroup container, int position) {
//                    return bannerViews.get(position);
//                }
//
//                @Override
//                public int getItemPosition(Object object) {
//                    return POSITION_NONE;
//                }
//
//                @Override
//                public void destroyItem(ViewGroup container, int position, Object object) {
//                    container.removeView((View) object);
//                }
//
//                @Override
//                public int getCount() {
//                    return headerBean.getBanner().size() + 2;
//                }
//
//                @Override
//                public boolean isViewFromObject(View view, Object o) {
//                    return view == o;
//                }
//
////                @Override
////                public void startUpdate(ViewGroup container) {
////                    super.startUpdate(container);
////                    ViewPager viewPager = (ViewPager) container;
////                    int position = viewPager.getCurrentItem();
////                    if (position == 0) {
////                        position = getFirstItemPosition();
////                    } else if (position == getCount() - 1) {
////                        position = getLastItemPosition();
////                    }
////                    viewPager.setCurrentItem(position, false);
////                }
//
//                private int getRealCount() {
//                    return headerBean.getBanner().size();
//                }
//
//                private int getRealPosition(int position) {
//                    return position % getRealCount();
//                }
//
//                private int getFirstItemPosition() {
//                    return Integer.MAX_VALUE / getRealCount() / 2 * getRealCount();
//                }
//
//                private int getLastItemPosition() {
//                    return Integer.MAX_VALUE / getRealCount() / 2 * getRealCount() - 1;
//                }
//            });
//            headerViewHolder.viewpager.setPageTransformer(true, new ScaleInTransformer());
//            headerViewHolder.viewpager.setCurrentItem(switchBannerIndex, false);
//            switchToPoint(0);
//            headerViewHolder.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//                @Override
//                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//                }
//
//                @Override
//                public void onPageSelected(int position) {
//                    switchToPoint(position % headerBean.getBanner().size());
//                }
//
//                @Override
//                public void onPageScrollStateChanged(int state) {
//
//                }
//            });
//            headerViewHolder.viewpager.setCurrentItem(0, false);
//            startBannerPlay();
//        }
//    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (isShowingAnimation(holder)) {
            holder.itemView.clearAnimation();
        }
    }

    private boolean isShowingAnimation(RecyclerView.ViewHolder holder) {
        return holder.itemView.getAnimation() != null && holder.itemView
                .getAnimation().hasStarted();
    }

    private void initPoints() {
        if (headerViewHolder != null) {
            for (int i = 0; i < headerBean.getBanner().size(); i++) {
                ImageView imageView = new ImageView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(BaseUtils.dip2px(context, 7),
                        BaseUtils.dip2px(context, 7));
                params.leftMargin = BaseUtils.dip2px(context, 8);
                imageView.setLayoutParams(params);
                imageView.setImageResource(R.drawable.common_point_selector);
                imageView.setSelected(true);
                headerViewHolder.ll_point.addView(imageView);
            }
        }
    }

    private void switchToPoint(int index) {
        if (headerViewHolder != null && headerViewHolder.ll_point != null) {
            for (int i = 0; i < headerViewHolder.ll_point.getChildCount(); i++) {
                if (index == i) {
                    headerViewHolder.ll_point.getChildAt(i).setSelected(true);
                } else {
                    headerViewHolder.ll_point.getChildAt(i).setSelected(false);
                }
            }
        }
    }

    private void setHeaderHotData() {
        if (headerViewHolder != null) {
            if (headerBean.getTop_news().size() > 0) {
                headerViewHolder.text_header_time.setText(headerBean.getTop_news().get(0).getTime());
                headerViewHolder.text_header_title.setFactory(new ViewSwitcher.ViewFactory() {
                    @Override
                    public View makeView() {
                        TextView textView = new TextView(context);
                        textView.setLines(2);
                        textView.setEllipsize(TextUtils.TruncateAt.END);
                        textView.setTextSize(15);
                        textView.setTextColor(ContextCompat.getColor(context, R.color.primary_text));
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.gravity = Gravity.CENTER_VERTICAL;
                        textView.setLayoutParams(params);
                        return textView;
                    }
                });
                startSwitchText();
            }
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.viewpager)
        ViewPager viewpager;
        @BindView(R.id.ll_point)
        LinearLayout ll_point;
        @BindView(R.id.text_header_time)
        TextView text_header_time;
        @BindView(R.id.text_header_title)
        TextSwitcher text_header_title;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_pic)
        ImageView image_pic;
        @BindView(R.id.text_content)
        TextView text_content;
        @BindView(R.id.text_time)
        TextView text_time;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    //热点新闻循环播放handler
    public Handler switchHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            return false;
        }
    });

    private void startSwitchText() {
        headerViewHolder.text_header_time.setText(headerBean.getTop_news().get(switchIndex).getTime());
        headerViewHolder.text_header_title.setText(headerBean.getTop_news().get(switchIndex).getSite_content());
        switchHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switchIndex++;
                if (switchIndex == headerBean.getTop_news().size()) {
                    switchIndex = 0;
                }
                startSwitchText();
            }
        }, 3000);
    }

    //停止循环播放
    public void stopHeadLineAutoPlay() {
        switchHandler.removeCallbacks(null);
    }

    //停止循环播放
    public void stopBannerAutoPlay() {
        scheduledExecutorService.shutdown();
    }

    private void startBannerPlay() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        //每隔2秒钟切换一张图片
        scheduledExecutorService.scheduleWithFixedDelay(new ViewPagerTask(), 2, 2, TimeUnit.SECONDS);
    }

    //切换图片
    private class ViewPagerTask implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            currentItem++;
            if (currentItem == headerBean.getBanner().size()) {
                currentItem = 0;
            }
            handler.obtainMessage().sendToTarget();
        }

    }

    private void initBannerViewList() {
        for (int i = 0; i < headerBean.getBanner().size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_news_banner_item, null);
            ImageView image_banner = view.findViewById(R.id.image_banner);
            TextView text_banner = view.findViewById(R.id.text_banner);
            Glide.with(context).load(headerBean.getBanner().get(i).getUrl()).
                    apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(BaseUtils.dip2px(context, 8), 0))
                            .placeholder(R.drawable.ic_launcher_background)).into(image_banner);
            text_banner.setText("比特币价格重挫45%，小投资者惨遭割韭菜");
            bannerViews.add(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "click position= " + i, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            //设置当前页面
            int position = headerViewHolder.viewpager.getCurrentItem();
            int first = Integer.MAX_VALUE / headerBean.getBanner().size() / 2 * headerBean.getBanner().size();
            int last = Integer.MAX_VALUE / headerBean.getBanner().size() / 2 * headerBean.getBanner().size() - 1;
            if (position == 0) {
                position = first;
            } else if (position == Integer.MAX_VALUE - 1) {
                position = last;
            }
            headerViewHolder.viewpager.setCurrentItem(position, false);
//            headerViewHolder.viewpager.setCurrentItem(currentItem, false);
        }

    };

}
