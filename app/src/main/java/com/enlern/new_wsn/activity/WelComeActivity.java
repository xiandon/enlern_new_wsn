package com.enlern.new_wsn.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.enlern.new_wsn.R;
import com.enlern.new_wsn.application.CustomActivityManager;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by oc on 2017/6/22.
 */

public class WelComeActivity extends BaseActivity {
    private Context context;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        CustomActivityManager.addActivity(WelComeActivity.this);
        context = WelComeActivity.this;
        ButterKnife.bind(this);
        welcome();


    }

    private void welcome() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(WelComeActivity.this, MainActivity.class);
                            startActivity(intent);
                            WelComeActivity.this.fileList();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @OnClick({R.id.button_phone, R.id.button_server})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_phone:
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.button_server:
                Intent intent1 = new Intent(context, WsnServerActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
