package com.example.lijinfeng.eses.db;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.example.lijinfeng.eses.bean.RecordBean;

public class EsesDBHelper {

    private ContentResolver resolver;
    private Context mContext;

    public EsesDBHelper(Context mContext) {
        this.mContext = mContext;
        this.resolver = mContext.getContentResolver();
    }

    /**
     * 添加记录
     * @param recordBean
     */
    public void addRecord(RecordBean recordBean) {
        ContentValues values = new ContentValues();
        values.put(RecordProvider.RecordConstants.RECORD_NO,recordBean.getRecordNo());
        values.put(RecordProvider.RecordConstants.START_DATE, recordBean.getStartDate());
        values.put(RecordProvider.RecordConstants.START_TIME, recordBean.getStartTime());
        values.put(RecordProvider.RecordConstants.SLEEP_DATE, recordBean.getSleepDate());
        values.put(RecordProvider.RecordConstants.SLEEP_TIME, recordBean.getSleepTime());
        values.put(RecordProvider.RecordConstants.SLEEP_TIME_SECOND, recordBean.getSleepTimeSecond());
        values.put(RecordProvider.RecordConstants.RECORD_TYPE, recordBean.getRecordType());
        values.put(RecordProvider.RecordConstants.RECORD_COMMENT, recordBean.getRecordComment());
        values.put(RecordProvider.RecordConstants.EXCEPTION_FLAG, recordBean.getExceptionFlag());

        resolver.insert(RecordProvider.CONTENT_URI, values);
    }

    public void queryAllRecords() {

    }

    public void deleteRecord() {

    }
}
