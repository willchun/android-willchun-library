/**
 *
 * Copyright 2014 IOTEK. All rights reserved.
 * TextSizeColor.java
 *
 */
package com.willchun.library.demo.view;

import android.os.Bundle;

import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import com.willchun.library.base.AndActivity;
import com.willchun.library.demo.R;

/**
 *@author willchun (277143980@qq.com)
 *@date 2014-3-14
 */
public class TextSizeColorViewDemo extends AndActivity{

    @Override
    protected void onCreate(Bundle savedState) {
        // TODO Auto-generated method stub
        super.onCreate(savedState);
        setContentView(R.layout.demo_view_text_size_color);

        aq.id(R.id.load_image3).image("http://images.quanjing.com/chineseview069/thu/tpgrf-bgs20110918.jpg");
    }
    
}
