package com.example.lijinfeng.eses.db;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.lijinfeng.eses.bean.RecordBean;

import java.lang.reflect.Array;
import java.util.ArrayList;

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

    /**
     * 查询所有记录
     * @param cursor
     * @return
     */
    public ArrayList<RecordBean> queryAllRecords(Cursor cursor) {
        ArrayList<RecordBean> records = new ArrayList<RecordBean>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            RecordBean recordBean = new RecordBean();
            recordBean.setRecordNo(cursor.getString(cursor.getColumnIndex(RecordProvider.RecordConstants.RECORD_NO)));
            recordBean.setStartDate(cursor.getString(cursor.getColumnIndex(RecordProvider.RecordConstants.START_DATE)));
            recordBean.setStartTime(cursor.getString(cursor.getColumnIndex(RecordProvider.RecordConstants.START_TIME)));
            recordBean.setSleepDate(cursor.getString(cursor.getColumnIndex(RecordProvider.RecordConstants.SLEEP_DATE)));
            recordBean.setSleepTime(cursor.getString(cursor.getColumnIndex(RecordProvider.RecordConstants.SLEEP_TIME)));
            recordBean.setSleepTimeSecond(cursor.getString(cursor.getColumnIndex(RecordProvider.RecordConstants.SLEEP_TIME_SECOND)));
            recordBean.setRecordType(cursor.getString(cursor.getColumnIndex(RecordProvider.RecordConstants.RECORD_TYPE)));
            recordBean.setRecordComment(cursor.getString(cursor.getColumnIndex(RecordProvider.RecordConstants.RECORD_COMMENT)));
            recordBean.setExceptionFlag(cursor.getString(cursor.getColumnIndex(RecordProvider.RecordConstants.EXCEPTION_FLAG)));
            records.add(recordBean);
            cursor.moveToNext();
        }
        cursor.close();
        return records;
    }

    public void deleteRecord() {


    }
}
