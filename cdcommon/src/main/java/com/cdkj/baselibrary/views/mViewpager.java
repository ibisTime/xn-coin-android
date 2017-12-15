package com.cdkj.baselibrary.views;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**重写Viewpager禁止左右滑动

 */
public class mViewpager extends ViewPager {

    private boolean isPagingEnabled = true;

    public mViewpager(Context context) {
        super(context);
    }

    public mViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
    }

    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }
    @Override
    public int getCurrentItem() {
        return PagerAdapter.POSITION_NONE;
    }
}
