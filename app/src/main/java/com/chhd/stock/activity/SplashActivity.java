package com.chhd.stock.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.chhd.stock.R;
import com.chhd.stock.util.SharedPreferencesUtil;
import com.umeng.analytics.MobclickAgent;

import cn.jpush.android.api.JPushInterface;

public class SplashActivity extends AppCompatActivity {

    private RelativeLayout rl_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initViews();

        startAnimation();

        delayStartActivity();
    }

    private void initViews() {
        rl_splash = (RelativeLayout) findViewById(R.id.rl_splash);
    }

    private void startAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.set_splash_in);
        rl_splash.startAnimation(animation);
    }


    private void delayStartActivity() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                boolean firstStart = SharedPreferencesUtil.getGuidetInfo(SplashActivity.this);
                Intent intent = null;
                if (firstStart) {
                    intent = new Intent(SplashActivity.this, GuideActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
//                    overridePendingTransition(R.anim.set_act2act_enter, R.anim.set_act2act_exit);
                }
                finish();
            }
        }, 2000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
        MobclickAgent.onPause(this);
    }
}
