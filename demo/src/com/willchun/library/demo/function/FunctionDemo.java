/**
 *
 * Copyright 2014 IOTEK. All rights reserved.
 * FunctionDemo.java
 *
 */
package com.willchun.library.demo.function;

import android.view.View;
import android.widget.AdapterView;

import com.willchun.library.demo.DemoListActivity;

/**
 *@author willchun (277143980@qq.com)
 *@date 2014-3-5
 */
public class FunctionDemo extends DemoListActivity {

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        switch (arg2) {
        case 0:
            _intent(PhotoAlbumDirFunctionActivity.class);
            break;

        default:
            break;
        }
    }

    @Override
    public String[] getListName() {
        // TODO Auto-generated method stub
        String[] ret = {"PhotoAlbumFunction"};
        return ret;
    }

    @Override
    public boolean isBackDisplay() {
        // TODO Auto-generated method stub
        return true;
    }

}
