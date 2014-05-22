package com.willchun.library.demo.view;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.willchun.library.base.AndActivity;
import com.willchun.library.demo.R;
import com.willchun.library.utils.Blur;
import com.willchun.library.utils.UIUtils;

public class BlurImageViewDemo extends AndActivity{
	
	private ListView mListView;
	private ImageView mBlurImageView;
	private ImageView mOriginalImageView;
	
	
	@Override
	protected void onCreate(Bundle savedState) {
		// TODO Auto-generated method stub
		super.onCreate(savedState);
		setContentView(R.layout.demo_view_blur_image);
		
		mListView = aq.id(R.id.demo_view_blur_lv).getListView();
		mListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		});	
		
		mBlurImageView = aq.id(R.id.demo_view_blur_iv).getImageView();
		mOriginalImageView = aq.id(R.id.demo_view_blur_original_iv).getImageView();
		
		mOriginalImageView.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
			
			@Override
			public boolean onPreDraw() {
				// TODO Auto-generated method stub
				mOriginalImageView.getViewTreeObserver().removeOnPreDrawListener(this);
				mOriginalImageView.buildDrawingCache();
				update();
				return true;
			}
		});
		String[] arrays = getResources().getStringArray(R.array.willchun_demo_list_content);
		mListView.setAdapter(new ArrayAdapter<String>(this, R.layout.demo_willchun_list_item,arrays));
	}
	
	
	public void update(){
		
		Bitmap originalBitmap = mOriginalImageView.getDrawingCache();
		Bitmap blurBitmap = Blur.fastblur(this, originalBitmap, 20);
		blurBitmap = Bitmap.createScaledBitmap(blurBitmap
				, UIUtils.getInstance(getWindowManager()).getWidth()
				, (int) (blurBitmap.getHeight()
						* ((float) UIUtils.getInstance(getWindowManager()).getWidth()) / (float) blurBitmap.getWidth())
				, false);
		blurBitmap = Bitmap.createBitmap(blurBitmap, 0, mBlurImageView.getTop(), mBlurImageView.getWidth(),
				UIUtils.getInstance(getWindowManager()).dip2Px(50));
		mBlurImageView.setImageBitmap(blurBitmap);
		
	}
}
