/**
 *
 * Copyright 2013 Anjuke. All rights reserved.
 * AndApplication.java
 *
 */
package com.willchun.library.base;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.widget.Toast;

import com.androidquery.util.AQUtility;

/**
 *@author willchun (wcly10@gmail.com)
 *@date 2013-5-21
 */
public class AndApplication extends Application {
    private boolean isDebug = true;
    
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        AQUtility.setContext(this);
        AQUtility.setDebug(isDebug);
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
    
    public void setDebug(boolean flag){
        this.isDebug = flag;
        AQUtility.setDebug(isDebug);
    }

    public boolean isDebug() {
        return isDebug;
    }
}
