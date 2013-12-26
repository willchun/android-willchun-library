/**
 *
 * Copyright 2013 Anjuke. All rights reserved.
 * ManifestUtils.java
 *
 */
package com.willchun.library.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 *@author chunwang (wcly10@gmail.com)
 *@date 2013-12-25
 */
public class ManifestUtils {
    
    /**
     * 获取 AndroidManifest.xml里面 MetaData里面的值
     * @param context
     * @param keyName
     * @return
     */
    public static Object getMetaData(Context context, String keyName) {
        try {
            ApplicationInfo appi = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            return appi.metaData.get(keyName);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取版本名称 android:versionName
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取应用版本编号   android:versionCode
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
