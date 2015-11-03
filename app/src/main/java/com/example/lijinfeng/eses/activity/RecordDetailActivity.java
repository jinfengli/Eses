package com.example.lijinfeng.eses.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.lijinfeng.eses.R;
import com.example.lijinfeng.eses.constants.ESConstants;
import com.example.lijinfeng.eses.util.CommonUtil;

/**
 * TODO: Record Detail
 *
 * @date 2015-10-04
 * @author li.jf
 */
public class RecordDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = RecordDetailActivity.class.getSimpleName();

    private Toolbar mToolbar;

    private TextView tvStartDateTime;
    private TextView tvSleepDateTime;
    private TextView tvTotalSleepTime;
    private TextView tvComment;
    private TextView tvRecordStatus;

    private String storeNo;
    private String startTime;
    private String sleepTime;
    /** 今天总共睡眠时常 */
    private String totalSleepTime;
    /** 备注(今天睡眠如何，工作效率如何) */
    private String comment;
    /** 记录状态是否正常 */
    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);

        handleIntent();
        initTitleView();
        initView();
    }

    private void handleIntent() {
        startTime = getIntent().getStringExtra(ESConstants.START_DATE_TIME);
        sleepTime =  getIntent().getStringExtra(ESConstants.SLEEP_DATE_TIME);
        // 总睡眠时间等于两个时间相减
        totalSleepTime= CommonUtil.getDiffHourMinutes(startTime,sleepTime);
        comment = getIntent().getStringExtra(ESConstants.RECORD_COMMENT);
        status = getIntent().getStringExtra(ESConstants.EXCEPTION_FLAG);
    }

    private void initTitleView() {
        CommonUtil.configToolBarParams(RecordDetailActivity.this);

        mToolbar = (Toolbar) findViewById(R.id.tl_custom);
        mToolbar.setTitle(R.string.record_detail);
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        mToolbar.setBackgroundColor(getResources().getColor(R.color.blue));
        setSupportActionBar(mToolbar);
    }

    private void initView() {
        tvStartDateTime = (TextView) findViewById(R.id.tv_detail_startDateTime);
        tvSleepDateTime = (TextView) findViewById(R.id.tv_detail_sleepDateTime);
        tvTotalSleepTime = (TextView) findViewById(R.id.tv_detail_daily_sleep_time);
        String sleepTotalDateFormat = getResources().getString(R.string.sleep_detail_total_time);
        tvTotalSleepTime.setText(String.format(sleepTotalDateFormat,totalSleepTime));
//        tvTotalSleepTime.setText(totalSleepTime);

        tvComment = (TextView) findViewById(R.id.tv_detail_sleep_comment);
        tvRecordStatus = (TextView) findViewById(R.id.tv_record_status);

        String startDateFormat = getResources().getString(R.string.start_detail_date_time);
        tvStartDateTime.setText(String.format(startDateFormat,startTime));

        String sleepDateFormat = getResources().getString(R.string.sleep_detail_date_time);
        tvSleepDateTime.setText(String.format(sleepDateFormat,sleepTime));

        String commentFormat = getResources().getString(R.string.sleep_detail_record_comment);
        tvComment.setText(String.format(commentFormat,comment));

//        String recordStatusFormat = getResources().getString(R.string.sleep_detail_status);
//        tvRecordStatus.setText(String.format(recordStatusFormat, status));
        if(Integer.valueOf(status) == 0) {
            tvRecordStatus.setText(R.string.normal);
        } else {
            tvRecordStatus.setText(R.string.abnormal);
            tvRecordStatus.setTextColor(Color.RED);
        }

    }

    @Override
    public void onClick(View view) {

    }
}
