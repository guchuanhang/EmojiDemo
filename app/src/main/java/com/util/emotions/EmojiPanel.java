package com.util.emotions;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class EmojiPanel extends FrameLayout implements OnEmojiClickListener {

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
    private EditText mEditText;
    private EmojiUtil mEmojiUtil;

    public void setEditText(EditText editText) {
        this.mEditText = editText;
    }

    private void init(Context context) {
        int padding = (int) (context.getResources().getDisplayMetrics().density * 8);

        LinearLayout totalLayout = new LinearLayout(context);
        totalLayout.setOrientation(LinearLayout.VERTICAL);

        mViewPager = new ViewPager(context);
        totalLayout.addView(mViewPager);

        mPagerIndicator = new CirclePageIndicator(context);
//        mPagerIndicator.setBackgroundColor(Color.parseColor("#00ff00"));
        mPagerIndicator.setGravity(Gravity.CENTER);
        mPagerIndicator.setPadding(padding, padding, padding, padding);
        totalLayout.addView(mPagerIndicator);


        LinearLayout.LayoutParams pageIndicatorParams = (LinearLayout.LayoutParams) mPagerIndicator.getLayoutParams();
        pageIndicatorParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
        pageIndicatorParams.width = LinearLayout.LayoutParams.MATCH_PARENT;


        addView(totalLayout);
        FrameLayout.LayoutParams totalParams = (FrameLayout.LayoutParams) totalLayout.getLayoutParams();
        totalParams.height = LayoutParams.MATCH_PARENT;
        totalParams.width = LayoutParams.MATCH_PARENT;
        int paddingTop = Util.dip2px(getContext(), 24);
        mViewPager.setPadding(0, paddingTop, 0, 0);
        LinearLayout.LayoutParams viewPagerParams = (LinearLayout.LayoutParams) mViewPager.getLayoutParams();
        viewPagerParams.height = paddingTop + calculateIconsHeight();
        viewPagerParams.width = LinearLayout.LayoutParams.MATCH_PARENT;


        mPagerAdapter = new EmojiPagerAdapter(getContext());
        mViewPager.setAdapter(mPagerAdapter);
        mPagerIndicator.setViewPager(mViewPager);
        mEmojiUtil = EmojiUtil.getInstance(getContext().getApplicationContext());
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPagerAdapter.setOnEmotionClickListener(EmojiPanel.this);
            }
        }, 100);
    }

    @Override
    public void onEmotionClick(final String emojiFile) {
        if (mEditText == null) {
            return;
        }

        if (EmojiUtil.BACKSPACE.equals(emojiFile)) {
            Editable editable = mEditText.getText();
            // 删除光标所在的前一个字符或表情
            int index = mEditText.getSelectionStart();
            if (index <= 0) {
                // 光标在最前部，不需要删除
                return;
            }
            char c = editable.charAt(index - 1);
            if (']' == c) {
                String text = editable.toString();
                // 排除"[开心]A]"这种情况
                int nextOpenBracket = text.lastIndexOf('[', index - 2);
                int nextCloseBracket = text.lastIndexOf(']', index - 2);
                if (nextCloseBracket < nextOpenBracket) {
                    // 删除一对儿
                    editable.delete(nextOpenBracket, index);
                    return;
                }
            }
            // 正常删除
            editable.delete(index - 1, index);
        } else {
            /*
             * 点击了一个表情->在光标处插入表情
			 */
            // 将SpannableString插入到光标处
            int index = mEditText.getSelectionStart();



            mEditText.getText().insert(
                    index,
                    mEmojiUtil
                            .getSpannableByEmojiName(getContext(), emojiFile));
        }
    }

    public int calculateIconsHeight() {

        int iconVerticalSpace = Util.dip2px(getContext(), 56) * 3;
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
