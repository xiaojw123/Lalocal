package com.lalocal.lalocal.live.entertainment.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.lalocal.lalocal.view.CustomTitleView;
import com.lalocal.lalocal.view.xlistview.XListView;

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
public class LiveAttentionOrFansActivity extends BaseActivity implements XListView.IXListViewListener, CustomTitleView.onBackBtnClickListener {
    @BindView(R.id.user_attention_title)
    CustomTitleView userAttentionTitle;
    @BindView(R.id.live_attention_search_et)
    EditText liveAttentionSearchEt;
    @BindView(R.id.seach_clear_btn)
    ImageView searchClearBtn;
    @BindView(R.id.live_attention_search_cancel)
    TextView liveAttentionSearchCancel;
    @BindView(R.id.search_text_hint)
    TextView searchTextHint;
    @BindView(R.id.live_attention_listview)
    XRecyclerView liveAttentionListview;
    @BindView(R.id.live_search_layout_to)
    LinearLayout liveSearchLayout;
    @BindView(R.id.live_search_layout_font)
    LinearLayout liveSearchLayoutFont;
    @BindView(R.id.search_result_null)
    TextView searchResultNull;

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
            userAttentionTitle.setTitle("关注(0)");
        } else {
            userAttentionTitle.setTitle("粉丝(0)");
        }
        userAttentionTitle.setOnBackClickListener(this);
        initXlistView();
        contentLoader = new ContentLoader(this);
        contentLoader.setCallBack(new MyCallBack());
       contentLoader.getAttentionOrFansList(typeId);

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onStart() {
        super.onStart();

        AppLog.i("TAG","getAttentionOrFansList："+"走了onStart");
       // contentLoader.getAttentionOrFansList(typeId);

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
            String searchName = liveAttentionSearchEt.getText().toString().trim();
            if (searchName.length() > 0) {
                searchTextHint.setVisibility(View.GONE);
                searchClearBtn.setVisibility(View.VISIBLE);
                if(allRows==null||allRows.size()==0){
                    searchResultNull.setVisibility(View.VISIBLE);
                    liveAttentionListview.setVisibility(View.GONE);
                    searchResultNull.setText("暂无搜索结果!");
                }else {
                    searchResultNull.setVisibility(View.GONE);
                    liveAttentionListview.setVisibility(View.VISIBLE);
                }

            } else {
                searchTextHint.setVisibility(View.VISIBLE);
                searchClearBtn.setVisibility(View.GONE);
                searchResultNull.setVisibility(View.GONE);
            }

            allRows.clear();
            for (LiveFansOrAttentionRowsBean liveFansOrAttentionRowsBean : allSearchRows) {
                String nickName = liveFansOrAttentionRowsBean.getNickName();
                if (searchName.length() > 0 && nickName.contains(searchName)) {
                    allRows.add(liveFansOrAttentionRowsBean);
                }
            }
            Collections.sort(allRows, comp);
            if(attentionOrFansRecyAdapter!=null){
                attentionOrFansRecyAdapter.refresh(allRows);
            }
        }
    };

    private void initXlistView() {
        CustomLinearLayoutManager layoutManager = new CustomLinearLayoutManager(this);
        layoutManager.setOrientation(CustomLinearLayoutManager.VERTICAL);
        liveAttentionListview.setLayoutManager(layoutManager);
        liveAttentionListview.setPullRefreshEnabled(true);
        liveAttentionListview.setLoadingMoreEnabled(true);
        liveAttentionListview.setRefreshing(true);
        XRecyclerviewLoadingListener xRecyclerviewLoadingListener = new XRecyclerviewLoadingListener();
        liveAttentionListview.setLoadingListener(xRecyclerviewLoadingListener);
    }

    int searchPages=1;
    @OnClick({R.id.live_attention_search_et, R.id.live_attention_search_cancel, R.id.live_search_layout_font, R.id.seach_clear_btn})
    public void clickButton(View view) {
        switch (view.getId()) {
            case R.id.live_search_layout_font:
                liveSearchLayoutFont.setVisibility(View.GONE);
                liveSearchLayout.setVisibility(View.VISIBLE);
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
                userAttentionTitle.setVisibility(View.GONE);
                if(attentionOrFansRecyAdapter!=null){
                    attentionOrFansRecyAdapter.refresh(null);
                }
                isSearchFansOrAttention = true;
                allSearchRows.clear();
                allRows.clear();
                if(totalPages>0){
                    String typeIdTotal = "type=" + liveType + "&userId=" + userId + "&pageSize=10&pageNumber=" + searchPages;
                    contentLoader.getAttentionOrFansList(typeIdTotal);
                }

                liveAttentionSearchEt.addTextChangedListener(watcher);
                break;
            case R.id.live_attention_search_cancel:
                searchPages=1;
                liveSearchLayoutFont.setVisibility(View.VISIBLE);
                liveSearchLayout.setVisibility(View.GONE);
                isSearchFansOrAttention = false;
                allRows.clear();
                allSearchRows.clear();
                liveAttentionSearchEt.setText("");
                liveAttentionSearchCancel.setVisibility(View.GONE);
                userAttentionTitle.setVisibility(View.VISIBLE);

                contentLoader.getAttentionOrFansList(typeId);
                InputMethodManager inputManager =
                        (InputMethodManager) liveAttentionSearchEt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(liveAttentionSearchEt.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                break;
            case R.id.seach_clear_btn:
                liveAttentionSearchEt.setText("");
                if(attentionOrFansRecyAdapter!=null){
                    attentionOrFansRecyAdapter.refresh(null);
                }
                searchClearBtn.setVisibility(View.GONE);
                break;
        }
    }


    List<LiveFansOrAttentionRowsBean> allRows = new ArrayList<LiveFansOrAttentionRowsBean>();
    boolean isSearchFansOrAttention = false;
    List<LiveFansOrAttentionRowsBean> allSearchRows = new ArrayList<LiveFansOrAttentionRowsBean>();

    @Override
    public void onBackClick() {
        setResult(AccountEidt1Activity.UPDATE_ME_DATA);
    }

    @Override
    public void onBackPressed() {
        setResult(AccountEidt1Activity.UPDATE_ME_DATA);
        super.onBackPressed();

    }

    public class MyCallBack extends ICallBack {
        @Override
        public void onLiveFansOrAttention(LiveFansOrAttentionResp liveFansOrAttentionResp, boolean isSecarch) {
            super.onLiveFansOrAttention(liveFansOrAttentionResp, isSecarch);
            if (isSearchFansOrAttention) {

                LiveFansOrAttentionResultBean result = liveFansOrAttentionResp.getResult();
                searchRows = result.getRows();
                allSearchRows.addAll(allSearchRows.size(), searchRows);
                Collections.sort(allSearchRows, comp);
                ++searchPages;
                if(searchPages<=totalPages){
                    String typeIdTotal = "type=" + liveType + "&userId=" + userId + "&pageSize=10&pageNumber=" + searchPages;
                    contentLoader.getAttentionOrFansList(typeIdTotal);
                }

                return;
            }
            if (liveFansOrAttentionResp.getReturnCode() == 0) {
                if(searchRows!=null){
                    searchRows.clear();
                }
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
                 //   liveAttentionSearchEt.setEnabled(false);

                } else {
                  //  liveAttentionSearchEt.setEnabled(true);
                    searchResultNull.setVisibility(View.GONE);
                    liveAttentionListview.setVisibility(View.VISIBLE);
                    totalPages = result.getTotalPages();
                    if ("0".equals(liveType)) {
                        userAttentionTitle.setTitle("关注(" + totalRows + ")");
                    } else {
                        userAttentionTitle.setTitle("粉丝(" + totalRows + ")");
                    }
                    List<LiveFansOrAttentionRowsBean> rows = result.getRows();
                    Collections.sort(rows, comp);
                    if (isFirstLoadData) {
                        isFirstLoadData = false;

                        attentionOrFansRecyAdapter = new AttentionOrFansRecyAdapter(LiveAttentionOrFansActivity.this,rows);

                        liveAttentionListview.setAdapter(attentionOrFansRecyAdapter);
                        listItemAdapter();
                        allRows.addAll(rows);
                        Collections.sort(allRows, comp);
                    } else {
                        if (isRefreshStatus == 1) {
                            allRows.clear();
                            pages = 2;
                            liveAttentionListview.refreshComplete();
                        }else if(isRefreshStatus==2){
                            liveAttentionListview.loadMoreComplete();
                        }
                        allRows.addAll(allRows.size(), rows);
                        Collections.sort(allRows, comp);
                        attentionOrFansRecyAdapter.refresh(allRows);
                    }
                }

            }
        }
    }

    private void listItemAdapter() {
        attentionOrFansRecyAdapter.setOnAttentionToFansItemClickListener(new AttentionOrFansRecyAdapter.OnAttentionToFansItemClickListener() {
            @Override
            public void onItemClick(int id) {
                Intent intent = new Intent(LiveAttentionOrFansActivity.this, LiveHomePageActivity.class);
                intent.putExtra("userId",String.valueOf(id));
              //  intent.putExtra("back","2");
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

            return lhs.getAttentionVO().getStatus()-rhs.getAttentionVO().getStatus();
        }
    };



    public  class  XRecyclerviewLoadingListener implements XRecyclerView.LoadingListener {

        @Override
        public void onRefresh() {
            isRefreshStatus = 1;
            contentLoader.getAttentionOrFansList(typeId);
        }

        @Override
        public void onLoadMore() {
            isRefreshStatus = 2;
            if (pages <= totalPages) {
                typeIdMore = "type=" + liveType + "&userId=" + userId + "&pageSize=10&pageNumber=" + pages;
                contentLoader.getAttentionOrFansList(typeIdMore);
                pages = pages + 1;
            } else {
                liveAttentionListview.setNoMore(true);
            }
        }

    }





    boolean isFirstLoadData = true;
    int isRefreshStatus = 0;

    @Override
    public void onRefresh() {
        isRefreshStatus = 1;
        contentLoader.getAttentionOrFansList(typeId);
    }

    int pages = 2;
    String typeIdMore;

    @Override
    public void onLoadMore() {
        isRefreshStatus = 2;
        if (pages <= totalPages) {
            typeIdMore = "type=" + liveType + "&userId=" + userId + "&pageSize=10&pageNumber=" + pages;
            contentLoader.getAttentionOrFansList(typeIdMore);
            pages = pages + 1;
        } else {
            Toast.makeText(LiveAttentionOrFansActivity.this, "没有更多数据", Toast.LENGTH_SHORT).show();
       //     liveAttentionListview.stopLoadMore();
        }

    }
}
