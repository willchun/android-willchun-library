/**
 *
 * Copyright 2014 IOTEK. All rights reserved.
 * ViewDemo.java
 *
 */
package com.willchun.library.demo.view;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.willchun.library.demo.DemoListActivity;

/**
 *@author willchun (277143980@qq.com)
 *@date 2014-2-11
 */
public class ViewDemo extends DemoListActivity {

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        switch (arg2) {
        case 0:
            _intent(ToastViewDemo.class);
            break;
        case 1:
            _intent(RoundImageViewDemo.class);
            break;
        case 2:
            _intent(TextSizeColorViewDemo.class);
            break;
        case 3:
            _intent(FormEditTextViewDemo.class);
            break;
        case 4:
        	_intent(BlurImageViewDemo.class);
        	break;
        case 5:
            _intent(MixedChoiceAdapterViewDemo.class);
        default:
            break;
        }
    }

    @Override
    public String[] getListName() {
        // TODO Auto-generated method stub
        String[] str = {"ToastView", "RoundImageView", "TextSizeColor", "FormEditText", "BlurImageView", "MixedChoiceAdapter"};
        return str;
    }

    @Override
    public boolean isBackDisplay() {
        // TODO Auto-generated method stub
        return true;
    }

}
