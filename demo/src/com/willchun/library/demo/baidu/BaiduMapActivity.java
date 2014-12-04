package com.willchun.library.demo.baidu;

import android.graphics.Color;
import android.graphics.Point;
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
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.*;
import com.baidu.mapapi.search.route.*;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.willchun.library.base.AndActivity;
import com.willchun.library.base.AndQuery;
import com.willchun.library.demo.R;
import com.willchun.library.platform.baidu.BaiduPlatformLocationAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by Administrator on 2014/9/1.
 */
public class BaiduMapActivity extends AndActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, OnGetPoiSearchResultListener, OnGetSuggestionResultListener
                                                            , OnGetRoutePlanResultListener{

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

    private HeatMap mheatMap;
    //Poi图标搜索
    private PoiSearch mPoiSearch;
    //搜索模块
    private RoutePlanSearch mRoutePlanSearch;
    //路线
    RouteLine mRouteLine = null;
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
        // 关闭俯瞰图
        UiSettings uiS = mBaiduMap.getUiSettings();
        uiS.setOverlookingGesturesEnabled(false);



        //初始化搜索模块 注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        //初始化搜索模块 注册搜索监听
        mRoutePlanSearch = RoutePlanSearch.newInstance();
        mRoutePlanSearch.setOnGetRoutePlanResultListener(this);

        initLocation();
        initMarker();
    }

    private void init(){
        ((RadioButton) findViewById(R.id.type_map_normal_rb)).setOnCheckedChangeListener(this);
        ((RadioButton)findViewById(R.id.type_map_satellite_rb)).setOnCheckedChangeListener(this);
        aq.id(R.id.traffic_cb).getCheckBox().setOnCheckedChangeListener(this);
        aq.id(R.id.custom_loc_icon_cb).getCheckBox().setOnCheckedChangeListener(this);
        aq.id(R.id.op_marker_cb).getCheckBox().setOnCheckedChangeListener(this);
        aq.id(R.id.op_other_btn).getButton().setOnClickListener(this);
        aq.id(R.id.op_clear_btn).getButton().setOnClickListener(this);
        aq.id(R.id.loc_mode_btn).clicked(this);
        aq.id(R.id.bounds).clicked(this);
        aq.id(R.id.hot).clicked(this);
        aq.id(R.id.clean_hot).clicked(this);
        aq.id(R.id.search_shop).clicked(this);


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


    private void initMarker() {
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                if("覆盖物A".equals(marker.getTitle())){
                    return true;
                }
                //创建InfoWindow展示的view
                Button button = new Button(getApplicationContext());
                button.setBackgroundResource(R.drawable.map_popup);
                //定义用于显示该InfoWindow的坐标点
                final LatLng ll = marker.getPosition();
                //将地理坐标转换为屏幕坐标
                Point p = mBaiduMap.getProjection().toScreenLocation(ll);
                p.y -= 50;//y 轴偏移量
                LatLng infoLL = mBaiduMap.getProjection().fromScreenLocation(p);
                InfoWindow.OnInfoWindowClickListener listener = null;
                 if("覆盖物B".equals(marker.getTitle())){
                    button.setText("位置更改");
                    listener = new InfoWindow.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick() {
                            LatLng newLL = new LatLng(ll.latitude + 0.05, ll.longitude + 0.05);
                            marker.setPosition(newLL);
                            mBaiduMap.hideInfoWindow();
                        }
                    };

                }else if("覆盖物C".equals(marker.getTitle())){
                    //
                    button.setText("图标修改");
                    listener = new InfoWindow.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick() {
                            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding));
                            mBaiduMap.hideInfoWindow();
                        }
                    };
                }else if("覆盖物D".equals(marker.getTitle())){
                    //
                    button.setText("消失");
                    listener = new InfoWindow.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick() {
                            marker.remove();
                            mMarkers.remove(marker);
                            mBaiduMap.hideInfoWindow();
                        }
                    };
                }
                //创建InfoWindow , 传入 view， 地理坐标
                InfoWindow infoWindow = new InfoWindow(button , infoLL, 50);
                mBaiduMap.showInfoWindow(infoWindow);
                return true;
            }
        });

        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            public void onMarkerDrag(Marker marker) {
                //拖拽中
                Log.i("marker", "onMarkerDrag");

            }
            public void onMarkerDragEnd(Marker marker) {
                //拖拽结束
                Log.i("marker", "onMarkerDragEnd");
                Toast.makeText(
                        BaiduMapActivity.this,
                        "拖拽结束，新位置：" + marker.getPosition().latitude + ", "
                                + marker.getPosition().longitude,
                        Toast.LENGTH_LONG).show();
            }
            public void onMarkerDragStart(Marker marker) {
                //开始拖拽
                Log.i("marker", "onMarkerDragStart");
            }
        });

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
        mRoutePlanSearch.destroy();
        mPoiSearch.destroy();
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
            //覆盖物 - 标记类 -
            case R.id.op_marker_cb:
                if(isChecked){
                    double offer = 0.05;
                    MyLocationData myData = mBaiduMap.getLocationData();
                    BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
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
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_markb))//覆盖物图片
                            .title("覆盖物B")//覆盖物标题
                            .zIndex(9)//覆盖物层级
                            .draggable(false);//设置覆盖物是否能拖拽

                    Marker mMulchMarker2 = (Marker)(mBaiduMap.addOverlay(oo2));
                    mMarkers.add(mMulchMarker2);

                    OverlayOptions oo3 = new MarkerOptions()
                            .position(new LatLng(myData.latitude - offer, myData.longitude - offer))//经纬度信息
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_markc))//覆盖物图片
                            .title("覆盖物C")//覆盖物标题
                            .zIndex(9)//覆盖物层级
                            .draggable(false);//设置覆盖物是否能拖拽

                    Marker mMulchMarker3 = (Marker)(mBaiduMap.addOverlay(oo3));
                    mMarkers.add(mMulchMarker3);

                    OverlayOptions oo4 = new MarkerOptions()
                            .position(new LatLng(myData.latitude + offer, myData.longitude - offer))//经纬度信息
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_markd))//覆盖物图片
                            .title("覆盖物D")//覆盖物标题
                            .zIndex(9)//覆盖物层级
                            .draggable(false);//设置覆盖物是否能拖拽

                    Marker mMulchMarker4 = (Marker)(mBaiduMap.addOverlay(oo4));
                    mMarkers.add(mMulchMarker4);

                }else{

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
            case R.id.op_other_btn:
                    MyLocationData data = mBaiduMap.getLocationData();
                    //--- TextOptions --
                    Point pointTo = new Point();
                    pointTo.x = 240;
                    pointTo.y = 300;

                    LatLng ll1 = mBaiduMap.getProjection().fromScreenLocation(pointTo);

                    OverlayOptions to = new TextOptions()
                            .bgColor(0xaaffff00)//背景
                            .fontColor(0xFFFF00FF)
                            .fontSize(24)//字体大小
                            .rotate(20)//文字覆盖物旋转角度，逆时针
                            .position(ll1)
                            .text("晴天小猪");
                    mBaiduMap.addOverlay(to);


                    double offer = 0.05;
                    MyLocationData myData = mBaiduMap.getLocationData();
                    LatLng latLng1 = new LatLng(myData.latitude, myData.longitude - offer*3);
                    LatLng latLng2 = new LatLng(myData.latitude + offer, myData.longitude - offer*2);
                    LatLng latLng3 = new LatLng(myData.latitude + offer*2, myData.longitude - offer*3);

                    List<LatLng> points = new ArrayList<LatLng>();
                    points.add(latLng1);
                    points.add(latLng2);
                    points.add(latLng3);

                    //折线
                    OverlayOptions plo = new PolylineOptions().points(points);
                    mBaiduMap.addOverlay(plo);
                    //弧线
                    OverlayOptions ao = new ArcOptions().points(latLng1, latLng2, latLng3);
                    mBaiduMap.addOverlay(ao);
                    //
                    // 添加圆
                    LatLng llCircle = new LatLng(myData.latitude + offer*2, myData.longitude + offer*3);
                    OverlayOptions ooCircle = new CircleOptions().fillColor(0x000000FF)
                        .center(llCircle).stroke(new Stroke(5, 0xAA000000))
                        .radius(1400);
                    mBaiduMap.addOverlay(ooCircle);
                    //添加点
                    LatLng llDot = new LatLng(myData.latitude, myData.longitude + offer);
                    OverlayOptions ooDot = new DotOptions().center(llDot).radius(6)
                        .color(0xFF0000FF);
                    mBaiduMap.addOverlay(ooDot);
                    //多边形
                    LatLng po1 = new LatLng(myData.latitude, myData.longitude + offer*2);
                    LatLng po2 = new LatLng(myData.latitude, myData.longitude + offer*3);
                    LatLng po3 = new LatLng(myData.latitude - offer, myData.longitude + offer*3);
                    LatLng po4 = new LatLng(myData.latitude - offer, myData.longitude + offer*2);
                    List<LatLng> pos = new ArrayList<LatLng>();
                    pos.add(po1);
                    pos.add(po2);
                    pos.add(po3);
                    pos.add(po4);
                    OverlayOptions po = new PolygonOptions().points(pos).stroke(new Stroke(5, 0xAA00FF00)).fillColor(0xAAFFFF00);;
                    mBaiduMap.addOverlay(po);

                break;
            case R.id.op_clear_btn:
                mBaiduMap.clear();
                break;
            case R.id.bounds:
                MyLocationData bdData = mBaiduMap.getLocationData();
                float offer2 = 0.03f;
                LatLng sw = new LatLng(bdData.latitude + offer2, bdData.longitude + offer2);
                LatLng ne = new LatLng(bdData.latitude - offer2, bdData.longitude - offer2);
                LatLngBounds bounds = new LatLngBounds.Builder()
                        .include(sw)
                        .include(ne)
                        .build();
                //定义ground显示的图片
                BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.ground_overlay);
                //定于Ground覆盖选项
                OverlayOptions oo = new GroundOverlayOptions().positionFromBounds(bounds).image(bd).transparency(0.75f);
                mBaiduMap.addOverlay(oo);
                break;
            case R.id.hot:
                MyLocationData hotData = mBaiduMap.getLocationData();
                /* 第一步、设置颜色变化*/
                //设置渐变颜色值
                int[] COLORS = {Color.rgb(102, 255, 0), Color.rgb(255, 0, 0)};
                //设置渐变颜色起始值
                float[] POINTS = {0.2f, 1.0f};
                //构造颜色渐变对象
                Gradient gradient = new Gradient(COLORS, POINTS);
                /* 第二步、准备数据*/
                List<LatLng> randomList = new ArrayList<LatLng>();
                Random r = new Random();
                for(int i=0; i<500; i++){
                    int rlat =r.nextInt(370000);
                    int rlng = r.nextInt(370000);
                    double lat = hotData.latitude + rlat/1e6;
                    double lng = hotData.longitude + rlng/1e6;
                    LatLng ll = new LatLng(lat, lng);
                    randomList.add(ll);
                }
                /* 第三步、添加显示热力图*/
                mheatMap = new HeatMap.Builder().data(randomList).gradient(gradient).build();
                mBaiduMap.addHeatMap(mheatMap);
                break;
            case R.id.clean_hot:
                if(mheatMap != null)
                    mheatMap.removeHeatMap();
                break;
            case R.id.search_shop:
                PoiNearbySearchOption pso = new PoiNearbySearchOption();
                if(null != mBaiduMap.getLocationData())
                pso.location(new LatLng(mBaiduMap.getLocationData().latitude, mBaiduMap.getLocationData().longitude));
                pso.radius(1500);
                pso.keyword("厕所");
                mPoiSearch.searchNearby(pso);
                break;
        }
    }

    /**
     * 切换定位模式
     */
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

    @Override
    public void onGetPoiResult(PoiResult result) {
        if (result == null
                || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            mBaiduMap.clear();
            PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result);
            overlay.addToMap();
            overlay.zoomToSpan();
            return;
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            String strInfo = "在";
            for (CityInfo cityInfo : result.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }
            strInfo += "找到结果";
            Toast.makeText(this, strInfo, Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        //建议查询请求结果
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult result) {
        //步行
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            mRouteLine = result.getRouteLines().get(0);
            WalkingRouteOverlay wro = new WalkingRouteOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(wro);
            wro.setData(result.getRouteLines().get(0));
            wro.addToMap();
            wro.zoomToSpan();
        }
    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult result) {
        //换乘路线
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            mRouteLine = result.getRouteLines().get(0);
            TransitRouteOverlay tro = new TransitRouteOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(tro);
            tro.setData(result.getRouteLines().get(0));
            tro.addToMap();
            tro.zoomToSpan();
        }
    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
        //驾车路线结果回调
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            mRouteLine = result.getRouteLines().get(0);
            DrivingRouteOverlay dro = new DrivingRouteOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(dro);
            dro.setData(result.getRouteLines().get(0));
            dro.addToMap();
            dro.zoomToSpan();
        }
    }

    private class MyPoiOverlay extends PoiOverlay{

        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int i) {
            super.onPoiClick(i);
            return true;
        }
    }


    /**
     * 搜索的监听
     * @param v
     */
    public void searchButtonProcess(View v){
        //重置数据节点
        mRouteLine = null;
        mBaiduMap.clear();
        //搜索响应
        PlanNode stNote = PlanNode.withCityNameAndPlaceName("上海", aq.id(R.id.route_start_et).getEditText().getText().toString());
        PlanNode enNote = PlanNode.withCityNameAndPlaceName("上海",aq.id(R.id.route_end_et).getEditText().getText().toString());

        if(v.getId() == R.id.route_jc_btn){
            mRoutePlanSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNote).to(enNote));
        }else if(v.getId() == R.id.route_gj_btn){
            mRoutePlanSearch.transitSearch((new TransitRoutePlanOption()).from(stNote).to(enNote).city("上海"));
        }else if(v.getId() == R.id.route_bx_btn){
            mRoutePlanSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNote).to(enNote));
        }
    }
}
