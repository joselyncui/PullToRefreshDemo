package com.vicky.pullrefresh.holder;

import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.vicky.pullrefresh.demo.ViewUtil;

/**
 * Created by vicky on 2016/7/28.
 */
public class FooterHolder extends BaseHolder {

    private int normalFooterHeight;

    private View mFooterView;

    public FooterHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onReleaseToRefresh() {
        super.onReleaseToRefresh();
    }
}
