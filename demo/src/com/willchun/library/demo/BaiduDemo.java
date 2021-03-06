package com.willchun.library.demo;

import android.view.View;
import android.widget.AdapterView;
import com.willchun.library.demo.baidu.BaiduMapActivity;
import com.willchun.library.demo.baidu.LocationActivity;
import com.willchun.library.demo.map.PaMapActivity;

/**
 * Created by Administrator on 2014/8/28.
 */
public class BaiduDemo extends  DemoListActivity{
    @Override
    public String[] getListName() {
        String[] ret = {"百度定位", "百度地图", "PA地图"};
        return ret;
    }

    @Override
    public boolean isBackDisplay() {
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                _intent(LocationActivity.class);
                break;
            case 1:
                _intent(BaiduMapActivity.class);
                break;
            case 2:
                _intent(PaMapActivity.class);
                break;
        }
    }
}
