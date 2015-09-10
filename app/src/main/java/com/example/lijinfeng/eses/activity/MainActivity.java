package com.example.lijinfeng.eses.activity;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lijinfeng.eses.R;
import com.example.lijinfeng.eses.adapter.MainAdapter;
import com.example.lijinfeng.eses.base.BaseActivity;
import com.example.lijinfeng.eses.bean.RecordBean;
import com.example.lijinfeng.eses.db.EsesDBHelper;
import com.example.lijinfeng.eses.db.RecordProvider;
import com.example.lijinfeng.eses.view.MorePopupWindow;
import com.github.clans.fab.FloatingActionButton;
import com.umeng.update.UmengUpdateAgent;

import java.util.ArrayList;
import java.util.logging.Logger;

/*
 *  TODO: MainActivity
 *
 *  Date: 15-8-23 下午5:53
 *  Copyright (c) li.jf All ri reserved.
 */
public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private FloatingActionButton fabMenu;

    private MainAdapter mainAdapter;
    private ListView lvRecords;

    private ArrayList<RecordBean> recordBeans;
    /** 数据库操作工具类 */
    private EsesDBHelper dbHelper;

    private ImageView ivBack;
    private TextView tvHeadTitle;
    private ImageView ivHeadRight;

    private MorePopupWindow morePopupWindow;

    private RelativeLayout rlHeader;

    private ContentObserver recordChangeObserver = new RecordChangeObserver(new Handler());
    private ContentResolver mContentResolver;

//    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initTitleView();
        initView();
        init();

        setListener();
    }

    @Override
    protected void initTitleView() {
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setVisibility(View.GONE);
        tvHeadTitle = (TextView) findViewById(R.id.tvHeaderTitle);
        ivHeadRight = (ImageView) findViewById(R.id.ivHeaderRight);
    }

    @Override
    protected void initView() {
        fabMenu = (FloatingActionButton) findViewById(R.id.fab);
        lvRecords = (ListView) findViewById(R.id.lvESTime);
        lvRecords.setDivider(new ColorDrawable(Color.GRAY));
        lvRecords.setDividerHeight(1);
        rlHeader = (RelativeLayout) findViewById(R.id.rl_header);
    }

    private void setListener() {
        ivBack.setOnClickListener(this);
        ivHeadRight.setOnClickListener(this);
        fabMenu.setOnClickListener(this);

        lvRecords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, " 您点击的是" + position, Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this, RecordDetailActivity.class));
            }
        });

//        mContentResolver.registerContentObserver(RecordProvider.CONTENT_URI, true, recordChangeObserver);
    }

    private void init() {
        mContentResolver = getContentResolver();
        mContentResolver.registerContentObserver(RecordProvider.CONTENT_URI, true, recordChangeObserver);

        UmengUpdateAgent.update(this);
        morePopupWindow = new MorePopupWindow(MainActivity.this);
        mainAdapter = new MainAdapter(MainActivity.this);
        dbHelper = new EsesDBHelper(this);
        recordBeans = new ArrayList<RecordBean>();
        recordBeans = dbHelper.queryAllRecords();

        mainAdapter.setRecordDatas(recordBeans);
        lvRecords.setAdapter(mainAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                startActivity(new Intent(MainActivity.this, AddRecordActivity.class));
                break;

            case R.id.ivHeaderRight:
                setPopwindowPosition();
                break;

            default:
                break;
        }
    }

    /**
     * 设置popwindow在屏幕上显示deep位置
     */
    private void setPopwindowPosition() {
        if (morePopupWindow.isShowing()) {
            return;
        }
        // morePopupWindow.setWidth((int) (window_w * 0.4));
        // 是宽度大小根据屏幕density进行设定
        morePopupWindow.setWidth((int) (380));
        int[] location = new int[2];
        rlHeader.getLocationOnScreen(location);
        int y = location[1] + rlHeader.getHeight() -2;
        morePopupWindow.showAtLocation(MainActivity.this.findViewById(R.id.ivHeaderRight),
                Gravity.TOP | Gravity.RIGHT, 20, y);
    }

    @Override
    protected void onResume() {
        super.onResume();

        recordBeans = new ArrayList<RecordBean>();
        recordBeans = dbHelper.queryAllRecords();

        mainAdapter.setRecordDatas(recordBeans);
        lvRecords.setAdapter(mainAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContentResolver.unregisterContentObserver(recordChangeObserver);
    }

    private class RecordChangeObserver extends ContentObserver {

        public RecordChangeObserver(Handler handler){
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            int size = recordBeans.size();

            Log.i(TAG,"数据库改变"+ size);

            recordBeans = new EsesDBHelper(MainActivity.this).queryAllRecords();
            mainAdapter.setRecordDatas(recordBeans);
            lvRecords.setAdapter(mainAdapter);
        }
    }
}
