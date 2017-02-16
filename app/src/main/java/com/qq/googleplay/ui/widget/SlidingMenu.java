package com.qq.googleplay.ui.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.qq.googleplay.R;
/**
 * ============================================================
 * Copyright：Google有限公司版权所有 (c) 2017
 * Author：   陈冠杰
 * Email：    815712739@qq.com
 * GitHub：   https://github.com/JackChen1999
 * 博客：     http://blog.csdn.net/axi295309066
 * 微博：     AndroidDeveloper
 * <p>
 * Project_Name：GooglePlay
 * Package_Name：com.qq.googleplay
 * Version：1.0
 * time：2016/2/16 13:33
 * des ：${TODO}
 * gitVersion：$Rev$
 * updateAuthor：$Author$
 * updateDate：$Date$
 * updateDes：${TODO}
 * ============================================================
 **/
public class SlidingMenu extends FrameLayout {
    public static final int LEFT = 0;
    public static final int LEFT_RIGHT = 2;
    public static final int RIGHT = 1;
    public static final int SLIDING_CONTENT = 1;
    public static final int SLIDING_WINDOW = 0;
    private static final String TAG = "SlidingMenu";
    public static final int TOUCHMODE_FULLSCREEN = 1;
    public static final int TOUCHMODE_MARGIN = 0;
    public static final int TOUCHMODE_NONE = 2;
    private boolean mActionbarOverlay;
    private OnCloseListener mCloseListener;
    private Context mContext;
    private Handler mHandler;
    private LocalActivityManager mLocalActivityManager;
    private OnOpenListener mOpenListener;
    private OnMenuStateChangeListener mSlidingMenuStateChangeListener;
    private CustomViewAbove mViewAbove;
    private int mViewAboveOffset;
    private CustomViewBehind mViewBehind;

    public interface CanvasTransformer {
        void transformCanvas(Canvas canvas, float f);
    }

    public interface OnCloseListener {
        void onClose();
    }

    public interface OnClosedListener {
        void onClosed();
    }

    public interface OnMenuStateChangeListener {
        void onMenuState(State state);

        void onScrolling(int i);
    }

    public interface OnOpenListener {
        void onOpen();
    }

    public interface OnOpenedListener {
        void onOpened();
    }

    public static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        private final int mItem;

        public SavedState(Parcelable parcelable, int i) {
            super(parcelable);
            this.mItem = i;
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.mItem = parcel.readInt();
        }

        public int getItem() {
            return this.mItem;
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.mItem);
        }
    }

    public enum State {
        OPEN,
        CLOSE
    }

    public SlidingMenu(Context context) {
        this(context, null);
    }

    public SlidingMenu(Activity activity, int i) {
        this((Context) activity, null);
        attachToActivity(activity, i);
    }

    public SlidingMenu(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SlidingMenu(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mActionbarOverlay = false;
        this.mViewAboveOffset = 0;
        this.mHandler = new Handler();
        this.mContext = context;
        LayoutParams layoutParams = new LayoutParams(-1, -1);
        this.mViewBehind = new CustomViewBehind(context);
        addView(this.mViewBehind, layoutParams);
        layoutParams = new LayoutParams(-1, -1);
        this.mViewAbove = new CustomViewAbove(context);
        addView(this.mViewAbove, layoutParams);
        this.mViewAbove.setCustomViewBehind(this.mViewBehind);
        this.mViewBehind.setCustomViewAbove(this.mViewAbove);
        this.mViewAbove.setOnPageChangeListener(new CustomViewAbove.OnPageChangeListener() {
            public static final int POSITION_CLOSE = 1;
            public static final int POSITION_OPEN = 0;

            public void onPageScrolled(int i, float f, int i2) {
                if (SlidingMenu.this.mSlidingMenuStateChangeListener != null) {
                    SlidingMenu.this.mSlidingMenuStateChangeListener.onScrolling(i2);
                }
            }

            public void onPageSelected(int i) {
                if (i == 0 && SlidingMenu.this.mOpenListener != null) {
                    SlidingMenu.this.mOpenListener.onOpen();
                } else if (i == 1 && SlidingMenu.this.mCloseListener != null) {
                    SlidingMenu.this.mCloseListener.onClose();
                }
                if (SlidingMenu.this.mSlidingMenuStateChangeListener != null) {
                    State state;
                    if (i == 1) {
                        state = State.CLOSE;
                    } else {
                        state = State.OPEN;
                    }
                    SlidingMenu.this.mSlidingMenuStateChangeListener.onMenuState(state);
                }
            }
        });
        setShadowDrawable(R.drawable.drawer_shadow_light);
        setMenuWidthRes(R.dimen.slidingmenu_menu_width);
    }

    public void attachToActivity(Activity activity, int i) {
        attachToActivity(activity, i, false);
    }

    public void attachToActivity(Activity activity, int i, boolean z) {
        if (i != 0 && i != 1) {
            throw new IllegalArgumentException("slideStyle must be either SLIDING_WINDOW or SLIDING_CONTENT");
        } else if (getParent() != null) {
            throw new IllegalStateException("This SlidingMenu appears to already be attached");
        } else {
            TypedArray obtainStyledAttributes = activity.getTheme().obtainStyledAttributes(new int[]{16842836});
            int resourceId = obtainStyledAttributes.getResourceId(0, 0);
            obtainStyledAttributes.recycle();
            ViewGroup viewGroup;
            View view;
            switch (i) {
                case 0:
                    this.mActionbarOverlay = false;
                    viewGroup = (ViewGroup) activity.getWindow().getDecorView();
                    view = (ViewGroup) viewGroup.getChildAt(0);
                    view.setBackgroundResource(resourceId);
                    viewGroup.removeView(view);
                    viewGroup.addView(this);
                    setContent(view);
                    return;
                case 1:
                    this.mActionbarOverlay = z;
                    viewGroup = (ViewGroup) activity.findViewById(android.R.id.content);
                    view = viewGroup.getChildAt(0);
                    viewGroup.removeView(view);
                    viewGroup.addView(this);
                    setContent(view);
                    if (view.getBackground() == null) {
                        view.setBackgroundResource(resourceId);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public void setup(LocalActivityManager localActivityManager) {
        this.mLocalActivityManager = localActivityManager;
    }

    public void setContent(int i) {
        setContent(LayoutInflater.from(getContext()).inflate(i, null));
    }

    public void setContent(View view) {
        this.mViewAbove.setContent(view);
        showContent();
    }

    public void setContent(Intent intent) {
        if (this.mLocalActivityManager == null) {
            throw new IllegalStateException("Did you forget to call 'public void setup(LocalActivityManager activityManager)'?");
        }
        View viewFromActivity = getViewFromActivity(this.mLocalActivityManager, intent);
        if (viewFromActivity == null) {
            throw new IllegalStateException("get content from Activity failed!");
        }
        setContent(viewFromActivity);
    }

    private View getViewFromActivity(LocalActivityManager localActivityManager, Intent intent) {
        View view = null;
        TypedArray obtainStyledAttributes = getContext().getTheme().obtainStyledAttributes(new int[]{16842836});
        int resourceId = obtainStyledAttributes.getResourceId(0, 0);
        obtainStyledAttributes.recycle();
        Window startActivity = localActivityManager.startActivity(null, intent);
        if (startActivity != null) {
            view = startActivity.getDecorView();
        }
        view.setBackgroundResource(resourceId);
        if (view != null) {
            view.setVisibility(View.VISIBLE);
            view.setFocusableInTouchMode(true);
            ((ViewGroup) view).setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        }
        return view;
    }

    public View getContent() {
        return this.mViewAbove.getContent();
    }

    public void setMenu(int i) {
        setMenu(LayoutInflater.from(getContext()).inflate(i, null));
    }

    public void setMenu(View view) {
        this.mViewBehind.setContent(view);
    }

    public void setMenu(Intent intent) {
        if (this.mLocalActivityManager == null) {
            throw new IllegalStateException("Did you forget to call 'public void setup(LocalActivityManager activityManager)'?");
        }
        View viewFromActivity = getViewFromActivity(this.mLocalActivityManager, intent);
        if (viewFromActivity == null) {
            throw new IllegalStateException("get content from Activity failed!");
        }
        setMenu(viewFromActivity);
    }

    public View getMenu() {
        return this.mViewBehind.getContent();
    }

    public void setSecondaryMenu(int i) {
        setSecondaryMenu(LayoutInflater.from(getContext()).inflate(i, null));
    }

    public void setSecondaryMenu(View view) {
        this.mViewBehind.setSecondaryContent(view);
    }

    public View getSecondaryMenu() {
        return this.mViewBehind.getSecondaryContent();
    }

    public void setSlidingEnabled(boolean z) {
        this.mViewAbove.setSlidingEnabled(z);
    }

    public boolean isSlidingEnabled() {
        return this.mViewAbove.isSlidingEnabled();
    }

    public void setMode(int i) {
        if (i == 0 || i == 1 || i == 2) {
            this.mViewBehind.setMode(i);
            return;
        }
        throw new IllegalStateException("SlidingMenu mode must be LEFT, RIGHT, or LEFT_RIGHT");
    }

    public void setMenuVisibleAlways(boolean z) {
        this.mViewBehind.setMenuVisibleAlways(z);
    }

    public int getMode() {
        return this.mViewBehind.getMode();
    }

    public void setStatic(boolean z) {
        if (z) {
            setSlidingEnabled(false);
            this.mViewAbove.setCustomViewBehind(this.mViewBehind);
            this.mViewAbove.setCurrentItem(1);
            return;
        }
        this.mViewAbove.setCurrentItem(1);
        this.mViewAbove.setCustomViewBehind(this.mViewBehind);
        setSlidingEnabled(true);
    }

    public void showMenu() {
        showMenu(true);
    }

    public void showMenu(boolean z) {
        this.mViewAbove.setCurrentItem(0, z);
    }

    public void showSecondaryMenu() {
        showSecondaryMenu(true);
    }

    public void showSecondaryMenu(boolean z) {
        this.mViewAbove.setCurrentItem(2, z);
    }

    public void showContent() {
        showContent(true);
    }

    public void showContent(boolean z) {
        this.mViewAbove.setCurrentItem(1, z);
    }

    public void toggle() {
        toggle(true);
    }

    public void toggle(boolean z) {
        if (isMenuShowing()) {
            showContent(z);
        } else {
            showMenu(z);
        }
    }

    public boolean isMenuShowing() {
        return this.mViewAbove.getCurrentItem() == 0 || this.mViewAbove.getCurrentItem() == 2;
    }

    public boolean isSecondaryMenuShowing() {
        return this.mViewAbove.getCurrentItem() == 2;
    }

    public int getBehindOffset() {
        return ((RelativeLayout.LayoutParams) this.mViewBehind.getLayoutParams()).rightMargin;
    }

    public void setMenuOffset(int i) {
        int i2;
        Display defaultDisplay = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        try {
            Class[] clsArr = new Class[]{Point.class};
            Point point = new Point();
            Display.class.getMethod("getSize", clsArr).invoke(defaultDisplay, new Object[]{point});
            i2 = point.x;
        } catch (Exception e) {
            i2 = defaultDisplay.getWidth();
        }
        setMenuWidth(i2 - i);
    }

    public void setMenuOffsetRes(int i) {
        setMenuOffset((int) getContext().getResources().getDimension(i));
    }

    public void setContentOffsetLeft(int i) {
        this.mViewAboveOffset = i;
        this.mViewAbove.setAboveOffsetLeft(i);
        ((MarginLayoutParams) this.mViewAbove.getLayoutParams()).setMargins(this.mViewAboveOffset, 0, 0, 0);
        requestLayout();
    }

    public int getMenuOffset() {
        return ((RelativeLayout.LayoutParams) this.mViewBehind.getLayoutParams()).rightMargin;
    }

    public int getContentOffsetLeft() {
        return this.mViewAboveOffset;
    }

    public void setContentOffsetRes(int i) {
        setContentOffsetLeft((int) getContext().getResources().getDimension(i));
    }

    public void setMenuWidth(int i) {
        this.mViewBehind.setMenuWidth(i);
    }

    public void setMenuWidthRes(int i) {
        setMenuWidth((int) getContext().getResources().getDimension(i));
    }

    public float getBehindScrollScale() {
        return this.mViewBehind.getScrollScale();
    }

    public void setBehindScrollScale(float f) {
        if (f >= 0.0f || f <= 1.0f) {
            this.mViewBehind.setScrollScale(f);
            return;
        }
        throw new IllegalStateException("ScrollScale must be between 0 and 1");
    }

    public void setBehindCanvasTransformer(CanvasTransformer canvasTransformer) {
        this.mViewBehind.setCanvasTransformer(canvasTransformer);
    }

    public int getTouchModeAbove() {
        return this.mViewAbove.getTouchMode();
    }

    public void setTouchModeAbove(int i) {
        if (i == 1 || i == 0 || i == 2) {
            this.mViewAbove.setTouchMode(i);
            return;
        }
        throw new IllegalStateException("TouchMode must be set to eitherTOUCHMODE_FULLSCREEN or TOUCHMODE_MARGIN or TOUCHMODE_NONE.");
    }

    public void setTouchModeBehind(int i) {
        if (i == 1 || i == 0 || i == 2) {
            this.mViewBehind.setTouchMode(i);
            return;
        }
        throw new IllegalStateException("TouchMode must be set to eitherTOUCHMODE_FULLSCREEN or TOUCHMODE_MARGIN or TOUCHMODE_NONE.");
    }

    public void setShadowDrawable(int i) {
        setShadowDrawable(getContext().getResources().getDrawable(i));
    }

    public void setShadowDrawable(Drawable drawable) {
        this.mViewBehind.setShadowDrawable(drawable);
    }

    public void setSecondaryShadowDrawable(int i) {
        setSecondaryShadowDrawable(getContext().getResources().getDrawable(i));
    }

    public void setSecondaryShadowDrawable(Drawable drawable) {
        this.mViewBehind.setSecondaryShadowDrawable(drawable);
    }

    public void setShadowWidthRes(int i) {
        setShadowWidth((int) getResources().getDimension(i));
    }

    public void setShadowWidth(int i) {
        this.mViewBehind.setShadowWidth(i);
    }

    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int defaultSize = getDefaultSize(0, i);
        int defaultSize2 = getDefaultSize(0, i2);
        setMeasuredDimension(defaultSize, defaultSize2);
        defaultSize = getChildMeasureSpec(i, 0, defaultSize - this.mViewAboveOffset);
        defaultSize2 = getChildMeasureSpec(i2, 0, defaultSize2);
        if (this.mViewAbove != null) {
            this.mViewAbove.measure(defaultSize, defaultSize2);
        }
    }

    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
    }

    public void setFadeEnabled(boolean z) {
        this.mViewBehind.setFadeEnabled(z);
    }

    public void setFadeDegree(float f) {
        this.mViewBehind.setFadeDegree(f);
    }

    public void setSelectorEnabled(boolean z) {
        this.mViewBehind.setSelectorEnabled(true);
    }

    public void setSelectedView(View view) {
        this.mViewBehind.setSelectedView(view);
    }

    public void setSelectorDrawable(int i) {
        this.mViewBehind.setSelectorBitmap(BitmapFactory.decodeResource(getResources(), i));
    }

    public void setSelectorBitmap(Bitmap bitmap) {
        this.mViewBehind.setSelectorBitmap(bitmap);
    }

    public void addIgnoredView(View view) {
        this.mViewAbove.addIgnoredView(view);
    }

    public void removeIgnoredView(View view) {
        this.mViewAbove.removeIgnoredView(view);
    }

    public void clearIgnoredViews() {
        this.mViewAbove.clearIgnoredViews();
    }

    public void setOnOpenListener(OnOpenListener onOpenListener) {
        this.mOpenListener = onOpenListener;
    }

    public void setOnCloseListener(OnCloseListener onCloseListener) {
        this.mCloseListener = onCloseListener;
    }

    public void setOnOpenedListener(OnOpenedListener onOpenedListener) {
        this.mViewAbove.setOnOpenedListener(onOpenedListener);
    }

    public void setOnClosedListener(OnClosedListener onClosedListener) {
        this.mViewAbove.setOnClosedListener(onClosedListener);
    }

    public void setOnMenuStateChangeListener(OnMenuStateChangeListener onMenuStateChangeListener) {
        this.mSlidingMenuStateChangeListener = onMenuStateChangeListener;
    }

    protected Parcelable onSaveInstanceState() {
        return new SavedState(super.onSaveInstanceState(), this.mViewAbove.getCurrentItem());
    }

    protected void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.mViewAbove.setCurrentItem(savedState.getItem());
    }

    @TargetApi(11)
    public void manageLayers(float f) {
        int i = 0;
        if (VERSION.SDK_INT >= 11) {
            int i2 = (f <= 0.0f || f >= 1.0f) ? 0 : 1;
            if (i2 != 0) {
                i = 2;
            }
            if (i != getContent().getLayerType()) {
                final int finalI = i;
                this.mHandler.post(new Runnable() {
                    public void run() {
                        Log.v(SlidingMenu.TAG, "changing layerType. hardware? " + (finalI == 2));
                        SlidingMenu.this.getContent().setLayerType(finalI, null);
                        SlidingMenu.this.getMenu().setLayerType(finalI, null);
                        if (SlidingMenu.this.getSecondaryMenu() != null) {
                            SlidingMenu.this.getSecondaryMenu().setLayerType(finalI, null);
                        }
                    }
                });
            }
        }
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(SlidingMenu.class.getName());
    }
}
