package com.example.lijinfeng.eses.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lijinfeng.eses.R;
import com.example.lijinfeng.eses.bean.RecordBean;
import com.example.lijinfeng.eses.db.EsesDBHelper;
import com.example.lijinfeng.eses.util.CommonAlertDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

/*
 *  TODO: 添加记录
 *
 *  Date: 15-8-29 下午11:24
 *  Copyright (c) li.jf All rights reserved.
 */
public class AddRecordActivity extends AppCompatActivity implements OnClickListener,
        CommonAlertDialog.OnSubmitListener{

    private static final String TAG = AddRecordActivity.class.getSimpleName();
    private Toolbar mToolbar;

    private TextView tvStartDatePicker;
    private TextView tvStartTimePicker;
    private TextView tvEndDatePicker;
    private TextView tvEndTimePicker;

    private EditText etCommnet;
    private EsesDBHelper dbHelper;

    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;

    RecordBean recordBean;
    Calendar currentDate = Calendar.getInstance();

    private CommonAlertDialog mCommonAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        init();
        initTitleView();
        initView();
        setListener();
    }

    private void init() {
        dbHelper = new EsesDBHelper(this);
        mCommonAlertDialog = new CommonAlertDialog(this);
        mCommonAlertDialog.setOnSubmitListener(this);
    }

    protected void initTitleView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.title_activity_add_record);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    protected void initView() {
        tvStartDatePicker = (TextView) findViewById(R.id.tvStartDatePicker);
        tvStartTimePicker = (TextView) findViewById(R.id.tvStartTimePicker);
        tvEndDatePicker = (TextView) findViewById(R.id.tvEndDatePicker);
        tvEndTimePicker = (TextView) findViewById(R.id.tvEndTimePicker);
        etCommnet = (EditText) findViewById(R.id.et_comment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_record, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if(item.getItemId() == R.id.add_record) {
            saveToDb();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveToDb() {
        recordBean = new RecordBean();
        recordBean.setStartDate(startDate);
        recordBean.setStartTime(startTime);
        recordBean.setSleepDate(endDate);
        recordBean.setSleepTime(endTime);
        if(!startDate.equals(endDate)) {
            recordBean.setExceptionFlag("1");
        } else {
            // -->同一天
            // 开始时间大于结束时间
            if(Integer.parseInt(startTime.split("\\:")[0])> Integer.parseInt(endTime.split("\\:")[0])) {
                recordBean.setExceptionFlag("2"); //  错误的时间
            } else {
                // 同一天  start < sleep  正常
                recordBean.setExceptionFlag("0");
            }
        }
        recordBean.setRecordComment(etCommnet.getText().toString());
        recordBean.setRecordNo("111");

        mCommonAlertDialog.show();
        mCommonAlertDialog.setTitleVisibityGone();
        mCommonAlertDialog.setMeaage("确定添加该条记录？");
        mCommonAlertDialog.setComfirmText(R.string.ok);
        mCommonAlertDialog.setCancelText(R.string.cancel);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tvStartDatePicker) {
            setStartDate();
        } else if(v.getId() == R.id.tvStartTimePicker) {
            setStartTime();
        } else if (v.getId() == R.id.tvEndDatePicker) {
            setSleepDate();
        } else if(v.getId() == R.id.tvEndTimePicker) {
            setSleepTime();
        }
    }
    private void setStartDate() {
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {
                        tvStartDatePicker.setText(year + "/" + monthOfYear + "/" + dayOfMonth);
                        startDate = tvStartDatePicker.getText().toString();
                    }
                },
                currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    private void setStartTime() {
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hour, int minuteOfhour) {
                        tvStartTimePicker.setText(hour + ":" + minuteOfhour);
                        startTime = tvStartTimePicker.getText().toString();
                    }
                },
                currentDate.get(Calendar.HOUR_OF_DAY),
                currentDate.get(Calendar.MINUTE),
                false
        );
        tpd.setThemeDark(false);
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }
    private void setSleepDate() {
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {
                        tvEndDatePicker.setText(year + "/" + monthOfYear + "/" + dayOfMonth);
                        endDate = tvEndDatePicker.getText().toString();
                    }
                },
                currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");

    }

    private void setSleepTime() {
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hour, int minuteOfhour) {
                        tvEndTimePicker.setText(hour + ":" + minuteOfhour);
                        endTime = tvEndTimePicker.getText().toString();
                    }
                },
                currentDate.get(Calendar.HOUR_OF_DAY),
                currentDate.get(Calendar.MINUTE),
                false
        );
        tpd.setThemeDark(false);
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    private void setListener() {
        tvStartDatePicker.setOnClickListener(this);
        tvStartTimePicker.setOnClickListener(this);
        tvEndDatePicker.setOnClickListener(this);
        tvEndTimePicker.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void ok() {
        dbHelper.addRecord(recordBean);
        AddRecordActivity.this.finish();
    }
}
