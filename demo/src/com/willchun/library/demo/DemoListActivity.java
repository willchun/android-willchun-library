/**
 *
 * Copyright 2014 IOTEK. All rights reserved.
 * DemoListActivity.java
 *
 */
package com.willchun.library.demo;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

/**
 *@author willchun (277143980@qq.com)
 *@date 2014-2-11
 */
public abstract class DemoListActivity extends ListActivity implements OnItemClickListener{
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        
        getActionBar().setDisplayHomeAsUpEnabled(isBackDisplay());
        getActionBar().setHomeButtonEnabled(isBackDisplay());
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
                android.R.layout.simple_list_item_1, android.R.id.text1, getListName());
        
        getListView().setAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }
    
    public void _intent(Class<?> cls){
        startActivity(new Intent(this, cls));
    }
    
    public abstract String[] getListName();
    
    public abstract boolean isBackDisplay();
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            if(isBackDisplay())
                finish();
            break;
        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
}
