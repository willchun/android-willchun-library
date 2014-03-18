/**
 *
 * Copyright 2014 IOTEK. All rights reserved.
 * PhotoAlbumPicFunctionActivity.java
 *
 */
package com.willchun.library.function.photoalbum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.R.integer;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.ImageColumns;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.willchun.library.R;
import com.willchun.library.base.AndActivity;
import com.willchun.library.base.AndAdapter;
import com.willchun.library.base.AndQuery;
import com.willchun.library.utils.UIUtils;
import com.willchun.library.view.ToastView;

/**
 *@author willchun (277143980@qq.com)
 *@date 2014-3-6
 */
public class PhotoAlbumPicFunctionActivity extends AndActivity{

    private String bucketId = "";
    private final String IMG_JPG = "image/jpg";
    private final String IMG_JPEG = "image/jpeg";
    private final String IMG_PNG = "image/png";
    private final String IMG_GIF = "image/gif";
    
    private HashMap<String, ImageView> selectedData = new HashMap<String, ImageView>(); 
    private HorizontalScrollView mHorizontalScrollView;
    private LinearLayout mImageLayout;
    private GridView mGridView;
    
    private AndAdapter<String> mAdapter;
    
    private static final String KEY_ID = "key_id";
    private static final String KEY_NAME = "key_name";
    private static final String KEY_CHOICE = "key_choice";
    private static final String KEY_NUMBER = "key_number";
    
    private int choiceMode = PhotoAlbumDirFunctionActivity.CHOICE_MODE_MUTIPLE;
    private int limitNum;
    
    /**
     * 获取 相册详情的类
     * @param context
     * @param dirId 相关文件的buckId
     * @param dirName 目录名字
     * @param choiceMode 相册选择的模式
     * @param limitNum  多选相册时 限制的个数
     * @return
     */
    public static Intent getLaunchIntent(Context context, String dirId, String dirName, int choiceMode, int limitNum){
        return new Intent(context, PhotoAlbumPicFunctionActivity.class).putExtra(KEY_ID, dirId)
                .putExtra(KEY_NAME, dirName)
                .putExtra(KEY_CHOICE, choiceMode).putExtra(KEY_NUMBER, limitNum);
    }
    
    @Override
    protected void onCreate(Bundle savedState) {
        // TODO Auto-generated method stub
        super.onCreate(savedState);
        setContentView(R.layout.willchun_lib_activity_photo_album_pic);
        
        
        if(getIntent() != null){
            bucketId = getIntent().getStringExtra(KEY_ID); 
            choiceMode = getIntent().getIntExtra(KEY_CHOICE, PhotoAlbumDirFunctionActivity.CHOICE_MODE_MUTIPLE);
            limitNum = getIntent().getIntExtra(KEY_NUMBER, 99);
            if(!TextUtils.isEmpty(getIntent().getStringExtra(KEY_NAME))){
                setTitle(getIntent().getStringExtra(KEY_NAME));
            }
        }
        
        if(TextUtils.isEmpty(bucketId)){
            ToastView.show(this, "数据异常");
            finish();
        }
        
        //单选没有滑动
        if(choiceMode == PhotoAlbumDirFunctionActivity.CHOICE_MODE_SINGLE){
            aq.id(R.id.willchun_lib_activity_photo_album_pic_pre_hs).invisible();
        }
        
        mAdapter = new AndAdapter<String>(this, R.layout.willchun_lib_item_photo_album_pic_grid) {

            @Override
            protected void update(String item, AndQuery aq, int position) {
                // TODO Auto-generated method stub
                
            }

            @Override
            protected void update(final String item, final int position, final AndQuery aq, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                super.update(item, position, aq, convertView, parent);
                //处理图片为方块
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
                
                dealUpdate(item, aq, convertView);
                
            }
        };
        mGridView = (GridView) aq.id(R.id.willchun_lib_activity_photo_album_pic_grid).getView(); 
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                //暂时的  大框图 也触发选择事件
                if(selectedData.containsKey(mAdapter.getItem(arg2))){
                    ((CheckBox)arg1.findViewById(R.id.willchun_lib_item_photo_album_pic_grid_cb)).setChecked(false);
                }else {
                    ((CheckBox)arg1.findViewById(R.id.willchun_lib_item_photo_album_pic_grid_cb)).setChecked(true);
                }
            }
        });
        
        
        
        mHorizontalScrollView = (HorizontalScrollView) aq.id(R.id.willchun_lib_activity_photo_album_pic_pre_hs).getView();
        mImageLayout = (LinearLayout)aq.id(R.id.selected_image_layout).getView();
        
        aq.id(R.id.willchun_lib_activity_photo_album_pic_pre_btn).clicked(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(selectedData == null || selectedData.size() == 0){
                    return;
                }
                Iterator<String> iterator = selectedData.keySet().iterator();
                ArrayList<String> ret = new ArrayList<String>();
                while(iterator.hasNext()){
                    ret.add(iterator.next());
                }
                Intent intent = new Intent();
                intent.putStringArrayListExtra(PhotoAlbumDirFunctionActivity.DATA_KEY, ret);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        refreshData();
    }
    
    
    /**
     * 多选的处理
     * @param aq
     */
    private void dealUpdate(final String item, final AndQuery aq, final View convertView){
        CheckBox button = (CheckBox)aq.id(R.id.willchun_lib_item_photo_album_pic_grid_cb).getView();
        
        button.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                
                
                if(isChecked){
                    if (!selectedData.containsKey(item)) {
                        convertView.findViewById(R.id.willchun_lib_item_photo_album_pic_grid_edge_iv).setVisibility(View.VISIBLE);
                        if(choiceMode == PhotoAlbumDirFunctionActivity.CHOICE_MODE_SINGLE){
                            selectedData.clear();
                            selectedData.put(item, null);
                            mAdapter.notifyDataSetChanged();
                        }else if(choiceMode == PhotoAlbumDirFunctionActivity.CHOICE_MODE_MUTIPLE){
                            if(selectedData.size() >= limitNum){
                                ((CheckBox)buttonView).setChecked(false);
                                convertView.findViewById(R.id.willchun_lib_item_photo_album_pic_grid_edge_iv).setVisibility(View.GONE);
                                Toast.makeText(getBaseContext(), "你最多只能选择" + limitNum + "张照片", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            
                            ImageView imageView = (ImageView) LayoutInflater.from(getBaseContext()).inflate(
                                    R.layout.willchun_lib_item_phtoto_album_pic_choose_imageview, mImageLayout,
                                    false);
                            AndQuery andQuery = new AndQuery(imageView);
                            andQuery.id(R.id.choose_iv).image(item, true, true, UIUtils.getInstance(getWindowManager()).dip2Px(36), 0, null, 0);
                      
                            selectedData.put(item, imageView);
                            mImageLayout.addView(imageView);
                            imageView.postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    int off = mImageLayout.getMeasuredWidth() - mHorizontalScrollView.getWidth();
                                    if (off > 0) {
                                        mHorizontalScrollView.smoothScrollTo(off, 0);
                                    }
                                }
                            }, 100);
                            
                            imageView.setOnClickListener(new OnClickListener() {
                                
                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    if (selectedData.containsKey(item)) {
                                        mImageLayout.removeView(selectedData.get(item));
                                        selectedData.remove(item);
                                        ((CheckBox)buttonView).setChecked(false);
                                        convertView.findViewById(R.id.willchun_lib_item_photo_album_pic_grid_edge_iv).setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                    }
                }else{
                    if (selectedData.containsKey(item)) {
                        convertView.findViewById(R.id.willchun_lib_item_photo_album_pic_grid_edge_iv).setVisibility(View.GONE);
                        mImageLayout.removeView(selectedData.get(item));
                        selectedData.remove(item);
                    }
                    
                }
                displayFinish();
            }
        });
        
        if(selectedData.containsKey(item)){
            button.setChecked(true);
            aq.id(R.id.willchun_lib_item_photo_album_pic_grid_edge_iv).visible();
        }else {
            button.setChecked(false);
            aq.id(R.id.willchun_lib_item_photo_album_pic_grid_edge_iv).gone();
        }
    }
    
    private void refreshData(){
        new AsyncTask<Void, Void, ArrayList<String>>(){
            
            
            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                aq.id(R.id.progressbar).visible();
            }

            @Override
            protected ArrayList<String> doInBackground(Void... params) {
                // TODO Auto-generated method stub
                ArrayList<String> ret = new ArrayList<String>();
                ContentResolver mResolver = getBaseContext().getContentResolver();
                String[] IMAGE_PROJECTION = new String[] { ImageColumns.DATA };

                String selection = ImageColumns.BUCKET_ID + "= ?  AND "
                        + ImageColumns.MIME_TYPE + " IN (?,?,?)";
                
                String[] selectionArgs = new String[] { bucketId, IMG_JPG,
                        IMG_JPEG, IMG_PNG };
                Cursor cursor = mResolver.query(
                        Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        selection, selectionArgs, null);
                if (null != cursor) {
                    if (cursor.getCount() > 0) {
                        while (cursor.moveToNext()) {
                            ret.add(cursor.getString(0));
                        }
                    }
                    cursor.close();
                }
                return ret;
            }

            @Override
            protected void onPostExecute(ArrayList<String> result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                aq.id(R.id.progressbar).gone();
                for(String str : result)
                    mAdapter.add(str);
                mAdapter.notifyDataSetChanged();
            }
            
        }.execute();
    }
    
    private void displayFinish(){
        if(selectedData.size() == 0){
            aq.id(R.id.willchun_lib_activity_photo_album_pic_pre_btn).text("选择");
        }else {
            if(choiceMode == PhotoAlbumDirFunctionActivity.CHOICE_MODE_MUTIPLE){
                aq.id(R.id.willchun_lib_activity_photo_album_pic_pre_btn).text("确定(" + selectedData.size() +")");
            }else {
                aq.id(R.id.willchun_lib_activity_photo_album_pic_pre_btn).text("确定");
            }
                
        }
        
    }
}
