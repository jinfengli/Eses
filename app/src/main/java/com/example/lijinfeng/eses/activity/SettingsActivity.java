package com.example.lijinfeng.eses.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lijinfeng.eses.R;
import com.example.lijinfeng.eses.base.BaseActivity;

/*
 *  TODO:SettingsActivity
 *
 *  Date: 15-9-2 上午6:58
 *  Copyright (C) li.jf All rights reserved.
 */

public class SettingsActivity extends BaseActivity {
    private ImageView ivBack;
    private TextView tvHeadTitle;
    private ImageView ivHeadRight;

    private TextView tvSetTheme;
    private TextView tvFeedBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initTitleView();
        initView();
        setListener();
    }

    @Override
    protected void initTitleView() {
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvHeadTitle = (TextView) findViewById(R.id.tvHeaderTitle);
        tvHeadTitle.setText(R.string.title_activity_settings);
        ivHeadRight = (ImageView) findViewById(R.id.ivHeaderRight);
        ivHeadRight.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initView() {
        tvSetTheme = (TextView) findViewById(R.id.tv_set_theme);
        tvFeedBack = (TextView) findViewById(R.id.tv_feedback);
        // 设置页面，
        // 1、添加修改主题
        // 2、umeng检查更新
        // 3、记录备份 --> 最好是导出到excel文件中.
        // 4、反馈
    }

    private void setListener() {
        ivBack.setOnClickListener(this);
        ivHeadRight.setOnClickListener(this);
        tvSetTheme.setOnClickListener(this);
        tvFeedBack.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivBack) {
            onBackPressed();
        } else if(view.getId() == R.id.tv_feedback) {

            feedbackTOMe();


        }

    }

    private void feedbackTOMe() {
        Intent data=new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailto:lijinfeng_ljf@foxmail.com"));
        //  Build.MODEL: H60-L01    Device: hwH60  release 4.4.2
        data.putExtra(Intent.EXTRA_SUBJECT, "来自 "
                +  Build.DEVICE
                + "("
                + Build.VERSION.RELEASE
                + ") 的反馈");
        data.putExtra(Intent.EXTRA_TEXT, "输入反馈内容...");
        startActivity(data);
    }
}
