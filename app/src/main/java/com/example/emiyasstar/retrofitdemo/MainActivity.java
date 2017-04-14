package com.example.emiyasstar.retrofitdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import rx.Observer;
import rx.Scheduler;
import rx.schedulers.Schedulers;
import rx.android.schedulers.AndroidSchedulers;


public class MainActivity extends AppCompatActivity {

    private static String TAG="MainActivity";

    private TextView mTextMessage;

    private RecyclerView recyclerView;

    private ZhuangbiListAdapter adapter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText("elenmt");
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText("flatmap");
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText("operation");
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        adapter=new ZhuangbiListAdapter();
        recyclerView=(RecyclerView)findViewById(R.id.imagelist) ;
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Network.getZhuangbiApi()
                .search("110")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(t->{Log.i(TAG,"do!!!!!next!@1!!");}).subscribe(new Observer<List<ZhuangbiImage>>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(getApplicationContext(),"加载成功",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(),"加载失败",Toast.LENGTH_SHORT).show();
                        Log.e(TAG,e.toString());
                    }

                    @Override
                    public void onNext(List<ZhuangbiImage> zhuangbiImages) {
                        Toast.makeText(getApplicationContext(),"加载数据", Toast.LENGTH_SHORT).show();
                        adapter.setImages(zhuangbiImages);
                    }
                });


    }
}
