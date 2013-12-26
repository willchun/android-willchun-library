/**
 *
 * Copyright 2013 Anjuke. All rights reserved.
 * AndCallback.java
 *
 */
package com.willchun.library.base;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.AQUtility;


/**
 *@author chunwang (wcly10@gmail.com)
 *@date 2013-11-2
 */
public abstract class AndCallback<T> extends AjaxCallback<T> {
    public static final int HTTP_200 = 200;//请求成功
    public static final int HTTP_500 = 500;//内部服务器错误
    public static final int HTTP_403 = 403;//禁止访问
    public static final int HTTP_404 = 404;//无法找到文件
    @Override
    public void callback(String url, T object, AjaxStatus status) {
        // TODO Auto-generated method stub
        super.callback(url, object, status);
        AQUtility.debug("result:" + object + "*********url:" + url);
        onComplete(url, object, status);
        switch (status.getCode()) {
        case AjaxStatus.TRANSFORM_ERROR:
            break;
        case HTTP_200:
            on200Success(url, object);
            break;
       case HTTP_403:
            on403NetError(url, status);
            break;
        case HTTP_404:
            on404NetError(url, status);
            break;    
        case HTTP_500:
            on500NetError(url, status);
            break;
        default:
            break;
        }
    }
    /**
     * 请求成功
     * @param url
     * @param object
     */
    protected abstract void on200Success(String url, T object);
    
    /**
     * 禁止访问
     * @param url
     * @param status
     */
    protected void on403NetError(String url, AjaxStatus status) {
    }
    
    /**
     * 无法找到文件
     * @param url
     * @param status
     */
    protected void on404NetError(String url, AjaxStatus status) {
    }

    /**
     * 内部服务器错误
     * @param url
     * @param status
     */
    protected void on500NetError(String url, AjaxStatus status) {
    
    }
    
    /**
     * 请求完成
     * @param url
     * @param object
     * @param status
     */
    protected void onComplete(String url, T object,  AjaxStatus status) {
        
    }
    
    
    
}
