package com.willchun.library.demo.map;/**
 * Created by Administrator on 2014/12/10.
 */

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Toast;
import com.baidu.location.*;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.RouteNode;
import com.baidu.mapapi.search.core.SearchResult;
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
    //重点路线
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
        mRoutePlanSearch.setOnGetRoutePlanResultListener(this);

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
        mRoutePlanSearch.destroy();
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
            case 0:

                break;
            case 1:

                break;
            case 2:

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
        if(result == null || result.error != SearchResult.ERRORNO.NO_ERROR){
            return;
        }

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {

    }

    public void searchRoutePlan(LatLng startLL, LatLng endLL){
        //重置线路
        mRouteLine = null;
        mBaiduMap.clear();
        //搜索组装
        RouteNode startNode = RouteNode.location(startLL);
        RouteNode endNode = RouteNode.location(endLL);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SubMenu subMenu = menu.addSubMenu("路线选择");
        subMenu.add(0, 1, 0, "公交");
        subMenu.add(0, 2, 0, "自驾");
        subMenu.add(0, 3, 0, "步行");

        return super.onCreateOptionsMenu(menu);
    }

}
