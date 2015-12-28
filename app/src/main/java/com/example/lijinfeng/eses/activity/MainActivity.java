package com.example.lijinfeng.eses.activity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.example.lijinfeng.eses.R;
import com.example.lijinfeng.eses.adapter.MainAdapter;
import com.example.lijinfeng.eses.bean.RecordBean;
import com.example.lijinfeng.eses.constants.ESConstants;
import com.example.lijinfeng.eses.db.EsesDBHelper;
import com.example.lijinfeng.eses.db.RecordProvider;
import com.example.lijinfeng.eses.util.CommonUtil;
import com.example.lijinfeng.eses.util.PreferenceUtils;
import com.example.lijinfeng.eses.view.swipeMenuListView.SwipeMenu;
import com.example.lijinfeng.eses.view.swipeMenuListView.SwipeMenuCreator;
import com.example.lijinfeng.eses.view.swipeMenuListView.SwipeMenuItem;
import com.example.lijinfeng.eses.view.swipeMenuListView.SwipeMenuListView;
import com.github.clans.fab.FloatingActionButton;
import com.umeng.update.UmengUpdateAgent;

import java.util.ArrayList;

/*
 *  TODO: MainActivity
 *
 *  @date: 15-8-23 下午5:53
 *  @author li.jf
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private FloatingActionButton fabMenu;
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private MainAdapter mainAdapter;
    private SwipeMenuListView lvRecords;

    private ArrayList<RecordBean> recordBeans;
    /** 数据库操作工具类 */
    private EsesDBHelper dbHelper;

    private ContentObserver recordChangeObserver = new RecordChangeObserver(new Handler());
    private ContentResolver mContentResolver;

    private AlertView mAlertView;
    private TextView tvRegisterTime;
    private TextView tvUsername;
    private TextView tvSetting;
    private TextView tvFeedback;
    private TextView tvShare;

    /** 删除记录 */
    private static final int DELETE_ITEM = 0;
    /** 修改记录 */
    private static final int MODIFY_ITEM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTitleView();
        initView();
        init();
        initAlertView();
        setListener();
    }

    private void initTitleView() {
        CommonUtil.configToolBarParams(MainActivity.this);

        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setBackgroundColor(getResources().getColor(R.color.blue));
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(onMenuItemClicker);

//        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
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
    }

    private void initView() {
        fabMenu = (FloatingActionButton) findViewById(R.id.fab);
        lvRecords = (SwipeMenuListView) findViewById(R.id.lvESTime);
        lvRecords.setDivider(new ColorDrawable(Color.GRAY));
        lvRecords.setDividerHeight(1);

        tvUsername = (TextView) findViewById(R.id.tv_username);
        tvRegisterTime = (TextView) findViewById(R.id.tv_register_time);
        tvUsername.setText(PreferenceUtils.getPrefString(this, ESConstants.USER_NAME, "") + " ("
                + PreferenceUtils.getPrefString(this, ESConstants.USER_EMAIL, "") + ")");
        tvRegisterTime.setText(String.format(getResources().getString(R.string.register_time),
            PreferenceUtils.getPrefString(this, ESConstants.USER_REGISTER_TIME, "")));

        tvSetting = (TextView) findViewById(R.id.tv_main_setting);
        tvFeedback = (TextView) findViewById(R.id.tv_main_feedback);
        tvShare = (TextView) findViewById(R.id.tv_main_share);
    }

    private void setListener() {
        fabMenu.setOnClickListener(this);
        lvRecords.setOnItemClickListener(this);
        tvFeedback.setOnClickListener(this);
        tvShare.setOnClickListener(this);
        tvSetting.setOnClickListener(this);
        mContentResolver.registerContentObserver(RecordProvider.CONTENT_URI, true, recordChangeObserver);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(MainActivity.this, RecordDetailActivity.class);
        RecordBean recordBean = recordBeans.get(position);
        intent.putExtra(ESConstants.START_DATE_TIME,
                recordBean.getStartDate() + "  " + recordBean.getStartTime());
        intent.putExtra(ESConstants.SLEEP_DATE_TIME,
                recordBean.getSleepDate() + "  " + recordBean.getSleepTime());
        intent.putExtra(ESConstants.RECORD_COMMENT, recordBean.getRecordComment());
        intent.putExtra(ESConstants.EXCEPTION_FLAG, recordBean.getExceptionFlag());
        intent.putExtra(ESConstants.SLEEP_TIME_SECOND, recordBean.getSleepTimeSecond());
        startActivity(intent);
    }

    private void init() {
        mContentResolver = getContentResolver();
        mContentResolver.registerContentObserver(RecordProvider.CONTENT_URI, true,
            recordChangeObserver);

        UmengUpdateAgent.update(this);
        mainAdapter = new MainAdapter(MainActivity.this);
        dbHelper = new EsesDBHelper(this);
        recordBeans = new ArrayList<RecordBean>();
        recordBeans = dbHelper.queryAllRecords();

        mainAdapter.setRecordDatas(recordBeans);
        lvRecords.setAdapter(mainAdapter);

        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                createMenu1(menu);
            }

            private void createMenu1(SwipeMenu menu) {
                SwipeMenuItem item1 = new SwipeMenuItem(getApplicationContext());
                item1.setBackground(new ColorDrawable(Color.rgb(0xE5, 0x18, 0x5E)));
                item1.setWidth(CommonUtil.dp2px(MainActivity.this, 90));
                item1.setIcon(R.drawable.ic_action_discard);
                menu.addMenuItem(item1);

                SwipeMenuItem item2 = new SwipeMenuItem(getApplicationContext());
                item2.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                item2.setWidth(CommonUtil.dp2px(MainActivity.this, 90));
                item2.setTitleSize(16);
                item2.setTitleColor(Color.RED);
                item2.setTitle(R.string.modify);
                menu.addMenuItem(item2);
            }
        };
        // set creator
        lvRecords.setMenuCreator(creator);

        // step 2. listener item click event
        lvRecords.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case DELETE_ITEM:
                        deleteRecordItem(recordBeans, position);
                        //mAlertView.show();
                        break;
                    case MODIFY_ITEM:
                        // modify
                        Intent intent = new Intent(MainActivity.this, EditRecordActivity.class);
                        intent.putExtra(ESConstants.RECORD_NO, recordBeans.get(position).getRecordNo());
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 初始化AlertView
     */
    private void initAlertView() {
        mAlertView = new AlertView(getResources().getString(R.string.tips),
            getResources().getString(R.string.confirm_to_quit), getResources().getString(R.string.no),
            new String[]{getResources().getString(R.string.yes)},
            null, this, AlertView.Style.Alert, listener).setCancelable(true);
    }

    private OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void onItemClick(Object o, int i) {
            if(i == -1) {
                mAlertView.dismiss();
            } else if(i == 0) {
//                ToastUtil.showToastL(MainActivity.this, o.getClass() + "");
                AVUser.logOut();
                MainActivity.this.finish();

                // 不能这样搞，这样getClass都是com...AlertView。
                //if(o.getClass() == MenuItem.class) {
                //    AVUser.logOut();
                //} else if(o.getClass() == RecordBean.class) {
                //
                //}
            }
        }
    };

    /**
     * 删除指定位置的记录
     * @param recordBeans
     * @param position
     */
    private void deleteRecordItem(final ArrayList<RecordBean> recordBeans, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.tips);
        builder.setMessage(R.string.sure_to_delete);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                dbHelper.deleteRecord(recordBeans.get(position).getRecordNo());
                recordBeans.remove(position);
                // mainAdapter.notifyDataSetChanged();
                mainAdapter.setRecordDatas(recordBeans);
                lvRecords.setAdapter(mainAdapter);
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                startActivity(new Intent(MainActivity.this, AddRecordActivity.class));
                break;
            case R.id.tv_main_setting:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
            case R.id.tv_main_feedback:
                CommonUtil.feedbackTOMe(MainActivity.this);
                break;
            case R.id.tv_main_share:
                CommonUtil.shareText(MainActivity.this);
                break;

            default:
                break;
        }
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

    @Override
    public void onBackPressed() {
        if(mAlertView.isShowing()) {
            mAlertView.dismiss();
            return;
        }
        // 侧滑处于开启状态时，点击返回键，先关闭侧滑(不是直接和Activity一起关闭)
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
            return;
        }

        super.onBackPressed();
    }


    private class RecordChangeObserver extends ContentObserver {
        public RecordChangeObserver(Handler handler){
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);

            Log.d(TAG, "database changed: " + recordBeans.size());
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
        return super.onOptionsItemSelected(item);
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClicker = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_chart:
                    startActivity(new Intent(MainActivity.this, ChartActivity.class));
                    break;
                case R.id.action_about:
                    startActivity(new Intent(MainActivity.this, AboutActivity.class));
                    break;
                case R.id.action_exit:
                    mAlertView.show();
                    break;
            }
            return false;
        }
    };

}
