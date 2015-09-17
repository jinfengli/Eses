package com.example.lijinfeng.eses.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.example.lijinfeng.eses.R;
import com.example.lijinfeng.eses.base.BaseActivity;


/*
 * TODO: Splash page
 * @author li.jf
 * Copyright (C) 15-9-17 下午3:49 wonhigh.cn All rights reserved.
 */

public class SplashActivity extends BaseActivity {

    private ImageView ivSplash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setttingNoTitle();
        setContentView(R.layout.activity_splash);

        initTitleView();
        initView();
        init();
    }

    private void setttingNoTitle() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void initTitleView() {

    }

    @Override
    protected void initView() {
        ivSplash = (ImageView) findViewById(R.id.iv_splash);
    }

    private void init() {
        final ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scaleAnim.setFillAfter(true);
        scaleAnim.setDuration(2000);
        scaleAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ivSplash.startAnimation(scaleAnim);
    }

    @Override
    public void onClick(View view) {

    }

    private void startActivity() {
        SplashActivity.this.finish();
        startActivity(new Intent(this, MainActivity.class));
    }

}
