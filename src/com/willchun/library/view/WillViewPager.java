/**
 *
 * Copyright 2014 IOTEK. All rights reserved.
 * WillViewPager.java
 *
 */
package com.willchun.library.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

/**
 *@author willchun (277143980@qq.com)
 *@date 2014-1-2
 */
public class WillViewPager extends ViewPager implements OnGestureListener{
    
    private GestureDetector mGestureDetector;
    private OnPhotoItemListener mOnPhotoItemListener;

    public WillViewPager(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init();
    }
    
    public WillViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
    }
    
    protected void init(){
        mGestureDetector = new GestureDetector(getContext(), this);
    }
    
    

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        // TODO Auto-generated method stub
        try {
            mGestureDetector.onTouchEvent(arg0);
            getParent().requestDisallowInterceptTouchEvent(true);
            return super.onTouchEvent(arg0);
        } catch (IllegalArgumentException e) {
            // TODO: handle exception
            return false;
        }

    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        // TODO Auto-generated method stub
        try {
            return super.onInterceptTouchEvent(arg0);
        } catch (IllegalArgumentException e) {
            // TODO: handle exception
            return false;
        }
        
    }
    

    @Override
    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        if(mOnPhotoItemListener != null && getAdapter() != null){
            mOnPhotoItemListener.onItemClick(getCurrentItem());
        }
        return false;
    }
    
    
    public void setOnPhotoItemListener(OnPhotoItemListener listener){
        this.mOnPhotoItemListener = listener;  
    }
    
    public OnPhotoItemListener getOnPhotoItemListener(){
        return this.mOnPhotoItemListener;
    }
    
    public interface OnPhotoItemListener{
        void onItemClick(int position);
    }
}
