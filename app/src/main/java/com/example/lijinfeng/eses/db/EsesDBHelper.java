package com.example.lijinfeng.eses.db;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.lijinfeng.eses.bean.RecordBean;

import java.util.ArrayList;

public class EsesDBHelper {

    private ContentResolver resolver;
    private Context mContext;

    public EsesDBHelper(Context mContext) {
        this.mContext = mContext;
        this.resolver = mContext.getContentResolver();
    }

    public RecordBean queryRecordByRecordNo(String recordNo) {
        String []projection = new String [] {

                RecordProvider.RecordConstants._ID,
                RecordProvider.RecordConstants.RECORD_NO,
                RecordProvider.RecordConstants.START_DATE,
                RecordProvider.RecordConstants.START_TIME,
                RecordProvider.RecordConstants.SLEEP_DATE,
                RecordProvider.RecordConstants.SLEEP_TIME,
                RecordProvider.RecordConstants.SLEEP_TIME_SECOND,
                RecordProvider.RecordConstants.RECORD_TYPE,
                RecordProvider.RecordConstants.RECORD_COMMENT,
                RecordProvider.RecordConstants.EXCEPTION_FLAG,

        };

        String selection = RecordProvider.RecordConstants.RECORD_NO + " = " +recordNo;
        Cursor cursor = resolver.query(RecordProvider.CONTENT_URI,projection,selection, null, null);

        if(cursor != null) {
            cursor.moveToFirst();
        }

        RecordBean record = new RecordBean(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7),
                cursor.getString(8),
                cursor.getString(9));

        return record;
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
        values.put(RecordProvider.RecordConstants.SLEEP_TIME,  recordBean.getSleepTime());
        values.put(RecordProvider.RecordConstants.TIME_DIFF,  recordBean.getTimeDiff());
        values.put(RecordProvider.RecordConstants.SLEEP_TIME_SECOND, recordBean.getSleepTimeSecond());
        values.put(RecordProvider.RecordConstants.RECORD_TYPE, recordBean.getRecordType());
        values.put(RecordProvider.RecordConstants.RECORD_COMMENT, recordBean.getRecordComment());
        values.put(RecordProvider.RecordConstants.EXCEPTION_FLAG, recordBean.getExceptionFlag());

        resolver.insert(RecordProvider.CONTENT_URI, values);
    }

    /**
     * 查询所有记录
     * @param
     * @return
     */
    public ArrayList<RecordBean> queryAllRecords() {
        ArrayList<RecordBean> records = new ArrayList<RecordBean>();

        String []projections = new String[]{
                RecordProvider.RecordConstants.RECORD_NO,
                RecordProvider.RecordConstants.START_DATE,
                RecordProvider.RecordConstants.START_TIME,
                RecordProvider.RecordConstants.SLEEP_DATE,
                RecordProvider.RecordConstants.SLEEP_TIME,
                RecordProvider.RecordConstants.TIME_DIFF,
                RecordProvider.RecordConstants.SLEEP_TIME_SECOND,
                RecordProvider.RecordConstants.RECORD_TYPE,
                RecordProvider.RecordConstants.RECORD_COMMENT,
                RecordProvider.RecordConstants.EXCEPTION_FLAG
        };

        Cursor cursor = resolver.query(RecordProvider.CONTENT_URI, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            RecordBean recordBean = new RecordBean();
            recordBean.set_id(cursor.getInt(cursor.getColumnIndex(RecordProvider.RecordConstants._ID)));
            recordBean.setRecordNo(
                cursor.getString(cursor.getColumnIndex(RecordProvider.RecordConstants.RECORD_NO)));
            recordBean.setStartDate(
                cursor.getString(cursor.getColumnIndex(RecordProvider.RecordConstants.START_DATE)));
            recordBean.setStartTime(
                cursor.getString(cursor.getColumnIndex(RecordProvider.RecordConstants.START_TIME)));
            recordBean.setSleepDate(
                cursor.getString(cursor.getColumnIndex(RecordProvider.RecordConstants.SLEEP_DATE)));
            recordBean.setSleepTime(
                cursor.getString(cursor.getColumnIndex(RecordProvider.RecordConstants.SLEEP_TIME)));
            recordBean.setTimeDiff(cursor.getString(cursor.getColumnIndex(RecordProvider.RecordConstants.TIME_DIFF)));
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

    /**
     * 删除单条记录   第二个参数中 whereArgs后面应该由”＝？“， 否则会报错误：
     * java.lang.IllegalArgumentException: Too many bind arguments.
     * 1 arguments were provided but the statement needs 0 arguments
     * @param recordNo
     */
    public void deleteRecord(String recordNo) {
        resolver.delete(RecordProvider.CONTENT_URI, RecordProvider.RecordConstants.RECORD_NO + "=?", new String[]{recordNo});
    }

    public int updateRecord(RecordBean record) {
        ContentValues values = new ContentValues();
        values.put(RecordProvider.RecordConstants.START_DATE,record.getStartDate());
        values.put(RecordProvider.RecordConstants.START_TIME,record.getStartTime());
        values.put(RecordProvider.RecordConstants.SLEEP_DATE,record.getSleepDate());
        values.put(RecordProvider.RecordConstants.SLEEP_TIME,record.getSleepTime());
        values.put(RecordProvider.RecordConstants.SLEEP_TIME_SECOND,record.getSleepTimeSecond());
        values.put(RecordProvider.RecordConstants.EXCEPTION_FLAG,record.getExceptionFlag());
        values.put(RecordProvider.RecordConstants.RECORD_COMMENT,record.getRecordComment());
        values.put(RecordProvider.RecordConstants.RECORD_TYPE, record.getRecordType());

        return resolver.update(RecordProvider.CONTENT_URI,
                values,
                RecordProvider.RecordConstants.RECORD_NO + " =? ",
                new String[] {record.getRecordNo()});
    }

   /* public void updateRecord(String recordNo) {
        ContentValues values = new ContentValues();
        values.put(ESConstants.RECORD_NO, recordNo);
        String where = RecordProvider.RecordConstants.RECORD_NO + " =? ";
        String [] selectionArgs = new String[]{recordNo};
        resolver.update(RecordProvider.CONTENT_URI,values,where, selectionArgs);
    }*/

  /**
   * 获取时间差值列表
   * @return
   */
  public ArrayList<String> getTimeDiffs() {
    ArrayList<String> recordTimeDiffs = new ArrayList<String>();

    Cursor cursor = resolver.query(RecordProvider.CONTENT_URI, null, null, null, null);
    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      recordTimeDiffs.add(cursor.getString(cursor.getColumnIndex(RecordProvider.RecordConstants.TIME_DIFF)));
      cursor.moveToNext();
    }

    cursor.close();

    return recordTimeDiffs;
  }


}
