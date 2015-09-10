package com.example.lijinfeng.eses.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.OnItemClickListener;
import com.example.lijinfeng.eses.R;
import com.example.lijinfeng.eses.bean.RecordBean;
import com.example.lijinfeng.eses.db.EsesDBHelper;
import com.example.lijinfeng.eses.util.CommonAlertDialog;
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
public class AddRecordActivity extends Activity implements OnClickListener,
        CommonAlertDialog.OnSubmitListener,OnItemClickListener,SegmentControl.OnSegmentControlClickListener{

    private static final String TAG = AddRecordActivity.class.getSimpleName();
//    private Toolbar mToolbar;

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

    private ImageView ivBack;
    private TextView tvHeadTitle;
    private ImageView ivHeadRight;

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

    protected void initTitleView() {
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvHeadTitle = (TextView) findViewById(R.id.tvHeaderTitle);
        tvHeadTitle.setText("添加记录");
        ivHeadRight = (ImageView) findViewById(R.id.ivHeaderRight);
        ivHeadRight.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_ok));
    }

    protected void initView() {
        tvStartDatePicker = (TextView) findViewById(R.id.tvStartDatePicker);
        tvStartTimePicker = (TextView) findViewById(R.id.tvStartTimePicker);
        tvEndDatePicker = (TextView) findViewById(R.id.tvEndDatePicker);
        tvEndTimePicker = (TextView) findViewById(R.id.tvEndTimePicker);
        etCommnet = (EditText) findViewById(R.id.et_comment);
        segmentControl = (SegmentControl) findViewById(R.id.segment_control);
    }

    private void init() {
        dbHelper = new EsesDBHelper(this);
        mCommonAlertDialog = new CommonAlertDialog(this);
        recordBean = new RecordBean();
    }

    private void setListener() {
        ivBack.setOnClickListener(this);
        ivHeadRight.setOnClickListener(this);

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
        recordBean.setStartDate(startDate);
        recordBean.setStartTime(startTime);
        recordBean.setSleepDate(endDate);
        recordBean.setSleepTime(endTime);
        recordBean.setSleepTimeSecond("tomorrow_second");
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
        if(!TextUtils.isEmpty(etCommnet.getText().toString())) {
            recordBean.setRecordComment(etCommnet.getText().toString());
        }
        // 以当前时间的毫秒数来作为RecordNo的唯一标识
        recordBean.setRecordNo(System.currentTimeMillis() + "");

        mCommonAlertDialog.show();
        mCommonAlertDialog.setTitleVisibityGone();
        mCommonAlertDialog.setMeaage("确定添加该条记录？");
        mCommonAlertDialog.setComfirmText(R.string.ok);
        mCommonAlertDialog.setCancelText(R.string.cancel);

//        new AlertView("上传头像", null, "取消", null,
//                new String[]{"拍照", "从相册中选择"},
//                this, AlertView.Style.Alert, new OnItemClickListener(){
//            public void onItemClick(Object o,int position){
//                if(position == 0) {
//                    dbHelper.addRecord(recordBean);
//                    AddRecordActivity.this.finish();
//                }
//            }
//        }).show();

//        new AlertView(null, null, null,
//                new String[]{"高亮按钮1", "高亮按钮2"},
//                new String[]{"其他按钮1", "其他按钮2"},
//                AddRecordActivity.this, AlertView.Style.Alert, this).show();
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
            //睡眠
            recordBean.setRecordType("type_sleep");
        } else if(index == 1) {
            // 健身
            recordBean.setRecordType("type_exercise");
        }
    }
}
