package com.willchun.library.demo.map;/**
 * Created by Administrator on 2014/12/4.
 */

import android.text.TextUtils;
import android.util.Log;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.search.core.PoiInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于百度地图标签的 自定义列表类
 * @author willchun (277143980@qq.com)
 * @github https://github.com/willchun
 * @date 2014/12/4
 *
 * 使用说明：
 * 1.因为内部调用了BaiduMap和Bitmap 所以在 activity或Fragment的onDestroy要释放资源 调用本类的onDesteroy
 *
 */
public class MyMarker {

    private ArrayList<Marker> markers = new ArrayList<Marker>();
    private BaiduMap mBaiduMap;
    private BitmapDescriptor bitmapDescriptor;
    private int imageId;

    public MyMarker(BaiduMap baiduMap, int resId){
        mBaiduMap = baiduMap;
        this.imageId = resId;
        bitmapDescriptor = BitmapDescriptorFactory.fromResource(resId);
    }

    public void addMarker(List<PoiInfo> list){
        if(null == list){
            return;
        }
        for(PoiInfo info : list){
            String title = info.name + '\n' + info.city + info.address;
            OverlayOptions oo = new MarkerOptions().icon(bitmapDescriptor).position(info.location).title(title);
            Marker m = (Marker)mBaiduMap.addOverlay(oo);
            markers.add(m);
        }
    }

    public void clear(){
        for(Marker marker : markers){
            marker.remove();
        }
        markers.clear();
    }

    public Marker getMarkerByTitle(String title){
        if(TextUtils.isEmpty(title)){
            return null;
        }
        for(Marker marker : markers){
            if(title.equals(marker.getTitle())){
                return marker;
            }
        }
        return null;
    }


    public void onDestroy(){
        clear();
        if(bitmapDescriptor != null){
            bitmapDescriptor.recycle();
            bitmapDescriptor = null;
        }
        mBaiduMap = null;
        markers = null;
    }

    public int getImageId(){
        return imageId;
    }

}
