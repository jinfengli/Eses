package com.example.lijinfeng.eses.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

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
}
