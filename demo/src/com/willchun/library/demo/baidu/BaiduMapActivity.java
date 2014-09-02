package com.willchun.library.demo.baidu;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.baidu.location.BDGeofence;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.willchun.library.base.AndActivity;
import com.willchun.library.base.AndQuery;
import com.willchun.library.demo.R;
import com.willchun.library.platform.baidu.BaiduPlatformLocationAgent;


/**
 * Created by Administrator on 2014/9/1.
 */
public class BaiduMapActivity extends AndActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener{

    private MapView mMapView = null;
    //定义 BaiduMap 地图对象的操作方法与接口
    private BaiduMap mBaiduMap = null;
    //定位模式
    private MyLocationConfiguration.LocationMode mCurrentLocationMode;
    //当前地图的图片，自定义
    private BitmapDescriptor mCurrentMarker;

    private boolean isFirstLoc = true;

    private AndQuery aq;


    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_baidu_map);
        aq = new AndQuery(this);

        init();

        mMapView = (MapView) findViewById(R.id.bd_mapview);
        mBaiduMap = mMapView.getMap();

        //设置定位模式
        mCurrentLocationMode = MyLocationConfiguration.LocationMode.NORMAL;
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);


        initLocation();
    }

    private void init(){
        ((RadioButton) findViewById(R.id.type_map_normal_rb)).setOnCheckedChangeListener(this);
        ((RadioButton)findViewById(R.id.type_map_satellite_rb)).setOnCheckedChangeListener(this);
        aq.id(R.id.traffic_cb).getCheckBox().setOnCheckedChangeListener(this);
        aq.id(R.id.loc_mode_btn).clicked(this);
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType(BDGeofence.COORD_TYPE_BD09LL);//coorType - 取值有3个： 返回国测局经纬度坐标系：gcj02 返回百度墨卡托坐标系 ：bd09 返回百度经纬度坐标系 ：bd09ll
        option.setScanSpan(1000);//设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);//返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
        option.setOpenGps(true);
        BaiduPlatformLocationAgent.getInstance(aq.getContext()).setLocationClientOption(option);

        BaiduPlatformLocationAgent.getInstance(aq.getContext()).registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {

                if(mMapView == null || bdLocation == null)
                    return;
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(bdLocation.getRadius())
                                // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(bdLocation.getDirection()).latitude(bdLocation.getLatitude())
                        .longitude(bdLocation.getLongitude()).build();
                mBaiduMap.setMyLocationData(locData);

                if(isFirstLoc){
                    isFirstLoc = false;
                    LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                    MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                    mBaiduMap.animateMapStatus(u);

                }
            }
        });

        BaiduPlatformLocationAgent.getInstance(aq.getContext()).startLocation();


    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        BaiduPlatformLocationAgent.getInstance(aq.getContext()).stopLocation();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        super.onDestroy();

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            //开启 关闭交通图
            case R.id.traffic_cb:
                if(isChecked){
                    mBaiduMap.setTrafficEnabled(true);
                }else{
                    mBaiduMap.setTrafficEnabled(false);
                }
                break;
            case R.id.type_map_normal_rb:
                //普通地图
                if(isChecked)
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;
            case R.id.type_map_satellite_rb:
                //卫星地图
                if(isChecked)
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //定位模式相关
            case R.id.loc_mode_btn:
                switchLocMode();
                break;
        }
    }

    public void switchLocMode(){

        switch (mCurrentLocationMode){
            case NORMAL:
                aq.id(R.id.loc_mode_btn).text("跟随");
                mCurrentLocationMode = MyLocationConfiguration.LocationMode.FOLLOWING;
                mBaiduMap
                        .setMyLocationConfigeration(new MyLocationConfiguration(
                                mCurrentLocationMode, true, mCurrentMarker));

                break;
            case FOLLOWING:
                aq.id(R.id.loc_mode_btn).text("罗盘");
                mCurrentLocationMode = MyLocationConfiguration.LocationMode.COMPASS;
                mBaiduMap
                        .setMyLocationConfigeration(new MyLocationConfiguration(
                                mCurrentLocationMode, true, mCurrentMarker));

                break;
            case COMPASS:
                aq.id(R.id.loc_mode_btn).text("普通");
                mCurrentLocationMode = MyLocationConfiguration.LocationMode.NORMAL;
                mBaiduMap
                        .setMyLocationConfigeration(new MyLocationConfiguration(
                                mCurrentLocationMode, true, mCurrentMarker));
                break;
        }
    }
}
