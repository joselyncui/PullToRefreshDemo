package com.vicky.pullrefresh.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vicky.pullrefresh.demo.R;
import com.vicky.pullrefresh.demo.ViewModel;
import com.vicky.pullrefresh.holder.FooterHolder;
import com.vicky.pullrefresh.holder.HeaderHolder;

import java.util.List;

/**
 * Created by vicky on 2016/7/28.
 */
public class ExAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int TYPE_HEADER = 0X01;
    private final int TYPE_FOOTER = 0X02;
    private final int TYPE_NORMAL = 0X03;

    private List<ViewModel> mItems;
    private int mItemLayout;

    public ExAdapter(List<ViewModel> items,int layout){
        this.mItems = items;
        this.mItemLayout = layout;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View view = null;
        switch (viewType){
            case TYPE_HEADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_layout,parent,false);
                holder = new HeaderHolder(view);
                break;
            case TYPE_FOOTER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_layout,parent,false);
                holder = new FooterHolder(view);
                break;
            case TYPE_NORMAL:
                view = LayoutInflater.from(parent.getContext()).inflate(mItemLayout,parent,false);
                holder = new NormalViewHolder(view);
                break;
        }
        return holder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type== TYPE_NORMAL){
            ViewModel item = mItems.get(position-1);
            ((NormalViewHolder)holder).mTitleView.setText(item.title);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position==0) return TYPE_HEADER;
        if (position==getItemCount()-1) return TYPE_FOOTER;
        return TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        return mItems.size() + 2;
    }



    public static class NormalViewHolder extends RecyclerView.ViewHolder{
        public TextView mTitleView;

        public NormalViewHolder(View view){
            super(view);
            mTitleView = (TextView) view.findViewById(R.id.title);
        }

    }
}
