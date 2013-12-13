/**
 *
 * Copyright 2013 Anjuke. All rights reserved.
 * AndFragment.java
 *
 */
package com.willchun.library.base;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
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
    protected static final int NO_LAYOUT = 0;

    protected Intent intent(Class<?> clz) {
        return new Intent(getActivity(), clz);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (contentView() != NO_LAYOUT)
            return (aq = new AndQuery(getActivity(), inflater.inflate(contentView(), container,
                    false))).getView();

        // 无界面的Fragment
        aq = new AndQuery(getActivity());
        return null;
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

    public abstract void onRefreshUI();

    protected abstract int contentView();
    
    protected ActionBar getSherlockActionBar(){
        return getActivity().getActionBar();
    }
}
