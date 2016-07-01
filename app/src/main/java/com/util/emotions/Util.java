package com.util.emotions;

import android.content.Context;

/**
 * Created by Chuanhang.Gu on 2016/6/30.
 */
public class Util {

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
