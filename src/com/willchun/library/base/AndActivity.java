/**
 *
 * Copyright 2013 Anjuke. All rights reserved.
 * AndActivity.java
 *
 */
package com.willchun.library.base;


import com.androidquery.callback.BitmapAjaxCallback;
import com.androidquery.util.AQUtility;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import com.willchun.library.utils.StatisProxy;

public abstract class AndActivity extends FragmentActivity {
    private boolean isClickHomeFinish = true;
    private Bundle mSavedState;
    protected AndQuery aq;
    
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        if(aq == null){
            aq = new AndQuery(this);
        }
        
        if (savedState == null) {
            this.mSavedState = getIntent() == null? null :getIntent().getExtras();
        } else {
            this.mSavedState = savedState;
        }
        
        if(Build.VERSION.SDK_INT >= 11){
            if (getActionBar() != null) {
                getActionBar().setHomeButtonEnabled(isHomeButtonEnabled());
                getActionBar().setDisplayHomeAsUpEnabled(
                        isHomeButtonEnabled());
            }
        }
            
    };
    
    protected Intent intent(Class<?> clz) {
        return new Intent(this, clz);
    }
    
    /**
     * 获取Intent传过来的参数，用于替代方法getIntent().getExtras() <br>
     * 用于解决Activity重新启动后，原有的Intent中的参数丢失问题，已做容错处理，可以放心调用
     * 
     * @return
     */
    public Bundle getIntentExtras() {
        if (mSavedState == null)
            mSavedState = new Bundle();
        return mSavedState;
    }
    
    
    protected boolean isHomeButtonEnabled() {
        return true;
    }
    
    public AndApplication app(){
        return (AndApplication)getApplication();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            if(isClickHomeFinish)
                finish();
            break;
        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void setClickHomeFinish(boolean isClickHomeFinish) {
        this.isClickHomeFinish = isClickHomeFinish;
    }
    
    

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        StatisProxy.onPause(this);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        StatisProxy.onResume(this);
    }
    
    

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (isTaskRoot()) {
            AQUtility.cleanCacheAsync(this);
        }
    }
    
    

    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        super.onLowMemory();
        //低内存处理
        //clear all memory cached images when system is in low memory
        //note that you can configure the max image cache count, see CONFIGURATION
         BitmapAjaxCallback.clearCache();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mSavedState != null)
            outState.putAll(mSavedState);
    }

    public void replaceFragment(int containerViewId, Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(containerViewId, fragment).commit();
    }
    
    public void replaceFragmentBackStack(int containerViewId, Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(containerViewId, fragment).addToBackStack(null).commit();
    }
}
