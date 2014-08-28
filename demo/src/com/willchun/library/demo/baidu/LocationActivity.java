package com.willchun.library.demo.baidu;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.willchun.library.base.AndActivity;
import com.willchun.library.demo.DemoApp;
import com.willchun.library.demo.R;

/**
 * Created by Administrator on 2014/8/28.
 */
public class LocationActivity extends AndActivity {

    TextView mContentTV;
    LocationClient mLocationClient;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_location);

        ((DemoApp)app()).mLocationResult = (TextView)findViewById(R.id.content);
        mLocationClient = ((DemoApp)app()).mLocationClient;
        initOption();
        CheckBox checkBox = (CheckBox)findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    buttonView.setText("location ok");
                    mLocationClient.start();
                }else{
                    buttonView.setText("location canncel");
                    mLocationClient.stop();
                }
            }
        });
    }

    public void initOption(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType("bd09ll");//coorType - 取值有3个： 返回国测局经纬度坐标系：gcj02 返回百度墨卡托坐标系 ：bd09 返回百度经纬度坐标系 ：bd09ll
        option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);//返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
        option.setOpenGps(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.stop();
    }
}
