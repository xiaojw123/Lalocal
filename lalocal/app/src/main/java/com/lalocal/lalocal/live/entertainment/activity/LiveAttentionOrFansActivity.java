package com.lalocal.lalocal.live.entertainment.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.AccountEidt1Activity;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.live.entertainment.adapter.AttentionOrFansRecyAdapter;
import com.lalocal.lalocal.live.entertainment.ui.CustomLinearLayoutManager;
import com.lalocal.lalocal.model.LiveFansOrAttentionResp;
import com.lalocal.lalocal.model.LiveFansOrAttentionResultBean;
import com.lalocal.lalocal.model.LiveFansOrAttentionRowsBean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.KeyboardUtil;
import com.lalocal.lalocal.view.ClearEditText;
import com.lalocal.lalocal.view.CustomTitleView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by android on 2016/8/7.
 */
public class LiveAttentionOrFansActivity extends BaseActivity implements CustomTitleView.onBackBtnClickListener,View.OnTouchListener {

    @BindView(R.id.attention_tilte)
    LinearLayout attentionTitle;
    @BindView(R.id.attention_search_cet)
    ClearEditText attentionSearchCet;
    @BindView(R.id.attention_cancel_tv)
    TextView attentionCancelTv;
    @BindView(R.id.attention_search_layout)
    RelativeLayout attentionSearchLayout;
    @BindView(R.id.attention_title_back)
    ImageView attentionTitleBack;
    @BindView(R.id.user_attention_title)
    TextView userAttentionTitle;
    @BindView(R.id.attention_search)
    ImageView attentionSearch;
    @BindView(R.id.search_result_null)
    TextView searchResultNull;
    @BindView(R.id.live_attention_listview)
    XRecyclerView liveAttentionListview;
    private String liveType;

    private String typeId;
    private ContentLoader contentLoader;
    private int totalPages;
    private String userId;

    private List<LiveFansOrAttentionRowsBean> searchRows;
    private Timer timer;
    private AttentionOrFansRecyAdapter attentionOrFansRecyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_attention_list_layout);
        ButterKnife.bind(this);
        liveType = getIntent().getStringExtra("liveType");
        userId = getIntent().getStringExtra("userId");
        typeId = "type=" + liveType + "&userId=" + userId;
        if ("0".equals(liveType)) {
            userAttentionTitle.setText("关注(0)");
        } else {
            userAttentionTitle.setText("粉丝(0)");
        }
        contentLoader = new ContentLoader(this);
        contentLoader.setCallBack(new MyCallBack());
        attentionSearchCet.addTextChangedListener(watcher);
        initXlistView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();

        AppLog.i("TAG", "getAttentionOrFansList：" + "走了onStart");

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
            String searchName = attentionSearchCet.getText().toString().trim();
            showSearchRows.clear();
            for (LiveFansOrAttentionRowsBean liveFansOrAttentionRowsBean : allSearchRows) {
                String nickName = liveFansOrAttentionRowsBean.getNickName();
                if (searchName.length() > 0 && nickName.contains(searchName)) {
                    showSearchRows.add(liveFansOrAttentionRowsBean);
                }
            }
            Collections.sort(showSearchRows, comp);
            if (attentionOrFansRecyAdapter != null) {
                attentionOrFansRecyAdapter.refresh(showSearchRows);
            }
            if (searchName.length() > 0) {
                if (showSearchRows == null || showSearchRows.size() == 0) {
                    searchResultNull.setVisibility(View.VISIBLE);
                    liveAttentionListview.setVisibility(View.GONE);
                    AppLog.i("TAG","搜索1");
                    searchResultNull.setText("暂无搜索结果!");
                } else {
                    searchResultNull.setVisibility(View.GONE);
                    liveAttentionListview.setVisibility(View.VISIBLE);
                    AppLog.i("TAG","搜索2");
                }

            } else {
                searchResultNull.setVisibility(View.GONE);
                AppLog.i("TAG","搜索3");
            }
        }
    };

    private void initXlistView() {
        CustomLinearLayoutManager layoutManager = new CustomLinearLayoutManager(this);
        layoutManager.setOrientation(CustomLinearLayoutManager.VERTICAL);
        liveAttentionListview.setLayoutManager(layoutManager);
        XRecyclerviewLoadingListener xRecyclerviewLoadingListener = new XRecyclerviewLoadingListener();
        liveAttentionListview.setLoadingListener(xRecyclerviewLoadingListener);
        liveAttentionListview.setPullRefreshEnabled(true);
        liveAttentionListview.setLoadingMoreEnabled(true);
        liveAttentionListview.setRefreshing(true);
        attentionOrFansRecyAdapter = new AttentionOrFansRecyAdapter(LiveAttentionOrFansActivity.this, null);
        liveAttentionListview.setAdapter(attentionOrFansRecyAdapter);
        liveAttentionListview.setOnTouchListener(this);
        listItemAdapter();
    }

    int searchPages = 1;

    @OnClick({R.id.attention_search_cet,R.id.attention_title_back,R.id.attention_search,R.id.attention_cancel_tv})
    public void clickButton(View view) {
        switch (view.getId()) {

            case R.id.attention_title_back:
                finish();
                break;
            case R.id.attention_cancel_tv:
                showSearchRows.clear();
                allSearchRows.clear();
                searchPages = 1;
                isSearchFansOrAttention = false;
                attentionSearchCet.setText("");
                attentionTitle.setVisibility(View.VISIBLE);
                attentionSearchLayout.setVisibility(View.GONE);
                if(liveAttentionListview.getVisibility()==View.GONE){
                    liveAttentionListview.setVisibility(View.VISIBLE);
                }
                liveAttentionListview.setPullRefreshEnabled(true);
                liveAttentionListview.setLoadingMoreEnabled(true);
                attentionOrFansRecyAdapter.refresh(allRows);
                InputMethodManager inputManager =
                        (InputMethodManager) attentionSearchCet.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(attentionSearchCet.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                break;
            case R.id.attention_search:
                attentionTitle.setVisibility(View.GONE);
                attentionSearchLayout.setVisibility(View.VISIBLE);
                attentionSearchCet.requestFocus();
                attentionSearchCet.setFocusable(true);
                liveAttentionListview.setPullRefreshEnabled(false);
                liveAttentionListview.setLoadingMoreEnabled(false);
            case R.id.attention_search_cet:
                if (timer == null) {
                    timer = new Timer();
                }
                timer.schedule(new TimerTask() {
                                   public void run() {
                                       InputMethodManager inputManager =
                                               (InputMethodManager) attentionSearchCet.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                       inputManager.showSoftInput(attentionSearchCet, 0);
                                   }
                               },
                        500);
                isSearchFansOrAttention = true;
                if (totalPages > 0) {
                    String typeIdTotal = "type=" + liveType + "&userId=" + userId + "&pageSize=10&pageNumber=" + searchPages;
                    contentLoader.getAttentionOrFansList(typeIdTotal);
                }

                break;
        }
    }


    List<LiveFansOrAttentionRowsBean> allRows = new ArrayList<LiveFansOrAttentionRowsBean>();
    boolean isSearchFansOrAttention = false;
    List<LiveFansOrAttentionRowsBean> allSearchRows = new ArrayList<LiveFansOrAttentionRowsBean>();
    List<LiveFansOrAttentionRowsBean> showSearchRows = new ArrayList<LiveFansOrAttentionRowsBean>();
    @Override
    public void onBackClick() {
        setResult(AccountEidt1Activity.UPDATE_ME_DATA);
    }

    @Override
    public void onBackPressed() {
        setResult(AccountEidt1Activity.UPDATE_ME_DATA);
        super.onBackPressed();
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            KeyboardUtil.hidenSoftKey(attentionSearchCet);
        }
        return false;
    }

    public class MyCallBack extends ICallBack {

        @Override
        public void onError(VolleyError volleyError) {
            super.onError(volleyError);

            isRefreshStatus = false;
            liveAttentionListview.refreshComplete();
            liveAttentionListview.loadMoreComplete();
        }

        @Override
        public void onLiveFansOrAttention(LiveFansOrAttentionResp liveFansOrAttentionResp, boolean isSecarch) {
            super.onLiveFansOrAttention(liveFansOrAttentionResp, isSecarch);
            if (isSearchFansOrAttention) {
                LiveFansOrAttentionResultBean result = liveFansOrAttentionResp.getResult();
                searchRows = result.getRows();
                allSearchRows.addAll(allSearchRows.size(), searchRows);
                Collections.sort(allSearchRows, comp);
                ++searchPages;
                if (searchPages <= totalPages) {
                    String typeIdTotal = "type=" + liveType + "&userId=" + userId + "&pageSize=10&pageNumber=" + searchPages;
                    contentLoader.getAttentionOrFansList(typeIdTotal);
                }
                return;
            }
            if (liveFansOrAttentionResp.getReturnCode() == 0) {
                isSearchFansOrAttention =false;
                LiveFansOrAttentionResultBean result = liveFansOrAttentionResp.getResult();
                int totalRows = result.getTotalRows();
                if (totalRows == 0) {
                    searchResultNull.setVisibility(View.VISIBLE);
                    liveAttentionListview.setVisibility(View.GONE);
                    if ("0".equals(liveType)) {
                        searchResultNull.setText(getString(R.string.no_attention));

                    } else {
                        searchResultNull.setText("暂时没有粉丝哦!");
                    }
                } else {
                    searchResultNull.setVisibility(View.GONE);
                    liveAttentionListview.setVisibility(View.VISIBLE);
                    totalPages = result.getTotalPages();
                    if ("0".equals(liveType)) {
                        userAttentionTitle.setText("关注(" + totalRows + ")");
                    } else {
                        userAttentionTitle.setText("粉丝(" + totalRows + ")");
                    }
                    List<LiveFansOrAttentionRowsBean> rows = result.getRows();
                    if (isRefreshStatus) {
                        allRows.clear();
                        pages = 2;
                        liveAttentionListview.refreshComplete();
                    } else{
                        liveAttentionListview.loadMoreComplete();
                    }
                    allRows.addAll(allRows.size(), rows);
                    if ("0".equals(liveType)) {
                    } else {
                        Collections.sort(allRows, comp);
                    }
                    attentionOrFansRecyAdapter.refresh(allRows);
                }
            }
        }
    }

    private void listItemAdapter() {
        attentionOrFansRecyAdapter.setOnAttentionToFansItemClickListener(new AttentionOrFansRecyAdapter.OnAttentionToFansItemClickListener() {
            @Override
            public void onItemClick(int id) {
                Intent intent = new Intent(LiveAttentionOrFansActivity.this, LiveHomePageActivity.class);
                intent.putExtra("userId", String.valueOf(id));
                startActivity(intent);
                finish();
            }
        });

    }

    private static Comparator<LiveFansOrAttentionRowsBean> comp = new Comparator<LiveFansOrAttentionRowsBean>() {
        @Override
        public int compare(LiveFansOrAttentionRowsBean lhs, LiveFansOrAttentionRowsBean rhs) {
            if (lhs == null) {
                return 1;
            }
            if (rhs == null) {
                return -1;
            }

            return lhs.getAttentionVO().getStatus() - rhs.getAttentionVO().getStatus();
        }
    };

    public class XRecyclerviewLoadingListener implements XRecyclerView.LoadingListener {

        @Override
        public void onRefresh() {
            AppLog.i("TAG","刷新页面。。。。。");
            isRefreshStatus=true;
            contentLoader.getAttentionOrFansList(typeId);
        }

        @Override
        public void onLoadMore() {
            isRefreshStatus=false;
            if (pages <= totalPages) {
                typeIdMore = "type=" + liveType + "&userId=" + userId + "&pageSize=10&pageNumber=" + pages;
                contentLoader.getAttentionOrFansList(typeIdMore);
                pages = pages + 1;
            } else {
                liveAttentionListview.setNoMore(true);
            }
        }

    }



    boolean isRefreshStatus=true;
    int pages = 2;
    String typeIdMore;


}
