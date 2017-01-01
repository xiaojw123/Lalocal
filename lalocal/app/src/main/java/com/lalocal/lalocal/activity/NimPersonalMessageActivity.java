package com.lalocal.lalocal.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.me.NotificationActivity;
import com.lalocal.lalocal.me.PraiseCommentActivity;
import com.lalocal.lalocal.net.callback.ICallBack;

/**
 * Created by xiaojw on 2016/12/14.
 */

public class NimPersonalMessageActivity extends BaseActivity implements View.OnClickListener {
    TextView countTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nim_personal_msg);
        setLoaderCallBack(new NimPerMsgCallBack());
        FrameLayout notifyLayout = (FrameLayout) findViewById(R.id.my_immessage_notification);
        FrameLayout praiseLayout = (FrameLayout) findViewById(R.id.my_immessage_praise_and_comment);
        countTv = (TextView) findViewById(R.id.my_immessage_praise_and_comment_num);
        notifyLayout.setOnClickListener(this);
        praiseLayout.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mContentloader.getMessageCount();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_immessage_notification:
                NotificationActivity.start(this);
                break;
            case R.id.my_immessage_praise_and_comment:
                PraiseCommentActivity.start(this);
                break;
        }
    }

    class NimPerMsgCallBack extends ICallBack {

        @Override
        public void onGetMessageCount(int unReadCount) {
            if (unReadCount > 0) {
                countTv.setVisibility(View.VISIBLE);
                String fomartCount = unReadCount > 99 ? unReadCount + "+" : String.valueOf(unReadCount);
                countTv.setText(fomartCount);
            }else{
                countTv.setVisibility(View.GONE);
            }
        }
    }
}
