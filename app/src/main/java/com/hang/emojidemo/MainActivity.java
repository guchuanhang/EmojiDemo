package com.hang.emojidemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.util.emotions.IMEBar;

public class MainActivity extends AppCompatActivity {
    private IMEBar mIMEBar;
    private EditText mInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIMEBar = (IMEBar) findViewById(R.id.ime_bar);
        mInput = (EditText) findViewById(R.id.et_input);
        // bind target
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, SecondActivity.class);

                intent.putExtra("data", mInput.getText().toString().trim());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mIMEBar.isEmotionsPanelShown()) {
            mIMEBar.hideEmotionsPanel();
        } else {

            super.onBackPressed();
        }
    }
}
