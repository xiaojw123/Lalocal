package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.entertainment.activity.AudienceActivity;
import com.lalocal.lalocal.model.LiveSeachItem;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.adapter.LiveSearchAdapter;
import com.lalocal.lalocal.view.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LiveSearchActivity extends BaseActivity implements TextView.OnEditorActionListener {

    @BindView(R.id.live_search_edt)
    EditText liveSearchEdt;
    @BindView(R.id.live_search_cancel_tv)
    TextView liveSearchCancelTv;
    @BindView(R.id.live_search_xrlv)
    XRecyclerView liveSearchXrlv;
    @BindView(R.id.live_search_null)
    TextView liveSearchNull;
    @BindColor(R.color.color_b3)
    int searchIconColor;
    @BindDimen(R.dimen.dimen_size_8_dp)
    int drawablePadding;

    int mPageNumb;
    String mLastSearhText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_search_layout);
        unbinder = ButterKnife.bind(this);
        liveSearchEdt.setCompoundDrawablePadding(drawablePadding);
        liveSearchEdt.setCompoundDrawables(getColorDrawable(searchIconColor), null, null, null);
        liveSearchEdt.setOnEditorActionListener(this);
        liveSearchXrlv.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
        liveSearchXrlv.setPullRefreshEnabled(false);
        setLoaderCallBack(new LiveSearchCallBack());

    }

    @NonNull
    private Drawable getColorDrawable(int color) {
        Drawable drawable = getResources().getDrawable(R.drawable.searchbar_searchicon);
        Drawable colorDrawable = DrawableUtils.tintDrawable(drawable, ColorStateList.valueOf(color));
        colorDrawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return colorDrawable;
    }


    @OnClick(R.id.live_search_cancel_tv)
    public void onClick() {
        finish();
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        String searchText = v.getText().toString();
        searchText = searchText.replaceAll(" ", "");
        searchText = searchText.trim();
        if (actionId == EditorInfo.IME_ACTION_SEARCH && !TextUtils.isEmpty(searchText) && !searchText.equals(mLastSearhText)) {
            mLastSearhText = searchText;
            mPageNumb = 1;
            mContentloader.searchLive(mPageNumb, searchText);
        } else {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                liveSearchNull.setVisibility(View.VISIBLE);
                liveSearchXrlv.setVisibility(View.GONE);
            }
        }
        return false;
    }

    class LiveSearchCallBack extends ICallBack implements XRecyclerView.LoadingListener, OnItemClickListener {
        int toalPages;
        LiveSearchAdapter adapter;
        List<LiveSeachItem.RowsBean> mAllRows = new ArrayList<>();

        @Override
        public void onSearchLive(LiveSeachItem item) {
            int toalRows = item.getTotalRows();
            if (toalRows <= 0) {
                liveSearchNull.setVisibility(View.VISIBLE);
                liveSearchXrlv.setVisibility(View.GONE);
                return;
            } else {
                liveSearchNull.setVisibility(View.GONE);
                liveSearchXrlv.setVisibility(View.VISIBLE);
            }
            mPageNumb = item.getPageNumber();
            toalPages = item.getTotalPages();
            if (mPageNumb <= 1) {
                mAllRows.clear();
            } else {
                liveSearchXrlv.loadMoreComplete();
            }
            AppLog.print("row size___" + item.getRows().size());
            mAllRows.addAll(item.getRows());
            AppLog.print("size___" + mAllRows.size());
            if (adapter == null) {
                if (toalPages > 1) {
                    liveSearchXrlv.setLoadingMoreEnabled(true);
                } else {
                    liveSearchXrlv.setLoadingMoreEnabled(false);
                }
                liveSearchXrlv.setLayoutManager(new LinearLayoutManager(LiveSearchActivity.this));
                liveSearchXrlv.setLoadingListener(this);
                adapter = new LiveSearchAdapter(mAllRows);
                adapter.setOnItemClickListener(this);
                liveSearchXrlv.setAdapter(adapter);
            } else {
                adapter.updatItems(mAllRows);
            }
        }

        @Override
        public void onRefresh() {
            liveSearchXrlv.refreshComplete();
        }

        @Override
        public void onResponseFailed() {
            super.onResponseFailed();
        }

        @Override
        public void onError(VolleyError volleyError) {
            super.onError(volleyError);
        }

        @Override
        public void onLoadMore() {
            AppLog.print("onLoadMore______");
            if (mPageNumb < toalPages) {
                ++mPageNumb;
                mContentloader.searchLive(mPageNumb, mLastSearhText);
            } else {
                liveSearchXrlv.loadMoreComplete();
            }

        }

        @Override
        public void onItemClickListener(View view, int position) {
            Intent intent = new Intent(LiveSearchActivity.this, AudienceActivity.class);
            intent.putExtra(AudienceActivity.LIVE_SEARCH_ITEM, (LiveSeachItem.RowsBean) view.getTag());
            startActivity(intent);
        }
    }


}
