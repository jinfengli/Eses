package com.example.lijinfeng.eses.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.lijinfeng.eses.R;
import com.example.lijinfeng.eses.adapter.MainAdapter;
import com.example.lijinfeng.eses.bean.RecordBean;
import com.example.lijinfeng.eses.db.EsesDBHelper;
import com.github.clans.fab.FloatingActionButton;
import com.github.mikephil.charting.charts.Chart;
import com.umeng.update.UmengUpdateAgent;

import java.util.ArrayList;
import java.util.List;

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

//    private RecyclerView recyclerViewRecords;
    private MainAdapter mainAdapter;

    private ListView lvRecords;

    private ArrayList<RecordBean> recordBeans;

    private EsesDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTitleView();
        initView();
        init();

        setListener();
    }

    private void initTitleView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("ES");
    }

    protected void initView() {
        fabMenu = (FloatingActionButton) findViewById(R.id.fab);
        lvRecords = (ListView) findViewById(R.id.lvESTime);
        lvRecords.setDivider(new ColorDrawable(Color.GRAY));
        lvRecords.setDividerHeight(1);
    }

    private void setListener() {
        fabMenu.setOnClickListener(this);
    }

    private void init() {
        // 默认只在wifi情况下才更新
//        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(this);

        mainAdapter = new MainAdapter(MainActivity.this);
        dbHelper = new EsesDBHelper(this);
        recordBeans = new ArrayList<RecordBean>();
        recordBeans = dbHelper.queryAllRecords();

        mainAdapter.setRecordDatas(recordBeans);
        lvRecords.setAdapter(mainAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(Menu.NONE, Menu.FIRST + 1, 5, "图表").setIcon(R.drawable.ic_launcher);

//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.action_settings:
//                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
//                return true;
//
//            case R.id.action_about:
//                startActivity(new Intent(MainActivity.this, AboutActivity.class));
//                return true;
            case Menu.FIRST +1:
                startActivity(new Intent(MainActivity.this, ChartActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fab:
                startActivity(new Intent(MainActivity.this, AddRecordActivity.class));
                break;

            default:
                break;
        }
    }
}
