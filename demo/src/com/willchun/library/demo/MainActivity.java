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
import android.widget.AdapterView;

import com.willchun.library.base.AndActivity;
import com.willchun.library.demo.service.ServiceDemo;
import com.willchun.library.demo.view.ViewDemo;
import com.willchun.library.service.NetworkStateService;
import com.willchun.library.view.ToastView;

/**
 *@author chunwang (wcly10@gmail.com)
 *@date 2013-12-26
 */
public class MainActivity extends DemoListActivity{

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        switch (arg2) {
        case 0:
            _intent(ServiceDemo.class);
            break;
        case 1:
            _intent(ViewDemo.class);
            break;
        default:
            break;
        }
    }

    @Override
    public String[] getListName() {
        // TODO Auto-generated method stub
        String[] ret = {"Service", "View"};
        return ret;
    }

    @Override
    public boolean isBackDisplay() {
        // TODO Auto-generated method stub
        return false;
    }
    
}
