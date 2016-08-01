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
    public static final int STATUS_PULL_DOWN_RELEASE = 0X03;//pull down release

    public static final int STATUS_PULL_UP = 0X04;//pulling up
    public static final int STATUS_PULL_UP_REFRESHING = 0X05;//refreshing
    public static final int STATUS_PULL_UP_RELEASE = 0X06;//pull up release

    private int mLastVisiablePosition;
    private int mFirstVisiablePosition;
    private float mFactor = 0.3f;
    private ExAdapter mAdapter;
    int mLastY;

    private int mCurrentStatus;

    private OnRefreshlistener mRefreshListener;
    private BaseHolder mHeaderHolder;
    private BaseHolder mFooterHolder;
    private int mTriggerDistance = 100;

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
                Log.i("action move ===","last " + difY +"  " + newY +"  " + isArriveTop());

                if ( isArriveTop()&& difY>0){
                    mHeaderHolder = getHeaderHolder();
                    if (mHeaderHolder!= null){
                        mHeaderHolder.onPull((int)(difY*mFactor));
                        mCurrentStatus = STATUS_PULL_DOWN;
                        return false;
                    }

                } else if (isArriveBottom() && difY<0){
                    mFooterHolder = getFooterHolder();
                    if (mFooterHolder!= null){
                        mFooterHolder.onPull((int)(-difY*mFactor));
                        mCurrentStatus = STATUS_PULL_UP;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mCurrentStatus == STATUS_PULL_DOWN){
                    mHeaderHolder= getHeaderHolder();
                    if (mHeaderHolder!= null){
                        mHeaderHolder.onReleaseToRefresh();
                        if (mRefreshListener!= null){
                            mRefreshListener.onRefresh();
                        }
                        mCurrentStatus = STATUS_PULL_DOWN_RELEASE;
                    }
                } else if (mCurrentStatus == STATUS_PULL_UP){
                    mFooterHolder = getFooterHolder();
                    if (mFooterHolder!= null){
                        mFooterHolder.onReleaseToRefresh();
                        if (mRefreshListener!= null){
                            mRefreshListener.onLoadMore();
                        }
                        mCurrentStatus = STATUS_PULL_UP_RELEASE;
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(e);
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
    }

    public void loadmoreComplete(){
        if (mFooterHolder!= null){
            mFooterHolder.onRefreshComplete();
        }
    }

    public void setOnRefreshListener(OnRefreshlistener listener){
        this.mRefreshListener = listener;
    }

    public interface OnRefreshlistener{
        void onRefresh();
        void onLoadMore();
    }

}
