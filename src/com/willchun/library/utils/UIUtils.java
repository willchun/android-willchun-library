package com.willchun.library.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 此方法提供了关于Android屏幕显示相关的功能 (使用前调用initDisplayMetrics，进行初始化)
 * 
 * @author will
 */
public class UIUtils {

    private static String ERROR_INIT = "初始化失败，请使用前调用initDisplayMetrics进行初始化";
	private static DisplayMetrics dm = null;

    public static void initDisplayMetrics(WindowManager wm) {
		if (dm == null) {
			dm = new DisplayMetrics();
			wm.getDefaultDisplay().getMetrics(dm);
		}
	}

    /**
     * 获取屏幕绝对的宽度（px）
     * 
     * @return
     */
    public static int getWidth() {
        try {
            return dm.widthPixels;
        } catch (NullPointerException e) {
            // TODO: handle exception
            throw new RuntimeException(ERROR_INIT);
        }

    }

    /**
     * 获取屏幕绝对的高度（px）
     * 
     * @return
     */
    public static int getHeight() {
        try {
            return dm.heightPixels;
        } catch (NullPointerException e) {
            // TODO: handle exception
            throw new RuntimeException(ERROR_INIT);
        }
    }

    /**
     * 判断小屏幕(像素密度为1.0， 160dip ，320*480)
     *  // 1.0 160 320x480 在每英寸160点的显示器上，1dp = 1px。
     *  // 1.5 240 480x800
     *  // 1.5 240 540x960
     *  // 2.0 320 720x1280
     * @return true 是小屏幕
     */
	public static boolean isSmallScreen() {
        try {
            return ((dm.density <= 1.0F) && (dm.densityDpi <= 160));
        } catch (NullPointerException e) {
            // TODO: handle exception
            throw new RuntimeException("初始化失败，请使用前调用initDisplayMetrics进行初始化");
        }
	}

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2Px(int dip) {
        try {
            if (dip == 0) {
                return 0;
            } else {
                return (int) (dip * dm.density + 0.5f);
            }
        } catch (NullPointerException e) {
            // TODO: handle exception
            throw new RuntimeException(ERROR_INIT);
        }
    }
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2Dip(int px) {
        try {
            if (px == 0) {
                return 0;
            } else {
                return (int) ((px - 0.5f) / dm.density);
            }
        } catch (NullPointerException e) {
            // TODO: handle exception
            throw new RuntimeException(ERROR_INIT);
        }
    }


    public static void setLayoutSize(RelativeLayout v, int width, int height) {
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) v
				.getLayoutParams();
		if (lp == null) {
			lp = new RelativeLayout.LayoutParams(width, height);
		} else {
			lp.width = width;
			lp.height = height;
		}
		v.setLayoutParams(lp);
	}
	
    public static void setLayoutSize(FrameLayout v, int width, int height) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) v
                .getLayoutParams();
        if (lp == null) {
            lp = new FrameLayout.LayoutParams(width, height);
        } else {
            lp.width = width;
            lp.height = height;
        }
        v.setLayoutParams(lp);
    }
	
    public static void setLayoutSize(LinearLayout v, int width, int height) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) v
                .getLayoutParams();
        if (lp == null) {
            lp = new LinearLayout.LayoutParams(width, height);
        } else {
            lp.width = width;
            lp.height = height;
        }
        v.setLayoutParams(lp);
    }

    public static void setLayoutHeight(RelativeLayout v, int height) {
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) v
				.getLayoutParams();
		lp.height = height;
		v.setLayoutParams(lp);
	}

    public static void setLayoutWidth(RelativeLayout v, int width) {
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) v
				.getLayoutParams();
		lp.width = width;
		v.setLayoutParams(lp);
	}

    public static void setLayoutWidth(LinearLayout v, int width) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) v
                .getLayoutParams();
        lp.width = width;
        v.setLayoutParams(lp);
    }

	public static boolean touchInView(View v, MotionEvent e) {
		return false;
	}

	public static boolean touchInDialog(Activity activity, MotionEvent e) {
        try {

            // WindowManager.LayoutParams wlp =
            // activity.getWindow().getAttributes();
            int leftW, rightW, topH, bottomH;

            // if (wlp.width > 0 && wlp.height > 0) {
            // leftW = (dm.widthPixels - wlp.width) / 2;
            // rightW = dm.widthPixels - leftW;
            // topH = (dm.heightPixels - wlp.height) / 2;
            // bottomH = dm.heightPixels - topH;
            // } else {
            leftW = 8; // (dm.widthPixels - 16) / 2;
            rightW = dm.widthPixels - leftW;
            topH = 0; // (dm.heightPixels - 80) / 2;
            bottomH = 450;
            // }
            return ((e.getX() > leftW) && (e.getX() < rightW) && (e.getY() > topH) && (e
                    .getY() < bottomH));

        } catch (NullPointerException exception) {
            // TODO: handle exception
            throw new RuntimeException(ERROR_INIT);
        }

	}

	public static boolean isScreenCenter(MotionEvent e) {
        try {
            boolean ret = true;
            if (e.getX() < (dm.widthPixels / 2 - 25)) {
                ret = false;
            }
            if (e.getX() > (dm.widthPixels / 2 + 25)) {
                ret = false;
            }
            if (e.getY() < (dm.heightPixels / 2 - 25)) {
                ret = false;
            }
            if (e.getY() > (dm.heightPixels / 2 + 25)) {
                ret = false;
            }
            return ret;
        } catch (NullPointerException nullPointerException) {
            // TODO: handle exception
            throw new RuntimeException(ERROR_INIT);
        }

	}

	public static PointF getLeftBottomPoint() {
        try {
            return new PointF((dm.widthPixels / 4) + 0.09f,
                    (dm.heightPixels / 4 * 3) + 0.09f);
        } catch (NullPointerException e) {
            // TODO: handle exception
            throw new RuntimeException(ERROR_INIT);
        }
	}

	public static PointF getRightBottomPoint() {
        try {
            return new PointF((dm.widthPixels / 4 * 3) + 0.09f,
                    (dm.heightPixels / 4 * 3) + 0.09f);
        } catch (NullPointerException e) {
            // TODO: handle exception
            throw new RuntimeException(ERROR_INIT);
        }

	}

	public static PointF getLeftPoint() {
        try {
            return new PointF(20, dm.heightPixels / 2);
        } catch (NullPointerException e) {
            // TODO: handle exception
            throw new RuntimeException(ERROR_INIT);
        }

	}

	public static PointF getRightPoint() {
        try {
            return new PointF(dm.widthPixels - 20, dm.heightPixels / 2);
        } catch (NullPointerException e) {
            // TODO: handle exception
            throw new RuntimeException(ERROR_INIT);
        }

	}

	public static boolean isTouchLeft(MotionEvent e) {
        try {
            return (e.getX() < (dm.widthPixels / 2));
        } catch (NullPointerException e2) {
            // TODO: handle exception
            throw new RuntimeException(ERROR_INIT);
        }

	}

	public static int getStatusbarHeight(Context context) {
		Drawable ico = context.getResources().getDrawable(
				android.R.drawable.stat_sys_phone_call);
		return ico.getIntrinsicHeight();
	}

	public static void setActivitySizePos(Activity activity) {
		WindowManager m = activity.getWindowManager();
		Display d = m.getDefaultDisplay();
		WindowManager.LayoutParams p = activity.getWindow().getAttributes();
		p.y = 4;
		p.height = (int) (d.getHeight() - 72);
		activity.getWindow().setAttributes(p);
	}



	public static float pxToScaledPx(int px) {
        try {
            return px / dm.density;
        } catch (NullPointerException e) {
            // TODO: handle exception
            throw new RuntimeException(ERROR_INIT);
        }
	}

	public static int scaledPxToPx(float scaledPx) {
        try {
            return (int) (scaledPx * dm.density);
        } catch (NullPointerException e) {
            // TODO: handle exception
            throw new RuntimeException(ERROR_INIT);
        }

	}

	public static int getButtonAdvWidth(int count, int margin) {
        try {
            int width = dm.widthPixels;
            width = width - (margin * (count + 1));
            width = width / count;
            return width;
        } catch (NullPointerException e) {
            // TODO: handle exception
            throw new RuntimeException(ERROR_INIT);
        }

	}


	
    /**
     * 把图片处理成圆角
     * 
     * @param bitmap
     * @return
     */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                        .getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        // final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 24;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.WHITE);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
	}
	
    /**
     * 将view转化为bitmap
     * 
     * @param view
     * @return
     */
    public static Bitmap convertViewToBitmap(View view) {

        if (view.getLayoutParams() == null) {
            view.setLayoutParams(new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT));
        }

        measureView(view);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.destroyDrawingCache();
        view.buildDrawingCache();

        return view.getDrawingCache();
    }

    // 此方法直接照搬自网络上的一个下拉刷新的demo，此处是“估计”headView的width以及height
    /**
     * 测量View的宽和高 例： measureView(headView); headContentHeight =
     * headView.getMeasuredHeight(); headContentWidth =
     * headView.getMeasuredWidth();
     * 
     * @param child
     */
    public static void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    /**
     * 创建一个带水印的图片
     * 
     * @param src
     * @param watermark
     * @return
     */
    public static Bitmap createBitmap(Bitmap src, Bitmap watermark)
    {
        if (src == null)
        {
            return null;
        }

        int w = src.getWidth();
        int h = src.getHeight();
        int ww = watermark.getWidth();
        int wh = watermark.getHeight();
        // create the new blank bitmap
        Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        // draw src into
        cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
        // draw watermark into
        cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, null);// 在src的右下角画入水印
        // save all clip
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        // store
        cv.restore();// 存储
        return newb;
    }
}
