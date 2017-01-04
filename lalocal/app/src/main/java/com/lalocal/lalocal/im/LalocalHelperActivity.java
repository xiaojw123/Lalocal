package com.lalocal.lalocal.im;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.net.callback.ICallBack;

import org.json.JSONArray;
import org.json.JSONObject;

public class LalocalHelperActivity extends BaseActivity implements View.OnClickListener {
    LinearLayout helperContainer;
    int chlidSize;

    public static void start(Context context) {
        Intent intent = new Intent(context, LalocalHelperActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lalocal_helper);
        showLoadingAnimation();
        helperContainer = (LinearLayout) findViewById(R.id.lalocal_helper_container);
        setLoaderCallBack(new HelperCallBack());
        mContentloader.getUsersService();
    }

    private TextView getSericeTextView(int i) {
        TextView tv = new TextView(this);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_16_sp));
        tv.setTextColor(getResources().getColor(R.color.color_82dcff));
        tv.setOnClickListener(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (i == 0) {
            params.topMargin = (int) getResources().getDimension(R.dimen.dimen_size_30_dp);
        } else {
            params.topMargin = (int) getResources().getDimension(R.dimen.dimen_size_15_dp);
        }
        tv.setLayoutParams(params);
        return tv;
    }

    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        if (tag != null) {
            NimChatActivity.start(this, (Bundle) tag);
        }
    }

    class HelperCallBack extends ICallBack {
        @Override
        public void onGetUsersSerice(JSONArray resutJarray) {
            hidenLoadingAnimation();
            for (int i = 0; i < resutJarray.length(); i++) {
                JSONObject jobj = resutJarray.optJSONObject(i);
                if (jobj == null) {
                    continue;
                }
                String title = jobj.optString("msg");
                int type = jobj.optInt("type");
                TextView textView = getSericeTextView(i);
                textView.setTag(R.id.type, type);
                textView.setText(title);
                helperContainer.addView(textView);
            }
            chlidSize=helperContainer.getChildCount();
            requestUser(1);
        }

        @Override
        public void onGetUser(JSONObject resultJobj, int index) {
            View view = helperContainer.getChildAt(index);
            if (view != null) {
                Bundle bundle = new Bundle();
                bundle.putString(KeyParams.ACCID, resultJobj.optString("accId"));
                bundle.putString(KeyParams.NICKNAME, resultJobj.optString("nickName"));
                view.setTag(bundle);
            }
            index += 1;
            if (chlidSize> index) {
                requestUser(index);
            }
        }


    }

    public void requestUser(int index) {
        if (chlidSize>index) {
            View view = helperContainer.getChildAt(index);
            if (view != null) {
                Object tag = view.getTag(R.id.type);
                if (tag != null) {
                    mContentloader.getUsersService((int) tag, index);
                }
            }
        }

    }
}