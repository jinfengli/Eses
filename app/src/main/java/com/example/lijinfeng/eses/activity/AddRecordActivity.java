package com.example.lijinfeng.eses.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.OnItemClickListener;
import com.example.lijinfeng.eses.R;
import com.example.lijinfeng.eses.application.ESConstants;
import com.example.lijinfeng.eses.base.BaseActivity;
import com.example.lijinfeng.eses.bean.RecordBean;
import com.example.lijinfeng.eses.db.EsesDBHelper;
import com.example.lijinfeng.eses.util.CommonAlertDialog;
import com.example.lijinfeng.eses.util.ToastUtil;
import com.example.lijinfeng.eses.view.CustomLayout;
import com.example.lijinfeng.eses.view.SegmentControl;
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
public class AddRecordActivity extends BaseActivity implements
        CommonAlertDialog.OnSubmitListener,
        OnItemClickListener,
        SegmentControl.OnSegmentControlClickListener,
        CustomLayout.KeyboardStateListener{

    private static final String TAG = AddRecordActivity.class.getSimpleName();

    private TextView tvStartDatePicker;
    private TextView tvStartTimePicker;
    private TextView tvEndDatePicker;
    private TextView tvEndTimePicker;

    private EditText etCommnet;
    private EsesDBHelper dbHelper;

    private String startDate ="";
    private String startTime = "";
    private String endDate = "";
    private String endTime = "";

    private ImageView ivBack;
    private TextView tvHeadTitle;
    private ImageView ivHeadRight;

    private CustomLayout resizeLayout;
    private LinearLayout llStartTime;

    RecordBean recordBean;
    Calendar currentDate = Calendar.getInstance();

    SegmentControl segmentControl;
    private CommonAlertDialog mCommonAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_record);

        initTitleView();
        initView();
        init();
        setListener();
    }

    @Override
    protected void initTitleView() {
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvHeadTitle = (TextView) findViewById(R.id.tvHeaderTitle);
        tvHeadTitle.setText(R.string.add_record);
        ivHeadRight = (ImageView) findViewById(R.id.ivHeaderRight);
        ivHeadRight.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_ok));
    }

    @Override
    protected void initView() {
        tvStartDatePicker = (TextView) findViewById(R.id.tvStartDatePicker);
        tvStartTimePicker = (TextView) findViewById(R.id.tvStartTimePicker);
        tvEndDatePicker = (TextView) findViewById(R.id.tvEndDatePicker);
        tvEndTimePicker = (TextView) findViewById(R.id.tvEndTimePicker);
        etCommnet = (EditText) findViewById(R.id.et_comment);
        segmentControl = (SegmentControl) findViewById(R.id.segment_control);

        resizeLayout = (CustomLayout) findViewById(R.id.custom_root_layout);
        llStartTime = (LinearLayout) findViewById(R.id.ll_start_date_time);
    }

    private void init() {
        dbHelper = new EsesDBHelper(this);
        mCommonAlertDialog = new CommonAlertDialog(this);
        recordBean = new RecordBean();
    }

    private void setListener() {
        ivBack.setOnClickListener(this);
        ivHeadRight.setOnClickListener(this);

        resizeLayout.setKeyboardStateListener(this);
        segmentControl.setmOnSegmentControlClickListener(this);
        mCommonAlertDialog.setOnSubmitListener(this);

        tvStartDatePicker.setOnClickListener(this);
        tvStartTimePicker.setOnClickListener(this);
        tvEndDatePicker.setOnClickListener(this);
        tvEndTimePicker.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void saveRecordToDb() {
        // 以当前时间的毫秒数来作为RecordNo的唯一标识
        recordBean.setRecordNo(System.currentTimeMillis() + "");

        startDate = TextUtils.isEmpty(startDate)? "" : startDate;
        recordBean.setStartDate(startDate);
        startTime = TextUtils.isEmpty(startTime)? "" : startTime;
        recordBean.setStartTime(startTime);
        endDate = TextUtils.isEmpty(endDate)? "" : endDate;
        recordBean.setSleepDate(endDate);
        endTime = TextUtils.isEmpty(endTime)? "" : endTime;
        recordBean.setSleepTime(endTime);

        recordBean.setSleepTimeSecond("tomorrow_second");

        if( !TextUtils.isEmpty(startDate) && !TextUtils.isEmpty(endDate)) {
            if(!startDate.equals(endDate)) {
                recordBean.setExceptionFlag("1"); //起床睡眠日期不是同一天.
            } else {
                // 同一天
                if( !TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)) {
                    if(Integer.parseInt(startTime.split("\\:")[0])> Integer.parseInt(endTime.split("\\:")[0])
                            || Integer.parseInt(startTime.split("\\:")[0]) == Integer.parseInt(endTime.split("\\:")[0])
                            && Integer.parseInt(startTime.split("\\:")[1]) >= Integer.parseInt(endTime.split("\\:")[1])) {
                        ToastUtil.toastShort(AddRecordActivity.this, "开始时间和结束时间有误");
                        return;
                    } else {
                        recordBean.setExceptionFlag("0");
                    }
                } else {
                    ToastUtil.toastShort(AddRecordActivity.this, "开始和结束时间不能为空");
                    return;
                }
            }
        } else {
            ToastUtil.toastShort(AddRecordActivity.this, "开始和结束日期不能为空");
            return;
        }

        String commentMsg = etCommnet.getText().toString();
        if(!TextUtils.isEmpty(commentMsg)) {
            recordBean.setRecordComment(commentMsg);
        } else {
            ToastUtil.toastLong(AddRecordActivity.this, R.string.comment_should_not_null);
            return;
        }

        mCommonAlertDialog.show();
        mCommonAlertDialog.setTitleVisibityGone();
        mCommonAlertDialog.setMeaage(getResources().getString(R.string.confirm_add_this_record));
        mCommonAlertDialog.setComfirmText(R.string.ok);
        mCommonAlertDialog.setCancelText(R.string.cancel);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                AddRecordActivity.this.finish();
                break;
            case R.id.ivHeaderRight:
                saveRecordToDb();
                break;
            case R.id.tvStartDatePicker:
                setStartDate();
                break;
            case R.id.tvStartTimePicker:
                setStartTime();
                break;
            case R.id.tvEndDatePicker:
                setSleepDate();
                break;
            case R.id.tvEndTimePicker:
                setSleepTime();
                break;

            default:
                break;
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
                currentDate.get(Calendar.MONTH) + 1,
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
                true
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
                currentDate.get(Calendar.MONTH) + 1,
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
                true
        );
        tpd.setThemeDark(false);
        tpd.show(getFragmentManager(), "Timepickerdialog");
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

    @Override
    public void onItemClick(Object o, int i) {
        Toast.makeText(this, "点击了第" + i + "个", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSegmentControlClick(int index) {
        if (index == 0) {
            recordBean.setRecordType(ESConstants.TYPE_SLEEP);
        } else if(index == 1) {
            recordBean.setRecordType(ESConstants.TYPE_EXERCISE);
        }
    }

    @Override
    public void setChanged(int state) {
        switch (state) {
            case CustomLayout.KEYBOARD_HIDE:
                llStartTime.setVisibility(View.VISIBLE);
                break;

            case CustomLayout.KEYBOARD_SHOW:
                llStartTime.setVisibility(View.GONE);
                break;
        }
    }
}
