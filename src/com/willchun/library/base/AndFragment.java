/**
 *
 * Copyright 2013 Anjuke. All rights reserved.
 * AndFragment.java
 *
 */
package com.willchun.library.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author willchun (wcly10@gmail.com)
 * @date 2013-5-21
 */
public abstract class AndFragment extends Fragment {
    protected AndQuery aq;
    private Bundle mSaveBundle;

    protected Intent intent(Class<?> clz) {
        return new Intent(getActivity(), clz);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (aq = new AndQuery(getActivity(), inflater.inflate(contentView(), container, false))).getView();
    }
    
    

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState == null){
            mSaveBundle = getArguments();
        }else {
            mSaveBundle = savedInstanceState;
        }
        //特有的初始化
        willInit();
    }

    public Bundle getArguments_Will(){
        if(mSaveBundle == null)
            mSaveBundle = new Bundle();
        return mSaveBundle;
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        if(mSaveBundle != null)
            outState.putAll(mSaveBundle);
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
        getActivity().finish();
    }
    
    public AndActivity getAndActivity(){
        return (AndActivity) getActivity();
    }
    
    public abstract void willInit();

    public abstract void onRefreshUI();

    protected abstract int contentView();
   
}
