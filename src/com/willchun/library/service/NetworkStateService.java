/**
 *
 * Copyright 2013 Anjuke. All rights reserved.
 * NetworkStateService.java
 *
 */
package com.willchun.library.service;

import com.willchun.library.utils.LogUtils;
import com.willchun.library.utils.NetworkUtils;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * 
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 * 网络状态改变监测的服务
 *@author chunwang (wcly10@gmail.com)
 *@date 2013-12-26
 */
public class NetworkStateService extends Service {
    
    private BroadcastReceiver mReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if(action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){
                int stauts = NetworkUtils.getLocalNetworkState(getApplicationContext());
                switch (stauts) {
                case NetworkUtils.NETWORK_STATUS_NONE:
                    LogUtils.d("当前网络名称:无");
                    //Toast.makeText(context, "当前网络名称:无", 0).show();
                    break;
                case NetworkUtils.NETWORK_STATUS_MOBILE:
                    LogUtils.d("当前网络名称:mobile");
                    //Toast.makeText(context, "当前网络名称:mobile", 0).show();
                    break;
                case NetworkUtils.NETWORK_STATUS_WIFI:
                    LogUtils.d("当前网络名称:wifi");
                    //Toast.makeText(context, "当前网络名称:wifi", 0).show();
                    break;
                default:
                    break;
                }
            }
        }
        
    };
    
    

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, filter);
        LogUtils.d("当前网络名称:开启监听网络");
    }

    

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(mReceiver);
        LogUtils.d("当前网络名称:取消监听网络");
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

}
