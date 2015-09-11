package com.example.lijinfeng.eses.util;

import android.content.Context;
import android.widget.Toast;

/**
 * TODO：Toast 工具类
 *
 * @author: li.jf
 * @date: 2015/9/11 15:20
 */
public class ToastUtil {

    public static void toastLong(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
    }

    public static void toastLong(Context context, String resContent) {
        Toast.makeText(context, resContent, Toast.LENGTH_LONG).show();
    }


    public static void toastShort(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    public static void toastShort(Context context, String resContent) {
        Toast.makeText(context, resContent, Toast.LENGTH_SHORT).show();
    }

}


