package com.willchun.library.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import com.androidquery.AbstractAQuery;

public class AndQuery extends AbstractAQuery<AndQuery> {

	public AndQuery(Activity act) {
		super(act);
	}

	public AndQuery(View view) {
		super(view);
	}

	public AndQuery(Context context) {
		super(context);
	}

	public AndQuery(Activity act, View root) {
		super(act, root);
	}

	public AndQuery textColorId(int color) {
		if (view instanceof TextView) {
			TextView tv = (TextView) view;
			tv.setTextColor(getContext().getResources().getColor(color));
		}
		return this;
	}
	
    public Resources getResources() {
        if (view != null)
            return view.getResources();
        return null;
    }

}
