package com.example.lijinfeng.eses.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.PushService;
import com.example.lijinfeng.eses.R;
import com.example.lijinfeng.eses.base.BaseActivity;
import com.example.lijinfeng.eses.util.CommonUtil;

/*
 * TODO: Splash page
 *
 * @author li.jf
 */
public class SplashActivity extends BaseActivity {

    private ImageView ivSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CommonUtil.settingWindowNoTitle(this);
        setContentView(R.layout.activity_splash);

        initAVCloud();
        initView();
        init();
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
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void gotoMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}
