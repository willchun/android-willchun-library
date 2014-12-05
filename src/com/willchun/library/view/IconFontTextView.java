package com.willchun.library.view;/**
 * Created by Administrator on 2014/12/5.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author willchun (277143980@qq.com)
 * @github https://github.com/willchun
 * @date 2014/12/5
 */
public class IconFontTextView extends TextView{

    private Context mContext;

    public IconFontTextView(Context context) {
        super(context);
        mContext = context;
        initTypeface();
    }

    public IconFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initTypeface();
    }

    public IconFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initTypeface();
    }

    public void initTypeface(){
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "iconfont.ttf");
        setTypeface(typeface);
    }

    public IconFontTextView textCode(int codePoint){
        setText(new String(Character.toChars(codePoint)));
        return this;
    }


}
