/**
 *
 * Copyright 2014 IOTEK. All rights reserved.
 * PhotoAlbumFunctionDemo.java
 *
 */
package com.willchun.library.demo.function;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;

import com.willchun.library.base.AndActivity;
import com.willchun.library.base.AndAdapter;
import com.willchun.library.base.AndQuery;
import com.willchun.library.demo.R;
import com.willchun.library.function.photoalbum.PhotoAlbumDirFunctionActivity;
import com.willchun.library.utils.LogUtils;
import com.willchun.library.utils.UIUtils;

/**
 *@author willchun (277143980@qq.com)
 *@date 2014-3-7
 */
public class PhotoAlbumFunctionDemo extends AndActivity {

    private AndAdapter<String> mAdapter;
    
    @Override
    protected void onCreate(Bundle savedState) {
        // TODO Auto-generated method stub
        super.onCreate(savedState);
        setContentView(R.layout.demo_photo_album_function);
        
        mAdapter = new AndAdapter<String>(this, R.layout.willchun_lib_item_photo_album_pic_grid) {

            @Override
            protected void update(String item, AndQuery aq, int position) {
                // TODO Auto-generated method stub
                
            }

            @Override
            protected void update(String item, int position, AndQuery aq, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                super.update(item, position, aq, convertView, parent);
                AbsListView.LayoutParams lp = (LayoutParams) convertView.getLayoutParams();
                if(lp == null){
                    lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
               
                lp.height = (UIUtils.getInstance(getWindowManager()).getWidth() - UIUtils.getInstance(getWindowManager()).dip2Px(2)*2)/3;
                convertView.setLayoutParams(lp);
                Bitmap placeholder = aq.getCachedImage(R.drawable.willchun_lib_photo_thumb_bg);
                if(aq.shouldDelay(position, convertView, parent, item)){
                    aq.id(R.id.willchun_lib_item_photo_album_pic_grid_iv).image(placeholder);
                }else{
                    aq.id(R.id.willchun_lib_item_photo_album_pic_grid_iv).image(item, true, true, UIUtils.getInstance(getWindowManager()).dip2Px(75), 0, placeholder, 0);
                }
                aq.id(R.id.willchun_lib_item_photo_album_pic_grid_cb).gone();
            }
            
            
        };
        
        aq.id(R.id.photo_album_gv).adapter(mAdapter);
        
        aq.id(R.id.add_single_btn).clicked(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivityForResult(PhotoAlbumDirFunctionActivity.getLaunchIntentSingleChoice(getBaseContext()), 100);
            }
        });
        aq.id(R.id.add_mutiple_btn).clicked(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                int num = 0;
                if(!TextUtils.isEmpty(aq.id(R.id.limit_number_et).getEditText().getText().toString().trim()))
                   num = Integer.parseInt(aq.id(R.id.limit_number_et).getEditText().getText().toString().trim());
                        
                startActivityForResult(PhotoAlbumDirFunctionActivity.getLaunchIntentMutipleChoice(getBaseContext(), num), 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);
        if(arg0 == 100 && arg1 == RESULT_OK && arg2 != null){
            mAdapter.addAll(arg2.getStringArrayListExtra(PhotoAlbumDirFunctionActivity.DATA_KEY));
            mAdapter.notifyDataSetChanged();
        }
    } 
    
    
    
}
