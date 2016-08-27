package com.example.android.pulltorefresh;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class MainActivity extends Activity {

    protected static final int REFRESH = 0;
    private List<String> datas;
    private PullToRefreshView ptr;

    private Handler handler=new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case REFRESH:

                    ptr.finishLoading(true, true);
                    break;

                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ptr = (PullToRefreshView) findViewById(R.id.ptr);

    ptr.setAdapter(new BaseAdapter() {
    @Override
    public int getCount() {
        return 30;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View inflate = View.inflate(MainActivity.this, R.layout.item, null);
        return inflate;
    }
});
ptr.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
    @Override
    public void onRefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(REFRESH);
            }
        }).start();
    }
});
    }




}


