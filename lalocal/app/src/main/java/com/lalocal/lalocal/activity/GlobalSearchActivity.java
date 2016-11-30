package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.entertainment.activity.AudienceActivity;
import com.lalocal.lalocal.live.entertainment.activity.LiveHomePageActivity;
import com.lalocal.lalocal.live.entertainment.activity.PlayBackActivity;
import com.lalocal.lalocal.live.entertainment.adapter.AttentionOrFansRecyAdapter;
import com.lalocal.lalocal.model.HistoryItem;
import com.lalocal.lalocal.model.LiveFansOrAttentionRowsBean;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.model.RecommendRowsBean;
import com.lalocal.lalocal.model.SearchItem;
import com.lalocal.lalocal.model.SpecialToH5Bean;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.KeyboardUtil;
import com.lalocal.lalocal.view.adapter.LiveSearchAdapter;
import com.lalocal.lalocal.view.adapter.MoreRecyclerAdapter;
import com.lalocal.lalocal.view.adapter.ThemeAdapter;
import com.lalocal.lalocal.view.listener.OnItemClickListener;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lalocal.lalocal.R.id.gsearch_tab_special;

public class GlobalSearchActivity extends BaseActivity implements XRecyclerView.LoadingListener, TextView.OnEditorActionListener {
    @BindView(R.id.gsearch_cancel_tv)
    TextView gsearchCancelTv;
    @BindView(R.id.gsearch_edit)
    EditText gsearchEdit;
    @BindView(R.id.gsearch_tab_live)
    RelativeLayout gsearchTabLive;
    @BindView(R.id.gsearch_tab_user)
    RelativeLayout gsearchTabUser;
    @BindView(R.id.gsearch_tab_travelnote)
    RelativeLayout gsearchTabTravelnote;
    @BindView(gsearch_tab_special)
    RelativeLayout gsearchTabSpecial;
    @BindView(R.id.gsearch_tab_goods)
    RelativeLayout gsearchTabGoods;
    @BindView(R.id.gsearch_tab_route)
    RelativeLayout gsearchTabRoute;
    @BindView(R.id.gsearch_result_xrv)
    XRecyclerView gsearchResultXrv;
    @BindView(R.id.gsearch_tab_container)
    LinearLayout gsearchTabCotainer;
    @BindView(R.id.gsearch_result_empty)
    TextView searchEmptyTv;
    @BindView(R.id.gsearch_hint_cotainer)
    LinearLayout hintContainer;
    int mPageNum, mTotalPages;
    String mSearchKey;
    boolean isRefresh, isLoadMore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_search);
        ButterKnife.bind(this);
        setLoaderCallBack(new GloabSearchCallBack());
        gsearchResultXrv.setLayoutManager(new LinearLayoutManager(this));
        gsearchResultXrv.setPullRefreshEnabled(true);
        gsearchResultXrv.setLoadingMoreEnabled(true);
        gsearchResultXrv.setLoadingListener(this);
        gsearchEdit.setOnEditorActionListener(this);
        setSelecteTab(gsearchTabLive);
        mContentloader.getSearhHot();
    }


    @OnClick({R.id.gsearch_cancel_tv, R.id.gsearch_tab_live, R.id.gsearch_tab_user, R.id.gsearch_tab_travelnote, gsearch_tab_special, R.id.gsearch_tab_goods, R.id.gsearch_tab_route})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gsearch_cancel_tv:
                finish();
                break;
            case R.id.gsearch_tab_live:
            case R.id.gsearch_tab_user:
            case R.id.gsearch_tab_travelnote:
            case R.id.gsearch_tab_special:
            case R.id.gsearch_tab_goods:
            case R.id.gsearch_tab_route:
                if (view.isSelected()) {
                    return;
                }
                setSelecteTab(view);
                mSearchKey = getName();
                gsearchResultXrv.setRefreshing(true);
                break;
        }
    }


    private void setSelecteTab(View view) {
        AppLog.print("view___" + view);
        for (int i = 0; i < gsearchTabCotainer.getChildCount(); i++) {
            RelativeLayout layout = (RelativeLayout) gsearchTabCotainer.getChildAt(i);
            View view1 = layout.getChildAt(0);
            View view2 = layout.getChildAt(1);
            if (view == layout) {
                AppLog.print("sel__pos__" + i);
                layout.setSelected(true);
                view1.setActivated(true);
                view2.setActivated(true);
            } else {
                AppLog.print("sel__pos__" + i);
                layout.setSelected(false);
                view1.setActivated(false);
                view2.setActivated(false);
            }
        }


    }

    @Override
    public void onRefresh() {
        KeyboardUtil.hidenSoftKey(gsearchEdit);
        if (!TextUtils.isEmpty(mSearchKey)) {
            isRefresh = true;
            if (gsearchTabLive.isSelected()) {
                RecyclerView.Adapter adapter = gsearchResultXrv.getAdapter();
                if (adapter instanceof LiveSearchAdapter) {
                    mContentloader.getGlobalSearchPlayBack(mSearchKey, 1);
                } else {
                    mContentloader.getGlobalSearchLive(mSearchKey);
                }
            }
            if (gsearchTabUser.isSelected()) {
                mContentloader.getGloablSearchUser(mSearchKey, 1);
            }
            if (gsearchTabTravelnote.isSelected()) {
                mContentloader.getMoreAritleResult(mSearchKey, 1, 10);
            }
            if (gsearchTabSpecial.isSelected()) {
                mContentloader.getGlobalSearchSpecial(mSearchKey, 1);
            }
            if (gsearchTabGoods.isSelected()) {
                mContentloader.getMoreProductResult(mSearchKey, 1, 10);
            }
            if (gsearchTabRoute.isSelected()) {
                mContentloader.getMoreRouteResult(mSearchKey, 1, 10);
            }
        } else {
            showEmptResult();
            gsearchResultXrv.refreshComplete();
        }
    }

    @Override
    public void onLoadMore() {
        if (mPageNum < mTotalPages) {
            isLoadMore = true;
            ++mPageNum;
            if (gsearchTabLive.isSelected()) {
                mContentloader.getGlobalSearchPlayBack(mSearchKey, mPageNum);
            }
            if (gsearchTabUser.isSelected()) {
                mContentloader.getGloablSearchUser(mSearchKey, mPageNum);
            }
            if (gsearchTabTravelnote.isSelected()) {
                mContentloader.getMoreAritleResult(mSearchKey, mPageNum, 10);
            }
            if (gsearchTabSpecial.isSelected()) {
                mContentloader.getGlobalSearchSpecial(mSearchKey, mPageNum);
            }
            if (gsearchTabGoods.isSelected()) {
                mContentloader.getMoreProductResult(mSearchKey, mPageNum, 10);
            }
            if (gsearchTabRoute.isSelected()) {
                mContentloader.getMoreRouteResult(mSearchKey, mPageNum, 10);
            }
        } else {
            gsearchResultXrv.setNoMore(true);
        }

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            mSearchKey = v.getText().toString();
            if (TextUtils.isEmpty(mSearchKey)) {
                CommonUtil.showPromptDialog(this, "搜索词不可为空", null);
                return false;
            }
            KeyboardUtil.hidenSoftKey(gsearchEdit);
            RecyclerView.Adapter adapter = gsearchResultXrv.getAdapter();
            if (adapter instanceof LiveSearchAdapter) {
                mContentloader.getGlobalSearchPlayBack(mSearchKey, 1);
            } else {
                mContentloader.getGlobalSearchLive(mSearchKey);
            }
            mContentloader.getGloablSearchUser(mSearchKey, 1);
            mContentloader.getMoreAritleResult(mSearchKey, 1, 10);
            mContentloader.getGlobalSearchSpecial(mSearchKey, 1);
            mContentloader.getMoreProductResult(mSearchKey, 1, 10);
            mContentloader.getMoreRouteResult(mSearchKey, 1, 10);
            saveSearchHistory();
        }
        return false;
    }

    private void saveSearchHistory() {
        try {
            List<HistoryItem> items = DataSupport.findAll(HistoryItem.class);
            if (items == null) {
                items = new ArrayList<>();
            }
            for (HistoryItem item : items) {
                if (mSearchKey.equals(item.getName())) {
                    DataSupport.deleteAll(HistoryItem.class, "name=?", mSearchKey);
                    items = DataSupport.findAll(HistoryItem.class);
                }
            }
            HistoryItem item = new HistoryItem();
            item.setName(mSearchKey);
            items.add(item);
            Collections.reverse(items);
            if (items.size() > 3) {
                List<HistoryItem> deltes = items.subList(3, items.size());
                for (HistoryItem d : deltes) {
                    DataSupport.deleteAll(HistoryItem.class, "name=?", d.getName());
                }
                items.removeAll(items.subList(3, items.size()));
            }
            DataSupport.saveAll(items);

        } catch (Exception e) {
        }
    }

    class GloabSearchCallBack extends ICallBack {
        List<LiveRowsBean> rowsBeanList = new ArrayList<>();
        List<LiveRowsBean> playBackList = new ArrayList<>();
        List<LiveFansOrAttentionRowsBean> userList = new ArrayList<>();
        List<RecommendRowsBean> specialList = new ArrayList<>();
        List<SearchItem> articleItems = new ArrayList<>();
        List<SearchItem> productItems = new ArrayList<>();
        List<SearchItem> routeItems = new ArrayList<>();
        LiveSearchAdapter searchAdapter;
        AttentionOrFansRecyAdapter userAdapter;
        ThemeAdapter themeAdapter;
        MoreRecyclerAdapter articleAdapter, productAdapter, routeAdapter;
        int liveLen;

        @Override
        public void onGetGLiveSearch(List<LiveRowsBean> rowList, String name) {
            mSearchKey = name;
            liveLen = rowList.size();
            rowsBeanList.addAll(rowList);
            mContentloader.getGlobalSearchPlayBack(name, 1);

        }

        @Override
        public void onGetSearchHot(List<String> keys) {
            List<HistoryItem> items = null;
            try {
                items = DataSupport.findAll(HistoryItem.class);
                Collections.reverse(items);
            } catch (Exception e) {
            }
            showSearchHint(items, keys);

        }

        private void showSearchHint(List<HistoryItem> historyItems, List<String> hotKeys) {
            Resources res = getResources();
            if (historyItems != null && historyItems.size() > 0) {
                int len = historyItems.size();
                for (int i = 0; i < len; i++) {
                    LinearLayout.LayoutParams hParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    if (i == 0) {
                        hParams.topMargin = (int) res.getDimension(R.dimen.dimen_size_15_dp);
                    } else {
                        hParams.topMargin = (int) res.getDimension(R.dimen.dimen_size_10_dp);
                    }
                    TextView historyTv = new TextView(GlobalSearchActivity.this);
                    historyTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, res.getDimension(R.dimen.text_size_16_sp));
                    historyTv.setTextColor(res.getColor(R.color.color_50191000));
                    HistoryItem item = historyItems.get(i);
                    historyTv.setText(Html.fromHtml("<u>" + item.getName() + "</u>"));
                    historyTv.setTag(item.getName());
                    historyTv.setOnClickListener(hintClickListener);
                    hintContainer.addView(historyTv, hParams);
                }
                TextView hotTitleTv = new TextView(GlobalSearchActivity.this);
                hotTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, res.getDimension(R.dimen.text_size_15_sp));
                hotTitleTv.setTextColor(res.getColor(R.color.color_191000));
                hotTitleTv.setText("当下热门");
                LinearLayout.LayoutParams hotTitleParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                hotTitleParams.topMargin = (int) res.getDimension(R.dimen.dimen_size_50_dp);
                hotTitleParams.bottomMargin = (int) res.getDimension(R.dimen.dimen_size_15_dp);
                hintContainer.addView(hotTitleTv, hotTitleParams);
                int hotLen = hotKeys.size();
                boolean multiKey = true;
                for (int i = 0; i < hotLen; i++) {
                    TextView keyTv = new TextView(GlobalSearchActivity.this);
                    keyTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, res.getDimension(R.dimen.text_size_16_sp));
                    keyTv.setTextColor(res.getColor(R.color.color_50191000));
                    String key = hotKeys.get(i);
                    int nextPos = i + 1;
                    LinearLayout.LayoutParams keyParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    keyParams.bottomMargin = (int) res.getDimension(R.dimen.dimen_size_10_dp);
                    keyTv.setOnClickListener(hintClickListener);
                    if (nextPos % 3 == 0) {
                        multiKey = true;
                        keyTv.setTag(key);
                        keyTv.setText(Html.fromHtml("<u>" + key + "</u>"));
                    } else {
                        if (multiKey) {
                            multiKey = false;
                            if (nextPos < hotLen) {
                                keyTv.setTag(key);
                                keyTv.setText(Html.fromHtml("<u>" + key + "</u>"));
                                LinearLayout multiTextCotainer = new LinearLayout(GlobalSearchActivity.this);
                                TextView key2Tv = new TextView(GlobalSearchActivity.this);
                                String key2 = hotKeys.get(nextPos);
                                key2Tv.setTag(key2);
                                key2Tv.setOnClickListener(hintClickListener);
                                key2Tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, res.getDimension(R.dimen.text_size_16_sp));
                                key2Tv.setTextColor(res.getColor(R.color.color_50191000));
                                key2Tv.setText(Html.fromHtml("<u>" + key2 + "</u>"));
                                LinearLayout.LayoutParams key2Params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                key2Params.bottomMargin = (int) res.getDimension(R.dimen.dimen_size_10_dp);
                                key2Params.leftMargin = (int) res.getDimension(R.dimen.dimen_size_20_dp);
                                multiTextCotainer.setOrientation(LinearLayout.HORIZONTAL);
                                multiTextCotainer.addView(keyTv);
                                multiTextCotainer.addView(key2Tv, key2Params);
                                hintContainer.addView(multiTextCotainer);
                                continue;
                            } else {
                                keyTv.setTag(key);
                                keyTv.setText(Html.fromHtml("<u>" + key + "</u>"));
                            }
                        } else {
                            continue;
                        }
                    }
                    hintContainer.addView(keyTv, keyParams);
                }


            } else {
                TextView titleTv = new TextView(GlobalSearchActivity.this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.topMargin = (int) res.getDimension(R.dimen.dimen_size_50_dp);
                params.bottomMargin = (int) res.getDimension(R.dimen.dimen_size_20_dp);
                titleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, res.getDimension(R.dimen.text_size_24_sp));
                titleTv.setTextColor(res.getColor(R.color.color_191000));
                titleTv.setText("你可以这样搜");
                hintContainer.addView(titleTv, params);
                for (String key : hotKeys) {
                    TextView keyText = new TextView(GlobalSearchActivity.this);
                    keyText.setTextSize(TypedValue.COMPLEX_UNIT_PX, res.getDimension(R.dimen.text_size_16_sp));
                    keyText.setTextColor(res.getColor(R.color.color_50191000));
                    keyText.setText(Html.fromHtml("<u>" + key + "</u>"));
                    LinearLayout.LayoutParams keyParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    keyParams.bottomMargin = (int) res.getDimension(R.dimen.dimen_size_10_dp);
                    keyText.setTag(key);
                    keyText.setOnClickListener(hintClickListener);
                    hintContainer.addView(keyText, keyParams);
                }
            }
        }

        @Override
        public void onGetGPlayBackSearch(int pageNum, int toalPages, List<LiveRowsBean> rowList) {
            AppLog.print("onGetGPlayBackSearch_____" + rowList);
            mPageNum = pageNum;
            mTotalPages = toalPages;
            if (isRefresh) {
                isRefresh = false;
                gsearchResultXrv.refreshComplete();
                playBackList.clear();
            }
            if (isLoadMore) {
                isLoadMore = false;
                gsearchResultXrv.loadMoreComplete();
            }
            playBackList.addAll(rowList);
            rowsBeanList.addAll(playBackList);
            if (rowsBeanList.size() > 0) {
                showDataResult();
                if (searchAdapter == null) {
                    searchAdapter = new LiveSearchAdapter(rowsBeanList);
                    searchAdapter.setOnItemClickListener(itemClickListener);
                } else {
                    searchAdapter.updatItems(rowsBeanList);
                }
                gsearchResultXrv.setAdapter(searchAdapter);
            } else {
                showEmptResult();
            }
        }

        @Override
        public void onGetGUserSearch(String key, int pageNum, int toalPages, List<LiveFansOrAttentionRowsBean> beanList) {
            AppLog.print("onGetGUserSearch_____" + beanList);
            mSearchKey = key;
            mPageNum = pageNum;
            mTotalPages = toalPages;
            if (isRefresh) {
                isRefresh = false;
                gsearchResultXrv.refreshComplete();
                userList.clear();
            }
            if (isLoadMore) {
                isLoadMore = false;
                gsearchResultXrv.loadMoreComplete();
            }
            userList.addAll(beanList);
            if (userList.size() > 0) {
                showDataResult();
                if (userAdapter == null) {
                    userAdapter = new AttentionOrFansRecyAdapter(GlobalSearchActivity.this, userList);
                    userAdapter.setOnAttentionToFansItemClickListener(new AttentionOrFansRecyAdapter.OnAttentionToFansItemClickListener() {
                        @Override
                        public void onItemClick(int id) {
                            Intent intent = new Intent(GlobalSearchActivity.this, LiveHomePageActivity.class);
                            intent.putExtra("userId", String.valueOf(id));
                            //  intent.putExtra("back","2");
                            startActivity(intent);
                        }
                    });
                } else {
                    userAdapter.refresh(userList);
                }
                gsearchResultXrv.setAdapter(userAdapter);
            } else {
                showEmptResult();
            }
        }


        @Override
        public void onGetMoreItems(int type, String key, int pageNumber, int totalPages, List<SearchItem> items) {
            AppLog.print("onGetMoreItems_____" + items);
            mSearchKey = key;
            mPageNum = pageNumber;
            mTotalPages = totalPages;
            switch (type) {
                case MoreRecyclerAdapter.MODUEL_TYPE_ARTICLE:
                    updateMoreAdapter(articleAdapter, articleItems, items, type);
                    break;
                case MoreRecyclerAdapter.MODUEL_TYPE_PRODUCT:
                    updateMoreAdapter(productAdapter, productItems, items, MoreRecyclerAdapter.MODUEL_TYPE_PRODUCT);
                    break;
                case MoreRecyclerAdapter.MODUEL_TYPE_ROUTE:
                    updateMoreAdapter(routeAdapter, routeItems, items, MoreRecyclerAdapter.MODUEL_TYPE_PRODUCT);
                    break;

            }
        }

        @Override
        public void onGetGSpecialSearch(String key, int pageNum, int toalPages, List<RecommendRowsBean> beanList) {
            mSearchKey = key;
            mPageNum = pageNum;
            mTotalPages = toalPages;
            if (isRefresh) {
                isRefresh = false;
                gsearchResultXrv.refreshComplete();
                specialList.clear();
            }
            if (isLoadMore) {
                isLoadMore = false;
                gsearchResultXrv.loadMoreComplete();
            }
            specialList.addAll(beanList);
            if (specialList.size() > 0) {
                showDataResult();
                if (themeAdapter == null) {
                    themeAdapter = new ThemeAdapter(GlobalSearchActivity.this, specialList);
                } else {
                    themeAdapter.setResh(specialList);
                }
                gsearchResultXrv.setAdapter(themeAdapter);
            } else {
                showEmptResult();
            }
        }

        public void updateMoreAdapter(MoreRecyclerAdapter adapter, List<SearchItem> mItems, List<SearchItem> items, int type) {
            if (isRefresh) {
                isRefresh = false;
                gsearchResultXrv.refreshComplete();
                mItems.clear();
            }
            if (isLoadMore) {
                isLoadMore = false;
                gsearchResultXrv.loadMoreComplete();
            }
            mItems.addAll(items);
            if (mItems.size() > 0) {
                showDataResult();
                if (adapter == null) {
                    adapter = new MoreRecyclerAdapter(GlobalSearchActivity.this, mItems, type);
                    adapter.setOnItemClickListener(itemClickListener);
                } else {
                    adapter.updateItems(mItems);
                }
                gsearchResultXrv.setAdapter(adapter);
            } else {
                showEmptResult();
            }
        }
    }

    private OnItemClickListener itemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClickListener(View view, int position) {
            Object tagObj = view.getTag();
            if (tagObj == null) {
                return;
            }
            if (tagObj instanceof LiveRowsBean) {
                LiveRowsBean liveRowsBean = (LiveRowsBean) tagObj;
                if (liveRowsBean.getEndAt() != null && liveRowsBean.getStartAt() != null) {
                    Intent intent = new Intent(GlobalSearchActivity.this, PlayBackActivity.class);
                    intent.putExtra("id", String.valueOf(liveRowsBean.getId()));
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(GlobalSearchActivity.this, AudienceActivity.class);
                    intent.putExtra("id", String.valueOf(liveRowsBean.getId()));
                    startActivity(intent);
                }
            } else if (tagObj instanceof SearchItem) {
                SearchItem searchItem = (SearchItem) tagObj;
                switch (searchItem.getModeltype()) {
                    case MoreRecyclerAdapter.MODUEL_TYPE_ARTICLE:
                        gotoArticleDetail(searchItem);
                        break;
                    case MoreRecyclerAdapter.MODUEL_TYPE_PRODUCT:
                        gotoProductDetail(searchItem);
                        break;
                    case MoreRecyclerAdapter.MODUEL_TYPE_ROUTE:
                        gotoRouteDetail(searchItem);
                        break;

                }


            }

        }
    };

    public void showEmptResult() {
        searchEmptyTv.setVisibility(View.VISIBLE);
        hintContainer.setVisibility(View.GONE);
        gsearchResultXrv.setVisibility(View.GONE);
    }

    public void showDataResult() {
        searchEmptyTv.setVisibility(View.GONE);
        hintContainer.setVisibility(View.GONE);
        gsearchResultXrv.setVisibility(View.VISIBLE);
    }

    private View.OnClickListener hintClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object tagObj = v.getTag();
            if (tagObj != null) {
                mSearchKey = (String) tagObj;
                gsearchEdit.setText(mSearchKey);
                gsearchResultXrv.setRefreshing(true);
                saveSearchHistory();
            }
        }
    };

    public String getName() {
        return gsearchEdit.getText().toString();
    }

    private void gotoRouteDetail(SearchItem item) {
        Intent intent = new Intent();
        intent.setClass(this, RouteDetailActivity.class);
        intent.putExtra(RouteDetailActivity.DETAILS_ID, item.getId());
        startActivity(intent);
    }

    private void gotoProductDetail(SearchItem item) {
        Intent intent = new Intent();
        intent.setClass(this, ProductDetailsActivity.class);
        SpecialToH5Bean bean = new SpecialToH5Bean();
        bean.setTargetId(item.getId());
        intent.putExtra("productdetails", bean);
        startActivity(intent);
    }

    private void gotoArticleDetail(SearchItem item) {
        Intent intent = new Intent();
        intent.setClass(this, ArticleActivity.class);
        intent.putExtra("targetID", String.valueOf(item.getId()));
        startActivity(intent);
    }

}
