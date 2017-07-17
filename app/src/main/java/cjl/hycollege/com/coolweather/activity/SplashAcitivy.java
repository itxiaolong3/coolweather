package cjl.hycollege.com.coolweather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import cjl.hycollege.com.coolweather.R;

public class SplashAcitivy extends AppCompatActivity {
    private boolean isFirst = false;
    private static final int TIME = 1000;
    private static final int INTTOHOME = 100;
    private static final int INTOGUIDE = 101;
    private Handler mhanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case INTOGUIDE:
                    intoGuide();
                    break;
                case INTTOHOME:
                    intoHome();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_splash_acitivy);
        init();
    }

    private void init() {
        SharedPreferences perfs = getSharedPreferences("xiaozhun", MODE_PRIVATE);
        isFirst = perfs.getBoolean("isFirstIn", true);
        if (!isFirst) {
            mhanlder.sendEmptyMessageDelayed(INTTOHOME, TIME);
        }else{
            mhanlder.sendEmptyMessageDelayed(INTOGUIDE, TIME);

        }

    }

    //进入引导页
    private void intoGuide() {
        Intent i = new Intent(SplashAcitivy.this,Guide.class);
        startActivity(i);
        finish();
    }

    //进入主界面
    private void intoHome() {
        Intent i = new Intent(SplashAcitivy.this,MainActivity.class);
        startActivity(i);
        finish();
    }

}
