package com.vicky.pullrefresh.holder;

import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.vicky.pullrefresh.demo.ViewUtil;

/**
 * BaseHolder for header and footer view holder
 * Created by vicky on 2016/7/29.
 */
public class BaseHolder extends RecyclerView.ViewHolder{

    private View mHorderView;
    private int mNormalHeight;

    public BaseHolder(View itemView) {
        super(itemView);
        this.mHorderView = itemView;
        mNormalHeight = ViewUtil.getViewHeight(mHorderView);
        removeView();
    }

    /**
     * get holder view
     *
     * @return Veiw
     */
    public View getHoldView(){
        return mHorderView;
    }

    /**
     * set view height to 0
     */
    public void removeView(){
        resizeTo(0,false);
    }

    /**
     * resize view height
     *
     * @param targetHeight
     * @param isAnim use animation or not
     */
    private void resizeTo(int targetHeight,boolean isAnim){
        final ViewGroup.LayoutParams params = mHorderView.getLayoutParams();

        if (isAnim){
            final int height = params.height;
            ValueAnimator va = ValueAnimator.ofInt(height, targetHeight);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Integer value = (Integer) animation.getAnimatedValue();
                    params.height = value.intValue();
                    mHorderView.setLayoutParams(params);

                }
            });
            va.setDuration(250);

            va.start();
        } else {
            params.height = targetHeight;
            mHorderView.setLayoutParams(params);
        }
    }

    /**
     * when pull down or up
     *
     * @param height
     */
    public void onPull(int height){
        resizeTo(height,false);
    };

    /**
     * when release pull
     */
    public  void onReleaseToRefresh(){
        resizeTo(mNormalHeight,true);
    };

    public void onRefreshing(){};

    public void onRefreshComplete(){
        resizeTo(0,true);
    };
}
