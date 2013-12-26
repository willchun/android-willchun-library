/**
 *
 * Copyright 2013 Anjuke. All rights reserved.
 * LogUtils.java
 *
 */
package com.willchun.library.utils;

import android.util.Log;

/**
 * 日志显示的相关类
 *@author chunwang (wcly10@gmail.com)
 *@date 2013-12-26
 */
public class LogUtils {
    private static boolean debug = false;
    
    public static void d(String tag, String msg){
        Log.d(tag, msg);
    }
}
