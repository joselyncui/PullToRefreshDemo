package com.vicky.pullrefresh.demo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.vicky.pullrefresh.adapter.ExAdapter;
import com.vicky.pullrefresh.view.ExRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ExRecyclerView mRecyclerView;
    private List<ViewModel> mItems;
    private ExAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();

    }

    private void initData(){
        mItems = new ArrayList<>();
        for (int i = 0; i < 20; i++){
            ViewModel model = new ViewModel();
            model.title = "title--"+i;
            mItems.add(model);
        }
    }

    private void addData(){
        int size = mItems.size();
        for (int i =size; i <size+10; i++){
            ViewModel model = new ViewModel();
            model.title = "new add title --- " + i;
            mItems.add(model);
        }
    }

    private void initView(){
        mRecyclerView = (ExRecyclerView) findViewById(R.id.exrecyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ExAdapter(mItems,R.layout.item_layout);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnRefreshListener(new ExRecyclerView.OnRefreshlistener() {
            @Override
            public void onRefresh() {
                new RefreshTask().execute();
            }

            @Override
            public void onLoadMore() {

                new MyTask().execute();

            }
        });
    }

    class RefreshTask extends  AsyncTask<Void,Void,Void>{
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mRecyclerView.refreshComplete();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    class MyTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mRecyclerView.loadmoreComplete();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            addData();
            return null;
        }
    }
}
