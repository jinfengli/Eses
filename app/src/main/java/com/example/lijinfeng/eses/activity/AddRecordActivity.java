package com.example.lijinfeng.eses.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lijinfeng.eses.R;
import com.example.lijinfeng.eses.base.BaseActivity;
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
public class AddRecordActivity extends BaseActivity
        implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{

    private static final String TAG = AddRecordActivity.class.getSimpleName();

    private ImageView ivHeaderBack;
    private TextView tvHeaderTitle;
    private ImageView ivHeaderRight;

    private Button btnShowDatePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        initTitleView();
        initView();
        setListener();
    }

    @Override
    protected void initTitleView() {
        ivHeaderBack = (ImageView) findViewById(R.id.ivBack);
        tvHeaderTitle = (TextView) findViewById(R.id.tvHeaderTitle);
        tvHeaderTitle.setText(R.string.add_record);
        ivHeaderRight = (ImageView) findViewById(R.id.ivHeaderRight);
        ivHeaderRight.setVisibility(View.INVISIBLE);


    }

    @Override
    protected void initView() {
        btnShowDatePicker = (Button) findViewById(R.id.btnShowDatePicker);
    }

    private void setListener() {
        ivHeaderBack.setOnClickListener(this);
        btnShowDatePicker.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ivBack) {
            AddRecordActivity.this.finish();
        } else if(v.getId() == R.id.btnShowDatePicker) {
            setTime(v);
        }
    }

    public void setTime(View v){
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        tpd.setThemeDark(false);
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    // On clicking Date picker
    public void setDate(View v){
        Calendar currentDate = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String time = "您选择的日期是：" +year +monthOfYear + dayOfMonth;
        Toast.makeText(this,time,Toast.LENGTH_LONG).show();

    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hourOfDay, int minute) {
        String time = "您选择的时间是：" +hourOfDay +":" + minute;
        Toast.makeText(this,time,Toast.LENGTH_LONG).show();
    }
}
