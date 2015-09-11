package com.example.lijinfeng.eses.activity;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lijinfeng.eses.R;
import com.example.lijinfeng.eses.base.BaseActivity;
import com.example.lijinfeng.eses.bean.RecordBean;

public class RecordDetailActivity extends BaseActivity {

    private static final String TAG = RecordDetailActivity.class.getSimpleName();

    private ImageView ivBack;
    private TextView tvHeadTitle;
    private ImageView ivHeadRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);

        initTitleView();
        initView();
        setListener();
    }

    @Override
    protected void initTitleView() {
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvHeadTitle = (TextView) findViewById(R.id.tvHeaderTitle);
        tvHeadTitle.setText(R.string.title_activity_record_detail);
        ivHeadRight = (ImageView) findViewById(R.id.ivHeaderRight);
        ivHeadRight.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initView() {

    }

    private void setListener() {
        ivBack.setOnClickListener(this);
        ivHeadRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.ivBack) {
            RecordDetailActivity.this.finish();
        }

    }
}