package com.lalocal.lalocal.view.liveroomview.entertainment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.model.LiveFansOrAttentionResp;
import com.lalocal.lalocal.model.LiveFansOrAttentionResultBean;
import com.lalocal.lalocal.model.LiveFansOrAttentionRowsBean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.view.CustomTitleView;
import com.lalocal.lalocal.view.liveroomview.entertainment.adapter.AttentionOrFansAdapter;
import com.lalocal.lalocal.view.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by android on 2016/8/7.
 */
public class LiveAttentionOrFansActivity extends BaseActivity implements XListView.IXListViewListener {
    @BindView(R.id.user_attention_title)
    CustomTitleView userAttentionTitle;
    @BindView(R.id.live_attention_search_et)
    EditText liveAttentionSearchEt;
    @BindView(R.id.live_attention_search_cancel)
    TextView liveAttentionSearchCancel;
    @BindView(R.id.live_attention_listview)
    XListView liveAttentionListview;

    @BindView(R.id.live_search_layout_to)
    LinearLayout liveSearchLayout;
    @BindView(R.id.live_search_layout_font)
    LinearLayout liveSearchLayoutFont;
    private String liveType;
    private AttentionOrFansAdapter attentionOrFansAdapter;
    private String typeId;
    private ContentLoader contentLoader;
    private int totalPages;
    private String userId;

    private List<LiveFansOrAttentionRowsBean> searchRows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_attention_list_layout);
        ButterKnife.bind(this);
        liveType = getIntent().getStringExtra("liveType");
        userId = getIntent().getStringExtra("userId");
        typeId = "type=" + liveType + "&userId=" + userId;
        if ("0".equals(liveType)) {
            userAttentionTitle.setTitle("关注(0)");
        } else {
            userAttentionTitle.setTitle("粉丝(0)");
        }

        initXlistView();
        contentLoader = new ContentLoader(this);
        contentLoader.setCallBack(new MyCallBack());
        contentLoader.getAttentionOrFansList(typeId);

    }

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            AppLog.i("TAG", "beforeTextChanged");

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            AppLog.i("TAG", "onTextChanged");
        }

        @Override
        public void afterTextChanged(Editable s) {

            String  searchName = liveAttentionSearchEt.getText().toString().trim();
            AppLog.i("TAG","afterTextChanged走了:"+searchName);
            allRows.clear();
            for (LiveFansOrAttentionRowsBean liveFansOrAttentionRowsBean : allSearchRows) {
                String nickName = liveFansOrAttentionRowsBean.getNickName();
                if (searchName.length()>0&&nickName.contains(searchName)) {
                    AppLog.i("TAG","searchName"+searchName);
                    allRows.add(liveFansOrAttentionRowsBean);
                }
            }
            attentionOrFansAdapter.refresh(allRows);
        }
    };

    private void initXlistView() {
        liveAttentionListview.setPullLoadEnable(true);
        liveAttentionListview.setPullRefreshEnable(true);
        liveAttentionListview.setXListViewListener(this);
        liveAttentionListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LiveFansOrAttentionRowsBean liveFansOrAttentionRowsBean = allRows.get(position - 1);
                Intent intent=new Intent(LiveAttentionOrFansActivity.this,LiveHomePageActivity.class);
                int id1 = liveFansOrAttentionRowsBean.getId();
                AppLog.i("TAG","id:"+id1);
                intent.putExtra("userId",id1+"");
                startActivity(intent);

            }
        });
    }


    @OnClick({R.id.live_attention_search_et, R.id.live_attention_search_cancel,R.id.live_search_layout_font})
    public void clickButton(View view) {
        switch (view.getId()) {
            case R.id.live_search_layout_font:
                liveAttentionListview.setPullRefreshEnable(false);
                liveSearchLayoutFont.setVisibility(View.GONE);
                liveSearchLayout.setVisibility(View.VISIBLE);
            case R.id.live_attention_search_et:
                liveAttentionSearchEt.setCompoundDrawables(null,null,null,null);
                liveAttentionSearchCancel.setVisibility(View.VISIBLE);
                userAttentionTitle.setVisibility(View.GONE);
                attentionOrFansAdapter.refresh(null);
                isSearchFansOrAttention = true;
                allSearchRows.clear();

                liveAttentionSearchEt.addTextChangedListener(watcher);
                liveAttentionSearchEt.setText("");
                allRows.clear();
                for (int i = 1; i <=totalPages; i++) {
                    String  typeIdTotal = "type=" + liveType + "&userId=" + userId + "&pageSize=10&pageNumber="+ i;
                    contentLoader.getAttentionOrFansList(typeIdTotal);
                }
                break;
            case R.id.live_attention_search_cancel:
                liveSearchLayoutFont.setVisibility(View.VISIBLE);
                liveSearchLayout.setVisibility(View.GONE);
                liveAttentionListview.setPullRefreshEnable(true);
                isSearchFansOrAttention = false;
                allRows.clear();
                liveAttentionSearchCancel.setVisibility(View.GONE);
                userAttentionTitle.setVisibility(View.VISIBLE);
                liveAttentionSearchEt.setText("");
                contentLoader.getAttentionOrFansList(typeId);
                break;
        }
    }


    List<LiveFansOrAttentionRowsBean> allRows = new ArrayList<LiveFansOrAttentionRowsBean>();
    boolean isSearchFansOrAttention = false;
    List<LiveFansOrAttentionRowsBean> allSearchRows = new ArrayList<LiveFansOrAttentionRowsBean>();


    public class MyCallBack extends ICallBack {
        @Override
        public void onLiveFansOrAttention(LiveFansOrAttentionResp liveFansOrAttentionResp, boolean isSecarch) {
            super.onLiveFansOrAttention(liveFansOrAttentionResp, isSecarch);
            if (isSearchFansOrAttention) {
                LiveFansOrAttentionResultBean result = liveFansOrAttentionResp.getResult();
                searchRows = result.getRows();
                AppLog.i("TAG","searchRows.size()"+searchRows.size()+"");
                allSearchRows.addAll(allSearchRows.size(), searchRows);
                return;
            }
            if (liveFansOrAttentionResp.getReturnCode() == 0) {
                LiveFansOrAttentionResultBean result = liveFansOrAttentionResp.getResult();
                int totalRows = result.getTotalRows();
                totalPages = result.getTotalPages();
                if ("0".equals(liveType)) {
                    userAttentionTitle.setTitle("关注(" + totalRows + ")");
                } else {
                    userAttentionTitle.setTitle("粉丝(" + totalRows + ")");
                }
                List<LiveFansOrAttentionRowsBean> rows = result.getRows();
                if (isFirstLoadData) {
                    isFirstLoadData = false;
                    attentionOrFansAdapter = new AttentionOrFansAdapter(LiveAttentionOrFansActivity.this, rows);
                    liveAttentionListview.setAdapter(attentionOrFansAdapter);
                    allRows.addAll(rows);
                } else {
                    if(isRefreshStatus==1){
                        allRows.clear();
                        pages=2;
                    }
                    allRows.addAll(allRows.size(), rows);
                    attentionOrFansAdapter.refresh(allRows);
                }
                liveAttentionListview.stopRefresh();
                liveAttentionListview.stopLoadMore();
            }
        }


    }

    boolean isFirstLoadData = true;
    int isRefreshStatus=0;

    @Override
    public void onRefresh() {
        isRefreshStatus=1;
        contentLoader.getAttentionOrFansList(typeId);
    }

    int pages = 2;
    String typeIdMore;
    @Override
    public void onLoadMore() {
        isRefreshStatus=2;
        if (pages <=totalPages) {
            typeIdMore = "type=" + liveType + "&userId=" + userId + "&pageSize=10&pageNumber=" + pages;
            contentLoader.getAttentionOrFansList(typeIdMore);
            pages=pages+1;
        } else {
            Toast.makeText(LiveAttentionOrFansActivity.this, "没有更多数据", Toast.LENGTH_SHORT).show();
            liveAttentionListview.stopLoadMore();
        }

    }
}
