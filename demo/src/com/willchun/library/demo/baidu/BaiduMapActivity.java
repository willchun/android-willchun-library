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

import java.util.ArrayList;


/**
 * Created by Administrator on 2014/9/1.
 */
public class BaiduMapActivity extends AndActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener{

    private MapView mMapView = null;
    //定义 BaiduMap 地图对象的操作方法与接口
    private BaiduMap mBaiduMap = null;
    //定位模式
    private MyLocationConfiguration.LocationMode mCurrentLocationMode;


    //当前定位点的自定义图片
    private BitmapDescriptor mCustomLocBitmap;
    //当前地图的定位图片
    private BitmapDescriptor mCurrentMarker;
    //覆盖物组
    private ArrayList<Marker> mMarkers = new ArrayList<Marker>();

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

        //


        initLocation();
        initMulch();
    }

    private void init(){
        ((RadioButton) findViewById(R.id.type_map_normal_rb)).setOnCheckedChangeListener(this);
        ((RadioButton)findViewById(R.id.type_map_satellite_rb)).setOnCheckedChangeListener(this);
        aq.id(R.id.traffic_cb).getCheckBox().setOnCheckedChangeListener(this);
        aq.id(R.id.custom_loc_icon_cb).getCheckBox().setOnCheckedChangeListener(this);
        aq.id(R.id.mulch_cb).getCheckBox().setOnCheckedChangeListener(this);
        aq.id(R.id.loc_mode_btn).clicked(this);

        //自定义的定位点图片
        mCustomLocBitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);

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


    private void initMulch() {


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
        mMapView = null;
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
                if(isChecked)
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
            //自定义定位图标
            case R.id.custom_loc_icon_cb:
                if(isChecked){
                    mCurrentMarker = mCustomLocBitmap;
                }else{
                    mCurrentMarker = null;
                }
                mBaiduMap
                        .setMyLocationConfigeration(new MyLocationConfiguration(
                                mCurrentLocationMode, true, mCurrentMarker));
                break;
            //覆盖物
            case R.id.mulch_cb:
                if(isChecked){
                    double offer = 0.05;
                    MyLocationData myData = mBaiduMap.getLocationData();
                    BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.map_mulch);
                    OverlayOptions oo1 = new MarkerOptions()
                            .position(new LatLng(myData.latitude + offer, myData.longitude + offer))//经纬度信息
                            .icon(bd)//覆盖物图片
                            .title("覆盖物A")//覆盖物标题
                            .zIndex(9)//覆盖物层级
                            .draggable(true);//设置覆盖物是否能拖拽

                    Marker mMulchMarker1 = (Marker)(mBaiduMap.addOverlay(oo1));
                    mMarkers.add(mMulchMarker1);

                    OverlayOptions oo2 = new MarkerOptions()
                            .position(new LatLng(myData.latitude - offer, myData.longitude + offer))//经纬度信息
                            .icon(bd)//覆盖物图片
                            .title("覆盖物A")//覆盖物标题
                            .zIndex(9)//覆盖物层级
                            .draggable(true);//设置覆盖物是否能拖拽

                    Marker mMulchMarker2 = (Marker)(mBaiduMap.addOverlay(oo2));
                    mMarkers.add(mMulchMarker2);

                    OverlayOptions oo3 = new MarkerOptions()
                            .position(new LatLng(myData.latitude - offer, myData.longitude - offer))//经纬度信息
                            .icon(bd)//覆盖物图片
                            .title("覆盖物A")//覆盖物标题
                            .zIndex(9)//覆盖物层级
                            .draggable(true);//设置覆盖物是否能拖拽

                    Marker mMulchMarker3 = (Marker)(mBaiduMap.addOverlay(oo3));
                    mMarkers.add(mMulchMarker3);

                    mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
                        public void onMarkerDrag(Marker marker) {
                            //拖拽中
                            Log.i("mulch", "onMarkerDrag");
                        }
                        public void onMarkerDragEnd(Marker marker) {
                            //拖拽结束
                            Log.i("mulch", "onMarkerDragEnd");
                        }
                        public void onMarkerDragStart(Marker marker) {
                            //开始拖拽
                            Log.i("mulch", "onMarkerDragStart");
                        }
                    });


                }else{
                    /*if(mMulchMarker != null){
                        //调用Marker对象的remove方法实现指定marker的删除
                        mMulchMarker.remove();
                    }

                    mMulchMarker = null;*/

                    for(Marker marker : mMarkers){
                        marker.remove();
                    }

                    mMarkers.clear();
                }
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
