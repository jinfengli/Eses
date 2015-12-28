package com.example.lijinfeng.eses.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.example.lijinfeng.eses.R;
import com.example.lijinfeng.eses.bean.RecordBean;
import com.example.lijinfeng.eses.db.EsesDBHelper;
import com.example.lijinfeng.eses.db.RecordProvider;
import com.example.lijinfeng.eses.util.CommonUtil;
import com.example.lijinfeng.eses.util.ToastUtil;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import java.io.File;
import java.util.List;

import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.DateFormat;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/*
 *  TODO:SettingsActivity
 *
 *  @date: 15-9-12 上午6:58
 *  @author: Jinfeng Lee
 */
public class SettingsActivity extends AppCompatActivity implements
        OnItemClickListener, UmengUpdateListener, View.OnClickListener {

    private Toolbar mToolbar;

    private TextView tvSetTheme;
    private TextView tvFeedBack;
    private TextView tvCheckNewVersion;
    private TextView tvBackup;
    private TextView tvAboutApp;

    private EsesDBHelper dbHelper;

    private ProgressDialog progressDialog;

    private WritableCellFormat format;
    private WritableCellFormat dateFormat;

    private static final int ID_INDEX = 0;
    private static final int NO_INDEX = 1;
    private static final int START_DATE_INDEX = 2;
    private static final int START_TIME_INDEX = 3;
    private static final int SLEEP_DATE_INDEX = 4;
    private static final int SLEEP_TIME_INDEX = 5;
    private static final int SLEEP_TIME_SECOND_INDEX = 6;
    private static final int RECORD_TYPE_INDEX = 7;
    private static final int RECORD_COMMENT_INDEX = 8;
    private static final int EXCEPTION_FLAG_INDEX = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initTitleView();
        initView();
        init();
        setListener();
    }

    protected void initTitleView() {
        CommonUtil.configToolBarParams(this);
        mToolbar = (Toolbar) findViewById(R.id.tl_custom);
        mToolbar.setTitle(getResources().getString(R.string.title_activity_settings));//设置Toolbar标题
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        mToolbar.setBackgroundColor(getResources().getColor(R.color.blue));
        setSupportActionBar(mToolbar);
    }

    protected void initView() {
        tvSetTheme = (TextView) findViewById(R.id.tv_set_theme);
        tvFeedBack = (TextView) findViewById(R.id.tv_feedback);
        tvCheckNewVersion = (TextView) findViewById(R.id.tv_check_new_version);
        String s = getResources().getString(R.string.check_new_version);
        tvCheckNewVersion.setText(String.format(s, CommonUtil.getAppVersionName(this)));

        tvBackup = (TextView) findViewById(R.id.tv_backup);
        tvAboutApp = (TextView) findViewById(R.id.tv_about_app);
        // 1、添加修改主题
        // 2、umeng检查更新
        // 3、记录备份 --> 导出到excel文件中.
    }

    private void init() {
        format = new WritableCellFormat(WritableWorkbook.ARIAL_10_PT);
        try {
            format.setWrap(true);
            format.setBorder(Border.ALL, BorderLineStyle.THIN);

            dateFormat = new WritableCellFormat(new DateFormat("yyyy-MM-dd"));
            dateFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
        } catch (WriteException e) {
            e.printStackTrace();
        }

        dbHelper = new EsesDBHelper(this);
    }

    private void setListener() {
        tvSetTheme.setOnClickListener(this);
        tvFeedBack.setOnClickListener(this);
        tvCheckNewVersion.setOnClickListener(this);
        tvBackup.setOnClickListener(this);
        tvAboutApp.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.tv_feedback) {
            CommonUtil.feedbackTOMe(SettingsActivity.this);
        } else if(view.getId() == R.id.tv_check_new_version) {
            UmengUpdateAgent.setUpdateAutoPopup(false);
            UmengUpdateAgent.setUpdateListener(this);
            UmengUpdateAgent.update(this); // 不要把下面这行放在 onUpdateReturned()回调函数中，否则不起作用.
        } else if(view.getId() == R.id.tv_backup) {
            try {
                showProgressDialog();
//                progressDialog = ProgressDialog.show(SettingsActivity.this, "", "正在备份到Excel中...", true, false);
                handleExcel();
//                try {
//                    generateExcelReport(RecordProvider.TABLE_NAME);
////                    msgHandler.sendEmptyMessage(0);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
                ToastUtil.showToastS(SettingsActivity.this, "备份失败：" + e.getLocalizedMessage());
            }
        } else if(view.getId() == R.id.tv_about_app) {
            startActivity(new Intent(SettingsActivity.this,AboutActivity.class));
        }
    }

    private void handleExcel() {
        new Thread() {
            @Override
            public void run() {
                // 备份到Excel文件
                try {
                    msgHandler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 把数据库中的数据导入到excel表格中.
     * @param tablename
     * @throws Exception
     */
    private void generateExcelReport(String tablename) throws Exception {
//        String fileName = Environment.getExternalStorageDirectory()
//                + "/es_backup/" + tablename +".xls";
//        File myFile = new File(Environment.getExternalStorageDirectory(),"es_backup/" + tablename +".xls");
//        if (!myFile.exists()) {
//            myFile.mkdirs();
//            myFile.createNewFile();
//        }

        // 创建workbook
        WritableWorkbook workbook = Workbook.createWorkbook(
                getFilePath(Environment.getExternalStorageDirectory() + "/es_backup/", tablename + ".xls"));

        // 创建 Sheet
        WritableSheet reportSheet = workbook.createSheet("report", 0);
        // 设置列宽
        setColumnWidth(reportSheet);

        int row = 0;
        setTitle(reportSheet, row++);
        List<RecordBean> recordBeans = dbHelper.queryAllRecords();

        if(recordBeans.size() > 0) {
            for (RecordBean recordBean :recordBeans) {
                setRow(reportSheet, row++, recordBean);
            }

            // 从内存中写入到文件中
            workbook.write();
            ToastUtil.showToastS(this, "备份成功");
//            progressDialog.dismiss();
        } else {
            ToastUtil.showToastS(this, "数据项为空，不需要备份");
//            progressDialog.dismiss();
        }

        workbook.close();
    }


    public static File getFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {

        }
    }

    private void setColumnWidth(WritableSheet sheet) {
        sheet.setColumnView(ID_INDEX, 20);
        sheet.setColumnView(NO_INDEX, 20);
        sheet.setColumnView(START_DATE_INDEX, 40);
        sheet.setColumnView(START_TIME_INDEX, 40);
        sheet.setColumnView(SLEEP_DATE_INDEX, 40);
        sheet.setColumnView(SLEEP_TIME_INDEX, 40);
        sheet.setColumnView(SLEEP_TIME_SECOND_INDEX, 40);
        sheet.setColumnView(RECORD_TYPE_INDEX, 20);
        sheet.setColumnView(RECORD_COMMENT_INDEX, 20);
        sheet.setColumnView(EXCEPTION_FLAG_INDEX, 20);
    }

    private void setTitle(WritableSheet sheet, int row) throws Exception {
        sheet.addCell(new Label(ID_INDEX, row, "ID", format));
        sheet.addCell(new Label(NO_INDEX, row, "NO", format));
        sheet.addCell(new Label(START_DATE_INDEX, row, "开始日期", format));
        sheet.addCell(new Label(START_TIME_INDEX, row, "开始时间", format));
        sheet.addCell(new Label(SLEEP_DATE_INDEX, row, "结束日期", format));
        sheet.addCell(new Label(SLEEP_TIME_INDEX, row, "结束时间", format));
        sheet.addCell(new Label(SLEEP_TIME_SECOND_INDEX, row, "结束时间2", format));
        sheet.addCell(new Label(RECORD_TYPE_INDEX, row, "记录类型", format));
        sheet.addCell(new Label(RECORD_COMMENT_INDEX, row, "记录内容", format));
        sheet.addCell(new Label(EXCEPTION_FLAG_INDEX, row, "异常标记", format));
    }

    private void setRow(WritableSheet sheet, int row, RecordBean record) throws Exception {
        sheet.addCell(new jxl.write.Number(ID_INDEX, row, record.get_id(), format));
        sheet.addCell(new Label(NO_INDEX, row, record.getRecordNo(), format));

        sheet.addCell(new Label(START_DATE_INDEX, row, record.getStartDate(), format));
        sheet.addCell(new Label(START_TIME_INDEX, row, record.getStartTime(), format));
        sheet.addCell(new Label(SLEEP_DATE_INDEX, row, record.getSleepDate(), format));
        sheet.addCell(new Label(SLEEP_TIME_INDEX, row, record.getSleepTime(), format));
        sheet.addCell(new Label(SLEEP_TIME_SECOND_INDEX, row, record.getSleepTimeSecond(), format));

        sheet.addCell(new Label(RECORD_TYPE_INDEX, row, record.getRecordType(), format));
        sheet.addCell(new Label(RECORD_COMMENT_INDEX, row, record.getRecordComment(), format));
        sheet.addCell(new Label(EXCEPTION_FLAG_INDEX, row, record.getExceptionFlag(), format));
    }

    private void showNoUpdateAlertDialog() {
        new AlertView("版本更新",
                "已经是最新版本",
                null,
                new String[]{"确定"},
                null,
                SettingsActivity.this,
                AlertView.Style.Alert,
                this).show();
        progressDialog.dismiss();
    }

    @Override
    public void onItemClick(Object o, int i) {

    }

    @Override
    public void onUpdateReturned(int updateStatus, UpdateResponse updateResponse) {
        switch (updateStatus) {
            case UpdateStatus.Yes: // 有新版本
                UmengUpdateAgent.showUpdateDialog(SettingsActivity.this, updateResponse);
                break;
            case UpdateStatus.No: // 没有新版本
                showProgressDialog();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                showNoUpdateAlertDialog();
                break;
            case UpdateStatus.NoneWifi: // none wifi
                Toast.makeText(this, "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
                break;
            case UpdateStatus.Timeout: // time out
                Toast.makeText(this, "timeout", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void showProgressDialog() {
        progressDialog = ProgressDialog.show(SettingsActivity.this, "", "正在备份到Excel中...", true, false);

    }

    /**
     * 使用Handler来对UI进行更新
     */
    private Handler msgHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if(progressDialog != null) {
//                progressDialog.dismiss();
//            }
            switch (msg.arg1) {
                case 0:
                    try {
                        generateExcelReport(RecordProvider.TABLE_NAME);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                    break;
            }


        }
    };


}
