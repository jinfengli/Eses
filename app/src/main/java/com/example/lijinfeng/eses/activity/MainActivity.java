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
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.lijinfeng.eses.view.MorePopupWindow;
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
public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private FloatingActionButton fabMenu;

    //声明相关变量
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private MainAdapter mainAdapter;
    private SwipeMenuListView lvRecords;

    private ArrayList<RecordBean> recordBeans;
    /** 数据库操作工具类 */
    private EsesDBHelper dbHelper;

    private MorePopupWindow morePopupWindow;
    private RelativeLayout rlHeader;

    private ContentObserver recordChangeObserver = new RecordChangeObserver(new Handler());
    private ContentResolver mContentResolver;

    private AlertView mAlertView;

    private TextView tvRegisterTime;
    private TextView tvUsername;
    private TextView tvSetting;
    private TextView tvFeedback;
    private TextView tvShare;

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
        CommonUtil.configToolBarParams(MainActivity.this);

        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        toolbar.setTitle("ES");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setBackgroundColor(getResources().getColor(R.color.statusbar_bg));
        setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(onMenuItemClicker);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);

//        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        tvUsername.setText(PreferenceUtils.getPrefString(this, ESConstants.USER_NAME, "")
                + " (" + PreferenceUtils.getPrefString(this, ESConstants.USER_EMAIL, "") + ")");
        tvRegisterTime.setText(String.format(getResources().getString(R.string.register_time),
                PreferenceUtils.getPrefString(this, ESConstants.USER_REGISTER_TIME, "")));

        tvSetting = (TextView) findViewById(R.id.tv_main_setting);
        tvFeedback = (TextView) findViewById(R.id.tv_main_feedback);
        tvShare = (TextView) findViewById(R.id.tv_main_share);

    }

    private void setListener() {
        fabMenu.setOnClickListener(this);
        lvRecords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, RecordDetailActivity.class);
                intent.putExtra(ESConstants.START_DATE_TIME, recordBeans.get(position).getStartDate()
                        + "  " + recordBeans.get(position).getStartTime());
                intent.putExtra(ESConstants.SLEEP_DATE_TIME, recordBeans.get(position).getSleepDate()
                        + "  " + recordBeans.get(position).getSleepTime());
                intent.putExtra(ESConstants.RECORD_COMMENT, recordBeans.get(position).getRecordComment());
                intent.putExtra(ESConstants.EXCEPTION_FLAG, recordBeans.get(position).getExceptionFlag());
                intent.putExtra(ESConstants.SLEEP_TIME_SECOND, recordBeans.get(position).getSleepTimeSecond());

                startActivity(intent);
            }
        });

        tvFeedback.setOnClickListener(this);
        tvShare.setOnClickListener(this);
        tvSetting.setOnClickListener(this);
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


        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                createMenu1(menu);
            }

            private void createMenu1(SwipeMenu menu) {
                SwipeMenuItem item1 = new SwipeMenuItem(getApplicationContext());
                item1.setBackground(new ColorDrawable(Color.rgb(0xE5, 0x18,
                        0x5E)));
                item1.setWidth(dp2px(90));
                item1.setIcon(R.drawable.ic_action_discard);
                menu.addMenuItem(item1);

                SwipeMenuItem item2 = new SwipeMenuItem(getApplicationContext());
                item2.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                item2.setWidth(dp2px(90));
                item2.setTitleSize(16);
                item2.setTitleColor(Color.RED);
                item2.setTitle("修改");
                menu.addMenuItem(item2);
            }
        };
        // set creator
        lvRecords.setMenuCreator(creator);

        // step 2. listener item click event
        lvRecords.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                final RecordBean item = recordBeans.get(position);
                switch (index) {
                    case 0:
                        // delete
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                        builder.setIcon(R.drawable.ic_launcher);
                        builder.setTitle("提示");
                        builder.setMessage("确定删除吗？");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbHelper.deleteRecord(item.getRecordNo());
                                recordBeans.remove(position);
//                                mainAdapter.notifyDataSetChanged();
                                mainAdapter.setRecordDatas(recordBeans);
                                lvRecords.setAdapter(mainAdapter);
                            }
                        });
                        //    设置一个NegativeButton
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                Toast.makeText(MainActivity.this, "negative: " + which, Toast.LENGTH_SHORT).show();
                            }
                        });

                        builder.show();

                        break;
                    case 1:
                        // update
                        Intent intent = new Intent(MainActivity.this,EditRecordActivity.class);
                        intent.putExtra(ESConstants.RECORD_NO, recordBeans.get(position).getRecordNo());
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });

        mAlertView = new AlertView("提示", "退出后需重新登陆，确定退出？", "否",
                new String[]{"是"},
                null,
                this,
                AlertView.Style.Alert, this).setCancelable(true);
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
        // 记录添加完成之后会执行MainActivity的onResume().
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
    public void onItemClick(Object o, int position) {
        // 点击取消为-1，其他的从0开始算起
        if(position == 0) {
            AVUser.logOut();
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            MainActivity.this.finish();
        } else if(position == -1) {
            mAlertView.dismiss();
        }

    }

    @Override
    public void onBackPressed() {
        if(mAlertView.isShowing()) {
            mAlertView.dismiss();
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

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
