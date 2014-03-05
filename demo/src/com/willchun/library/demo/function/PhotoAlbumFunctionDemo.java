/**
 *
 * Copyright 2014 IOTEK. All rights reserved.
 * PhotoAlbumFunction.java
 *
 */
package com.willchun.library.demo.function;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.Log;


import com.willchun.library.base.AndActivity;
import com.willchun.library.utils.LogUtils;

/**
 *@author willchun (277143980@qq.com)
 *@date 2014-3-5
 */
public class PhotoAlbumFunctionDemo extends AndActivity {

    private final String IMG_JPG="image/jpg";
    private final String IMG_JPEG="image/jpeg";
    private final String IMG_PNG="image/png";
    
    @Override
    protected void onCreate(Bundle savedState) {
        // TODO Auto-generated method stub
        super.onCreate(savedState);
        getImageDir(this);
        
    }
    
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
//                  Log.e("PicPath==>", cursor.getString(2));
                    dirInfo.setPicCount(cursor.getInt(3));
                    dirInfo.setUserOtherPicSoft(false);
                    list.add(dirInfo);
                    LogUtils.e("Path", dirInfo.getDirName());
                }
            }
            cursor.close();
        }
        Log.e("list.size()===>",""+ list.size());
        return list;
    }
}
