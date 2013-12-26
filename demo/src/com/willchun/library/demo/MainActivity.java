/**
 *
 * Copyright 2013 Anjuke. All rights reserved.
 * MainActivity.java
 *
 */
package com.willchun.library.demo;

import android.os.Bundle;

import com.willchun.library.base.AndActivity;
import com.willchun.library.service.NetworkStateService;

/**
 *@author chunwang (wcly10@gmail.com)
 *@date 2013-12-26
 */
public class MainActivity extends AndActivity {

    @Override
    protected void onCreate(Bundle savedState) {
        // TODO Auto-generated method stub
        super.onCreate(savedState);
        setContentView(R.layout.main);
        
        startService(intent(NetworkStateService.class));
    }
    
}
