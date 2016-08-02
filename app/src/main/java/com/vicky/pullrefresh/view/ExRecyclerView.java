package com.vicky.pullrefresh.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.vicky.pullrefresh.adapter.ExAdapter;
import com.vicky.pullrefresh.holder.BaseHolder;
import com.vicky.pullrefresh.holder.FooterHolder;
import com.vicky.pullrefresh.holder.HeaderHolder;

/**
 * Created by vicky on 2016/7/28.
 */
public class ExRecyclerView extends RecyclerView {
    private final String TAG = ExRecyclerView.class.getSimpleName();

    public static final int STATUS_PULL_DOWN = 0X01;//pulling down
    public static final int STATUS_PULL_DOWN_REFRESHING = 0X02;//refreshing
    public static final int STATUS_PULL_DOWN_RELEASE_TO_REFRESH = 0X03;//pull down release to refresh
    public static final int STATUS_PULL_DOWN_REFRESH_COMPLETE = 0X04;//refresh complete

    public static final int STATUS_PULL_UP = 0X11;//pulling up
    public static final int STATUS_PULL_UP_REFRESHING = 0X12;//refreshing
    public static final int STATUS_PULL_UP_RELEASE_TO_REFRESH = 0X13;//pull up release to refresh
    public static final int STATUS_PULL_UP_REFRESH_COMPLETE = 0X014;//refresh complete

    private int mLastVisiablePosition;
    private int mFirstVisiablePosition;
    private float mFactor = 0.3f;
    private ExAdapter mAdapter;
    int mLastY;

    private int mCurrentStatus;

    private OnRefreshlistener mRefreshListener;
    private BaseHolder mHeaderHolder;
    private BaseHolder mFooterHolder;

    public ExRecyclerView(Context context) {
        super(context);
    }

    public ExRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ExRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        mAdapter = (ExAdapter) adapter;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        int action = e.getAction();

        switch (action){
            case MotionEvent.ACTION_DOWN:
                mLastY = (int)e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int newY = (int)e.getRawY();
                int difY = newY-mLastY;

                if ( isArriveTop()&& difY>0){// trigger pull down
                    mHeaderHolder = getHeaderHolder();
                    if (mHeaderHolder!= null){
                        mHeaderHolder.onPull((int)(difY*mFactor));

                        if (Math.abs(difY) >= 300){
                            mCurrentStatus = STATUS_PULL_DOWN_RELEASE_TO_REFRESH;
                        } else {
                            mCurrentStatus = STATUS_PULL_DOWN;
                        }

                        mHeaderHolder.refreshStatus(mCurrentStatus);
                        return false;
                    }

                } else if (isArriveBottom() && difY<0){//trigger pull up
                    mFooterHolder = getFooterHolder();
                    if (mFooterHolder!= null){
                        mFooterHolder.onPull((int)(-difY*mFactor));
                        if (Math.abs(difY)>=300){
                            mCurrentStatus = STATUS_PULL_UP_RELEASE_TO_REFRESH;
                        } else {
                            mCurrentStatus = STATUS_PULL_UP;
                        }
                        mFooterHolder.refreshStatus(mCurrentStatus);
                        scrollBy(0,-difY);
                        return false;

                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mCurrentStatus == STATUS_PULL_DOWN_RELEASE_TO_REFRESH){
                    pullDownRefreshing();
                } else if (mCurrentStatus== STATUS_PULL_DOWN){
                    refreshComplete();
                }else if (mCurrentStatus == STATUS_PULL_UP_RELEASE_TO_REFRESH){
                    pullUpLoadMore();
                } else if (mCurrentStatus == STATUS_PULL_UP){
                    loadmoreComplete();
                }
                break;
        }
        return super.dispatchTouchEvent(e);
    }


    private void pullDownRefreshing(){
        mHeaderHolder= getHeaderHolder();
        if (mHeaderHolder!= null){
            mHeaderHolder.onRefreshing();
            if (mRefreshListener!= null){
                mRefreshListener.onRefresh();
            }
            mCurrentStatus = STATUS_PULL_DOWN_REFRESHING;
            mHeaderHolder.refreshStatus(mCurrentStatus);
        }
    }

    private void pullUpLoadMore(){
        mFooterHolder = getFooterHolder();
        if (mFooterHolder!= null){
            mFooterHolder.onRefreshing();
            if (mRefreshListener!= null){
                mRefreshListener.onLoadMore();
            }
            mCurrentStatus = STATUS_PULL_UP_REFRESHING;
        }
        mFooterHolder.refreshStatus(mCurrentStatus);
    }
    private boolean isArriveTop(){
        LinearLayoutManager manager = (LinearLayoutManager) getLayoutManager();
        mFirstVisiablePosition = ((LinearLayoutManager)manager).findFirstCompletelyVisibleItemPosition();
        return mFirstVisiablePosition <=1;
    }

    private boolean isArriveBottom(){
        LinearLayoutManager manager = (LinearLayoutManager) getLayoutManager();
        mLastVisiablePosition = ((LinearLayoutManager)manager).findLastCompletelyVisibleItemPosition();

        int visiableItemCount = manager.getChildCount();
        int totalItemCount = manager.getItemCount();
        return visiableItemCount>0 && (mLastVisiablePosition) >= totalItemCount-2;

    }

    private BaseHolder getHeaderHolder(){
        if (mHeaderHolder == null){
            View headerView = getLayoutManager().findViewByPosition(0);
            if (headerView == null) return null;
            RecyclerView.ViewHolder holder = getChildViewHolder(headerView);
            if (holder instanceof HeaderHolder){
                return (HeaderHolder) holder;
            }
        }

        return mHeaderHolder;
    }

    private BaseHolder getFooterHolder(){
        if (mFooterHolder == null){
            View footerView = getLayoutManager().findViewByPosition(getAdapter().getItemCount()-1);
            if (footerView == null) return null;
            RecyclerView.ViewHolder holder = getChildViewHolder(footerView);
            if (holder instanceof FooterHolder){
                return (FooterHolder) holder;
            }
        }

        return mFooterHolder;
    }

    public void refreshComplete(){
        if (mHeaderHolder!= null){
            mHeaderHolder.onRefreshComplete();
        }
        mCurrentStatus = STATUS_PULL_DOWN_REFRESH_COMPLETE;
        mHeaderHolder.refreshStatus(mCurrentStatus);
    }

    public void loadmoreComplete(){
        if (mFooterHolder!= null){
            mFooterHolder.onRefreshComplete();
        }
        mCurrentStatus = STATUS_PULL_UP_REFRESH_COMPLETE;
        mFooterHolder.refreshStatus(mCurrentStatus);
    }

    public void setOnRefreshListener(OnRefreshlistener listener){
        this.mRefreshListener = listener;
    }

    public interface OnRefreshlistener{
        void onRefresh();
        void onLoadMore();
    }

}
