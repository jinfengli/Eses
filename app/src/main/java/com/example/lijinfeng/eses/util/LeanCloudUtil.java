package com.example.lijinfeng.eses.util;

import android.content.Context;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.lijinfeng.eses.bean.RecordBean;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: LeanCloudUtil
 *
 * @author kingfeng
 * @version 1.0.0
 * @date 2016/6/16
 * @copyright (c) kingfeng All rights reserved.
 */
public class LeanCloudUtil {

    private static LeanCloudUtil leanCloudUtil;
    private Context context;

    static ArrayList<RecordBean> records = new ArrayList<RecordBean>();

    private LeanCloudUtil(Context context) {
        this.context = context;
    }

    public static LeanCloudUtil getInstance(Context context) {
        if(leanCloudUtil == null) {
            leanCloudUtil = new LeanCloudUtil(context.getApplicationContext());
        }
        return leanCloudUtil;
    }

    public static ArrayList<RecordBean> query(String username) {
        AVQuery<AVObject> query = new AVQuery<>("Record");
        query.whereEqualTo("userName", username);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(!list.isEmpty()) {
                    int resultSize = list.size();
                    for (int i = 0; i < resultSize; i++) {
                        RecordBean recordBean = new RecordBean();
                        recordBean.setRecordType(list.get(i).getString("recordType"));
                        if(!(list.get(i).getBoolean("exceptionFlag"))) {
                            recordBean.setExceptionFlag("0");
                        } else {
                            recordBean.setExceptionFlag("1");
                        }
                        recordBean.setStartDate(list.get(i).getString("startDate"));
                        recordBean.setStartTime(list.get(i).getString("startTime"));
                        recordBean.setSleepDate(list.get(i).getString("sleepDate"));
                        recordBean.setSleepTime(list.get(i).getString("sleepTime"));
                        recordBean.setRecordComment(list.get(i).getString("recordComment")+ "");

                        records.add(recordBean);
                    }
                }
            }
        });

        return records;
    }

}
