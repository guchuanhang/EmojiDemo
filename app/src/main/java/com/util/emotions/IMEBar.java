package com.util.emotions;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hang.emojidemo.R;

public class IMEBar extends LinearLayout implements OnClickListener,
        OnEmojiClickListener {
    public IMEBar(Context context) {
        super(context);
        init();
    }


    public IMEBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IMEBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {
    /*在构造方法中，IMEBar实例化尚未完成，下面的操作，必须延迟执行；new Thread? No
    only the original thread that created a view hierarchy can touch its views
    这个和只有UI线程才能修改UI，是一样的
    通过下面的方法还是比较好的
    */
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                getEditText(IMEBar.this);
                if (mEditText == null) {
                    throw new RuntimeException(
                            "must contain a EditText  for putting content in");
                }
                mEditText.setOnClickListener(IMEBar.this);
                setup();
            }
        }, 100);
    }

    /*通过递归的方法，获取EditText
    */
    public void getEditText(ViewGroup viewGroup) {

        for (int j = 0; j < viewGroup.getChildCount(); j++) {
            View childView = viewGroup.getChildAt(j);
            if (childView instanceof ViewGroup) {
                getEditText((ViewGroup) childView);
            } else {
                if (childView instanceof EditText) {
                    mEditText = (EditText) childView;
                }
            }
        }
    }


    private InputMethodManager mInputMethodManager;
    private EmojiUtil mEmojiUtil;
    private EditText mEditText;
    /**
     * 放置弹出内容的区域，需要一个id为fl_content的FrameLayout
     */
    private FrameLayout mFrameContent;
    /**
     * 表情栏
     */
    private EmojiPanel mEmotionsPanel;
    private ImageView mSwitchView;

    public void setup() throws RuntimeException {
        /**
         * 1.表情管理工具<br/>
         * 2.表情容器<br/>
         * 3.表情开关按钮<br/>
         */

        // IME manager
        mInputMethodManager = (InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // 表情管理
        mEmojiUtil = EmojiUtil.getInstance(getContext().getApplicationContext());

        // 放置弹出的容器
        mFrameContent = (FrameLayout) findViewById(R.id.fl_content);
        if (mFrameContent == null) {
            throw new RuntimeException(
                    "must specify a FrameLayout with id \"fl_content\" for putting content in");
        }
        //进行keyboard&&icon 切换
        mSwitchView = (ImageView) findViewById(R.id.iv_switch);
        if (mSwitchView == null) {
            throw new RuntimeException(
                    "must specify a View with id \"btn_emojis\"");
        }
        // bind listener
        mSwitchView.setOnClickListener(this);

        // 表情众面板
        mEmotionsPanel = new EmojiPanel(getContext());
        mEmotionsPanel.setOnEmotionClickListener(this);
        // 面板加入布局中，并默认不可见
        mEmotionsPanel.setVisibility(View.GONE);
        mFrameContent.addView(mEmotionsPanel);
    }


    public void onShowEmotionsPanel() {
        mEmotionsPanel.resetToFirstPage();
        mEmotionsPanel.setVisibility(View.VISIBLE);
    }


    public boolean isEmotionsPanelShown() {
        return mEmotionsPanel != null
                && mEmotionsPanel.getVisibility() == View.VISIBLE;
    }


    public void hideEmotionsPanel() {

        mEmotionsPanel.setVisibility(View.GONE);
    }

    private void showKeyboard() {
        if (mInputMethodManager != null && mEditText != null) {
            mInputMethodManager.showSoftInput(mEditText,
                    InputMethodManager.SHOW_IMPLICIT);
        }
    }


    private void hideSoftKeyInput() {
        if (mInputMethodManager != null && mEditText != null) {
            mInputMethodManager.hideSoftInputFromWindow(
                    mEditText.getApplicationWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_switch: {
                if (isEmotionsPanelShown()) {
                    hideEmotionsPanel();
                    showKeyboard();
                    mSwitchView.setImageResource(R.drawable.icon_icon);
                } else {
                    hideSoftKeyInput();
                    onShowEmotionsPanel();
                    mSwitchView.setImageResource(R.drawable.keyboard);
                }

                break;
            }

            case R.id.et_input: {
                // 点击输入框->隐藏表情
                hideEmotionsPanel();
                break;
            }

            default:
                break;
        }
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
}
