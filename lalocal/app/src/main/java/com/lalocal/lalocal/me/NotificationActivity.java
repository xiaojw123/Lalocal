package com.lalocal.lalocal.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.help.TargetPage;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.model.MessageItem;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.adapter.MyMessageAdapter;
import com.lalocal.lalocal.view.listener.OnItemClickListener;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationActivity extends BaseActivity {

    @BindView(R.id.my_message_customer)
    RelativeLayout myMessageCustomer;
    @BindView(R.id.my_message_rlv)
    RecyclerView myMessageRlv;
    List<MessageItem> mItems;
    public static  void start(Context context){
        Intent intent = new Intent(context, NotificationActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
        myMessageRlv.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));
        myMessageRlv.setHasFixedSize(true);
        try {
            mItems = DataSupport.findAll(MessageItem.class);
        } catch (Exception e) {
            AppLog.print("get data exec");
        }
        if (mItems == null) {
            mItems = new ArrayList<>();
        }
        setLoaderCallBack(new MessageCallBack());
        mContentloader.getPushLogs(UserHelper.getDateTime(this));

    }



    @OnClick({R.id.my_message_customer})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_message_customer:
                CommonUtil.startCustomService(this);
                break;
        }
    }

    class MessageCallBack extends ICallBack implements OnItemClickListener {

        @Override
        public void onGetPushLogs(String date, List<MessageItem> items) {
            mItems.addAll(items);
            DataSupport.saveAll(mItems);
            UserHelper.updateDateTime(NotificationActivity.this, date);
            MyMessageAdapter messageAdapter = new MyMessageAdapter(mItems);
            messageAdapter.setOnItemClickListener(this);
            myMessageRlv.setAdapter(messageAdapter);
        }

        @Override
        public void onItemClickListener(View view, int position) {
            Object obj = view.getTag();
            if (obj != null) {
                MessageItem item = (MessageItem) obj;
                switch (item.getTargetType()) {
                    case -1:
                        TargetPage.gotoWebDetail(NotificationActivity.this, item.getTargetUrl(),"",false);
                        break;
                    case 0:
                        TargetPage.gotoUser(NotificationActivity.this, String.valueOf(item.getTargetId()), true);
                        break;
                    case 1://文章
                    case 13://资讯
                        TargetPage.gotoArticleDetail(NotificationActivity.this, String.valueOf(item.getTargetId()),false);
                        break;
                    case 2://产品
                        TargetPage.gotoProductDetail(NotificationActivity.this, String.valueOf(item.getTargetId()), String.valueOf(item.getTargetType()),false);
                        break;
                    case 9://线路
                        TargetPage.gotoRouteDetail(NotificationActivity.this, String.valueOf(item.getTargetId()),false);
                        break;
                    case 10://专题
                        TargetPage.gotoSpecialDetail(NotificationActivity.this, String.valueOf(item.getTargetId()),false);
                        break;
                    case 15:
                        TargetPage.gotoLive(NotificationActivity.this, String.valueOf(item.getTargetId()), true);
                        break;
                    case 20:
                        TargetPage.gotoPlayBack(NotificationActivity.this, String.valueOf(item.getTargetId()), true);
                        break;

                }

            }

        }
    }


}
