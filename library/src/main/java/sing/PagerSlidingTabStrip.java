package sing;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

import sing.frame.R;

public class PagerSlidingTabStrip extends HorizontalScrollView implements OnPageChangeListener {

    private static final int[] ATTRS = new int[]{android.R.attr.textSize, android.R.attr.textColor};

    private LinearLayout.LayoutParams tabViewLayoutParams;

    private LinearLayout tabsContainer;

    private ViewPager pager;

    private int tabCount;

    private int currentPosition = 0;


    private Paint rectPaint;

    private Paint dividerPaint;

    private int indicatorColor = 0xFF0888ff;

    //    private int underlineColor = getResources().getColor(R.color.skin_page_tab_underline_color);// 0xFFD9D9D9;
    private int underlineColor = getResources().getColor(R.color.white);// 0xFFD9D9D9;

    private int dividerColor = 0x00000000;

    private int checkedTextColor = R.color.color_blue_0888ff;

    private int unCheckedTextColor = R.color.action_bar_tittle_color_555555;

    private boolean textAllCaps = true;

    private int scrollOffset = 52;

    private int indicatorHeight = 3;

    private int underlineHeight = 2;

    private int dividerPadding = 12;

    private int tabPadding = 0;

    private int dividerWidth = 1;

    private int lastScrollX = 0;

    private Locale locale;

    private OnTabClickListener onTabClickListener = null;

    private OnTabDoubleTapListener onTabDoubleTapListener = null;

    private OnCustomTabListener onCustomTabListener = null;

    private Context context;

    public PagerSlidingTabStrip(Context context) {
        this(context, null);
        this.context = context;
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
        this.context = context;

        setFillViewport(true);
        setWillNotDraw(false);

        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(tabsContainer);

        DisplayMetrics dm = getResources().getDisplayMetrics();

        scrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
        indicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
        underlineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, underlineHeight, dm);
        dividerPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerPadding, dm);
        tabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
        dividerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerWidth, dm);

        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);

        a.recycle();

        a = context.obtainStyledAttributes(attrs, R.styleable.PagerSlidingTabStrip);

        indicatorColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsIndicatorColor, indicatorColor);
        underlineColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsUnderlineColor, underlineColor);
        dividerColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsDividerColor, dividerColor);

        checkedTextColor = a.getResourceId(R.styleable.TwoTextView_ttLeftTextColor, R.color.color_blue_0888ff);
        unCheckedTextColor = a.getResourceId(R.styleable.TwoTextView_ttLeftTextColor, R.color.action_bar_tittle_color_555555);
        indicatorHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsIndicatorHeight, indicatorHeight);
        underlineHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsUnderlineHeight, underlineHeight);
        dividerPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsDividerPadding, dividerPadding);
        tabPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsTabPaddingLeftRight, tabPadding);
        scrollOffset = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsScrollOffset, scrollOffset);
        textAllCaps = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsTextAllCaps, textAllCaps);

        a.recycle();

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Style.FILL);

        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(dividerWidth);

        tabViewLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);

        if (locale == null) {
            locale = getResources().getConfiguration().locale;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        currentPosition = position;
        scrollToChild(position, (int) (positionOffset * tabsContainer.getChildAt(position).getWidth()));

        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            scrollToChild(pager.getCurrentItem(), 0);
        }
    }

    @Override
    public void onPageSelected(int position) {
        setChooseTabViewTextColor(position);
    }

    public void setViewPager(ViewPager pager) {
        this.pager = pager;

        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }

        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        tabsContainer.removeAllViews();
        tabCount = pager.getAdapter().getCount();

        for (int i = 0; i < tabCount; i++) {
            addTabView(i, pager.getAdapter().getPageTitle(i).toString());
        }

        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                currentPosition = pager.getCurrentItem();
                setChooseTabViewTextColor(currentPosition);
                scrollToChild(currentPosition, 0);
            }
        });
    }

    private void setChooseTabViewTextColor(int position) {
        int childCount = tabsContainer.getChildCount();
        LinearLayout tabView;
        TextView textView;
        ImageView tabPicIV;
        for (int i = 0; i < childCount; ++i) {
            tabView = (LinearLayout) tabsContainer.getChildAt(i);
            textView = (TextView) tabView.findViewById(R.id.tab_title_label);
            tabPicIV = (ImageView) tabView.findViewById(R.id.image_tab_label);
            if (i == position) {
                textView.setTextColor(getResources().getColor(checkedTextColor));
                if (null != tabPicIV) {
                    tabPicIV.setBackgroundResource(iconListPress[i]);
                }
            } else {
                textView.setTextColor(getResources().getColor(unCheckedTextColor));
                if (null != tabPicIV) {
                    tabPicIV.setBackgroundResource(iconList[i]);
                }
            }
        }
    }

    private void addTabView(final int position, String title) {
        View tabView = null;
        boolean screenAdaptation = false;
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        if (this.onCustomTabListener != null) {
            int tabResId = onCustomTabListener.getTabLayoutResId(position);
            if (tabResId != 0) {
                tabView = inflater.inflate(tabResId, null);
            } else {
                tabView = onCustomTabListener.getTabLayoutView(inflater, position);
            }
            screenAdaptation = onCustomTabListener.screenAdaptation();
        }
        if (tabView == null) {
            tabView = inflater.inflate(R.layout.tab_layout_main, null);
        }
        TextView titleTV = (TextView) tabView.findViewById(R.id.tab_title_label);
        final boolean needAdaptation = getResources().getDisplayMetrics().density <= 1.5 && screenAdaptation;
        final Resources resources = getContext().getResources();
        if (titleTV != null) {
            titleTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, needAdaptation ? resources.getDimensionPixelSize(R.dimen.text_size_12) : resources.getDimensionPixelSize(R.dimen.text_size_16));
            titleTV.setText(title);
        }
/*------------------------------wgd20170208加入，tab图片显示----------------------------------------------------------------------------------------------*/
        ImageView tabIV = (ImageView) tabView.findViewById(R.id.image_tab_label);
        int imgsize = dip2px(context, 21);
        LinearLayout.LayoutParams imgLParams = new LinearLayout.LayoutParams(imgsize, imgsize);
        imgLParams.topMargin = dip2px(context, 3);

        if (null != tabIV) tabIV.setLayoutParams(imgLParams);

        for (int i = 0; i < strResIds.length; i++) {
            if (title.equals(strResIds[i])){
                tabIV.setBackgroundResource(iconList[i]);
            }
        }

        addTab(position, tabView);
    }

    private int[] strResIds;
    private int[] iconList;
    private int[] iconListPress;
    public void setTabValue(int[] strResIds,int[] iconList,int[] iconListPress){
        if (strResIds.length != iconList.length || iconList.length != iconListPress.length){
            throw new RuntimeException("Data length is not equal ！");
        }
        this.strResIds = strResIds;
        this.iconList = iconList;
        this.iconListPress = iconListPress;
    }

    private void addTab(final int position, View tab) {
        tab.setFocusable(true);
        tab.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (pager.getCurrentItem() == position && onTabClickListener != null) {
                    onTabClickListener.onCurrentTabClicked(position);
                } else {
                    pager.setCurrentItem(position, true);
                }
            }
        });
        addTabDoubleTapListener(position, tab);
        tab.setPadding(tabPadding, 0, tabPadding, 0);
        tabsContainer.addView(tab, position, tabViewLayoutParams);
    }

    private void addTabDoubleTapListener(final int position, View tab) {
        final GestureDetector gd = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (onTabDoubleTapListener != null)
                    onTabDoubleTapListener.onCurrentTabDoubleTap(position);

                return true;
            }
        });

        tab.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gd.onTouchEvent(event);
            }
        });
    }

    private void scrollToChild(int position, int offset) {
        if (tabCount == 0) {
            return;
        }

        int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;

        if (position > 0 || offset > 0) {
            newScrollX -= scrollOffset;
        }

        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        // currentPosition = savedState.currentPosition;
        currentPosition = 0;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = currentPosition;
        return savedState;
    }

    static class SavedState extends BaseSavedState {

        int currentPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    public void setOnTabClickListener(OnTabClickListener onTabClickListener) {
        this.onTabClickListener = onTabClickListener;
    }

    public void setOnTabDoubleTapListener(OnTabDoubleTapListener onTabDoubleTapListener) {
        this.onTabDoubleTapListener = onTabDoubleTapListener;
    }

    /**
     * must invoke before setViewPager
     *
     * @param onCustomTabListener
     */
    public void setOnCustomTabListener(OnCustomTabListener onCustomTabListener) {
        this.onCustomTabListener = onCustomTabListener;
    }

    /**
     * TAB 的点击监�?
     */
    public interface OnTabClickListener {
        void onCurrentTabClicked(int position);
    }

    /**
     * TAB 的双击监�?
     */
    public interface OnTabDoubleTapListener {
        void onCurrentTabDoubleTap(int position);
    }

    /**
     * 获取每个TAB的自定义布局
     */
    public static class OnCustomTabListener {

        /**
         * �?要自定义TAB的布�?�?
         * 重写该方法，返回对应的layout id
         *
         * @param position
         * @return
         */
        public int getTabLayoutResId(int position) {
            return 0;
        }

        /**
         * �?要自定义TAB的布�?
         * 重写该方法，直接返回对应的view
         *
         * @param inflater
         * @param position
         * @return
         */
        public View getTabLayoutView(LayoutInflater inflater, int position) {
            return null;
        }

        /**
         * 是否�?要小屏幕适配,只在存在多个tab，且tab的布�?较紧时�?�配,现在只用于主界面
         *
         * @return
         */
        public boolean screenAdaptation() {
            return false;
        }
    }

    public int dip2px(Context context, float dipValue) {
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        float density = dm.density;
        return (int) (dipValue * density + 0.5f);
    }
}
