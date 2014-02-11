/**
 *
 * Copyright 2014 IOTEK. All rights reserved.
 * NetworkStateServiceDemo.java
 *
 */
package com.willchun.library.demo.service;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.willchun.library.base.AndActivity;
import com.willchun.library.demo.R;
import com.willchun.library.service.NetworkStateService;

/**
 *@author willchun (277143980@qq.com)
 *@date 2014-2-11
 */
public class NetworkStateServiceDemo extends AndActivity implements OnClickListener{

    @Override
    protected void onCreate(Bundle savedState) {
        // TODO Auto-generated method stub
        super.onCreate(savedState);
        setContentView(R.layout.demo_network_state_service);
       
        aq.id(R.id.btn01).clicked(this);
        aq.id(R.id.btn02).clicked(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.btn01:
            startService(intent(NetworkStateService.class));
            break;
        case R.id.btn02:
            stopService(intent(NetworkStateService.class));
            break;
        default:
            break;
        }
    }
    
}
