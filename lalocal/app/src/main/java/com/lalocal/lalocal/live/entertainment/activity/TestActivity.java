package com.lalocal.lalocal.live.entertainment.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.util.AppLog;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.chatroom.ChatRoomServiceObserver;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by android on 2016/8/3.
 */
public class TestActivity extends BaseActivity {

    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_test_activity);
        ButterKnife.bind(this);
        registerObservers(true);
    }
    @OnClick({R.id.tv1,R.id.tv2})
    public  void click(View v){
        switch (v.getId()){
            case R.id.tv1:
                IMMessage message = MessageBuilder.createTextMessage("user_10136", SessionTypeEnum.P2P,"你好");
                NIMClient.getService(MsgService.class).sendMessage(message,true);
                break;
            case R.id.tv2:
                IMMessage message1 = MessageBuilder.createTextMessage("user_10348", SessionTypeEnum.P2P,"我不好");
                NIMClient.getService(MsgService.class).sendMessage(message1,true);
                break;


        }
    }

    protected void registerObservers(boolean register) {
        NIMClient.getService(ChatRoomServiceObserver.class).observeReceiveMessage(incomingChatRoomMsg, register);

    }
    Observer<List<ChatRoomMessage>> incomingChatRoomMsg = new Observer<List<ChatRoomMessage>>() {

        @Override
        public void onEvent(List<ChatRoomMessage> messages) {
            if (messages == null || messages.isEmpty()) {
                return;
            }
            IMMessage message = messages.get(0);
            Map<String, Object> remoteExtension = message.getRemoteExtension();
            if (remoteExtension != null) {
                Iterator<Map.Entry<String, Object>> iterator = remoteExtension.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Object> next = iterator.next();
                    String key = next.getKey();
                    Object value = next.getValue();
                    AppLog.i("TAG","获取消息内容:"+key+"   value:"+value.toString());
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerObservers(false);
    }
}
