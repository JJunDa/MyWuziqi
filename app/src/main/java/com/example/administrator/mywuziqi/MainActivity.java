package com.example.administrator.mywuziqi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mButton;
    private Button mReButton;
    private WuziqiView mWuziqiView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();



    }

    private void init() {
        mButton = (Button) findViewById(R.id.btn);
        mReButton = (Button) findViewById(R.id.btn_re);
        mWuziqiView = (WuziqiView) findViewById(R.id.wuziqi);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWuziqiView.reStart();
            }
        });

        mReButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWuziqiView.BackOneStep();
            }
        });
    }

}
