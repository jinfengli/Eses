package com.example.lijinfeng.eses.base;


import android.view.View;

import com.example.lijinfeng.eses.R;

/*
 *  TODO:
 *
 *  Date: 15-8-31 下午9:51
 *  Copyright (c) li.jf All rights reserved.
 */
public class DemoBase extends BaseActivity {
    protected String[] mMonths = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };

    protected String[] mParties = new String[] {
            "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
            "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
            "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
            "Party Y", "Party Z"
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    }

    @Override
    protected void initTitleView() {

    }

    @Override
    protected void initView() {

    }

    @Override
    public void onClick(View v) {

    }
}
