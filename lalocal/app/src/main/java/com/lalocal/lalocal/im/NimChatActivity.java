package com.lalocal.lalocal.im;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.help.KeyParams;

public class NimChatActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nim_chat);
        ChatFragment fragment = new ChatFragment();
        Bundle bundle = new Bundle();
        Intent intent = getIntent();
        bundle.putString(KeyParams.ACCID, intent.getStringExtra(KeyParams.ACCID));
        bundle.putString(KeyParams.NICKNAME, intent.getStringExtra(KeyParams.NICKNAME));
        fragment.setArguments(bundle);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.activity_nim_chat, fragment);
        ft.commit();
    }
    public static void start(Context context, String accId, String nicKName) {
        Intent intent = new Intent(context, NimChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KeyParams.ACCID, accId);
        bundle.putString(KeyParams.NICKNAME, nicKName);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, NimChatActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


}
