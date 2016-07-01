package com.hang.emojidemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.widget.TextView;

import com.util.emotions.EmojiUtil;

public class SecondActivity extends AppCompatActivity {
    EmojiUtil emojiUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        emojiUtil = EmojiUtil.getInstance(getApplicationContext());
        String dataString = getIntent().getStringExtra("data");
        TextView textView = new TextView(this);
        textView.setText("接收到的表情文本信息为：\n");
        textView.append(dataString);
        textView.append("\n");
        textView.append("文本信息转化为表情为：\n");

        SpannableStringBuilder stringBuilder = emojiUtil.convert(getApplicationContext(), dataString);
        if (stringBuilder != null) {
            textView.append(stringBuilder);
        }
        setContentView(textView);

    }
}
