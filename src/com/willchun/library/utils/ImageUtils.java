/**
 *
 * Copyright 2014 IOTEK. All rights reserved.
 * ImageUtils.java
 *
 */
package com.willchun.library.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

/**
 *@author willchun (277143980@qq.com)
 *@date 2014-1-15
 */
public class ImageUtils {
    
    
    public static Bitmap getCircleBitmap(Bitmap bitmap)
    {
        return getCircleBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight());
    }

    public static Bitmap getCircleBitmap(Bitmap bitmap, int width, int height)
    {
        Bitmap croppedBitmap = scaleCenterCrop(bitmap, width, height);
        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();

        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        int radius = 0;
        if(width > height)
        {
            radius = height / 2;
        }
        else
        {
            radius = width / 2;
        }

        canvas.drawCircle(width / 2, height / 2, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(croppedBitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth)
    {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.max(xScale, yScale);

        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;

        float left = (newWidth - scaledWidth) / 2;
        float top = (newHeight - scaledHeight) / 2;

        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(source, null, targetRect, null);

        return dest;
    }
    
    /**
     * 创建一个带水印的图片
     * 
     * @param src
     * @param watermark
     * @return
     */
    public static Bitmap createBitmapWaterMark(Bitmap src, Bitmap watermark) {
        if (src == null) {
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
    
    

    /**
     * 把图片处理成弧度角
     * 
     * @param bitmap
     * @param roundPx 弧度角
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, final float roundPx) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        // final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        //final float roundPx = 24;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.WHITE);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
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
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }
    
    /**
     * 保存bitmap到sd卡
     * 
     * @param bitmap 待保存的bitmap
     * @param path 待保存的目录路径
     * @param fileName 待保存的文件名
     * @param compress 图片压缩率
     * @return 图片路径或者null
     * 
     * 
     * 把图片背景变成白色的
     *                   Bitmap bgBitmap = Bitmap.createBitmap(bitmap);
     *                  Canvas canvas = new Canvas(bgBitmap);
     *                   canvas.drawARGB(255, 255, 255, 255);
     *                   canvas.drawBitmap(bitmap, 0, 0, null);
     */
    public String saveImage2Local(Bitmap bitmap, String path, String fileName, int compress) {
        File imagePath = null;
        try {
            if (bitmap != null && !bitmap.isRecycled()) {

                File imgDir = new File(path);
                if (!imgDir.exists()) {// 如果存储的不存在，先创建
                    imgDir.mkdirs();
                }

                imagePath = new File(path, fileName + ".png");// 给新照的照片文件命名

                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(imagePath));

                /* 采用压缩转档方法 */
                bitmap.compress(Bitmap.CompressFormat.PNG, compress, bos);

                /* 调用flush()方法，更新BufferStream */
                bos.flush();

                /* 结束OutputStream */
                bos.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (imagePath != null && bitmap != null) {
            return imagePath.toString();
        } else {
            return null;
        }

    }
}
