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

import android.util.Log;

/**    <uses-permission android:name="android.permission.INTERNET" />  
 * 网络通信相关工具类
 * 1)获得当前的IPv4地址     getLocalIpAddressV4
 * 
 * @author chunwang (wcly10@gmail.com)
 * @date 2013-12-12
 */
public class NetworkUtils {

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
            //暂时没有自己的日志内，暂时不显示
            Log.e("NetworkUtils", e.toString());
        }

        return null;
    }
}
