package com.sharechain.finance.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharechain.finance.R;
import com.sharechain.finance.bean.NewsChannelTable;
import com.sharechain.finance.view.helper.OnDragVHListener;
import com.sharechain.finance.view.helper.OnItemMoveListener;

import java.util.List;

/**
 * Created by Chu on 2018/1/5.
 */

public class ChannelFixedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnItemMoveListener {
    // 我的频道
    public static final int TYPE_MY = 1;
    private static final long ANIM_TIME = 360L;

    // touch 点击开始时间
    private long startTime;
    // touch 间隔时间  用于分辨是否是 "点击"
    private static final long SPACE_TIME = 100;

    private LayoutInflater mInflater;
    private ItemTouchHelper mItemTouchHelper;

    // 是否为 编辑 模式
    private boolean isEditMode;

    private List<NewsChannelTable> mMyChannelItems;

    // 我的频道点击事件
    private OnMyChannelItemClickListener mChannelItemClickListener;
    private Context context;
    private boolean isDataChanged = false;

    public ChannelFixedAdapter(Context context, ItemTouchHelper helper, List<NewsChannelTable> mMyChannelItems, List<NewsChannelTable> mOtherChannelItems) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mItemTouchHelper = helper;
        this.mMyChannelItems = mMyChannelItems;
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_MY;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = mInflater.inflate(R.layout.item_my, parent, false);
        final ChannelFixedAdapter.MyViewHolder myHolder = new ChannelFixedAdapter.MyViewHolder(view);
        myHolder.text_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                int position = myHolder.getAdapterPosition();
                if (isEditMode) {
                    if (mMyChannelItems.get(position).getNewsChannelFixed()) {
                        return;
                    }
                    RecyclerView recyclerView = ((RecyclerView) parent);
                    View targetView = recyclerView.getLayoutManager().findViewByPosition(mMyChannelItems.size());
                    View currentView = recyclerView.getLayoutManager().findViewByPosition(position);
                    // 如果targetView不在屏幕内,则indexOfChild为-1  此时不需要添加动画,因为此时notifyItemMoved自带一个向目标移动的动画
                    // 如果在屏幕内,则添加一个位移动画
                    if (recyclerView.indexOfChild(targetView) >= 0) {
                        int targetX, targetY;
                        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                        int spanCount = ((GridLayoutManager) manager).getSpanCount();
                        // 移动后 高度将变化 (我的频道Grid 最后一个item在新的一行第一个)
                        if ((mMyChannelItems.size()) % spanCount == 0) {
                            View preTargetView = recyclerView.getLayoutManager().findViewByPosition(mMyChannelItems.size() - 1);
                            targetX = preTargetView.getLeft();
                            targetY = preTargetView.getTop();
                        } else {
                            targetX = targetView.getLeft();
                            targetY = targetView.getTop();
                        }
                        moveMyToOther(myHolder);
//                                startAnimation(recyclerView, currentView, targetX, targetY);
                    } else {
                        moveMyToOther(myHolder);
                    }
                } else {
                    mChannelItemClickListener.onItemClick(v, position, false);
                }
            }
        });

        myHolder.text_item.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isEditMode) {
                    switch (MotionEventCompat.getActionMasked(event)) {
                        case MotionEvent.ACTION_DOWN:
                            startTime = System.currentTimeMillis();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (System.currentTimeMillis() - startTime > SPACE_TIME) {
                                isDataChanged = true;
                                mItemTouchHelper.startDrag(myHolder);
                            }
                            break;
                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_UP:
                            startTime = 0;
                            break;
                    }

                }
                return false;
            }
        });
        return myHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            ChannelFixedAdapter.MyViewHolder myHolder = (ChannelFixedAdapter.MyViewHolder) holder;
            int pos = position;
            myHolder.text_item.setText(mMyChannelItems.get(pos).getNewsChannelName());
            if (mMyChannelItems.get(pos).getNewsChannelFixed()) {
                myHolder.text_item.setBackgroundResource(R.drawable.common_transparent_bg);
                myHolder.text_item.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
                myHolder.image_delete.setVisibility(View.GONE);
            } else {
                myHolder.text_item.setBackgroundResource(R.drawable.common_tag_circlr_bg);
                myHolder.text_item.setTextColor(ContextCompat.getColor(context, R.color.about_font));
                if (isEditMode) {
                    myHolder.image_delete.setVisibility(View.VISIBLE);
                } else {
                    myHolder.image_delete.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        // 我的频道  标题 + 我的频道.size + 其他频道 标题 + 其他频道.size
        return mMyChannelItems.size();
    }

    /**
     * 开始增删动画
     */
    private void startAnimation(RecyclerView recyclerView, final View currentView, float targetX, float targetY) {
        final ViewGroup viewGroup = (ViewGroup) recyclerView.getParent();
        final ImageView mirrorView = addMirrorView(viewGroup, recyclerView, currentView);
        Animation animation = getTranslateAnimator(
                targetX - currentView.getLeft(), targetY - currentView.getTop());
        currentView.setVisibility(View.INVISIBLE);
        mirrorView.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                viewGroup.removeView(mirrorView);
                if (currentView.getVisibility() == View.INVISIBLE) {
                    currentView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 我的频道 移动到 其他频道
     *
     * @param myHolder
     */
    private void moveMyToOther(ChannelFixedAdapter.MyViewHolder myHolder) {
        int position = myHolder.getAdapterPosition();

        int startPosition = position;
        if (startPosition > mMyChannelItems.size() - 1) {
            return;
        }
        if (mChannelItemClickListener != null) {
            mChannelItemClickListener.onItemClick(myHolder.itemView, startPosition, true);
        }
        isDataChanged = true;
    }

    /**
     * 添加需要移动的 镜像View
     */
    private ImageView addMirrorView(ViewGroup parent, RecyclerView recyclerView, View view) {
        /**
         * 我们要获取cache首先要通过setDrawingCacheEnable方法开启cache，然后再调用getDrawingCache方法就可以获得view的cache图片了。
         buildDrawingCache方法可以不用调用，因为调用getDrawingCache方法时，若果cache没有建立，系统会自动调用buildDrawingCache方法生成cache。
         若想更新cache, 必须要调用destoryDrawingCache方法把旧的cache销毁，才能建立新的。
         当调用setDrawingCacheEnabled方法设置为false, 系统也会自动把原来的cache销毁。
         */
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        final ImageView mirrorView = new ImageView(recyclerView.getContext());
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        mirrorView.setImageBitmap(bitmap);
        view.setDrawingCacheEnabled(false);
        int[] locations = new int[2];
        view.getLocationOnScreen(locations);
        int[] parenLocations = new int[2];
        recyclerView.getLocationOnScreen(parenLocations);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(bitmap.getWidth(), bitmap.getHeight());
        params.setMargins(locations[0], locations[1] - parenLocations[1], 0, 0);
        parent.addView(mirrorView, params);

        return mirrorView;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (isChannelFixed(fromPosition, toPosition)) {
            return;
        }
        NewsChannelTable item = mMyChannelItems.get(fromPosition);
        mMyChannelItems.remove(fromPosition);
        mMyChannelItems.add(toPosition, item);
        notifyItemMoved(fromPosition, toPosition);
    }

    private boolean isChannelFixed(int fromPosition, int toPosition) {
        return mMyChannelItems.get(fromPosition).getNewsChannelFixed() ||
                mMyChannelItems.get(toPosition).getNewsChannelFixed();
    }

    /**
     * 开启编辑模式
     */
    public void startEditMode() {
        isEditMode = true;
        notifyDataSetChanged();
    }

    /**
     * 完成编辑模式
     */
    public void cancelEditMode() {
        isEditMode = false;
        notifyDataSetChanged();
    }

    /**
     * 获取位移动画
     */
    private TranslateAnimation getTranslateAnimator(float targetX, float targetY) {
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.ABSOLUTE, targetX,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.ABSOLUTE, targetY);
        // RecyclerView默认移动动画250ms 这里设置360ms 是为了防止在位移动画结束后 remove(view)过早 导致闪烁
        translateAnimation.setDuration(ANIM_TIME);
        translateAnimation.setFillAfter(true);
        return translateAnimation;
    }

    public interface OnMyChannelItemClickListener {
        void onItemClick(View v, int position, boolean isManage);
    }

    public void setOnMyChannelItemClickListener(ChannelFixedAdapter.OnMyChannelItemClickListener listener) {
        this.mChannelItemClickListener = listener;
    }

    /**
     * 我的频道
     */
    class MyViewHolder extends RecyclerView.ViewHolder implements OnDragVHListener {
        private TextView text_item;
        private ImageView image_delete;

        public MyViewHolder(View itemView) {
            super(itemView);
            text_item = itemView.findViewById(R.id.text_item);
            image_delete = itemView.findViewById(R.id.image_delete);
        }

        /**
         * item 被选中时
         */
        @Override
        public void onItemSelected() {
//            textView.setBackgroundResource(R.drawable.bg_channel_p);
        }

        /**
         * item 取消选中时
         */
        @Override
        public void onItemFinish() {
//            textView.setBackgroundResource(R.drawable.bg_channel);
        }
    }

    public List<NewsChannelTable> getmMyChannelItems() {
        return mMyChannelItems;
    }

    public boolean isDataChanged() {
        return isDataChanged;
    }

    public void setDataChanged(boolean isDataChanged) {
        this.isDataChanged = isDataChanged;
    }

}