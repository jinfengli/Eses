package com.example.lijinfeng.eses;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.example.lijinfeng.eses.constants.ESConstants;

/*
 * TODO: ESApplication
 *
 * Copyright (C) 15-9-19 下午3:14 li.jf All rights reserved.
 */
public class ESApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AVOSCloud.useAVCloudCN();
//        AVOSCloud.initialize(ESApplication.this, "XrARrV25Agqi3mfaQVY3uO3y","ATIbQxIBymfdHHhWCK3hWYHk");
        AVOSCloud.initialize(ESApplication.this, ESConstants.APPLICATION_ID,
                ESConstants.CLIENT_KEY);
    }
}
