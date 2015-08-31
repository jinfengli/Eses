package com.example.lijinfeng.eses.base;

import android.app.Activity;
import android.os.Bundle;

import com.example.lijinfeng.eses.R;
import com.umeng.analytics.MobclickAgent;

/**
 *
 */
public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
