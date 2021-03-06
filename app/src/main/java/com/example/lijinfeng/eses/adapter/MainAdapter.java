package com.example.lijinfeng.eses.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lijinfeng.eses.R;
import com.example.lijinfeng.eses.bean.RecordBean;
import com.example.lijinfeng.eses.util.CommonUtil;

import java.util.ArrayList;

/*
 *  TODO: MainAdapter
 *
 *  Date: 15-8-24 上午7:52
 *  Copyright (c) li.jf All rights reserved.
 */
public class MainAdapter extends BaseAdapter {

    private static final String TAG = MainAdapter.class.getSimpleName();

    private LayoutInflater mLayoutInflater;

    private ArrayList<RecordBean> recordDatas;
    private Context context;

    public MainAdapter(Context context) {
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        recordDatas = new ArrayList<RecordBean>();
    }

    public ArrayList<RecordBean> getRecordDatas() {
        return recordDatas;
    }

    public void setRecordDatas(ArrayList<RecordBean> recordDatas) {
        this.recordDatas = recordDatas;
    }

    @Override
    public int getCount() {
        if(null != recordDatas) {
            return recordDatas.size();
        }

        return 0;
    }

    @Override
    public Object getItem(int position) {
        return recordDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if(convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_record, null);
            holder = new ViewHolder();
            holder.tvStartTime = (TextView) convertView.findViewById(R.id.tv_item_start_time);
            holder.tvSleepTime = (TextView) convertView.findViewById(R.id.tv_item_sleep_time);
            holder.tvDiffTime = (TextView) convertView.findViewById(R.id.tv_differ_time);
            holder.tvExceptionFlag = (TextView) convertView.findViewById(R.id.tv_record_flag);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(recordDatas != null && recordDatas.size() > 0) {
            String startDateTime;
            String sleepDateTime;
            RecordBean recordBean = recordDatas.get(position);
            startDateTime = recordBean.getStartDate() + " " + recordBean.getStartTime();
            sleepDateTime = recordBean.getSleepDate() +" "+ recordBean.getSleepTime();
            holder.tvStartTime.setText(startDateTime);
            holder.tvSleepTime.setText(sleepDateTime);
            holder.tvDiffTime.setText(CommonUtil.getDiffHourMinutes(startDateTime, sleepDateTime));

            if(recordBean.getExceptionFlag().equals("0")) {
                holder.tvExceptionFlag.setText("正常");
            } else if(recordBean.getExceptionFlag().equals("1")) {
                holder.tvExceptionFlag.setText("异常");
                holder.tvExceptionFlag.setTextColor(Color.RED);
            }

        }
        return convertView;
    }

    static class ViewHolder {
        private TextView tvStartTime;
        private TextView tvSleepTime;
        private TextView tvExceptionFlag;
        private TextView tvDiffTime;
    }
}
