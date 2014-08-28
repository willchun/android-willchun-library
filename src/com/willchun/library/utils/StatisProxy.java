package com.willchun.library.utils;

import android.content.Context;
import com.umeng.analytics.MobclickAgent;

/**
 * 统计协议相关的管理类
 */
public class StatisProxy {
    /**
     * 初始化统计相关信息
     */
    public static void init(boolean isDebug){
        MobclickAgent.setDebugMode(isDebug);
    }


    public static void onResume(Context context){
        MobclickAgent.onResume(context);
    }

    public static void onPause(Context context){
        MobclickAgent.onPause(context);
    }


}