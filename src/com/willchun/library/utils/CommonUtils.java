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
import android.net.Uri;

/**
 * 应用工具类
 *@author chunwang (wcly10@gmail.com)
 *@date 2013-10-28
 */
public class CommonUtils {

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
    

    
    /**
     * 跳转到游览器
     * @param url
     */
    public static void jump2Browser(Context context, String url) {
        Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(url));
        context.startActivity(viewIntent);
    }
 
}
