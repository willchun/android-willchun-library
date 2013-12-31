/**
 *
 * Copyright 2013 IOTEK. All rights reserved.
 * ToastView.java
 *
 */
package com.willchun.library.view;

import java.util.Timer;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.willchun.library.R;


/**
 * 自定义一个带图框的Toast
 *@author willchun (277143980@qq.com)
 *@date 2013-12-26
 */
public class ToastView {
    
    public static Toast toast;

    /**
     * 自定义一个Toast
     * @param context
     * @param text 内容
     * @param resId 图片id
     * @param gravity 重心
     * @return
     */
    public static Toast make(Context context, String text, int resId, int gravity, int duration){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.willchun_lib_toast_view, null);
        TextView t = (TextView) view.findViewById(R.id.willchun_lib_toast_text);
        t.setText(text);
        if(resId > 0){
            ImageView iv = (ImageView) view.findViewById(R.id.willchun_lib_toast_iv);
            iv.setImageResource(resId);
        }
        if(toast != null) {
            toast.cancel();
        }
        toast = new Toast(context);
        toast.setDuration(duration);
        toast.setGravity(gravity, 0, 0);
        toast.setView(view);
        return toast;
    }
    
    public static void show(Context context, String text){
        make(context, text, 0, Gravity.CENTER, Toast.LENGTH_SHORT).show();
    }
    
    public static void show(Context context, String text, int imageId){
        make(context, text, imageId, Gravity.CENTER, Toast.LENGTH_SHORT).show();
    }
    
    public static void show(Context context, int textId){
        show(context, context.getResources().getString(textId));
    }
    
    public static void show(Context context, int textId, int imageId){
        show(context, context.getResources().getString(textId), imageId);
    }

}
