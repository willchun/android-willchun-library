/**
 *
 * Copyright 2013 Anjuke. All rights reserved.
 * DbUtils.java
 *
 */
package com.willchun.library.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

/**
 * 文件操作相关的工具类
 *@author chunwang (wcly10@gmail.com)
 *@date 2013-11-1
 */
public class FileUtils {
    
    
    /**
     * 指定Assests应用目录下 assetsDirectoryPath位置下的所有文件到应用database目录下
     * @param context
     * @param assetsDirectoryPath
     * @throws IOException
     */
    public static void copyAssetsDirectory2Database(Context context, String assetsDirectoryPath) throws IOException{
        copyAssetsDirectory2Path(context, assetsDirectoryPath, "/data/data/" + context.getPackageName() + "/databases");
    }
    
    /**
     * 指定Assests应用目录下 assetsDirectoryPath位置下的所有文件到目标位置
     * @param assetsDirectoryPath assests目录下某文件的路径
     * @param targetPath 目标路径
     * @return
     * @throws IOException 
     */
    public static void copyAssetsDirectory2Path(Context context, String assetsDirectoryPath, String targetPath) throws IOException{
        
        if(checkFileDirectoryPath(targetPath, true)){
            String[] fileNames = context.getAssets().list(assetsDirectoryPath);
            for(String fileName : fileNames){
                copyFileStream2Path(context.getAssets().open(assetsDirectoryPath + "/" + fileName), targetPath, fileName);
            }
        }
    }
    
    /**
     * 复制文件流 到  指定目录下
     * @param inputStream   目标文件流 
     * @param filePath  文件路径
     * @param fileName  文件名字
     * @throws IOException 
     */
    public static boolean copyFileStream2Path(InputStream inputStream, String filePath, String fileName) throws IOException{
        //判断文件路径是否存在
        if(checkFileDirectoryPath(filePath, true) 
                && checkFilePath(filePath + "/" + fileName, true)){
            FileOutputStream outputStream = new FileOutputStream(filePath + "/" + fileName);
            byte[] b = new byte[1024];
            int len;
            while ((len = inputStream.read(b)) != -1) {
                outputStream.write(b, 0, len);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        }else {
            return false;
        }
        return true;
    }
    
    /**
     * 检查文件夹路径是否存在    
     * @param fileDirectoryPath 文件夹路径
     * @param dealFile true：当指定文件夹不存在时创建，  false： 不做处理
     * @return 表示该文件路径是否存在
     */
    public static boolean checkFileDirectoryPath(String fileDirectoryPath, boolean dealFile){
        
        File path = new File(fileDirectoryPath);
        if(!path.exists() || !path.isDirectory()){
            if(dealFile){
                path.mkdirs();
                if(path.isDirectory()){
                    return true;
                }else {
                    return false;
                }
            }else{
                return false;
            }
        }else {
            return true;
        }
    }
    
    
    /**
     * 检测文件是否存在
     * @param filePath 文件路径 带文件名
     * @param dealFile true：当指定文件不存在时创建，  false： 不做处理
     * @return 表示该文件路径是否存在
     * @throws IOException 
     */
    public static boolean checkFilePath(String fileNamePath, boolean dealFile) throws IOException{
       
        File path = new File(fileNamePath);
        if(!path.exists() || !path.isFile()){
            if(dealFile){
                path.createNewFile();
                if(path.isFile()){
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
                
        }else {
            return true;
        }
    }
}
