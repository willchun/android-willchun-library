package com.willchun.library.demo.baidu;

import android.os.Bundle;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.willchun.library.base.AndActivity;
import com.willchun.library.demo.R;

/**
 * Created by Administrator on 2014/9/1.
 */
public class BaiduMapActivity extends AndActivity {

    private MapView mMapView = null;
    //定义 BaiduMap 地图对象的操作方法与接口
    private BaiduMap mBaiduMap = null;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_baidu_map);

        mMapView = (MapView) findViewById(R.id.bd_mapview);
        mBaiduMap = mMapView.getMap();


    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}
