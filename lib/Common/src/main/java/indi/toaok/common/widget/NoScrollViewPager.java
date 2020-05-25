package indi.toaok.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * 禁止滑动的ViewPager
 *
 * @author Toaok
 * @version 1.0  2018/9/27.
 */
public class NoScrollViewPager extends ViewPager {

    private boolean isScrollEnabled = false;

    public NoScrollViewPager(@NonNull Context context) {
        super(context);
    }

    public NoScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isScrollEnabled && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isScrollEnabled && super.onTouchEvent(ev);
    }
}