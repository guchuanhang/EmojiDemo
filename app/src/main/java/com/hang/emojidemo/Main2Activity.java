package com.hang.emojidemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.util.emotions.EmojiUtil;

public class Main2Activity extends AppCompatActivity {
    EmojiUtil emojiUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        setContentView(textView);
        emojiUtil = EmojiUtil.getInstance(this.getApplicationContext());
        String dataInString = getIntent().getStringExtra("data");
        if (!TextUtils.isEmpty(dataInString)) {
            textView.setText(dataInString);
            textView.append("\n");
            textView.append(emojiUtil.convert(this, dataInString));
        }

    }
}
