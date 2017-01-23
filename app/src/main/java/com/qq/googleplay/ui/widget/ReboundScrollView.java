package com.qq.googleplay.ui.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

public class ReboundScrollView extends ScrollView {
    private View mView;
    private float startY;
    private Rect mRect;
    private boolean animationFinish;

    public ReboundScrollView(Context context) {
        super(context);
        mRect = new Rect();
        animationFinish = true;
    }

    public ReboundScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRect = new Rect();
        animationFinish = true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            mView = getChildAt(0);
        }
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mView == null) {
            return super.onTouchEvent(ev);
        } else {
            commOnTouchEvent(ev);
            return super.onTouchEvent(ev);
        }
    }

    public void commOnTouchEvent(MotionEvent ev) {
        if (animationFinish) {
            int action = ev.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    startY = ev.getY();
                    super.onTouchEvent(ev);
                    break;

                case MotionEvent.ACTION_UP:
                    startY = 0;
                    if (isNeedAnimation())
                        animation();
                    super.onTouchEvent(ev);
                    break;

                case MotionEvent.ACTION_MOVE:
                    float preY = startY != 0 ? startY : ev.getY();
                    float nowY = ev.getY();
                    int deltaY = (int) (preY - nowY);
                    startY = nowY;
                    if (isNeedMove()) {
                        if (mRect.isEmpty())
                            mRect.set(mView.getLeft(), mView.getTop(), mView.getRight(), mView
                                    .getBottom());
                        mView.layout(mView.getLeft(), mView.getTop() - deltaY / 2, mView.getRight
                                (), mView.getBottom() - deltaY / 2);
                    } else {
                        super.onTouchEvent(ev);
                    }
                    break;
            }
        }
    }

    public void animation() {
        TranslateAnimation ta = new TranslateAnimation(0, 0, 0, mRect.top - mView.getTop());
        ta.setDuration(200);
        ta.setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) {
                animationFinish = false;
            }

            public void onAnimationRepeat(Animation animation1) {
            }

            public void onAnimationEnd(Animation animation) {
                mView.clearAnimation();
                mView.layout(mRect.left, mRect.top, mRect.right, mRect.bottom);
                mRect.setEmpty();
                animationFinish = true;
            }

        });
        mView.startAnimation(ta);
    }

    public boolean isNeedAnimation() {
        return !mRect.isEmpty();
    }

    public boolean isNeedMove() {
        int offset = mView.getMeasuredHeight() - getHeight();
        int scrollY = getScrollY();
        return scrollY == 0 || scrollY == offset;
    }

}
