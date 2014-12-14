package com.willchun.library.demo.map;/**
 * Created by Administrator on 2014/12/10.
 */

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import com.baidu.location.*;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.route.*;
import com.willchun.library.demo.R;

/**
 * @author willchun (277143980@qq.com)
 * @github https://github.com/willchun
 * @date 2014/12/10
 */
public class PaRouteMapActivity extends Activity implements OnGetRoutePlanResultListener{

    private BaiduMap mBaiduMap;
    private MapView mMapView;
    //搜索模块
    private RoutePlanSearch mRoutePlanSearch;
    //路线
    private RouteLine mRouteLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pa_route_map);
        mMapView = (MapView)findViewById(R.id.pa_mapview);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mBaiduMap = mMapView.getMap();

        //关闭俯瞰图手势
        mBaiduMap.getUiSettings().setOverlookingGesturesEnabled(false);

        mRoutePlanSearch = RoutePlanSearch.newInstance();
        mRoutePlanSearch.setOnGetRoutePlanResultListener();

        //数据异常
        if(getIntent() == null){
            Toast.makeText(this, "数据异常", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        //定位图层是否显示
        Double lat = getIntent().getDoubleExtra("lat", 0);
        Double lng = getIntent().getDoubleExtra("lng", 0);
        if(lat != 0 && lng != 0){


        }else{
            Toast.makeText(this, "定位未成功", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }



    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
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
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult result) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {

    }
}
