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

        aq.id(R.id.load_image).image("https://file.pinganfang.com/view/99054ab3f56b5a55ede5f01d70b8c6a92614f790/96x72.jpg");

        aq.id(R.id.load_image2).image("http://file.pinganfang.com/view/01ae50919e46c410a2d87825a990bcd6a53574e1/200x150.jpg");


        aq.id(R.id.load_image3).image("http://images.quanjing.com/chineseview069/thu/tpgrf-bgs20110918.jpg");
    }
    
}
