/**
 *
 * Copyright 2013 Anjuke. All rights reserved.
 * AppUtils.java
 *
 */
package com.willchun.library.utils;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 *@author chunwang (wcly10@gmail.com)
 *@date 2013-10-28
 */
public class AppUtils {
    /**
     * 获取应用版本编号   android:versionCode
     * @return
     */
    public int getVerCode(Context context){
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return verCode;
    }
    
    /**
     * 获取版本名称 android:versionName
     * @return
     */
    public String getVerName(Context context){
        String verName = null;
        try {
            verName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return verName;
    }
}
