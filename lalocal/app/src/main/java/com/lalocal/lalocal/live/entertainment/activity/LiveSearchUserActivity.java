package com.lalocal.lalocal.live.entertainment.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.model.LiveFansOrAttentionResp;
import com.lalocal.lalocal.model.LiveFansOrAttentionRowsBean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.view.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by android on 2016/9/21.
 */
public class LiveSearchUserActivity extends BaseActivity implements XListView.IXListViewListener {


    @BindView(R.id.live_attention_search_et)
    EditText liveAttentionSearchEt;
    @BindView(R.id.search_text_hint)
    TextView searchTextHint;
    @BindView(R.id.seach_clear_btn)
    ImageView seachClearBtn;
    @BindView(R.id.live_attention_search_cancel)
    TextView liveAttentionSearchCancel;
    @BindView(R.id.live_search_layout_to)
    LinearLayout liveSearchLayoutTo;
    @BindView(R.id.search_result_null)
    TextView searchResultNull;
    @BindView(R.id.live_attention_listview)
    XListView liveAttentionListview;
    private Timer timer;
    private ContentLoader contentLoader;
    List<LiveFansOrAttentionRowsBean> allRows = new ArrayList<LiveFansOrAttentionRowsBean>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_search_live_layout);
        ButterKnife.bind(this);
       String liveType = getIntent().getStringExtra("liveType");
        String userId = getIntent().getStringExtra("userId");
        String typeId = "type=" + liveType + "&userId=" + userId;
        initXlistView();
        contentLoader = new ContentLoader(this);
        contentLoader.setCallBack(new MyCallBack());
        contentLoader.getAttentionOrFansList(typeId);
    }

    private void initXlistView() {
        liveAttentionListview.setPullLoadEnable(true);
        liveAttentionListview.setPullRefreshEnable(true);
        liveAttentionListview.setXListViewListener(this);
        liveAttentionListview.setDivider(null);
        liveAttentionListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LiveFansOrAttentionRowsBean liveFansOrAttentionRowsBean = allRows.get(position - 1);
                Intent intent = new Intent(LiveSearchUserActivity.this, LiveHomePageActivity.class);
                int id1 = liveFansOrAttentionRowsBean.getId();
                AppLog.i("TAG", "id:" + id1);
                intent.putExtra("userId", id1 + "");
                startActivity(intent);

            }
        });

    }


    @OnClick({R.id.live_attention_search_et, R.id.live_attention_search_cancel,R.id.seach_clear_btn})
    public void clickButton(View view) {
        switch (view.getId()){
            case R.id.live_attention_search_et:
                liveAttentionSearchEt.requestFocus();
                liveAttentionSearchEt.setFocusable(true);
                liveAttentionSearchEt.setFocusableInTouchMode(true);
                liveAttentionSearchEt.setCompoundDrawables(null, null, null, null);
                if (timer == null) {
                    timer = new Timer();
                }
                timer.schedule(new TimerTask() {
                                   public void run() {
                                       InputMethodManager inputManager =
                                               (InputMethodManager) liveAttentionSearchEt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                       inputManager.showSoftInput(liveAttentionSearchEt, 0);
                                   }
                               },
                        500);
                liveAttentionSearchCancel.setVisibility(View.VISIBLE);


                break;
            case R.id.live_attention_search_cancel:

                break;
            case R.id.seach_clear_btn:
                break;

        }

    }

    public class MyCallBack extends ICallBack {
        @Override
        public void onLiveFansOrAttention(LiveFansOrAttentionResp liveFansOrAttentionResp, boolean isSearch) {
            super.onLiveFansOrAttention(liveFansOrAttentionResp, isSearch);
        }
    }


    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
