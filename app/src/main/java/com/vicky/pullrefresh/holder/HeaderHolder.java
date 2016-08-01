package com.vicky.pullrefresh.holder;

import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vicky.pullrefresh.demo.R;
import com.vicky.pullrefresh.demo.ViewUtil;
import com.vicky.pullrefresh.view.ExRecyclerView;

/**
 * Created by vicky on 2016/7/28.
 */
public class HeaderHolder extends BaseHolder {
    TextView textView;

    public HeaderHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.header_text);
    }

    @Override
    public void refreshStatus(int status) {

        switch (status){
            case ExRecyclerView.STATUS_PULL_DOWN:
                textView.setText("下拉刷新");
                break;
            case ExRecyclerView.STATUS_PULL_DOWN_REFRESHING:
                textView.setText("正在刷新");
                break;
            case ExRecyclerView.STATUS_PULL_DOWN_REFRESH_COMPLETE:
                textView.setText("刷新完成");
                break;
            case ExRecyclerView.STATUS_PULL_DOWN_RELEASE_TO_REFRESH:
                textView.setText("释放刷新");
                break;

        }
    }
}
