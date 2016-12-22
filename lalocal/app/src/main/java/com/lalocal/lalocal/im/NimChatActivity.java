package com.lalocal.lalocal.im;

import android.app.FragmentManager;
import android.os.Bundle;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;

public class NimChatActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nim_chat);
        FragmentManager fm = getFragmentManager();
        ChatFragment fragment = (ChatFragment) fm.findFragmentById(R.id.nim_chat_fragment);
    }

//    public static \void start(Context context, String accid, String nickName, String avatar, long time) {
//        Intent imIntent = new Intent(context, ChatActivity.class);
//        imIntent.putExtra(ChatActivity.NIM_TO_ACCOUNT, accid);
//        imIntent.putExtra(KeyParams.NICKNAME, nickName);
//        imIntent.putExtra(KeyParams.AVATAR, avatar);
//        context.startActivity(imIntent);
//    }


}
