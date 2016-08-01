package com.vicky.pullrefresh.holder;

import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vicky.pullrefresh.demo.R;
import com.vicky.pullrefresh.demo.ViewUtil;
import com.vicky.pullrefresh.view.ExRecyclerView;

import org.w3c.dom.Text;

/**
 * Created by vicky on 2016/7/28.
 */
public class FooterHolder extends BaseHolder {

    private View mFooterView;
    private TextView textView;

    public FooterHolder(View itemView) {
        super(itemView);
        this.mFooterView = itemView;
        textView = (TextView) mFooterView.findViewById(R.id.footer_text);
    }

    @Override
    public void refreshStatus(int status) {
        switch (status){
            case ExRecyclerView.STATUS_PULL_UP:
                textView.setText("上拉加载更多");
                break;
            case ExRecyclerView.STATUS_PULL_UP_RELEASE_TO_REFRESH:
                textView.setText("释放加载更多");
                break;
            case ExRecyclerView.STATUS_PULL_UP_REFRESHING:
                textView.setText("正在加载");
                break;
            case ExRecyclerView.STATUS_PULL_UP_REFRESH_COMPLETE:
                textView.setText("加载完成");
                break;

        }
    }
}
