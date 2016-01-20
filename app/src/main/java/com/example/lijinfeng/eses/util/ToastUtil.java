package com.example.lijinfeng.eses.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lijinfeng.eses.R;

/**
 * TODO：Toast 工具类
 *
 * @author: Jinfeng lee
 */
public class ToastUtil {

    public static void showToastL(Context context, String content) {
        Toast toast = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_toast, null);
        TextView text = (TextView) layout.findViewById(R.id.tv_toast_content);
        text.setText(content);
        toast = new Toast(context.getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    /**
     * 展示Long Toast
     *
     * @param context
     * @param contentId
     */
    public static void showToastL(Context context, int contentId) {
        showToastL(context, context.getString(contentId));
    }

    /**
     * 展示 Short Toast
     *
     * @param context
     * @param content toast的内容
     */
    public static void showToastS(Context context, String content) {
        Toast toast = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_toast, null);
        TextView text = (TextView) layout.findViewById(R.id.tv_toast_content);
        text.setText(content);
        toast = new Toast(context.getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    /**
     * 展示 Short Toast
     *
     * @param context
     * @param contentId 展示内容的id
     */
    public static void showToastS(Context context, int contentId) {
        showToastS(context, context.getString(contentId));
    }


    // 简单封装系统的一些toast
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


