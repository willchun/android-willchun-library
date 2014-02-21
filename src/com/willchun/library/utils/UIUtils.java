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

    private static DisplayMetrics dm = null;
    private static UIUtils mInstance;

    public static synchronized UIUtils getInstance(WindowManager wm) {
        if (mInstance == null || dm == null) {
            mInstance = new UIUtils(wm);
        }
        return mInstance;
    }

    private UIUtils(WindowManager wm) {
        dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
    }

    /**
     * 获取屏幕绝对的宽度（px）
     * 
     * @return
     */
    public int getWidth() {
        return dm.widthPixels;
    }

    /**
     * 获取屏幕绝对的高度（px）
     * 
     * @return
     */
    public int getHeight() {
        return dm.heightPixels;
    }

    /**
     * 判断小屏幕(像素密度为1.0， 160dip ，320*480) // 1.0 160 320x480 在每英寸160点的显示器上，1dp =
     * 1px。 // 1.5 240 480x800 // 1.5 240 540x960 // 2.0 320 720x1280
     * 
     * @return true 是小屏幕
     */
    public boolean isSmallScreen() {
        return ((dm.density <= 1.0F) && (dm.densityDpi <= 160));
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2Px(int dip) {
        if (dip == 0) {
            return 0;
        } else {
            return (int) (dip * dm.density + 0.5f);
        }
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public int px2Dip(int px) {
        if (px == 0) {
            return 0;
        } else {
            return (int) ((px - 0.5f) / dm.density);
        }
    }

    public static void setLayoutSize(RelativeLayout v, int width, int height) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) v.getLayoutParams();
        if (lp == null) {
            lp = new RelativeLayout.LayoutParams(width, height);
        } else {
            lp.width = width;
            lp.height = height;
        }
        v.setLayoutParams(lp);
    }

    public static void setLayoutSize(FrameLayout v, int width, int height) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) v.getLayoutParams();
        if (lp == null) {
            lp = new FrameLayout.LayoutParams(width, height);
        } else {
            lp.width = width;
            lp.height = height;
        }
        v.setLayoutParams(lp);
    }

    public static void setLayoutSize(LinearLayout v, int width, int height) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) v.getLayoutParams();
        if (lp == null) {
            lp = new LinearLayout.LayoutParams(width, height);
        } else {
            lp.width = width;
            lp.height = height;
        }
        v.setLayoutParams(lp);
    }

    public static void setLayoutHeight(RelativeLayout v, int height) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) v.getLayoutParams();
        lp.height = height;
        v.setLayoutParams(lp);
    }

    public static void setLayoutWidth(RelativeLayout v, int width) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) v.getLayoutParams();
        lp.width = width;
        v.setLayoutParams(lp);
    }

    public static void setLayoutWidth(LinearLayout v, int width) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) v.getLayoutParams();
        lp.width = width;
        v.setLayoutParams(lp);
    }

    public boolean touchInDialog(Activity activity, MotionEvent e) {
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
        return ((e.getX() > leftW) && (e.getX() < rightW) && (e.getY() > topH) && (e.getY() < bottomH));
    }

    public boolean isScreenCenter(MotionEvent e) {
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
    }

    public PointF getLeftBottomPoint() {
        return new PointF((dm.widthPixels / 4) + 0.09f, (dm.heightPixels / 4 * 3) + 0.09f);
    }

    public PointF getRightBottomPoint() {
        return new PointF((dm.widthPixels / 4 * 3) + 0.09f, (dm.heightPixels / 4 * 3) + 0.09f);
    }

    public PointF getLeftPoint() {
        return new PointF(20, dm.heightPixels / 2);
    }

    public PointF getRightPoint() {

        return new PointF(dm.widthPixels - 20, dm.heightPixels / 2);

    }

    public boolean isTouchLeft(MotionEvent e) {

        return (e.getX() < (dm.widthPixels / 2));

    }

    public static int getStatusbarHeight(Context context) {
        Drawable ico = context.getResources().getDrawable(android.R.drawable.stat_sys_phone_call);
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

    public float pxToScaledPx(int px) {
        return px / dm.density;
    }

    public int scaledPxToPx(float scaledPx) {

        return (int) (scaledPx * dm.density);

    }

    public static int getButtonAdvWidth(int count, int margin) {

        int width = dm.widthPixels;
        width = width - (margin * (count + 1));
        width = width / count;
        return width;

    }

}
