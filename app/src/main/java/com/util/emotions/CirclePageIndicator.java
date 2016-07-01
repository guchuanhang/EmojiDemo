package com.util.emotions;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hang.emojidemo.R;

public class CirclePageIndicator extends LinearLayout implements
        OnPageChangeListener {

    public CirclePageIndicator(Context context) {
        super(context);
    }

    public CirclePageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private ViewPager mViewPager;
    private int mNormalCircle = R.drawable.circle_normal;
    private int mSelectCircle = R.drawable.circle_selected;
    private int mRealCount = 0;
    /**
     * 圆点间距
     */
    private int mCirclePadding = 16;

    public void setCirclePadding(int padding) {
        mCirclePadding = padding < 0 ? 0 : padding;
    }

    public void setCircleDrawables(int normal, int selected) {
        mNormalCircle = normal;
        mSelectCircle = selected;
    }

    public void setViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(this);
        initCircles();
    }

    private void initCircles() {
        addCircles();
        setSelected(0);
    }

    private void addCircles() {
        PagerAdapter adapter = null;
        if (mViewPager != null && (adapter = mViewPager.getAdapter()) != null) {
            setOrientation(HORIZONTAL);
            removeAllViews();
            mRealCount = adapter.getCount();
            for (int i = 0; i < mRealCount; i++) {
                ImageView iv = new ImageView(getContext());
                iv.setPadding(i == 0 ? 0 : mCirclePadding, 0, 0, 0);
                addView(iv);
            }
        }
    }

    public void setSelected(int position) {
        if (mRealCount != 0) {
            position = position % mRealCount;
            for (int i = 0; i < getChildCount(); i++) {
                ImageView child = (ImageView) getChildAt(i);
                child.setImageResource(position == i ? mSelectCircle
                        : mNormalCircle);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // ignore
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // ignore
    }

    @Override
    public void onPageSelected(int position) {
        setSelected(position);
    }
}
