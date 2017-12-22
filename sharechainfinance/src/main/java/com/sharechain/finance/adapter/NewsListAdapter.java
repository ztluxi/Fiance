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
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sharechain.finance.R;
import com.sharechain.finance.utils.BaseUtils;
import com.sharechain.finance.view.AlphaPageTransformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class NewsListAdapter extends BaseRecyclerViewAdapter<String> {
    private HeaderViewHolder headerViewHolder;
    private List<String> imageList;
    private List<String> tipList;
    private PagerAdapter pagerAdapter;
    private List<ImageView> pointViews = new ArrayList<>();

    public NewsListAdapter(Context context, List<String> list, List<String> imageList, List<String> tipList) {
        super(context, list);
        this.imageList = imageList;
        this.tipList = tipList;
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
                initViewPager();
                initPoints();
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

    }

    private void initViewPager() {
        if (headerViewHolder != null) {
            headerViewHolder.viewpager.setPageMargin(BaseUtils.dip2px(context, 7));
            headerViewHolder.viewpager.setAdapter(pagerAdapter = new PagerAdapter() {
                @Override
                public Object instantiateItem(final ViewGroup container, int position) {
                    ImageView view = new ImageView(context);
                    view.setScaleType(ImageView.ScaleType.FIT_XY);
                    final int realPosition = getRealPosition(position);
                    Glide.with(context).load(imageList.get(realPosition)).
                            apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(BaseUtils.dip2px(context, 8), 0))
                                    .placeholder(R.drawable.ic_launcher_background)).into(view);
                    container.addView(view);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(context, "click position= " + realPosition, Toast.LENGTH_SHORT).show();
                        }
                    });
                    return view;
                }


                @Override
                public int getItemPosition(Object object) {
                    return POSITION_NONE;
                }

                @Override
                public void destroyItem(ViewGroup container, int position, Object object) {
                    container.removeView((View) object);
                }

                @Override
                public int getCount() {
                    return Integer.MAX_VALUE;
                }

                @Override
                public boolean isViewFromObject(View view, Object o) {
                    return view == o;
                }

                @Override
                public void startUpdate(ViewGroup container) {
                    super.startUpdate(container);
                    ViewPager viewPager = (ViewPager) container;
                    int position = viewPager.getCurrentItem();
                    if (position == 0) {
                        position = getFirstItemPosition();
                    } else if (position == getCount() - 1) {
                        position = getLastItemPosition();
                    }
                    viewPager.setCurrentItem(position, false);
                }

                private int getRealCount() {
                    return imageList.size();
                }

                private int getRealPosition(int position) {
                    return position % getRealCount();
                }

                private int getFirstItemPosition() {
                    return Integer.MAX_VALUE / getRealCount() / 2 * getRealCount();
                }

                private int getLastItemPosition() {
                    return Integer.MAX_VALUE / getRealCount() / 2 * getRealCount() - 1;
                }
            });
            headerViewHolder.viewpager.setPageTransformer(true, new AlphaPageTransformer());
            headerViewHolder.viewpager.setCurrentItem(0, false);
            switchToPoint(0);
            headerViewHolder.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    switchToPoint(position % imageList.size());
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

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
            for (int i = 0; i < imageList.size(); i++) {
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

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.viewpager)
        ViewPager viewpager;
        @BindView(R.id.ll_point)
        LinearLayout ll_point;

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

}
