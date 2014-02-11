/**
 *
 * Copyright 2014 IOTEK. All rights reserved.
 * RoundImageviewDemo.java
 *
 */
package com.willchun.library.demo.view;

import android.os.Bundle;

import com.willchun.library.base.AndActivity;
import com.willchun.library.demo.R;

/**
 *@author willchun (277143980@qq.com)
 *@date 2014-2-11
 */
public class RoundImageViewDemo extends AndActivity {

    String url = "http://images.quanjing.com/chineseview069/thu/tpgrf-bgs20110918.jpg";
    
    @Override
    protected void onCreate(Bundle savedState) {
        // TODO Auto-generated method stub
        super.onCreate(savedState);
        setContentView(R.layout.demo_round_imageview);
        
        aq.id(R.id.iv01).image(url);
        aq.id(R.id.iv02).image(url);
        aq.id(R.id.iv03).image(url);
    }
    
}
