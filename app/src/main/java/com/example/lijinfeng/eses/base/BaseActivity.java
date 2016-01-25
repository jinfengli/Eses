package com.example.lijinfeng.eses.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVUser;
import com.umeng.analytics.MobclickAgent;

/**
 * base 工具类
 *
 * @author li.jf
 */
public abstract class BaseActivity extends Activity implements View.OnClickListener {

    private Context context;
    private String userId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
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

    public void setUserId(String userId) {
        this.userId = userId;
    }

//    protected abstract void click(View view);
}
