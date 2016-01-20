package com.example.lijinfeng.eses.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * TODO: 检查网络可用工具类
 *
 * @author Jinfeng Lee
 */
public class NetworkUtil {

    /**
     * 网络是否可用
     * @param context
     * @return boolean
     */
    public static boolean isNetworkAvailable(Context context) {
        if (context != null) {
            ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] info = mgr.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isNet(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo infp = manager.getActiveNetworkInfo();
        if (infp == null) {
            return false;
        }
        return infp.isAvailable();
    }

    public static boolean isWifi(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo infp = manager.getActiveNetworkInfo();
        if (infp == null) {
            return false;
        }
        if (infp.getType() == ConnectivityManager.TYPE_WIFI){
            return true;
        }else{
            return false;
        }
    }
}
