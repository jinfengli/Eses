package com.example.lijinfeng.eses;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.example.lijinfeng.eses.constants.ESConstants;

/*
 * TODO: ESApplication
 *
 * @author li.jf
 * @date 2015-10-13
 */
public class ESApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AVOSCloud.useAVCloudCN();

        AVOSCloud.initialize(ESApplication.this, ESConstants.APPLICATION_ID, ESConstants.CLIENT_KEY);
    }
}
