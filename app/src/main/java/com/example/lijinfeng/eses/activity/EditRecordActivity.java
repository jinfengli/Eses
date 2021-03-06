package com.example.lijinfeng.eses.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.example.lijinfeng.eses.R;
import com.example.lijinfeng.eses.bean.RecordBean;
import com.example.lijinfeng.eses.constants.ESConstants;
import com.example.lijinfeng.eses.db.EsesDBHelper;
import com.example.lijinfeng.eses.util.CommonAlertDialog;
import com.example.lijinfeng.eses.util.CommonUtil;
import com.example.lijinfeng.eses.util.ToastUtil;
import com.example.lijinfeng.eses.view.CustomLayout;
import com.example.lijinfeng.eses.view.SegmentControl;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

/*
 *  TODO: 编辑记录
 *
 *  Date: 15-10-4 下午10:45
 *  Copyright (c) li.jf All rights reserved.
 */
public class EditRecordActivity extends AppCompatActivity implements
        CommonAlertDialog.OnSubmitListener,
        OnItemClickListener,
        SegmentControl.OnSegmentControlClickListener,
        CustomLayout.KeyboardStateListener,
        View.OnClickListener{

    private static final String TAG = AddRecordActivity.class.getSimpleName();

    private ScrollView scrollView;

    private TextView tvStartDatePicker;
    private TextView tvStartTimePicker;
    private TextView tvEndDatePicker;
    private TextView tvEndTimePicker;

    private EditText etCommnet;
    private TextView tvCommentNum;
    private EsesDBHelper dbHelper;

    private String recordNo = "";
    private String startDate ="";
    private String startTime = "";
    private String endDate = "";
    private String endTime = "";
    private String secondSleepTime = "";
    private String recordType = "";
    private String comment = "";

    private CustomLayout resizeLayout;
    private LinearLayout llStartTime;

    RecordBean recordBean;
    Calendar currentDate = Calendar.getInstance();

    SegmentControl segmentControl;
    private AlertView mAlertView;

    /** 备注中输入字符的最大数量 */
    private static final int MAX_COUNT = 50;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);


        initTitleView();
        initView();
        handleIntent();
        init();
        setListener();
    }

    private void handleIntent() {
        // 接收传过来的参数，默认显示
        recordNo = getIntent().getStringExtra(ESConstants.RECORD_NO);
    }

    protected void initTitleView() {
        CommonUtil.configToolBarParams(this);
        mToolbar = (Toolbar) findViewById(R.id.tl_custom);
        mToolbar.setTitle(R.string.edit_record);
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        mToolbar.setBackgroundColor(getResources().getColor(R.color.blue));
        setSupportActionBar(mToolbar);
        mToolbar.setOnMenuItemClickListener(onMenuItemClicker);
    }

    protected void initView() {
        tvStartDatePicker = (TextView) findViewById(R.id.tvStartDatePicker);
        tvStartTimePicker = (TextView) findViewById(R.id.tvStartTimePicker);
        tvEndDatePicker = (TextView) findViewById(R.id.tvEndDatePicker);
        tvEndTimePicker = (TextView) findViewById(R.id.tvEndTimePicker);
        etCommnet = (EditText) findViewById(R.id.et_comment);
        tvCommentNum = (TextView) findViewById(R.id.tv_comment_num);
        etCommnet.addTextChangedListener(mTextWatcher);
        etCommnet.setSelection(etCommnet.length());

        setLeftCount();

        segmentControl = (SegmentControl) findViewById(R.id.segment_control);
        resizeLayout = (CustomLayout) findViewById(R.id.custom_root_layout);
        llStartTime = (LinearLayout) findViewById(R.id.ll_start_date_time);
        llStartTime.setVisibility(View.GONE);

        scrollView = (ScrollView) findViewById(R.id.scrollView);
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClicker = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.update_record:
                  CommonUtil.hideSoftKeyBoard(EditRecordActivity.this);
                    updateRecordToDb();
//                    uploadRecord();
                    break;
            }
            return false;
        }
    };

    private void init() {
        dbHelper = new EsesDBHelper(this);
//        recordBean = new RecordBean();
        recordBean = dbHelper.queryRecordByRecordNo(recordNo);
        startDate = recordBean.getStartDate();
        startTime = recordBean.getStartTime();
        endDate = recordBean.getSleepDate();
        endTime = recordBean.getSleepTime();
        comment = recordBean.getRecordComment();
        tvStartDatePicker.setText(startDate);
        tvStartTimePicker.setText(startTime);
        tvEndDatePicker.setText(endDate);
        tvEndTimePicker.setText(endTime);
        etCommnet.setText(comment);
        etCommnet.setSelection(comment.length());

        mAlertView = new AlertView("修改记录", "确定修改该记录？", "否", new String[]{"是"},
                null,
                this,
                AlertView.Style.Alert, this).setCancelable(true);
    }

    private void setListener() {
        resizeLayout.setKeyboardStateListener(this);
        segmentControl.setmOnSegmentControlClickListener(this);
        tvStartDatePicker.setOnClickListener(this);
        tvStartTimePicker.setOnClickListener(this);
        tvEndDatePicker.setOnClickListener(this);
        tvEndTimePicker.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_edit_record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void updateRecordToDb() {
        // 以当前时间的毫秒数来作为RecordNo的唯一标识
//        recordBean.setRecordNo(System.currentTimeMillis() + "");

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
                secondSleepTime = endTime;   // 只记录时间，日期就是开始日期的第二天
            } else {
                // 同一天
                if( !TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)) {
                    if(Integer.parseInt(startTime.split("\\:")[0])> Integer.parseInt(endTime.split("\\:")[0])
                            || Integer.parseInt(startTime.split("\\:")[0]) == Integer.parseInt(endTime.split("\\:")[0])
                            && Integer.parseInt(startTime.split("\\:")[1]) >= Integer.parseInt(endTime.split("\\:")[1])) {
                        ToastUtil.showToastS(EditRecordActivity.this, "开始时间和结束时间有误");
                        return;
                    } else {
                        recordBean.setExceptionFlag("0");
                    }
                } else {
                    ToastUtil.showToastS(EditRecordActivity.this, "开始和结束时间不能为空");
                    return;
                }
            }
        } else {
            ToastUtil.showToastS(EditRecordActivity.this, "开始和结束日期不能为空");
            return;
        }

        String commentMsg = etCommnet.getText().toString();
        if(!TextUtils.isEmpty(commentMsg)) {
            recordBean.setRecordComment(commentMsg);
        } else {
            ToastUtil.showToastL(EditRecordActivity.this, R.string.comment_should_not_null);
            return;
        }

        recordBean.setRecordType(ESConstants.TYPE_SLEEP);
        mAlertView.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

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

 /*   private void uploadRecord() {
        String userName = PreferenceUtils.getPrefString(EditRecordActivity.this, ESConstants.USER_NAME,"");
        String userEmail = PreferenceUtils.getPrefString(EditRecordActivity.this, ESConstants.USER_EMAIL,"");

        AVObject record = new AVObject("Record");
        record.put("userName", userName);
        record.put("userEmail",userEmail);
        record.put("startDate", startDate);
        record.put("startTime", startTime);
        record.put("sleepDate", endDate);
        record.put("sleepTime", endTime);
        if(!TextUtils.isEmpty(secondSleepTime)) {
            record.put("sleepSecondTime",secondSleepTime);
            record.put("exceptionFlag", true); // 这个记录有异常
        } else {
            record.put("sleepSecondTime","");
            record.put("exceptionFlag",false);
        }
        record.put("recordComment", etCommnet.getText().toString());
        record.put("recordType", recordType);

        record.saveInBackground();
    }*/
    private void setStartDate() {
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {
                        setDateFormat(tvStartDatePicker, year, monthOfYear, dayOfMonth);
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
                        setTimeFormat(tvStartTimePicker, hour, minuteOfhour);
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
                        setDateFormat(tvEndDatePicker, year, monthOfYear, dayOfMonth);
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
                        setTimeFormat(tvEndTimePicker, hour, minuteOfhour);
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


    private void setDateFormat(TextView tvDate, int year, int monthOfYear, int dayOfMonth) {
        if(monthOfYear < 10 || dayOfMonth < 10) {
            if(monthOfYear < 10 && dayOfMonth < 10) {
                tvDate.setText(year + "/0" + monthOfYear + "/0" + dayOfMonth);
            } else if(monthOfYear < 10 && dayOfMonth >= 10) {
                tvDate.setText(year + "/0" + monthOfYear + "/" + dayOfMonth);
            } else {
                // month >= 10 && day < 10
                tvDate.setText(year + "/" + monthOfYear + "/0" + dayOfMonth);
            }

        } else {
            tvDate.setText(year + "/" + monthOfYear + "/" + dayOfMonth);
        }
    }

    private void setTimeFormat(TextView tvTime, int hour, int minuteOfhour) {
        if(hour < 10 || minuteOfhour < 10) {
            if(hour < 10 && minuteOfhour < 10) {
                tvTime.setText("0" + hour + ":0" + minuteOfhour);
            } else if(hour < 10 && minuteOfhour >= 10) {
                tvTime.setText("0" + hour + ":" + minuteOfhour);
            } else if(hour >= 10 && minuteOfhour < 10) {
                tvTime.setText(hour + ":0" + minuteOfhour);
            }
        } else {
            tvTime.setText(hour + ":" + minuteOfhour);
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void ok() {
//        dbHelper.addRecord(recordBean);
//        dbHelper.updateRecord(recordBean);
//        EditRecordActivity.this.finish();
    }

    @Override
    public void onItemClick(Object o, int position) {
        // 点击取消时为-1，其他的从0开始算起
        if(position == 0) {
//            dbHelper.updateRecord(recordBean);

//            uploadRecord();
            dbHelper.updateRecord(recordBean);
            EditRecordActivity.this.finish();
        } else {
            mAlertView.dismiss();
        }
    }

    @Override
    public void onSegmentControlClick(int index) {
        if (index == 0) {
            recordBean.setRecordType(ESConstants.TYPE_SLEEP);
            recordType = ESConstants.TYPE_SLEEP;
        } else if(index == 1) {
            recordBean.setRecordType(ESConstants.TYPE_EXERCISE);
            recordType = ESConstants.TYPE_EXERCISE;
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

    private TextWatcher mTextWatcher = new TextWatcher() {
        private int editStart;
        private int editEnd;
        public void afterTextChanged(Editable s) {
            editStart = etCommnet.getSelectionStart();
            editEnd = etCommnet.getSelectionEnd();

            // 先去掉监听器，否则会出现栈溢出
            etCommnet.removeTextChangedListener(mTextWatcher);

            // 注意这里只能每次都对整个EditText的内容求长度，不能对删除的单个字符求长度
            // 因为是中英文混合，单个字符而言，calculateLength函数都会返回1
            while (CommonUtil.calculateLength(s.toString()) > MAX_COUNT) { // 当输入字符个数超过限制的大小时，进行截断操作
                s.delete(editStart - 1, editEnd);
                editStart--;
                editEnd--;
            }
            // mEditText.setText(s);将这行代码注释掉就不会出现后面所说的输入法在数字界面自动跳转回主界面的问题了，多谢@ainiyidiandian的提醒
//			mEditText.setText(s);
            etCommnet.setSelection(editStart);

            // 恢复监听器
            etCommnet.addTextChangedListener(mTextWatcher);

            setLeftCount();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

    };

    /**
     * 刷新剩余可输入字数
     */
    private void setLeftCount() {
        tvCommentNum.setText(String.valueOf((MAX_COUNT - getInputCount())));
    }

    /**
     * 获取已经输入的内容长度
     */
    private long getInputCount() {
        return CommonUtil.calculateLength(etCommnet.getText().toString());
    }
}

