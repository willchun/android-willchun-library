/**
 *
 * Copyright 2014 IOTEK. All rights reserved.
 * ServiceDemo.java
 *
 */
package com.willchun.library.demo.service;

import android.view.View;
import android.widget.AdapterView;

import com.willchun.library.demo.DemoListActivity;

/**
 *@author willchun (277143980@qq.com)
 *@date 2014-2-11
 */
public class ServiceDemo extends DemoListActivity {

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        switch (arg2) {
        case 0:
            _intent(NetworkStateServiceDemo.class);
            break;

        default:
            break;
        }
    }

    @Override
    public String[] getListName() {
        // TODO Auto-generated method stub
        String[] ret = {"NetworkStateService"};
        return ret;
    }

    @Override
    public boolean isBackDisplay() {
        // TODO Auto-generated method stub
        return true;
    }

}
