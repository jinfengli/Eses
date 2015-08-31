package com.example.lijinfeng.eses.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.lijinfeng.eses.R;
import com.example.lijinfeng.eses.base.BaseActivity;
import com.github.clans.fab.FloatingActionButton;

/*
 *  TODO: MainActivity
 *
 *  Date: 15-8-23 下午5:53
 *  Copyright (c) li.jf All rights reserved.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private FloatingActionButton fabMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setListener();
    }

    protected void initView() {
        fabMenu = (FloatingActionButton) findViewById(R.id.fab);
    }

    private void setListener() {
        fabMenu.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            startActivity(new Intent(this, AboutActivity.class));
        }
    }
}
