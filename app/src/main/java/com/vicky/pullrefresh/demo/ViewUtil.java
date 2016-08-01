package com.vicky.pullrefresh.demo;

import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by vicky on 2016/7/28.
 */
public class ViewUtil {
    //measure height of view
    public static int getViewHeight(View view){

        DisplayMetrics dm = view.getContext().getResources().getDisplayMetrics();
        int w = View.MeasureSpec.makeMeasureSpec(dm.widthPixels, View.MeasureSpec.EXACTLY);
        int h = View.MeasureSpec.makeMeasureSpec(dm.heightPixels, View.MeasureSpec.AT_MOST);
        view.measure(w, h);
        return view.getMeasuredHeight();
    }
}
