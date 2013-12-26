/**
 *
 * Copyright 2013 Anjuke. All rights reserved.
 * NetworkUtils.java
 *
 */
package com.willchun.library.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.http.conn.util.InetAddressUtils;

import com.androidquery.util.AQUtility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

/**    <uses-permission android:name="android.permission.INTERNET" />  
 *     <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 * 网络通信相关工具类
 * 1)获得当前的IPv4地址     getLocalIpAddressV4
 * 
 * @author chunwang (wcly10@gmail.com)
 * @date 2013-12-12
 */
public class NetworkUtils {
    public static final int NETWORK_STATUS_NONE = 0;
    public static final int NETWORK_STATUS_WIFI = 1;
    public static final int NETWORK_STATUS_MOBILE = 2;
    
    /**
     * 获得当前的IPv4地址 
     * @return
     */
    public static String getLocalIpAddressV4() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    // 在4.0下会出现类似fe80::b607:f9ff:fee5:487e的IP地址，
                    // 这个是IPV6的地址，我们需要获得是的IPV4的地址，所以要在上诉代码中加一个判断
                    // 所以增加了InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())
                    if (!inetAddress.isLoopbackAddress()
                            && InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
            LogUtils.e(e.toString());
        }

        return null;
    }
    
    /**
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /> 
     * 获取当前的网络状态   如果不是wifi或mobile 代表无网络 .   判断依据是当前网络正在连接，或者已连接
     * @param context
     * @return 参考NetworkUtils.NETWORK_STATUS_NONE NetworkUtils.NETWORK_STATUS_WIFI  NetworkUtils.NETWORK_STATUS_MOBILE
     */
    public static int getLocalNetworkState(Context context){
        ConnectivityManager con = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting()){
            return NETWORK_STATUS_WIFI;
        }else if(con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting()){
            return NETWORK_STATUS_MOBILE;
        }else {
            return NETWORK_STATUS_NONE;
        }
    }
    
}
