package com.lalocal.lalocal.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.model.OrderItem;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.view.adapter.MyOrderRecyclerAdapter;

import java.util.List;

/**
 * Created by xiaojw on 2016/9/30.
 */

public class MyOrderActivity extends BaseActivity implements XRecyclerView.LoadingListener {
    public static final String ACTION_UPDATE_DATA = "action_update_data";
    public static final int UPDATE_MY_ORDER = 0x13;
    XRecyclerView mXRecyclerView;
    int userId;
    String token;
    boolean isRefresh;
    View nothingView;
    UpdateReciver updateReciver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        initView();
        setLoaderCallBack(new OrderCallBack());
        userId = UserHelper.getUserId(this);
        token = UserHelper.getToken(this);
        mXRecyclerView.setRefreshing(true);
        if (updateReciver == null) {
            updateReciver = new UpdateReciver();
        }
        registerReceiver(updateReciver, new IntentFilter(ACTION_UPDATE_DATA));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (updateReciver != null) {
            unregisterReceiver(updateReciver);
        }
    }

    private void initView() {
        nothingView = findViewById(R.id.my_item_noting);
        mXRecyclerView = (XRecyclerView) findViewById(R.id.my_order_xrlv);
        mXRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mXRecyclerView.setPullRefreshEnabled(true);
        mXRecyclerView.setLoadingMoreEnabled(false);
        mXRecyclerView.setLoadingListener(this);
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        mContentloader.getMyOrder(userId, token);
    }

    @Override
    public void onLoadMore() {

    }

    class OrderCallBack extends ICallBack{
        MyOrderRecyclerAdapter orderAdapter;

        @Override
        public void onCancelSuccess() {
            AppLog.print("MeCallBack onCancelSuccess____");
            mContentloader.getMyOrder(userId, token);
        }


        @Override
        public void onGetOrderItem(List<OrderItem> items) {
            if (items != null && items.size() > 0) {
                if (nothingView.getVisibility() == View.VISIBLE) {
                    nothingView.setVisibility(View.GONE);
                }
                if (mXRecyclerView.getVisibility() != View.VISIBLE) {
                    mXRecyclerView.setVisibility(View.VISIBLE);
                }
                if (isRefresh) {
                    isRefresh = false;
                    mXRecyclerView.refreshComplete();
                }
                if (orderAdapter == null) {
                    orderAdapter = new MyOrderRecyclerAdapter(items, mContentloader);
                    mXRecyclerView.setAdapter(orderAdapter);
                } else {
                    orderAdapter.updateItems(items);
                }
            } else {
                nothingView.setVisibility(View.VISIBLE);
                mXRecyclerView.setVisibility(View.GONE);
            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == UPDATE_MY_ORDER) {
            AppLog.print("更新订单————————" + UPDATE_MY_ORDER);
            mContentloader.getMyOrder(userId, token);
        }
    }

    class UpdateReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (UserHelper.isLogined(MyOrderActivity.this)) {
                mContentloader.getMyOrder(UserHelper.getUserId(MyOrderActivity.this), UserHelper.getToken(MyOrderActivity.this));
            }
        }
    }
}
