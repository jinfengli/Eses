package com.example.lijinfeng.eses.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVUser;
import com.umeng.analytics.MobclickAgent;

/**
 * base 工具类
 *
 * @author li.jf
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private String userId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }

    private void init() {
        context = BaseActivity.this;

        AVUser user = AVUser.getCurrentUser();
        if(user != null) {
            userId = user.getObjectId();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onResume(this);
        AVAnalytics.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        AVAnalytics.onPause(this);
    }

    protected abstract void initView();

    public String getUserId() {
        return userId;
    }

    /**
     * Jump target Activity
     * @param clz target activity
     * @param isFinish  If true, finish current activity
     */
    protected void gotoActivity(Class clz, boolean isFinish) {
        startActivity(new Intent(this, clz));
        if (isFinish) {
            this.finish();
        } else {
            // do not finish current activity
        }
    }
}
