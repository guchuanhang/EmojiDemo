package com.util.emotions;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class EmojiPanel extends FrameLayout {

    public EmojiPanel(Context context) {
        super(context);
        init(context);
    }

    public EmojiPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EmojiPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private ViewPager mViewPager;
    private CirclePageIndicator mPagerIndicator;
    private EmojiPagerAdapter mPagerAdapter;

    private void init(Context context) {
        int padding = (int) (context.getResources().getDisplayMetrics().density * 8);

        LinearLayout totalLayout = new LinearLayout(context);
        totalLayout.setOrientation(LinearLayout.VERTICAL);

        mViewPager = new ViewPager(context);
        totalLayout.addView(mViewPager);

        mPagerIndicator = new CirclePageIndicator(context);
        //TODO
        mPagerIndicator.setBackgroundColor(Color.parseColor("#00ff00"));
        mPagerIndicator.setGravity(Gravity.CENTER);
        mPagerIndicator.setPadding(padding, padding, padding, padding);
        totalLayout.addView(mPagerIndicator);


        LinearLayout.LayoutParams pageIndicatorParams = (LinearLayout.LayoutParams) mPagerIndicator.getLayoutParams();
        pageIndicatorParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
        pageIndicatorParams.width = LinearLayout.LayoutParams.MATCH_PARENT;


        addView(totalLayout);
        //TODO
        int keyboardHeight = 500;

        FrameLayout.LayoutParams totalParams = (FrameLayout.LayoutParams) totalLayout.getLayoutParams();
        //TODO set keyboard height;
        totalParams.height = keyboardHeight;
        totalParams.width = LayoutParams.MATCH_PARENT;

        int paddingTop = (keyboardHeight - calculateIconsHeight()) / 3;
        mViewPager.setPadding(0, paddingTop, 0, 0);
        LinearLayout.LayoutParams viewPagerParams = (LinearLayout.LayoutParams) mViewPager.getLayoutParams();
        viewPagerParams.height = paddingTop + calculateIconsHeight();
        viewPagerParams.width = LinearLayout.LayoutParams.MATCH_PARENT;


        mPagerAdapter = new EmojiPagerAdapter(getContext());
        mViewPager.setAdapter(mPagerAdapter);
        mPagerIndicator.setViewPager(mViewPager);
    }

    public int calculateIconsHeight() {

        int iconVerticalSpace = Util.dip2px(getContext(), 48) * 3 + 2 * Util.dip2px(getContext(), 8);
        return iconVerticalSpace;
    }

    public void resetToFirstPage() {
        if (mViewPager != null) {
            mViewPager.setCurrentItem(0, false);
        }
    }

    public void setOnEmotionClickListener(OnEmojiClickListener l) {
        mPagerAdapter.setOnEmotionClickListener(l);
    }
}
