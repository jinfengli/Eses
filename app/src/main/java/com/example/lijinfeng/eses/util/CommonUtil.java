package com.example.lijinfeng.eses.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.example.lijinfeng.eses.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TODO：
 *
 * @author: li.jf
 * @date: 2015/9/15 10:03
 * @copyright (C) wonhigh.cn
 */
public class CommonUtil {
    /**
     * 计算分享内容的字数，一个汉字=两个英文字母，
     * 一个中文标点=两个英文标点
     * 注意：该函数的不适用于对单个字符进行计算，因为单个字符四舍五入后都是1
     *
     * @param c
     * @return
     */
    public static long calculateLength(CharSequence c) {
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            int tmp = (int) c.charAt(i);
            if (tmp > 0 && tmp < 127) {
                len += 0.5;
            } else {
                len++;
            }
        }
        return Math.round(len);
    }

    /**
     * APP版本号
     *
     * @param context
     * @return
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            versionName = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 信息反馈
     * @param activity
     */
    public static void feedbackTOMe(Activity activity) {
        Intent data=new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailto:lijinfeng_ljf@foxmail.com"));
        //  Build.MODEL: H60-L01  Device: hwH60  release 4.4.2
        data.putExtra(Intent.EXTRA_SUBJECT, "来自 "
                +  Build.DEVICE
                + "("
                + Build.VERSION.RELEASE
                + ") 的反馈");
        data.putExtra(Intent.EXTRA_TEXT, "输入反馈内容...");
        activity.startActivity(data);
    }

    /**
     * 配置沉浸式状态栏信息
     * @param activity
     */
    public static void configToolBarParams(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            CommonUtil.setTranslucentStatus(activity, true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.statusbar_bg);
    }

    @TargetApi(19)
    public static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /***********************************/

    /**
     * 分享功能
     *
     * @param context
     *		  上下文
     * @param activityTitle
     *		  Activity的名字
     * @param msgTitle
     *		  消息标题
     * @param msgText
     *		  消息内容
     * @param imgPath
     *		  图片路径，不分享图片则传null
     */
    public static void shareMsg(Context context,
            String activityTitle,
                         String msgTitle,
                         String msgText,
                         String imgPath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (imgPath == null || imgPath.equals("")) {
            intent.setType("text/plain"); // 纯文本
        } else {
            File f = new File(imgPath);
            // f!=null 实际不需要进行判断
            if (f != null && f.exists() && f.isFile()) {
                intent.setType("image/jpg");
                Uri u = Uri.fromFile(f);
                intent.putExtra(Intent.EXTRA_STREAM, u);
            }
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, activityTitle));
    }

    public static void shareText(Context context){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "ES APP, keep up with your sleep and start up time");
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, "分享到"));
    }

    /**
     * 获取两个格式化的String类型的时间之间的差值(毫秒数)
     * @param timestart
     * @param timeEnd
     * @return
     */
    public static long getStringFormatTimeMillDiff(String timestart, String timeEnd) {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        long diff = 0L;
        try {
            Date dTimeEnd = df.parse(timeEnd);
            Date dTImeStart = df.parse(timestart);
            diff = dTimeEnd.getTime() - dTImeStart.getTime();
//            long days = diff / (1000 * 60 * 60 * 24);
        } catch (Exception e) {

        }
        return diff;
    }

    /**
     * 获取两个String类型时间之间差值的 小时:分钟
     * @param timestart
     * @param timeEnd
     * @return
     */
    public static String getDiffHourMinutes(String timestart, String timeEnd) {
        long hours = 0L;
        long minutes = 0L;
        long diffMill = getStringFormatTimeMillDiff(timestart,timeEnd);

        hours = diffMill / (1000 * 60 * 60);
//        diffMill = diffMill/ (1000 * 60 * 60);
        minutes = diffMill / (1000 * 60) % 60;
        if(hours == 0) {
            return minutes + "分钟";
        }

        if(minutes == 0) {
            return hours + "小时";
        }

        return hours +"h" + minutes + "mins";
    }

}
