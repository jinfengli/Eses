package com.example.lijinfeng.eses.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.lijinfeng.eses.R;
import com.github.clans.fab.FloatingActionButton;
import com.umeng.update.UmengUpdateAgent;

/*
 *  TODO: MainActivity
 *
 *  Date: 15-8-23 下午5:53
 *  Copyright (c) li.jf All rights reserved.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private FloatingActionButton fabMenu;
    private Toolbar mToolbar;

//    private MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setListener();
        init();
    }

    protected void initView() {
        fabMenu = (FloatingActionButton) findViewById(R.id.fab);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        mToolbar.setTitle("ESES");
    }

    private void setListener() {
        fabMenu.setOnClickListener(this);
    }

    private void init() {
//        UmengUpdateAgent.setUpdateOnlyWifi(false);
        // 默认只在wifi情况下才更新
        UmengUpdateAgent.update(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;

            case R.id.action_about:
                startActivity(new Intent(MainActivity.this,AboutActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fab:
                startActivity(new Intent(MainActivity.this, AddRecordActivity.class));
                break;

        }

    }
}
