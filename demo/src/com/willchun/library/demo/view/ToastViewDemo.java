/**
 *
 * Copyright 2014 IOTEK. All rights reserved.
 * ToastView.java
 *
 */
package com.willchun.library.demo.view;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;

import com.willchun.library.base.AndActivity;
import com.willchun.library.demo.R;
import com.willchun.library.view.ToastView;

/**
 *@author willchun (277143980@qq.com)
 *@date 2014-2-11
 */
public class ToastViewDemo extends AndActivity implements OnClickListener{

    @Override
    protected void onCreate(Bundle savedState) {
        // TODO Auto-generated method stub
        super.onCreate(savedState);
        setContentView(R.layout.demo_toastview);
        setTitle("show ToastView");
        
        aq.id(R.id.toastview_btn01).clicked(this);
        aq.id(R.id.toastview_btn02).clicked(this);
        aq.id(R.id.toastview_btn03).clicked(this);
        
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.toastview_btn01:
            ToastView.show(getBaseContext(), "~我是默认~");
            break;
        case R.id.toastview_btn02:
            ToastView.show(getBaseContext(), "图片改为了小微", R.drawable.toast_image);
            break;
        case R.id.toastview_btn03:
            ToastView.make(getBaseContext(), "我是自定义的:小微图, 底部, 延时200", R.drawable.toast_image, Gravity.BOTTOM, 200).show();
            break;
        default:
            break;
        }
    }
    
}
