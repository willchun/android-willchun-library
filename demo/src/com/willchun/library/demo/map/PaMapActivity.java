package com.willchun.library.demo.map;/**
 * Created by Administrator on 2014/12/1.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.baidu.location.*;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.*;
import com.willchun.library.demo.R;
import com.willchun.library.view.IconFontTextView;


/**
 * @author willchun (277143980@qq.com)
 * @github https://github.com/willchun
 * @date 2014/12/1
 */
public class PaMapActivity extends Activity implements View.OnClickListener, OnGetPoiSearchResultListener{

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private BitmapDescriptor mMainBitmap;

    private LinearLayout mTraffic_POI;
    private LinearLayout mSchool_POI;
    private LinearLayout mOther_POI;
    private LinearLayout mLife_POI;


    private boolean isTraffic = false;
    private boolean isSchool = false;
    private boolean isOther = false;
    private boolean isLife = false;

    private int              mSelectColor     = Color.rgb(33, 99, 00);
    private int              mNormalColor     = Color.rgb(66, 66, 66);

    private PoiSearch mPoiSearch;

    private final int POI_SEARCH_TYPE_TRAFFIC = 0x01;
    private final int POI_SEARCH_TYPE_SCHOOL = 0x02;
    private final int POI_SEARCH_TYPE_OTHER = 0x08;
    private final int POI_SEARCH_TYPE_LIFE = 0x10;
    private int mCurPoiSearchType;

    private Marker   mMainMarker;
    private MyMarker mTrafficMarker;
    private MyMarker mSchoolMarker;
    private MyMarker mOtherMarker;
    private MyMarker mLifeMarker;

    private LocationClient mLocationClient;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_pa_map);

        mMapView = (MapView)findViewById(R.id.pa_mapview);
        mBaiduMap = mMapView.getMap();

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        //关闭俯瞰图手势
        mBaiduMap.getUiSettings().setOverlookingGesturesEnabled(false);
        //开启定位图层
        initLocation();

        initMainMarker("万年的板蓝根", new LatLng(31.22, 121.48));
        initPoiMarker();

    }


    /**
     * 增加主要标注的图标
     */
    public void initMainMarker(String mainTitle, LatLng mainLatLng){

        mMainBitmap = BitmapDescriptorFactory.fromResource(R.drawable.lib_map_main);
        OverlayOptions mainOO = new MarkerOptions().icon(mMainBitmap).title(mainTitle).position(mainLatLng);
        mMainMarker = (Marker)mBaiduMap.addOverlay(mainOO);
        //3-19
        MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder(mBaiduMap.getMapStatus()).zoom(16.0f).target(mainLatLng).build());
        mBaiduMap.animateMapStatus(update, 300);

        showMyInfoWindow(mainTitle, mainLatLng, R.drawable.lib_map_main);
    }

    /**
     * 初始化Poi标签
     */
    public void initPoiMarker(){
        //搜索模块相关实例化
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);

        mTrafficMarker = new MyMarker(mBaiduMap, R.drawable.lib_map_traffic);
        mSchoolMarker = new MyMarker(mBaiduMap, R.drawable.lib_map_school);
        mOtherMarker = new MyMarker(mBaiduMap, R.drawable.lib_map_other);
        mLifeMarker = new MyMarker(mBaiduMap, R.drawable.lib_map_life);

        mTraffic_POI = getPoifindLL(R.id.pa_map_traffic_ng);
        mSchool_POI = getPoifindLL(R.id.pa_map_school_ng);
        mOther_POI = getPoifindLL(R.id.pa_map_other_ng);
        mLife_POI = getPoifindLL(R.id.pa_map_life_ng);

        mTraffic_POI.setOnClickListener(this);
        mSchool_POI.setOnClickListener(this);
        mOther_POI.setOnClickListener(this);
        mLife_POI.setOnClickListener(this);

        getPoifindIconFont(mTraffic_POI, R.id.map_detail_condition_ic).textCode(0xe644);
        getPoifindTextView(mTraffic_POI, R.id.map_detail_condition_tv).setText("交通");
        getPoifindIconFont(mSchool_POI, R.id.map_detail_condition_ic).textCode(0xe647);
        getPoifindTextView(mSchool_POI, R.id.map_detail_condition_tv).setText("学校");
        getPoifindIconFont(mOther_POI, R.id.map_detail_condition_ic).textCode(0xe645);
        getPoifindTextView(mOther_POI, R.id.map_detail_condition_tv).setText("楼盘");
        getPoifindIconFont(mLife_POI, R.id.map_detail_condition_ic).textCode(0xe646);
        getPoifindTextView(mLife_POI, R.id.map_detail_condition_tv).setText("生活");

        changeIconAndColor(mTraffic_POI, false);
        changeIconAndColor(mSchool_POI, false);
        changeIconAndColor(mOther_POI, false);
        changeIconAndColor(mLife_POI, false);


        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                checkOnMarkerClick(marker);
                return true;
            }
        });

    }

    /**
     * 开启定位图层
     */
    public void initLocation(){
        //定位图层是否显示
        //mBaiduMap.setMyLocationEnabled(true);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType(BDGeofence.COORD_TYPE_BD09LL);//coorType - 取值有3个： 返回国测局经纬度坐标系：gcj02 返回百度墨卡托坐标系 ：bd09 返回百度经纬度坐标系 ：bd09ll
        option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
        //option.setIsNeedAddress(true);//返回的定位结果包含地址信息
        //option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
        option.setOpenGps(true);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(new BDLocationListener() {
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

                /*LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);*/

            }
        });
        mLocationClient.start();

    }


    private LinearLayout getPoifindLL(int id){
        return (LinearLayout)findViewById(id);
    }

    private IconFontTextView getPoifindIconFont(View root, int id){
        return (IconFontTextView)root.findViewById(id);
    }

    private TextView getPoifindTextView(View root, int id){
        return (TextView)root.findViewById(id);
    }

    private void changeIconAndColor(View root, boolean isSelected){
        if(isSelected){
            getPoifindIconFont(root, R.id.map_detail_condition_ic).setTextColor(mSelectColor);
            getPoifindTextView(root, R.id.map_detail_condition_tv).setTextColor(mSelectColor);
        }else{
            getPoifindIconFont(root, R.id.map_detail_condition_ic).setTextColor(mNormalColor);
            getPoifindTextView(root, R.id.map_detail_condition_tv).setTextColor(mNormalColor);
        }
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
        if(mMainBitmap != null){
            mMainBitmap.recycle();
            mMainBitmap = null;
        }
        mTrafficMarker.onDestroy();
        mSchoolMarker.onDestroy();
        mLifeMarker.onDestroy();
        mOtherMarker.onDestroy();

        mMainMarker = null;
        mPoiSearch.destroy();
        mPoiSearch = null;
        mMapView.onDestroy();
        mMapView = null;
        if(mLocationClient != null){
            mLocationClient.stop();
            mLocationClient = null;
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pa_map_traffic_ng:
                isTraffic = !isTraffic;
                changeIconAndColor(mTraffic_POI, isTraffic);
                if(isTraffic){
                    searchPoiByKey(POI_SEARCH_TYPE_TRAFFIC);
                }else{
                    mTrafficMarker.clear();
                }
                break;
            case R.id.pa_map_school_ng:
                isSchool = !isSchool;
                changeIconAndColor(mSchool_POI, isSchool);
                if(isSchool) {
                    searchPoiByKey(POI_SEARCH_TYPE_SCHOOL);
                }else{
                    mSchoolMarker.clear();
                }
                break;
            case R.id.pa_map_life_ng:
                isLife = !isLife;
                changeIconAndColor(mLife_POI, isLife);
                if(isLife) {
                    searchPoiByKey(POI_SEARCH_TYPE_LIFE);
                }else{
                    mLifeMarker.clear();
                }
                break;
            case R.id.pa_map_other_ng:
                isOther = !isOther;
                changeIconAndColor(mOther_POI, isOther);
                if(isOther) {
                    searchPoiByKey(POI_SEARCH_TYPE_OTHER);
                }else{
                    mOtherMarker.clear();
                }
                break;
        }
    }

    /**
     * 检索关键词 的方法
     * @param
     */
    private void searchPoiByKey(int type){
        mCurPoiSearchType = type;
        String key = null;
        switch (type){
            case POI_SEARCH_TYPE_TRAFFIC:
            key = "公交";
            break;
            case POI_SEARCH_TYPE_SCHOOL:
            key = "学校";
            break;
            case POI_SEARCH_TYPE_OTHER:
            key = "小区";
            break;
            case POI_SEARCH_TYPE_LIFE:
            key = "购物";
            break;
        }
        PoiNearbySearchOption po = new PoiNearbySearchOption();
        po.location(mMainMarker.getPosition());
        po.keyword(key);
        po.radius(1500);
        po.pageCapacity(15);
        mPoiSearch.searchNearby(po);
    }

    @Override
    public void onGetPoiResult(PoiResult result) {
        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND){
            return;
        }

        if(result.error == SearchResult.ERRORNO.NO_ERROR){
            switch (mCurPoiSearchType){
                case POI_SEARCH_TYPE_TRAFFIC:
                    mTrafficMarker.addMarker(result.getAllPoi());
                    break;
                case POI_SEARCH_TYPE_SCHOOL:
                    mSchoolMarker.addMarker(result.getAllPoi());
                    break;
                case POI_SEARCH_TYPE_OTHER:
                    mOtherMarker.addMarker(result.getAllPoi());
                    break;
                case POI_SEARCH_TYPE_LIFE:
                    mLifeMarker.addMarker(result.getAllPoi());
                    break;
            }

        }

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    /**
     * 业务类型的检测 marker
     * @param marker
     */
    private void checkOnMarkerClick(Marker marker){

        //主题标点
        if(mMainMarker != null && marker.getTitle().equals(mMainMarker.getTitle())){
            showMyInfoWindow(marker.getTitle(), marker.getPosition(), R.drawable.lib_map_main);
            return;
        }

        //判断是不是交通类的标注
        Marker tmp = mTrafficMarker.getMarkerByTitle(marker.getTitle());
        if(null != tmp){
            showMyInfoWindow(marker.getTitle(), marker.getPosition(), mTrafficMarker.getImageId());
            return;
        }

        //学校
        tmp = mSchoolMarker.getMarkerByTitle(marker.getTitle());
        if(null != tmp){
            showMyInfoWindow(marker.getTitle(), marker.getPosition(), mSchoolMarker.getImageId());
            return;
        }
        //另一些
        tmp = mOtherMarker.getMarkerByTitle(marker.getTitle());
        if(null != tmp){
            showMyInfoWindow(marker.getTitle(), marker.getPosition(), mOtherMarker.getImageId());
            return;
        }
        //生活
        tmp = mLifeMarker.getMarkerByTitle(marker.getTitle());
        if(null != tmp){
            showMyInfoWindow(marker.getTitle(), marker.getPosition(), mLifeMarker.getImageId());
            return;
        }

    }

    /**
     * 获取需要用于显示的常规window，显示在图片的上面，为0就是显示在该点
     * @param title 显示的内容
     * @param parentLL 基于的位置
     * @param imgId 父类的图片
     * @return
     */
    private void showMyInfoWindow(String title, LatLng parentLL, int imgId){
        //创建InfoWindow的View
        TextView popTV = new TextView(getApplicationContext());
        popTV.setText(title);
        popTV.setBackgroundResource(R.drawable.lib_map_pop);
        popTV.setTextColor(getResources().getColor(android.R.color.black));


        int  height = 0;
        if(imgId > 0) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgId);
            //将经纬度坐标换成屏幕坐标
            height = bitmap.getHeight();
            bitmap = null;
        }
        mBaiduMap.showInfoWindow(new InfoWindow(popTV, parentLL, -height));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("查询路线").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        if("查询路线".equals(item.getTitle())){
            Intent i = new Intent(this, PaRouteMapActivity.class);
            MyLocationData data = mBaiduMap.getLocationData();
            if(data != null){
                i.putExtra("lat", data.latitude);
                i.putExtra("lng", data.longitude);
            }

            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

}
