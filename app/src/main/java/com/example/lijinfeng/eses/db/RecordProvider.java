package com.example.lijinfeng.eses.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;

/**
 * Record ContentProvider
 */
public class RecordProvider extends ContentProvider {
    private static final String TAG = RecordProvider.class.getSimpleName();

    public static final String AUTHORITY = "com.jinfengli.eses.provider.record";
    public static final String TABLE_NAME = "record";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

    private SQLiteOpenHelper mOpenHelper;

    public RecordProvider() {
        // default constructor
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = RecordDbHelper.getInstance(getContext());
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count;
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        count = db.delete(TABLE_NAME, selection, selectionArgs);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        ContentValues values = (initialValues != null) ? new ContentValues(
                initialValues) : new ContentValues();
        for (String colName : RecordConstants.getRequiredColumns()) {
            if (values.containsKey(colName) == false) {
                throw new IllegalArgumentException("Missing column: " + colName);
            }
        }
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long rowId = db.insert(TABLE_NAME, null, initialValues);
        if (rowId < 0) {
            throw new SQLException("Failed to insert row into " + uri);
        }

        Uri noteUri = ContentUris.withAppendedId(CONTENT_URI, rowId);

        return noteUri;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor ret = db.query(TABLE_NAME, projection, selection,
                selectionArgs, null, null, null);

        return ret;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count;
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        count = db.update(TABLE_NAME, values, selection, selectionArgs);
        return count;
    }

    public static class RecordDbHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "record.db";
        private static final int DATABASE_VERSION = 1;

        private static RecordDbHelper recordDbHelper;

        public RecordDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public static RecordDbHelper getInstance(Context context) {
            if (recordDbHelper == null) {
                recordDbHelper = new RecordDbHelper(context);
            }
            return recordDbHelper;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(TAG, "create table record.db");

            StringBuffer sqlStringBuffer = new StringBuffer();
            sqlStringBuffer.append("CREATE TABLE ").append(TABLE_NAME)
                    .append(" ( ").append(RecordConstants._ID)
                    .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                    .append(RecordConstants.RECORD_NO).append("  TEXT,")
                    .append(RecordConstants.START_DATE).append("  TEXT,")
                    .append(RecordConstants.START_TIME).append("  TEXT,")
                    .append(RecordConstants.SLEEP_DATE).append("  TEXT,")
                    .append(RecordConstants.SLEEP_TIME).append("  TEXT,")
                    .append(RecordConstants.SLEEP_TIME_SECOND).append("  TEXT,")
                    .append(RecordConstants.RECORD_TYPE).append("  TEXT,")
                    .append(RecordConstants.RECORD_COMMENT).append("  TEXT,")
                    .append(RecordConstants.EXCEPTION_FLAG).append("  TEXT );");

            Log.d(TAG, "the create sql is:" + sqlStringBuffer.toString());
            db.execSQL(sqlStringBuffer.toString());
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i(TAG, " onUpgrade: from " + oldVersion + " to " + newVersion);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    public static final class RecordConstants implements BaseColumns {
        private RecordConstants() {
            // default constructor
        }

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.example.record";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.example.record";

        public static final String RECORD_NO = "record_no"; // 记录编号
        public static final String START_DATE = "start_date";// 起床日期
        public static final String START_TIME = "start_time";// 起床时间
        public static final String SLEEP_DATE = "sleep_time";// 睡眠日期
        public static final String SLEEP_TIME = "sleep_time";// 睡眠时间
        public static final String SLEEP_TIME_SECOND = "sleep_time_second";// 睡眠时间（凌晨后）
        public static final String RECORD_TYPE = "record_type";// 记录类型
        public static final String RECORD_COMMENT = "record_comment"; // 记录备注
        public static final String EXCEPTION_FLAG = "exception_flag"; // 异常标识


        public static ArrayList<String> getRequiredColumns() {
            ArrayList<String> tmpList = new ArrayList<String>();
            tmpList.add(RECORD_NO);
            tmpList.add(START_DATE);
            tmpList.add(START_TIME);
            tmpList.add(SLEEP_DATE);
            tmpList.add(SLEEP_TIME);
            tmpList.add(SLEEP_TIME_SECOND);
            tmpList.add(RECORD_TYPE);
            tmpList.add(RECORD_COMMENT);
            tmpList.add(EXCEPTION_FLAG);

            return tmpList;
        }
    }

}
