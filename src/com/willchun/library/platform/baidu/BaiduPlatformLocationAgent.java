package com.willchun.library.platform.baidu;

import android.content.Context;
import com.baidu.location.*;

import java.util.List;

/**
 *
 * 百度定位相关代理类
 *
 *
 <!-- 这个权限用于进行网络定位-->
 <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"></uses-permission>
 <!-- 这个权限用于访问GPS定位-->
 <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"></uses-permission>
 <!-- 用于访问wifi网络信息，wifi信息会用于进行网络定位-->
 <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"></uses-permission>
 <!-- 获取运营商信息，用于支持提供运营商信息相关的接口-->
 <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"></uses-permission>
 <!-- 这个权限用于获取wifi的获取权限，wifi信息会用来进行网络定位-->
 <uses-permission android:name="android.permission.CHANGE_WIFI_STATE"></uses-permission>
 <!-- 用于读取手机当前的状态-->
 <uses-permission android:name="android.permission.READ_PHONE_STATE"></uses-permission>
 <!-- 写入扩展存储，向扩展卡写入数据，用于写入离线定位数据-->
 <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"></uses-permission>
 <!-- 访问网络，网络定位需要上网-->
 <uses-permission android:name="android.permission.INTERNET" />
 <!—SD卡读取权限，用户写入离线定位数据-->
 <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"></uses-permission>
 <!--允许应用读取低级别的系统日志文件 -->
 <uses-permission android:name="android.permission.READ_LOGS"></uses-permission>
 *
 * Created by Administrator on 2014/8/28.
 */
public class BaiduPlatformLocationAgent {

     private static BaiduPlatformLocationAgent _intance;
     private static String lock = new String();

     private LocationClient mLocationClient = null;//定位服务的客户端。宿主程序在客户端声明此类，并调用
     private GeofenceClient mGeofenceClient = null;//低功耗地理围栏类 (此功能在3.1.0上有bug)
     private LocationClientOption mLocationClientOption = null;//定位服务的配置信息


    /**
     * Baidu定位代理类
     *
     * 必须在主线程中声明
     * @param appContext Context需要时全进程有效的context,推荐用getApplicationConext获取全进程有效的context
     * @return
     */
      public static BaiduPlatformLocationAgent getInstance(Context appContext){
        if(_intance == null){
            synchronized (lock){
                if(_intance == null){
                    _intance = new BaiduPlatformLocationAgent(appContext);
                }
            }
        }
         return _intance;
     }


    private BaiduPlatformLocationAgent(Context appContext){
        mLocationClient = new LocationClient(appContext);
        mGeofenceClient = new GeofenceClient(appContext);
    }


    /**
     * 设置定位服务属性
     option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
     option.setCoorType(BDGeofence.COORD_TYPE_BD09LL);//coorType - 取值有3个： 返回国测局经纬度坐标系：gcj02 返回百度墨卡托坐标系 ：bd09 返回百度经纬度坐标系 ：bd09ll
     option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
     option.setIsNeedAddress(true);//返回的定位结果包含地址信息
     option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
     option.setOpenGps(true);
     * @param option
     */
    public void setLocationClientOption(LocationClientOption option){
        if(option != null){
            mLocationClientOption = option;
            mLocationClient.setLocOption(mLocationClientOption);
        }
    }

    /**
     * 注册定位监听类
     * @param listener
     */
    public void registerLocationListener(BDLocationListener listener){
        if(listener != null){
            mLocationClient.registerLocationListener(listener);
        }
    }

    /**
     * 进入围栏的回调类
     * @param listener
     */
    public void registerGeofenceTriggerListener(GeofenceClient.OnGeofenceTriggerListener listener){
        if(listener != null)
            mGeofenceClient.registerGeofenceTriggerListener(listener);
    }

    /**
     * 添加围栏的回调类
     * @param bdGeofence
     * @param listener
     */
    public void addBDGeofence(BDGeofence bdGeofence, GeofenceClient.OnAddBDGeofencesResultListener listener){
         mGeofenceClient.addBDGeofence(bdGeofence,listener);
    }

    /**
     * 删除围栏的回调类
     * @param l
     * @param listener
     */
    public void removeBDGeofences(List l, GeofenceClient.OnRemoveBDGeofencesResultListener listener){
        mGeofenceClient.removeBDGeofences(l, listener);
    }


    /**
     * 开启定位服务
     */
    public void startLocation(){
        mLocationClient.start();
    }

    /**
     * 停止定位服务
     */
    public void stopLocation(){
        mLocationClient.stop();
    }

    /**
     * 开启围栏
     */
    public void startGeoFence(){
        mGeofenceClient.start();
    }

    /**
     * 暂停围栏
     */
    public void stopGeoFence(){
        mGeofenceClient.stop();
    }

    /**
     * 注册位置提醒监听
     *
      mNotifyListener.SetNotifyLocation(((DemoApp)app()).mBDBdLocation.getLatitude(),
     ((DemoApp)app()).mBDBdLocation.getLongitude(), 5000, BDGeofence.COORD_TYPE_BD09LL);//4个参数代表要位置提醒的点的坐标，具体含义依次为：纬度，经度，距离范围，坐标系类型(gcj02,gps,bd09,bd09ll)
     * @param listener
     */
    public void registerNotify(BDNotifyListener listener){
        mLocationClient.registerNotify(listener);
    }

    /**
     * 取消注册的位置提醒监听
     * @param listener
     */
    public void removeNotifyEvent(BDNotifyListener listener){
        mLocationClient.removeNotifyEvent(listener);
    }

}

