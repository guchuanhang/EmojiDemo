package com.hang.emojidemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.util.emotions.EmojiPanel;

import cn.dreamtobe.kpswitch.util.KPSwitchConflictUtil;
import cn.dreamtobe.kpswitch.util.KeyboardUtil;
import cn.dreamtobe.kpswitch.widget.KPSwitchPanelLinearLayout;

public class MainActivity extends AppCompatActivity implements View.OnFocusChangeListener {
    final String TAG = MainActivity.class.getSimpleName();
    //    private IMEBar mIMEBar;
    private EditText mSendEdt;
    private LinearLayout mContentRyv;
    private KPSwitchPanelLinearLayout mPanelRoot;
    private ImageView mPlusIv;
    private EmojiPanel mEmojiPanel;
    private EditText mSecondEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mIMEBar = (IMEBar) findViewById(R.id.ime_bar);
        mSendEdt = (EditText) findViewById(R.id.et_input);
        mPanelRoot = (KPSwitchPanelLinearLayout) findViewById(R.id.panel_root);
        mPanelRoot = (KPSwitchPanelLinearLayout) findViewById(R.id.panel_root);
        mContentRyv = (LinearLayout) findViewById(R.id.root);
        mPlusIv = (ImageView) findViewById(R.id.iv_switch);
        mEmojiPanel = (EmojiPanel) findViewById(R.id.emotion_pannel);
        mEmojiPanel.setEditText(mSendEdt);
        mSecondEditText = (EditText) findViewById(R.id.top_input);
        mSecondEditText.setOnFocusChangeListener(this);
        mSendEdt.setOnFocusChangeListener(this);
        // bind target
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, SecondActivity.class);

                intent.putExtra("data", mSendEdt.getText().toString().trim());
                startActivity(intent);
            }
        });

        //1.获取键盘高度，adjustSize
        KeyboardUtil.attach(this, mPanelRoot,
                // Add keyboard showing state callback, do like this when you want to listen in the
                // keyboard's show/hide change.
                new KeyboardUtil.OnKeyboardShowingListener() {
                    @Override
                    public void onKeyboardShowing(boolean isShowing) {

                        if (isShowing) {
                            mPlusIv.setImageResource(R.drawable.icon_icon);
                        } else {
                            mPlusIv.setImageResource(R.drawable.keyboard);
                        }


                        Log.d(TAG, String.format("Keyboard is %s", isShowing ? "showing" : "hiding"));
                    }
                });
        //2.表情&&键盘切换时，gone表情部分，隐藏键盘部分
        //
        KPSwitchConflictUtil.attach(mPanelRoot, mPlusIv, mSendEdt,
                null);

        mContentRyv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    KPSwitchConflictUtil.hidePanelAndKeyboard(mPanelRoot);
                }

                return true;
            }
        });

    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (b) {
            mEmojiPanel.setEditText((EditText) view);
        }
    }

    //onBackPressed 无法捕获，收回键盘的event
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        mPlusIv.setImageResource(R.drawable.icon_icon);

        if (event.getAction() == KeyEvent.ACTION_UP &&
                event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (mPanelRoot.getVisibility() == View.VISIBLE) {
                KPSwitchConflictUtil.hidePanelAndKeyboard(mPanelRoot);
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
