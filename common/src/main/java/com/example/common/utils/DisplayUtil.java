package com.example.common.utils;

import android.content.Context;

/**
 * Created by John on 2017/7/6.
 */

public class DisplayUtil {
    public static int px2dp(Context context,float pxValue){
        float scale=context.getResources().getDisplayMetrics().density;
        return (int) (pxValue/scale+0.5f);
    }
    public static int dp2px(Context context,float dpValue){
        float scale=context.getResources().getDisplayMetrics().density;
        return (int) (dpValue*scale+0.5f);
    }
}
