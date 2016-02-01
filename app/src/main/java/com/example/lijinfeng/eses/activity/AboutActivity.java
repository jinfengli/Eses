package com.example.lijinfeng.eses.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.lijinfeng.eses.R;
import com.example.lijinfeng.eses.util.CommonUtil;

/*
 *  TODO: AboutActivity
 *
 *  @author:li.jf
 */
public class AboutActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initTitleView();
    }

    private void initTitleView() {
        CommonUtil.configToolBarParams(this);

        mToolbar = (Toolbar) findViewById(R.id.tl_custom);
        mToolbar.setTitle(R.string.title_activity_about);
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        mToolbar.setBackgroundColor(getResources().getColor(R.color.blue));
        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * A simple handler that stops the service if playback is not active (playing)
     */
//    private static class DelayedStopHandler extends Handler {
//        private final WeakReference<MusicService> mWeakReference;
//
//        private DelayedStopHandler(MusicService service) {
//            mWeakReference = new WeakReference<>(service);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            MusicService service = mWeakReference.get();
//            if (service != null && service.mPlayback != null) {
//                if (service.mPlayback.isPlaying()) {
//                    LogHelper.d(TAG, "Ignoring delayed stop since the media player is in use.");
//                    return;
//                }
//                LogHelper.d(TAG, "Stopping service with delay handler.");
//                service.stopSelf();
//                service.mServiceStarted = false;
//            }
//        }
//    }
}
