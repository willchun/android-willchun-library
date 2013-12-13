/**
 *
 * Copyright 2013 Anjuke. All rights reserved.
 * AppUtils.java
 *
 */
package com.willchun.library.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;

/**
 * 应用工具类
 *@author chunwang (wcly10@gmail.com)
 *@date 2013-10-28
 */
public class AppUtils {
    /**
     * 获取应用版本编号   android:versionCode
     * @return
     */
    public static int getVerCode(Context context){
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
    public static String getVerName(Context context){
        String verName = null;
        try {
            verName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return verName;
    }
    

    /**
     * 跳转到拨打电话页面
     * @param activity
     * @param phone 要拨打的电话号码
     */
    public static void makePhoneCall(Activity activity, String phone) {
        Uri uri = Uri.parse("tel:" + phone);
        Intent it = new Intent(Intent.ACTION_DIAL, uri);
        activity.startActivity(it);
    }
    
    /**
     * 跳转到发送短信页面
     * @param activity
     * @param phone 要发送的电话号码
     */
    public static void sendMessage(Activity activity, String phone){

      Uri uri = Uri.parse("smsto:"+phone);    
      Intent intent = new Intent(Intent.ACTION_SENDTO,uri);    
      activity.startActivity(intent);    
    }
    
    /**
     * 跳转到发送短信文本的页面
     * @param activity
     * @param text 要发送的文本信息
     */
    public static void sendMessageText(Activity activity, String text){
        Intent it = new Intent(Intent.ACTION_VIEW);     
        it.putExtra("sms_body", text);     
        it.setType("vnd.android-dir/mms-sms");     
        activity.startActivity(it);  
    }
    
    // 获取AppKey
    /**
     * 获取 AndroidManifest.xml里面 MetaData里面的值
     * @param context
     * @param metaKey
     * @return
     */
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }
    
    /**
     * 跳转到游览器
     * @param url
     */
    public static void jump2Browser(Context context, String url) {
        Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(url));
        context.startActivity(viewIntent);
    }
 
}
