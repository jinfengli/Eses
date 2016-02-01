package com.example.lijinfeng.eses.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.example.lijinfeng.eses.R;
import com.example.lijinfeng.eses.constants.ESConstants;
import com.example.lijinfeng.eses.util.CommonUtil;

/**
 * TODO: Record Detail
 *
 * @author li.jf
 */
public class RecordDetailActivity extends AppCompatActivity {
    private static final String TAG = RecordDetailActivity.class.getSimpleName();

    private Toolbar mToolbar;

    private TextView tvStartDateTime;
    private TextView tvSleepDateTime;
    private TextView tvTotalSleepTime;
    private TextView tvComment;
    private TextView tvRecordStatus;

    private String startTime;
    private String sleepTime;
    private String totalSleepTime;

    /**
     * comment
     */
    private String comment;
    /**
     * record status is normal or not
     */
    private String status;
    /**
     * NORMAL_RECORD
     */
    private static final int NORMAL_RECORD = 0;

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
        sleepTime = getIntent().getStringExtra(ESConstants.SLEEP_DATE_TIME);
        // total sleep time equals sleep time minus start time.
        totalSleepTime = CommonUtil.getDiffHourMinutes(startTime, sleepTime);
        comment = getIntent().getStringExtra(ESConstants.RECORD_COMMENT);
        status = getIntent().getStringExtra(ESConstants.EXCEPTION_FLAG);
        Log.d(TAG, "startTime:" + startTime);
        Log.d(TAG, "sleepTime:" + sleepTime);
        Log.d(TAG, "totalSleepTime:" + totalSleepTime);
        Log.d(TAG, "comment:" + comment);
        Log.d(TAG, "status:" + status);

    }

    private void initTitleView() {
        CommonUtil.configToolBarParams(RecordDetailActivity.this);
        mToolbar = (Toolbar) findViewById(R.id.tl_custom);
        mToolbar.setTitle(R.string.record_detail);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolbar.setBackgroundColor(getResources().getColor(R.color.blue));
        setSupportActionBar(mToolbar);
    }

    private void initView() {
        tvStartDateTime = (TextView) findViewById(R.id.tv_detail_startDateTime);
        tvSleepDateTime = (TextView) findViewById(R.id.tv_detail_sleepDateTime);
        tvTotalSleepTime = (TextView) findViewById(R.id.tv_detail_daily_sleep_time);
        String sleepTotalDateFormat = getResources().getString(R.string.sleep_detail_total_time);
        tvTotalSleepTime.setText(String.format(sleepTotalDateFormat, totalSleepTime));
        tvComment = (TextView) findViewById(R.id.tv_detail_sleep_comment);
        tvRecordStatus = (TextView) findViewById(R.id.tv_record_status);

        String startDateFormat = getResources().getString(R.string.start_detail_date_time);
        tvStartDateTime.setText(String.format(startDateFormat, startTime));

        String sleepDateFormat = getResources().getString(R.string.sleep_detail_date_time);
        tvSleepDateTime.setText(String.format(sleepDateFormat, sleepTime));

        String commentFormat = getResources().getString(R.string.sleep_detail_record_comment);
        tvComment.setText(String.format(commentFormat, comment));
        if (NORMAL_RECORD == Integer.valueOf(status)) {
            tvRecordStatus.setText(R.string.normal);
        } else {
            tvRecordStatus.setText(R.string.abnormal);
            tvRecordStatus.setTextColor(Color.RED);
        }
    }

}
