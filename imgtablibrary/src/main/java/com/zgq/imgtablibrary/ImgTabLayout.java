package com.zgq.imgtablibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static android.support.v4.view.ViewPager.SCROLL_STATE_IDLE;

/**
 * @author zgq
 * @version 1.0.0
 * @describe -- 自定义TabLayout，底部指示器可以设置为图片资源
 * @date 2019/3/22
 */

public class ImgTabLayout extends HorizontalScrollView {

    private int textGravity;//textView位置
    private int mTextColorDef;//文本默认颜色
    private int mTextColorSelect;//文本选中颜色
    private int mTextBgDefResId;//默认背景
    private int mTextBgSelectResId;
    private int indicatorHeight, indicatorWidth;//指示器宽高
    private int indicatorBgResId;// 指示器图片（或颜色）资源

    private int mScrollViewWidth = 0, mScrollViewMiddle = 0, selectedTabPosition = -1, tabCount;
    private int viewHeight, viewWidth, innerLeftMargin, innerRightMargin;// item高度，item宽度，item距左，item距右
    private float textSize = 18;//默认字体大小(px)。
    private boolean averageTab; // 是否屏幕等分
    private int mWidth;//总宽度
    private float textSizeSel;//选中后字体大小(px)。

    private Handler mHandler = null;
    private ViewPager mViewPager;
    private List<CharSequence> mTabList;
    private LinearLayout layContent;
    private ImageView indicatorView;
    private TabLayoutOnPageChangeListener mPageChangeListener;
    private OnTabLayoutItemSelectListener onTabLayoutItemSelectListener;
    private List<TextView> mViewsList;


    private static class StaticHandler extends Handler {
        private final WeakReference<Context> mWeakContext;
        private final WeakReference<ImgTabLayout> mParent;

        public StaticHandler(Context context, ImgTabLayout view) {
            mWeakContext = new WeakReference<>(context);
            mParent = new WeakReference<>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            Context context = mWeakContext.get();
            ImgTabLayout parent = mParent.get();
            if (null != context && null != parent) {
                switch (msg.what) {
                    case 0:
                        parent.changeItemLocation(parent.selectedTabPosition);
                        break;

                    default:
                        break;
                }

                super.handleMessage(msg);
            }
        }
    }


    public ImgTabLayout(Context context) {
        this(context, null);
    }

    public ImgTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImgTabLayout);
        mTextColorDef = a.getColor(R.styleable.ImgTabLayout_mTextColorDef, Color.BLACK);
        mTextColorSelect = a.getColor(R.styleable.ImgTabLayout_mTextColorSelect, Color.BLUE);
        mTextBgDefResId = a.getResourceId(R.styleable.ImgTabLayout_mTextBgDefResId, 0);
        mTextBgSelectResId = a.getResourceId(R.styleable.ImgTabLayout_mTextBgSelectResId, 0);
        indicatorBgResId = a.getResourceId(R.styleable.ImgTabLayout_indicatorBgResId, Color.RED);

        indicatorHeight = a.getDimensionPixelSize(R.styleable.ImgTabLayout_indicatorHeight, 5);
        indicatorWidth = a.getDimensionPixelSize(R.styleable.ImgTabLayout_indicatorWidth, 40);
        viewHeight = a.getDimensionPixelSize(R.styleable.ImgTabLayout_viewHeight, 0);
        viewWidth = a.getDimensionPixelSize(R.styleable.ImgTabLayout_viewWidth,0);
        innerLeftMargin = a.getDimensionPixelSize(R.styleable.ImgTabLayout_innerLeftMargin, 0);
        innerRightMargin = a.getDimensionPixelSize(R.styleable.ImgTabLayout_innerRightMargin, 0);

        textSize = a.getDimensionPixelSize(R.styleable.ImgTabLayout_textSize, 18);
        textSizeSel = a.getDimensionPixelSize(R.styleable.ImgTabLayout_textSizeSel, 0);

        textGravity = Gravity.CENTER_HORIZONTAL;//默认水平居中

        mHandler = new StaticHandler(context, this);
        mViewsList = new ArrayList<>();

        setHorizontalScrollBarEnabled(false);
        setHorizontalFadingEdgeEnabled(false);

        FrameLayout layParent = new FrameLayout(context);
        addView(layParent);
        layContent = new LinearLayout(context);
        layParent.addView(layContent);
        indicatorView = new ImageView(context);
        layParent.addView(indicatorView);

    }

    public int getViewWidth() {
        return viewWidth;
    }

    public int getViewHeight() {
        return viewHeight;
    }

    public void setTextGravity(int textGravity) {
        this.textGravity = textGravity;
    }

    public void setViewWidth(int viewWidth) {
        this.viewWidth = viewWidth;
    }

    public void setInnerLeftMargin(int innerLeftMargin) {
        this.innerLeftMargin = innerLeftMargin;
    }

    public void setInnerRightMargin(int innerRightMargin) {
        this.innerRightMargin = innerRightMargin;
    }

    public void setmTextBgDefResId(int mTextBgDefResId) {
        this.mTextBgDefResId = mTextBgDefResId;
    }

    public void setmTextBgSelectResId(int mTextBgSelectResId) {
        this.mTextBgSelectResId = mTextBgSelectResId;
    }

    public void setmTextColorSelect(int mTextColorSelect) {
        this.mTextColorSelect = mTextColorSelect;
    }

    public void setmTextColorSelectId(int colorId) {
        this.mTextColorSelect = getResources().getColor(colorId);
    }

    public void setmTextColorDef(int mTextColorDef) {
        this.mTextColorDef = mTextColorDef;
    }

    public void setmTextColorUnSelectId(int colorId) {
        this.mTextColorDef = getResources().getColor(colorId);
    }

    public void setViewHeight(int viewHeightPx) {
        this.viewHeight = viewHeightPx;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public void setTextSizeSel(float textSizeSel) {
        this.textSizeSel = textSizeSel;
    }

    public void setAverageTab(boolean averageTab, int mWidth) {
        this.averageTab = averageTab;
        this.mWidth = mWidth;
    }

    public void setIndicatorHeight(int indicatorHeight) {
        this.indicatorHeight = indicatorHeight;
    }

    public void setIndicatorWidth(int indicatorWidth) {
        this.indicatorWidth = indicatorWidth;
    }

    public void setIndicatorBgResId(int indicatorBgResId) {
        this.indicatorBgResId = indicatorBgResId;
    }

    public View getIndicatorView() {
        return indicatorView;
    }

    private void setData(List<CharSequence> mTabList) {
        if (mTabList == null || mTabList.size() == 0) return;
        this.mTabList = mTabList;
        initView();
    }

    //只是用标题, 不使用ViewPager
    public void setTabData(List<CharSequence> mTabList, int defaultPos) {
        if (mTabList == null || mTabList.size() == 0) return;
        this.mTabList = mTabList;
        if (defaultPos >= 0 && defaultPos < mTabList.size()) {
            selectedTabPosition = defaultPos;
        } else {
            selectedTabPosition = 0;
        }
        initView();
        clickTabSelItem(selectedTabPosition);
    }

    private void initView() {
        if (mTabList == null || mTabList.size() == 0) return;
        mViewsList = new ArrayList<>();
        layContent.removeAllViews();

        for (int i = 0; i < mTabList.size(); i++) {
            TextView textView = new TextView(getContext());
            if (averageTab) {
                LinearLayout linearLayout = new LinearLayout(getContext());
                linearLayout.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                if (mWidth > 0)
                    layoutParams.width = mWidth / mTabList.size();
                linearLayout.addView(textView);
                layContent.addView(linearLayout, layoutParams);
            } else {
                layContent.addView(textView);
            }

            textView.setGravity(textGravity);
            if (i == selectedTabPosition) {
                textView.setBackgroundResource(mTextBgSelectResId);
                textView.setTextColor(mTextColorSelect);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeSel <= 0 ? textSize : textSizeSel);//如果选中字体未设置，选用普通字体
            } else {
                textView.setBackgroundResource(mTextBgDefResId);
                textView.setTextColor(mTextColorDef);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);//数据类型px
            }
            textView.setTag(i);
            textView.setText(mTabList.get(i));
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = Integer.parseInt(view.getTag().toString());
                    if (mViewPager != null)
                        mViewPager.setCurrentItem(position);
                    else
                        clickTabSelItem(position);
                }
            });

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    viewWidth > 0 ? viewWidth : LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            if (!averageTab) {
                layoutParams.rightMargin = innerRightMargin;
                layoutParams.leftMargin = innerLeftMargin;
            }
            layoutParams.height = viewHeight<=0?LayoutParams.WRAP_CONTENT:viewHeight;
            textView.setLayoutParams(layoutParams);
            mViewsList.add(textView);
        }
        initIndicatorView();
        mHandler.sendEmptyMessageDelayed(0, 200);
    }

    private void initIndicatorView() {
        indicatorView.setImageResource(indicatorBgResId);
        LayoutParams fl = (LayoutParams) indicatorView.getLayoutParams();
        fl.width = indicatorWidth;
        fl.height = indicatorHeight;
        fl.gravity = Gravity.BOTTOM;
        indicatorView.setLayoutParams(fl);
    }

    private void clickTabSelItem(int position) {

        if (mViewsList == null) return;
        selectedTabPosition = position;
        if (null != onTabLayoutItemSelectListener)
            onTabLayoutItemSelectListener.onTabLayoutItemSelect(position);

        for (int i = 0; i < mViewsList.size(); i++) {
            TextView textView = mViewsList.get(i);
            if (Integer.parseInt(mViewsList.get(i).getTag().toString()) == position) {
                textView.setBackgroundResource(mTextBgSelectResId);
                textView.setTextColor(mTextColorSelect);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSizeSel <= 0 ? textSize : textSizeSel);//如果选中字体未设置，选用普通字体
                changeItemLocation(i);
            } else {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
                mViewsList.get(i).setBackgroundResource(mTextBgDefResId);
                mViewsList.get(i).setTextColor(mTextColorDef);
            }
        }

    }

    public void setCurrentItem(int position) {
        if (mViewPager != null) {
            mViewPager.setCurrentItem(position);
        } else
            clickTabSelItem(position);
    }

    public TextView getTextView(int position) {
        if (mViewsList == null || position >= mViewsList.size())
            throw new RuntimeException("mViewsList == null || position >= mViewsList.size()");
        return mViewsList.get(position);
    }

    public LinearLayout getLayContent() {
        return layContent;
    }

    /**
     * 改变tabItem滚动位置
     * @param position
     */
    private void changeItemLocation(int position) {
        if (position >= 0 && position < mViewsList.size()) {
            changeIndicatorLocation();
            int x = (mViewsList.get(position).getLeft() - getScrollViewMiddle() +
                    (getViewHeight(mViewsList.get(position)) / 2));
            smoothScrollTo(x, 0);
        }
    }

    /**
     * 改变底部指示器位置
     */
    private void changeIndicatorLocation() {
        if (selectedTabPosition >= 0 && selectedTabPosition < mViewsList.size()) {
            TextView textView = getTextView(selectedTabPosition);
            int x;
            if (averageTab) {
                int[] position = new int[2];
                textView.getLocationOnScreen(position);
                int l1 = position[0];
                if (l1 == 0) {
                    int sWidth = mWidth / mViewsList.size();//tab的宽度
                    int bWidth = sWidth / 2;//tab/2
                    textView.measure(0, 0);
                    l1 = sWidth * selectedTabPosition + bWidth - textView.getMeasuredWidth() / 2;
                }
                x = l1 + (textView.getRight() - textView.getLeft() - indicatorWidth) / 2;
            } else
                x = textView.getLeft() + (textView.getRight() - textView.getLeft() - indicatorWidth) / 2;
            LayoutParams fl = (LayoutParams) indicatorView.getLayoutParams();
            fl.leftMargin = x;
            indicatorView.setLayoutParams(fl);
        }
    }

    /**
     * 返回scrollview的中间位置
     *
     * @return
     */
    private int getScrollViewMiddle() {
        if (mScrollViewMiddle == 0)
            mScrollViewMiddle = getScrollViewWidth() / 2;
        return mScrollViewMiddle;
    }

    /**
     * 返回ScrollView的宽度
     *
     * @return
     */
    private int getScrollViewWidth() {
        if (mScrollViewWidth == 0)
            mScrollViewWidth = getRight() - getLeft();
        return mScrollViewWidth;
    }


    private int getViewHeight(View view) {
        return view.getBottom() - view.getTop();
    }


    /**
     * 设置viewPager
     * @param viewPager
     */
    public void setViewPager(@Nullable ViewPager viewPager) {
        if (viewPager != null) {
            mViewPager = viewPager;
            if (mPageChangeListener == null) {
                mPageChangeListener = new TabLayoutOnPageChangeListener(this);
            }
            mPageChangeListener.reset();
            viewPager.addOnPageChangeListener(mPageChangeListener);
            selectedTabPosition = viewPager.getCurrentItem();

            final PagerAdapter adapter = viewPager.getAdapter();
            if (adapter != null) {
                tabCount = adapter.getCount();
                List<CharSequence> tabList = new ArrayList<>();
                for (int i = 0; i < tabCount; i++) {
                    tabList.add(adapter.getPageTitle(i));
                }
                setData(tabList);
            } else {
                tabCount = 0;
            }
        }
    }

    public static class TabLayoutOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private final WeakReference<ImgTabLayout> mTabLayoutRef;
        private int mPreviousScrollState;
        private int mScrollState;

        public TabLayoutOnPageChangeListener(ImgTabLayout tabLayout) {
            mTabLayoutRef = new WeakReference<>(tabLayout);
        }

        @Override
        public void onPageScrollStateChanged(final int state) {
            mPreviousScrollState = mScrollState;
            mScrollState = state;
        }

        @Override
        public void onPageScrolled(final int position, final float positionOffset,
                                   final int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(final int position) {
            final ImgTabLayout tabLayout = mTabLayoutRef.get();
            if (tabLayout != null && tabLayout.getSelectedTabPosition() != position
                    && position < tabLayout.getTabCount()) {
                tabLayout.clickTabSelItem(position);
            }
        }

        void reset() {
            mPreviousScrollState = mScrollState = SCROLL_STATE_IDLE;
        }
    }

    public int getSelectedTabPosition() {
        return selectedTabPosition;
    }

    public int getTabCount() {
        return tabCount;
    }

    public void setOnTabLayoutItemSelectListener(OnTabLayoutItemSelectListener listener) {
        onTabLayoutItemSelectListener = listener;
    }

    public interface OnTabLayoutItemSelectListener {

        void onTabLayoutItemSelect(int position);

    }

    /* 根据手机的分辨率从 dp 的单位 转成为 px(像素) */
    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
