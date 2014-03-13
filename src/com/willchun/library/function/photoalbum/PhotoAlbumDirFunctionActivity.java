/**
 *
 * Copyright 2014 IOTEK. All rights reserved.
 * PhotoAlbumFunction.java
 *
 */
package com.willchun.library.function.photoalbum;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.ImageColumns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.willchun.library.R;
import com.willchun.library.base.AndActivity;
import com.willchun.library.base.AndAdapter;
import com.willchun.library.base.AndQuery;
import com.willchun.library.utils.UIUtils;

/**
 * 使用相册选择控件的步骤：
 * 
 * 1）在AndroidManifest里面 增加 声明用到的activity
 *          <activity android:name="com.willchun.library.function.photoalbum.PhotoAlbumDirFunctionActivity" android:screenOrientation="portrait"></activity>
 *          <activity android:name="com.willchun.library.function.photoalbum.PhotoAlbumPicFunctionActivity" android:screenOrientation="portrait"></activity>
 * 2）可以通过以下请求方式 分别获取 多选（可限定个数） 和 单选
 *  getLaunchIntentSingleChoice(Context context)
 *  getLaunchIntentMutipleChoice(Context context, int number)  number:代表多选的个数 
 * 3）使用startActivityForResult获取返回数据， 
 * 通过关键词 PhotoAlbumDirFunctionActivity.DATA_KEY 进行获取
 * eg.   data.getStringArrayListExtra(PhotoAlbumDirFunctionActivity.DATA_KEY)  当时单选的时候只返回一个
 *@author willchun (277143980@qq.com)
 *@date 2014-3-5
 */
public class PhotoAlbumDirFunctionActivity extends AndActivity {

    private final String IMG_JPG="image/jpg";
    private final String IMG_JPEG="image/jpeg";
    private final String IMG_PNG="image/png";
    
    private AndAdapter<PhotoDirInfo> mAdapter;
    public static final String DATA_KEY = "data_key";
    public static final int CHOICE_MODE_SINGLE = 0x01;
    public static final int CHOICE_MODE_MUTIPLE = 0x02;
    
    private static final String KEY_CHOICE = "key_choice";
    private static final String KEY_NUMBER = "key_number";
    private int choiceMode = CHOICE_MODE_MUTIPLE;
    /**
     * 限制的个数
     */
    private int limitNum = 99;

    /**
     * 获取单选相册的相关类
     * @param context
     * @return
     */
    public static Intent getLaunchIntentSingleChoice(Context context){
        return new Intent(context, PhotoAlbumDirFunctionActivity.class)
        .putExtra(KEY_CHOICE, CHOICE_MODE_SINGLE);
    }
    
    /**
     * 获取  多选相册的相关类
     * @param context
     * @param num 多选图的最多个数      如果为0 ： 采用模型默认的个数
     * @return
     */
    public static Intent getLaunchIntentMutipleChoice(Context context, int number){
        if(number < 0){
            throw new RuntimeException("多选相册(PhotoAlbumDirFunctionActivity)的选择个数不能小于0");
        }
        return new Intent(context, PhotoAlbumDirFunctionActivity.class)
        .putExtra(KEY_CHOICE, CHOICE_MODE_MUTIPLE).putExtra(KEY_NUMBER, number);
    }
    
    @Override
    protected void onCreate(Bundle savedState) {
        // TODO Auto-generated method stub
        super.onCreate(savedState);
        setContentView(R.layout.willchun_lib_activity_photo_album_dir);
        
        if(getIntent() != null){
            choiceMode = getIntent().getIntExtra(KEY_CHOICE, CHOICE_MODE_MUTIPLE);
            int num = getIntent().getIntExtra(KEY_NUMBER, 0);
            if(num != 0){
                limitNum = num;
            }
        }
        
        mAdapter = new AndAdapter<PhotoDirInfo>(this, R.layout.willchun_lib_item_photo_album_dir_list) {

            @Override
            protected void update(PhotoDirInfo item, AndQuery aq, int position) {
                // TODO Auto-generated method stub
            }

            @Override
            protected void update(PhotoDirInfo item, int position, AndQuery aq, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                super.update(item, position, aq, convertView, parent);
                aq.id(R.id.willchun_lib_item_photo_album_dir_list_title_tv).text(item.getDirName());
                aq.id(R.id.willchun_lib_item_photo_album_dir_list_content_tv).text(item.getPicCount() + "张");
                String url = item.getFirstPicPath();
                Bitmap placeholder = aq.getCachedImage(R.drawable.willchun_lib_photo_thumb_bg);
                if(aq.shouldDelay(position, convertView, parent, url)){
                    aq.id(R.id.willchun_lib_item_photo_album_dir_list_iv).image(placeholder);
                }else{
                    aq.id(R.id.willchun_lib_item_photo_album_dir_list_iv).image(url, true, true, UIUtils.getInstance(getWindowManager()).dip2Px(75), 0, placeholder, 0);
                }
            }
            
            

        };

        for(PhotoDirInfo info : getImageDir(this))
            mAdapter.add(info);
        
        aq.id(R.id.willchun_lib_photo_album_dir_list_lv).adapter(mAdapter);
        
        aq.id(R.id.willchun_lib_photo_album_dir_list_lv).getListView().setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                startActivityForResult(PhotoAlbumPicFunctionActivity.getLaunchIntent(getBaseContext(), mAdapter.getItems().get(arg2).getDirId(), choiceMode, limitNum)
                        , 101);
            }
        });
        
    }
    
    /**
     * 获取照片的目录
     * @param context
     * @return
     */
    private synchronized ArrayList<PhotoDirInfo> getImageDir(Context context) {
        ArrayList<PhotoDirInfo> list = null ;
        PhotoDirInfo dirInfo=null;
        ContentResolver mResolver = context.getContentResolver();
        String[] IMAGE_PROJECTION=new String[]{ImageColumns.BUCKET_ID,ImageColumns.BUCKET_DISPLAY_NAME,ImageColumns.DATA,"COUNT(*) AS "+ImageColumns.DATA};
        
        String selection=" 1=1 AND "+ImageColumns.MIME_TYPE+" IN (?,?,?)) GROUP BY ("+Images.ImageColumns.BUCKET_ID+") ORDER BY ("+Images.ImageColumns.BUCKET_DISPLAY_NAME;
        
        String[] selectionArgs=new String[] {IMG_JPG,IMG_JPEG,IMG_PNG};
//      String selection=ImageColumns.MIME_TYPE+" IN ("+IMG_JPG+","+IMG_PNG+")) GROUP BY ("+ImageColumns.BUCKET_ID+") ORDER BY ("+Images.ImageColumns.BUCKET_DISPLAY_NAME;
        
        Cursor cursor = mResolver.query(Images.Media.EXTERNAL_CONTENT_URI,IMAGE_PROJECTION,selection,selectionArgs,null);
        if(null!=cursor){
            if(cursor.getCount()>0){
                list=new ArrayList<PhotoDirInfo>();
                while(cursor.moveToNext()){
                    dirInfo=new PhotoDirInfo();
                    dirInfo.setDirId(cursor.getString(0));
                    dirInfo.setDirName(cursor.getString(1));
                    dirInfo.setFirstPicPath(cursor.getString(2));
                    dirInfo.setPicCount(cursor.getInt(3));
                    dirInfo.setUserOtherPicSoft(false);
                    list.add(dirInfo);
                }
            }
            cursor.close();
        }
        
        return list;
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);
        if(arg1 == RESULT_OK && arg0 == 101){
                setResult(RESULT_OK, arg2);
                finish();
        }
    }
    
    
    
}
