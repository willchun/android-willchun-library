/**
 *
 * Copyright 2013 Anjuke. All rights reserved.
 * AndApplication.java
 *
 */
package com.willchun.library.base;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.widget.Toast;

import com.androidquery.callback.BitmapAjaxCallback;
import com.androidquery.util.AQUtility;
import com.umeng.analytics.MobclickAgent;
import com.willchun.library.utils.LogUtils;
import com.willchun.library.utils.ManifestUtils;

/**
 * 
 *@author willchun (wcly10@gmail.com)
 *@date 2013-5-21
 */
public class AndApplication extends Application {
    private boolean debug;
    private AndQuery andQuery;
    
    public AndQuery getAndQuery(){
        if(andQuery==null)
            andQuery = new AndQuery(this);
        return andQuery;
    }
    
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        debug = isDebugMode();
        AQUtility.setContext(this);
        AQUtility.setDebug(debug);
        LogUtils.setDebug(debug);
        //设置友盟debug
        MobclickAgent.setDebugMode(debug);
        LogUtils.d("UMENG_CHANNEL:" + ManifestUtils.getMetaData(this, "UMENG_CHANNEL") + "-isDebug:" + isDebug()
                + "-versionName:" + ManifestUtils.getVersionName(this) + "-versionCode:"
                + ManifestUtils.getVersionCode(this));
    }

    public void showShortToast(int msgId) {
        showShortToast(getString(msgId));
    }

    public void showShortToast(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void showLongToast(int msgId) {
        showLongToast(getString(msgId));
    }

    public void showLongToast(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
    }
    
    /**
     * 是否是debug模式
     * @return
     */
    public boolean isDebug() {
        return debug;
    }
    
    /**
     * 判断应用是不是debug模式
     * @return
     */
    public boolean isDebugMode() {
        try {
            PackageManager pm = getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
            return ((pi.applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        super.onLowMemory();
        BitmapAjaxCallback.clearCache();
    }
    
    
}
