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
import com.baidu.location.LocationClient;
import com.willchun.library.base.AndApplication;
import org.w3c.dom.Text;

/**
 *@author chunwang (wcly10@gmail.com)
 *@date 2013-12-26
 */
public class DemoApp extends AndApplication {

    public LocationClient mLocationClient = null;
    public BDLocationListener mDBdLocationListener = null;
    public TextView mLocationResult,logMsg;

    @Override
    public void onCreate() {
        super.onCreate();

        mLocationClient = new LocationClient(getApplicationContext());
        mDBdLocationListener = new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                {
                    //Receive Location
                    StringBuffer sb = new StringBuffer(256);
                    sb.append("\ntime : ");
                    sb.append(location.getTime());
                    sb.append("\nerror code : ");
                    sb.append(location.getLocType());
                    sb.append("\nlatitude : ");
                    sb.append(location.getLatitude());
                    sb.append("\nlontitude : ");
                    sb.append(location.getLongitude());
                    sb.append("\nradius : ");
                    sb.append(location.getRadius());
                    if (location.getLocType() == BDLocation.TypeGpsLocation){
                        sb.append("\nspeed : ");
                        sb.append(location.getSpeed());
                        sb.append("\nsatellite : ");
                        sb.append(location.getSatelliteNumber());
                        sb.append("\ndirection : ");
                        sb.append("\naddr : ");
                        sb.append(location.getAddrStr());
                        sb.append(location.getDirection());
                    } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
                        sb.append("\naddr : ");
                        sb.append(location.getAddrStr());
                        ////运营商信息
                        sb.append("\noperationers : ");
                        sb.append(location.getOperators());
                    }
                    logMsg(sb.toString());
                    Log.i("BaiduLocationApiDem", sb.toString());
                }
            }
        };
    }

    /**
     * 显示请求字符串
     * @param str
     */
    public void logMsg(String str) {
        try {
            if (mLocationResult != null)
                mLocationResult.setText(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
