/**
 *
 * Copyright 2013 Anjuke. All rights reserved.
 * MainActivity.java
 *
 */
package com.willchun.library.demo;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.willchun.library.base.AndActivity;
import com.willchun.library.service.NetworkStateService;
import com.willchun.library.view.ToastView;

/**
 *@author chunwang (wcly10@gmail.com)
 *@date 2013-12-26
 */
public class MainActivity extends AndActivity implements OnClickListener{

    @Override
    protected void onCreate(Bundle savedState) {
        // TODO Auto-generated method stub
        super.onCreate(savedState);
        setContentView(R.layout.main);
        
        startService(intent(NetworkStateService.class));
        
        aq.id(R.id.showbtn).clicked(this);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        stopService(intent(NetworkStateService.class));
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.showbtn:
            ToastView.show(this, R.string.app_name, R.drawable.toast_image);
            break;

        default:
            break;
        }
    }
    
    
    
}
