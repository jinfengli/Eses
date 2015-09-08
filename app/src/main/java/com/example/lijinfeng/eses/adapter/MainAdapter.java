package com.example.lijinfeng.eses.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lijinfeng.eses.R;
import com.example.lijinfeng.eses.base.BaseActivity;
import com.example.lijinfeng.eses.bean.RecordBean;

import java.util.ArrayList;


/*
 *  TODO:
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

        } else {
            holder = (ViewHolder) convertView.getTag();
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
