package com.willchun.library.demo.map;/**
 * Created by Administrator on 2014/12/1.
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.willchun.library.base.AndActivity;
import com.willchun.library.demo.R;
/**
 * @author willchun (277143980@qq.com)
 * @github https://github.com/willchun
 * @date 2014/12/1
 */
public class PaMapActivity extends Activity implements View.OnClickListener{

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private BitmapDescriptor mMainBitmap;

    private LinearLayout mTraffic_POI;
    private LinearLayout mSchool_POI;
    private LinearLayout mOther_POI;
    private LinearLayout mLife_POI;

    /** poi搜索半径 */
    private final int        SEARCH_REDIUS    = 3 * 1000;

    private boolean isTraffic = false;
    private boolean isSchool = false;
    private boolean isOther = false;
    private boolean isLife = false;

    private int              mSelectColor     = Color.rgb(33, 99, 00);
    private int              mNormalColor     = Color.rgb(66, 66, 66);

    private LatLng mMainLatLng;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_pa_map);

        mMapView = (MapView)findViewById(R.id.pa_mapview);
        mBaiduMap = mMapView.getMap();
        //关闭俯瞰图手势
        mBaiduMap.getUiSettings().setOverlookingGesturesEnabled(false);


        initMainMarker("测试", new LatLng(31.22, 121.48));
        initPoiMarker();
    }


    /**
     * 增加主要标注的图标
     */
    public void initMainMarker(String mainTitle, LatLng mainLatLng){
        mMainLatLng = mainLatLng;
        mMainBitmap = BitmapDescriptorFactory.fromResource(R.drawable.lib_map_main);
        OverlayOptions mainOO = new MarkerOptions().icon(mMainBitmap).title("MainMarker").position(mainLatLng);
        mBaiduMap.addOverlay(mainOO);
        //3-19
        MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder(mBaiduMap.getMapStatus()).zoom(16.0f).target(mainLatLng).build());
        mBaiduMap.animateMapStatus(update, 300);

        //创建InfoWindow的View
        TextView popTV = new TextView(getApplicationContext());
        popTV.setText(mainTitle);
        popTV.setBackgroundResource(R.drawable.lib_map_pop);
        popTV.setTextColor(getResources().getColor(android.R.color.black));
        popTV.setGravity(Gravity.CENTER);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lib_map_main);
        //将经纬度坐标换成屏幕坐标
        int height = bitmap.getHeight();
        bitmap = null;
        InfoWindow infoWindow = new InfoWindow(popTV, mainLatLng, -height);
        mBaiduMap.showInfoWindow(infoWindow);
    }

    /**
     * 初始化Poi标签
     */
    public void initPoiMarker(){
        mTraffic_POI = getPoifindLL(R.id.pa_map_traffic_ng);
        mSchool_POI = getPoifindLL(R.id.pa_map_school_ng);
        mOther_POI = getPoifindLL(R.id.pa_map_other_ng);
        mLife_POI = getPoifindLL(R.id.pa_map_life_ng);

        mTraffic_POI.setOnClickListener(this);
        mSchool_POI.setOnClickListener(this);
        mOther_POI.setOnClickListener(this);
        mLife_POI.setOnClickListener(this);

        //mTraffic_IC = getPoifindTV(mTraffic_POI, R.id.map_detail_condition_ic);
        getPoifindTV(mTraffic_POI, R.id.map_detail_condition_tv).setText("交通");
        //mSchool_IC = getPoifindTV(mSchool_POI, R.id.map_detail_condition_ic);
        getPoifindTV(mSchool_POI, R.id.map_detail_condition_tv).setText("学校");
        //mOther_IC = getPoifindTV(mOther_POI, R.id.map_detail_condition_ic);
        getPoifindTV(mOther_POI, R.id.map_detail_condition_tv).setText("楼盘");
        //mLife_IC = getPoifindTV(mLife_POI, R.id.map_detail_condition_ic);
        getPoifindTV(mLife_POI, R.id.map_detail_condition_tv).setText("生活");

    }

    private LinearLayout getPoifindLL(int id){
        return (LinearLayout)findViewById(id);
    }

    private TextView getPoifindTV(View root, int id){
        return (TextView)root.findViewById(id);
    }

    private void changeIconAndColor(View root, boolean isSelected){
        if(isSelected){
            getPoifindTV(root, R.id.map_detail_condition_ic).setTextColor(mSelectColor);
            getPoifindTV(root, R.id.map_detail_condition_tv).setTextColor(mSelectColor);
        }else{
            getPoifindTV(root, R.id.map_detail_condition_ic).setTextColor(mNormalColor);
            getPoifindTV(root, R.id.map_detail_condition_tv).setTextColor(mNormalColor);
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
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pa_map_traffic_ng:
                isTraffic = !isTraffic;

                break;
            case R.id.pa_map_school_ng:
                isSchool = !isSchool;

                break;
            case R.id.pa_map_life_ng:
                isLife = !isLife;

                break;
            case R.id.pa_map_other_ng:
                isOther = !isOther;

                break;
        }
    }
}
