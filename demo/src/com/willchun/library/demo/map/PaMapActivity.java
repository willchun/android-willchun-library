package com.willchun.library.demo.map;/**
 * Created by Administrator on 2014/12/1.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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
public class PaMapActivity extends AndActivity{

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private BitmapDescriptor mMainBitmap;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_pa_map);

        mMapView = (MapView)(aq.id(R.id.pa_mapview).getView());
        mBaiduMap = mMapView.getMap();
        //关闭俯瞰图手势
        mBaiduMap.getUiSettings().setOverlookingGesturesEnabled(false);


        addMainMarker("测试", new LatLng(31.22, 121.48));

    }


    /**
     * 增加主要标注的图标
     */
    public void addMainMarker(String mainTitle, LatLng mainLatLng){
        mMainBitmap = BitmapDescriptorFactory.fromResource(R.drawable.lib_map_main);
        OverlayOptions mainOO = new MarkerOptions().icon(mMainBitmap).title("MainMarker").position(mainLatLng);
        //创建InfoWindow的View
        TextView popTV = new TextView(getApplicationContext());
        popTV.setText(mainTitle);
        popTV.setBackgroundResource(R.drawable.lib_map_pop);
        popTV.setTextColor(getResources().getColor(android.R.color.black));
        popTV.setGravity(Gravity.CENTER);
        //pop的位置
        Marker marker = (Marker)mBaiduMap.addOverlay(mainOO);

        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(mainLatLng);
        mBaiduMap.animateMapStatus(update, 300);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lib_map_main);
        //将经纬度坐标换成屏幕坐标
        Log.e("will", "bitmap h:"+ bitmap.getHeight() + " - project:" + mBaiduMap.getProjection());
        //Point point = mBaiduMap.getProjection().toScreenLocation(marker.getPosition());
        //point.y -= bitmap.getHeight();
        //LatLng windowLL = mBaiduMap.getProjection().fromScreenLocation(point);

        InfoWindow infoWindow = new InfoWindow(popTV, mainLatLng, null);
        mBaiduMap.showInfoWindow(infoWindow);


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
}
