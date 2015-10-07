package com.example.lijinfeng.eses.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.PushService;
import com.example.lijinfeng.eses.R;
import com.example.lijinfeng.eses.base.BaseActivity;


/*
 * TODO: Splash page
 * @author li.jf
 * @date 15-9-17 下午3:49
 * Copyright(C) li.jf  All rights reserved.
 */
public class SplashActivity extends BaseActivity {

    private ImageView ivSplash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setttingNoTitle();
        setContentView(R.layout.activity_splash);

        initAVCloud();
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

    private void initAVCloud() {
        AVAnalytics.trackAppOpened(getIntent());
        PushService.setDefaultPushCallback(this, SplashActivity.class);
        PushService.subscribe(this, "public", SplashActivity.class);
        AVInstallation.getCurrentInstallation().saveInBackground();
    }

    protected void initView() {
        ivSplash = (ImageView) findViewById(R.id.iv_splash);
    }

    private void init() {
    /*    final ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
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
                if(getUserId() != null) {
                    gotoMainActivity();
                } else {
                    gotoLoginActivity();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ivSplash.startAnimation(scaleAnim);*/

        if(getUserId() != null) {
            gotoMainActivity();
        } else {
            gotoLoginActivity();
        }
    }

    @Override
    public void onClick(View view) {

    }

    private void gotoLoginActivity() {
        SplashActivity.this.finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void gotoMainActivity() {
        if(getUserId() != null) {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            this.finish();
        }
    }

}
