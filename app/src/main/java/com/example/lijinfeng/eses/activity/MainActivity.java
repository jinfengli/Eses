package com.example.lijinfeng.eses.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.example.lijinfeng.eses.R;
import com.example.lijinfeng.eses.adapter.MainAdapter;
import com.example.lijinfeng.eses.bean.RecordBean;
import com.example.lijinfeng.eses.colorful.Colorful;
import com.example.lijinfeng.eses.colorful.setter.ViewGroupSetter;
import com.example.lijinfeng.eses.db.EsesDBHelper;
import com.example.lijinfeng.eses.db.RecordProvider;
import com.example.lijinfeng.eses.util.CommonUtil;
import com.example.lijinfeng.eses.util.ToastUtil;
import com.example.lijinfeng.eses.view.MorePopupWindow;
import com.github.clans.fab.FloatingActionButton;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.update.UmengUpdateAgent;

import java.util.ArrayList;

/*
 *  TODO: MainActivity
 *
 *  Date: 15-8-23 下午5:53
 *  Copyright (c) li.jf All ri reserved.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private FloatingActionButton fabMenu;

    //声明相关变量
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView lvLeftMenu;
    private String[] lvs = {"List Item 01", "List Item 02", "List Item 03", "List Item 04"};
    private ArrayAdapter arrayAdapter;

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

    Colorful mColorful;
    boolean isNight = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//       不要再设置Title了
//       requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initTitleView();
        initView();
        init();

        setListener();
    }

    protected void initTitleView() {
        CommonUtil.configToolBarParams(MainActivity.this);

        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        toolbar.setTitle("Toolbar");//设置Toolbar标题
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white)); //设置标题颜色
        toolbar.setBackgroundColor(getResources().getColor(R.color.statusbar_bg));
        setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(onMenuItemClicker);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
        lvLeftMenu = (ListView) findViewById(R.id.lv_left_menu);

        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                mAnimationDrawable.stop();
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
//                mAnimationDrawable.start();
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        //设置菜单列表
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lvs);
        lvLeftMenu.setAdapter(arrayAdapter);

    }

    private Toolbar.OnMenuItemClickListener onMenuItemClicker = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_settings:
                    startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                    break;

                case R.id.action_about:
                    startActivity(new Intent(MainActivity.this, AboutActivity.class));
                    break;
                case R.id.action_exit:
                    AVUser.logOut();
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                    MainActivity.this.finish();
                    break;
            }
            return false;
        }
    };

    protected void initView() {
        fabMenu = (FloatingActionButton) findViewById(R.id.fab);
        lvRecords = (ListView) findViewById(R.id.lvESTime);
        lvRecords.setDivider(new ColorDrawable(Color.GRAY));
        lvRecords.setDividerHeight(1);
    }

    private void setListener() {
        fabMenu.setOnClickListener(this);
        lvRecords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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

        setupColorful();

        mainAdapter.setRecordDatas(recordBeans);
        lvRecords.setAdapter(mainAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                startActivity(new Intent(MainActivity.this, AddRecordActivity.class));
                break;

//            case R.id.ivHeaderRight:
//                setPopwindowPosition();
//                break;
            default:
                break;
        }
    }

    private void changeThemeWithColorful() {
        if (!isNight) {
            mColorful.setTheme(R.style.NightTheme);
        } else {
            mColorful.setTheme(R.style.DayTheme);
        }
        isNight = !isNight;
    }

    private void setupColorful() {
        ViewGroupSetter listViewSetter = new ViewGroupSetter(lvRecords);
        // 绑定ListView的Item View中的news_title视图，在换肤时修改它的text_color属性
        listViewSetter.childViewTextColor(R.id.tv_item_start_time, R.attr.text_color);

        // 构建Colorful对象来绑定View与属性的对象关系
        mColorful = new Colorful.Builder(this)
                .backgroundDrawable(R.id.fl_parent, R.attr.root_view_bg)
                        // 设置view的背景图片
//                .backgroundColor(R.id.change_btn, R.attr.btn_bg)
                        // 设置背景色
//                .textColor(R.id.textview, R.attr.text_color)
                .setter(listViewSetter) // 手动设置setter
                .create(); // 设置文本颜色
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

//        if(id == android.R.id.se) {
//            onBackPressed();
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }
}
