/**
 *
 * Copyright 2013 Anjuke. All rights reserved.
 * LogUtils.java
 *
 */
package com.willchun.library.utils;

import android.R.bool;
import android.util.Log;

/**
 * 日志显示的相关类
 *@author chunwang (wcly10@gmail.com)
 *@date 2013-12-26
 */
public class LogUtils {
    private static boolean debug = false;
    private static String tag = "willchun-lib";
    
    
    
    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        LogUtils.debug = debug;
    }
    
    public static void setDebug(boolean debug, String tag){
        LogUtils.debug = debug;
        LogUtils.tag = tag;  
    }

    public static void d(String tag, String msg){
        if(debug) Log.d(tag, msg);
    }
    
    public static void w(String tag, String msg){
        if(debug) Log.w(tag, msg);
    }
    
    public static void e(String tag, String msg){
        if(debug) Log.e(tag, msg);
    }
    
    public static void d(String msg){
        d(tag, msg);
    }
    
    public static void w(String msg){
        w(tag, msg);
    }
    
    public static void e(String msg){
        e(tag, msg);
    }
}
