package com.example.lijinfeng.eses.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.lijinfeng.eses.R;
import com.example.lijinfeng.eses.util.CommonUtil;

/*
 *  TODO: AboutActivity
 *
 *  Date: 15-9-1 下午11:23
 *  Copyright (c) li.jf All rights reserved.
 */
public class AboutActivity extends AppCompatActivity {

    private static final String TAG = AboutActivity.class.getSimpleName();

    private Toolbar mToolbar; ///

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initTitleView();
    }

    private void initTitleView() {
        CommonUtil.configToolBarParams(this);

        mToolbar = (Toolbar) findViewById(R.id.tl_custom);
        mToolbar.setTitle(R.string.title_activity_about);
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        mToolbar.setBackgroundColor(getResources().getColor(R.color.blue));
        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
