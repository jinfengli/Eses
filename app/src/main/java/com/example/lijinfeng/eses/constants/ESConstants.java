package com.example.lijinfeng.eses.constants;


/*
 *  TODO: 常量类
 *
 *  Date: 15-9-28 下午9:42
 *  Copyright (c) li.jf All rights reserved.
 */

public class ESConstants {

    public static final String TYPE_SLEEP = "type_sleep";
    public static final String TYPE_EXERCISE = "type_exercise";

    public static final String USER_NAME = "user_account";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_PWD = "user_pwd";
    public static final String USER_REGISTER_TIME = "user_register_time";

    // 使用LeanCloud的初始化配置
    public static final String APPLICATION_ID = "XrARrV25Agqi3mfaQVY3uO3y";
    public static final String CLIENT_KEY = "ATIbQxIBymfdHHhWCK3hWYHk";

    public static final String START_DATE_TIME = "start_date_time";
    public static final String SLEEP_DATE_TIME = "sleep_date_time";
    public static final String SLEEP_TIME_SECOND = "sleep_time_second";
    public static final String RECORD_COMMENT = "record_comment";
    public static final String EXCEPTION_FLAG = "exception_flag";

    public static final String START_DATE = "start_date";
    public static final String START_TIME = "start_time";
    public static final String SLEEP_DATE = "sleep_date";
    public static final String SLEEP_TIME = "sleep_time";
    public static final String RECORD_NO = "record_no";

    /** 匹配邮件格式的正则表达式 */
    public static final String EMAIL_REGEX_1 = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";

    /**
     * 正则表达式匹配
     *  ref: http://blog.csdn.net/fatherican/article/details/8853062
     * */
    public static final String EMAIL_REGEX  = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

}
