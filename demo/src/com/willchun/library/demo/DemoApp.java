/**
 *
 * Copyright 2013 Anjuke. All rights reserved.
 * DemoApp.java
 *
 */
package com.willchun.library.demo;

import android.util.Log;
import android.widget.TextView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.willchun.library.base.AndApplication;
import com.willchun.library.platform.baidu.BaiduPlatformLocationAgent;
import org.w3c.dom.Text;

/**
 *@author chunwang (wcly10@gmail.com)
 *@date 2013-12-26
 */
public class DemoApp extends AndApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        BaiduPlatformLocationAgent.getInstance(this);

    }



}
