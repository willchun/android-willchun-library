/**
 *
 * Copyright 2013 Anjuke. All rights reserved.
 * AndCallback.java
 *
 */
package com.willchun.library.base;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.willchun.library.R;


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
        onComplete(url, object, status);
        
        switch (status.getCode()) {
        case AjaxStatus.TRANSFORM_ERROR:
            break;
        case HTTP_200:
            on200Success(url, object);
            break;
/*        case HTTP_403:
            on403NetError(url, status);
            break;
        case HTTP_404:
            on404NetError(url, status);
            break;    
        case HTTP_500:
            on500NetError(url, status);
            break;    */
        default:
            break;
        }
    }
    protected abstract void on200Success(String url, T object);
    
/*    protected void on403NetError(String url, AjaxStatus status) {
        onErrorLog(url, R.string.error_403, status);
    }
    
    protected void on404NetError(String url, AjaxStatus status) {
        onErrorLog(url, R.string.error_404, status);
    }

    protected void on500NetError(String url, AjaxStatus status) {
        onErrorLog(url, R.string.error_500, status);
    }*/
    private void onErrorLog(String url, int resId, AjaxStatus status){
       
    }
    
    protected void onComplete(String url, T object,  AjaxStatus status) {
        
    }
    
    
    
}
