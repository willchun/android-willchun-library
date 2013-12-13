/**
 *
 * Copyright 2013 Anjuke. All rights reserved.
 * AndActivity.java
 *
 */
package com.willchun.library.base;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;



public abstract class AndActivity extends FragmentActivity {
    
    private Bundle mSavedState;
    protected AndQuery aq;
    
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        aq = new AndQuery(this);
        
        if (savedState == null) {
            this.mSavedState = getIntent() == null? null :getIntent().getExtras();
        } else {
            this.mSavedState = savedState;
        }
        if (getActionBar() != null) {
            getActionBar().setHomeButtonEnabled(isHomeButtonEnabled());
            getActionBar().setDisplayHomeAsUpEnabled(
                    isHomeButtonEnabled());
        }
    };
    
    
    
    public AndQuery getAq() {
        return aq;
    }



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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            onHomeAsUpClick();
            break;
        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    protected void onHomeAsUpClick() {
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mSavedState != null)
            outState.putAll(mSavedState);
    }

    public void addFragment(int containerViewId, Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction().add(containerViewId, fragment, tag).commit();
    }

    public void addFragment(int containerViewId, Fragment fragment) {
        getSupportFragmentManager().beginTransaction().add(containerViewId, fragment).commit();
    }

    public void replaceFragment(int containerViewId, Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction().replace(containerViewId, fragment, tag)
                .commit();
    }
    
    public void replaceFragmentBackStack(int containerViewId, Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction().replace(containerViewId, fragment, tag).
                addToBackStack(null).commit();
    }

    public void replaceFragment(int containerViewId, Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(containerViewId, fragment).commit();
    }
    
    public void replaceFragmentBackStack(int containerViewId, Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(containerViewId, fragment).addToBackStack(null).commit();
    }
}
