/**
 *
 * Copyright 2014 IOTEK. All rights reserved.
 * FormEditTextViewDemo.java
 *
 */
package com.willchun.library.demo.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;

import com.willchun.library.base.AndActivity;
import com.willchun.library.demo.R;
import com.willchun.library.utils.CommonUtils;
import com.willchun.library.utils.LogUtils;

/**
 *@author willchun (277143980@qq.com)
 *@date 2014-4-1
 */
public class FormEditTextViewDemo extends AndActivity {
    
    private EditText editText;
    
    
    private char val = ' ';//分隔符
    @Override
    protected void onCreate(Bundle savedState) {
        // TODO Auto-generated method stub
        super.onCreate(savedState);
        setContentView(R.layout.demo_view_form_edittext);
        editText = (EditText)findViewById(R.id.demo_view_form_et);
        
        editText.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int  count, int after) {
                // TODO Auto-generated method stub
            }
            
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
        
        
    }
    
    public static String url = "^[0-9]{3} [0-9]{4} [0-9]{4}$";
    public static String url1 = "\\d+(\\.[0-9]{2})";

    
    public static Matcher getMatcher(String str){
        Pattern p = Pattern.compile(url);
        Matcher d;
        
        return p.matcher(str);
    }
    
    
    
    public class FormEditText extends EditText{

        public FormEditText(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            // TODO Auto-generated constructor stub
        }
        
        public FormEditText(Context context, AttributeSet attrs) {
            super(context, attrs);
            // TODO Auto-generated constructor stub
        }
        
        public FormEditText(Context context) {
            super(context);
            // TODO Auto-generated constructor stub
        }
        
    }
}
