package com.willchun.library.demo.baidu;

import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.baidu.location.*;
import com.willchun.library.base.AndActivity;
import com.willchun.library.demo.DemoApp;
import com.willchun.library.demo.R;
import com.willchun.library.platform.baidu.BaiduPlatformLocationAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/8/28.
 */
public class LocationActivity extends AndActivity implements View.OnClickListener{

    TextView wlTV;
    Vibrator mVibrator;
    NotifyListener mNotifyListener;
    BDLocation mBDBdLocation;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_location);
        mVibrator = (Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);

        initLocation();
        initWeiLan();
        initNotify();

    }


    /**
     * 定位初始化
     */
    public void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType(BDGeofence.COORD_TYPE_BD09LL);//coorType - 取值有3个： 返回国测局经纬度坐标系：gcj02 返回百度墨卡托坐标系 ：bd09 返回百度经纬度坐标系 ：bd09ll
        option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);//返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
        option.setOpenGps(true);
        BaiduPlatformLocationAgent.getInstance(LocationActivity.this).setLocationClientOption(option);

        CheckBox  locationCB = (CheckBox)findViewById(R.id.checkbox);
        locationCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    buttonView.setText("定位服务：开启");
                    BaiduPlatformLocationAgent.getInstance(LocationActivity.this).startLocation();
                }else{
                    buttonView.setText("定位服务：关闭");
                    BaiduPlatformLocationAgent.getInstance(LocationActivity.this).stopLocation();
                }
            }
        });

        BaiduPlatformLocationAgent.getInstance(this).registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                {
                    //Receive Location
                    mBDBdLocation = location;
                    StringBuffer sb = new StringBuffer(256);
                    sb.append("\ntime : ");
                    sb.append(location.getTime());
                    sb.append("\nerror code : ");
                    sb.append(location.getLocType());
                    sb.append("\nlatitude : ");
                    sb.append(location.getLatitude());
                    sb.append("\nlontitude : ");
                    sb.append(location.getLongitude());
                    sb.append("\nradius : ");
                    sb.append(location.getRadius());
                    if (location.getLocType() == BDLocation.TypeGpsLocation){
                        sb.append("\nspeed : ");
                        sb.append(location.getSpeed());
                        sb.append("\nsatellite : ");
                        sb.append(location.getSatelliteNumber());
                        sb.append("\ndirection : ");
                        sb.append("\naddr : ");
                        sb.append(location.getAddrStr());
                        sb.append(location.getDirection());
                    } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
                        sb.append("\naddr : ");
                        sb.append(location.getAddrStr());
                        ////运营商信息
                        sb.append("\noperationers : ");
                        sb.append(location.getOperators());
                    }
                    ((TextView)findViewById(R.id.content)).setText(sb.toString());
                    Log.i("BaiduLocationApiDem", sb.toString());
                }
            }
        });
    }

    public void initWeiLan(){
        wlTV = (TextView)findViewById(R.id.wl_content);
        //围栏
        findViewById(R.id.wl_btn01).setOnClickListener(this);
        findViewById(R.id.wl_btn02).setOnClickListener(this);
        BaiduPlatformLocationAgent.getInstance(LocationActivity.this).registerGeofenceTriggerListener(new TriggerFenceListener());
    }

    public void initNotify(){
        CheckBox checkBox = (CheckBox)findViewById(R.id.tx_checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                     if(mBDBdLocation == null){
                         buttonView.setChecked(false);
                         return;
                     }
                    mNotifyListener = new NotifyListener();
                    mNotifyListener.SetNotifyLocation(mBDBdLocation.getLatitude(),
                            mBDBdLocation.getLongitude(), 5000, BDGeofence.COORD_TYPE_BD09LL);//4个参数代表要位置提醒的点的坐标，具体含义依次为：纬度，经度，距离范围，坐标系类型(gcj02,gps,bd09,bd09ll)
                    BaiduPlatformLocationAgent.getInstance(LocationActivity.this).registerNotify(mNotifyListener);
                    Log.i("will", "提醒服务：开启");
                    buttonView.setText("提醒服务：开启");

                }else{
                    BaiduPlatformLocationAgent.getInstance(LocationActivity.this).removeNotifyEvent(mNotifyListener);
                    Log.i("will", "提醒服务：关闭");
                    buttonView.setText("提醒服务：关闭");
                }
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        BaiduPlatformLocationAgent.getInstance(LocationActivity.this).stopLocation();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wl_btn01:
                //add fence
                if(mBDBdLocation == null){
                    return;
                }
                BDGeofence bdGeofence = new BDGeofence.Builder().setGeofenceId("test").setCoordType("bd09ll").setCircularRegion(mBDBdLocation.getLongitude(),
                        mBDBdLocation.getLatitude(), BDGeofence.RADIUS_TYPE_SMALL).setCoordType(BDGeofence.COORD_TYPE_BD09LL).
                        setExpirationDruation(3600*1000*10).build();
                BaiduPlatformLocationAgent.getInstance(LocationActivity.this).addBDGeofence(bdGeofence, new AddGeoFenceListener());
                break;
            case R.id.wl_btn02:
                //delete
                List<String> l = new ArrayList<String>();
                l.add("test");
                BaiduPlatformLocationAgent.getInstance(LocationActivity.this).removeBDGeofences(l, new RemoveGeoFenceListener());
                break;
        }
    }


    public class AddGeoFenceListener implements GeofenceClient.OnAddBDGeofencesResultListener{

        @Override
        public void onAddBDGeofencesResult(int statusCode, String geofenceId) {
            try {
                if (statusCode == BDLocationStatusCodes.SUCCESS) {
                    // 开发者实现创建围栏成功的功能逻辑
                    Toast.makeText(LocationActivity.this, "围栏" + geofenceId + "添加成功", Toast.LENGTH_SHORT).show();
                    Log.i("will", "onAddBDGeofencesResult success:" + geofenceId);
                    // 在添加地理围栏成功后，开启地理围栏服务，对本次创建成功且已进入的地理围栏，可以实时的提醒
                    BaiduPlatformLocationAgent.getInstance(LocationActivity.this).startGeoFence();
                }else{
                    Toast.makeText(LocationActivity.this, "围栏" + geofenceId + "添加失败", Toast.LENGTH_SHORT).show();
                    Log.i("will", "onAddBDGeofencesResult faile:" + geofenceId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class RemoveGeoFenceListener implements GeofenceClient.OnRemoveBDGeofencesResultListener{

        @Override
        public void onRemoveBDGeofencesByRequestIdsResult(int statusCode, String[] geofenceRequestIds) {
            if(statusCode == BDLocationStatusCodes.SUCCESS){
                for(String id : geofenceRequestIds){
                    Log.i("will", "onRemoveBDGeofencesByRequestIdsResult :" + id);
                    Toast.makeText(LocationActivity.this, "围栏" + id + "删除成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public class  TriggerFenceListener  implements GeofenceClient.OnGeofenceTriggerListener{

        @Override
        public void onGeofenceTrigger(String s) {
            Log.i("will", "onGeofenceTrigger 进入围栏:" + s);
            Toast.makeText(LocationActivity.this, "进入围栏" + s, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onGeofenceExit(String s) {
            Log.i("will", "onGeofenceTrigger 退出围栏:" + s);
            Toast.makeText(LocationActivity.this, "退出围栏" + s, Toast.LENGTH_SHORT).show();
        }
    }

    public class NotifyListener extends BDNotifyListener{
        @Override
        public void onNotify(BDLocation bdLocation, float v) {
            super.onNotify(bdLocation, v);
            mVibrator.vibrate(3000);
            Log.i("will", "NotifyListener v: + v");
            Toast.makeText(LocationActivity.this, "震动提醒", Toast.LENGTH_SHORT).show();
        }
    }
}
