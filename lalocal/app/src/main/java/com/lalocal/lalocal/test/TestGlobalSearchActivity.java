package com.lalocal.lalocal.test;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.ArticleActivity;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.activity.ProductDetailsActivity;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.help.PageType;
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

public class TestGlobalSearchActivity extends BaseActivity implements XRecyclerView.LoadingListener, TextView.OnEditorActionListener, View.OnTouchListener {
    private static final int ID_SEARCH_HISTORY = 0x11;
    private static final int ID_SEARCH_HOT = 0x12;
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
    @BindView(R.id.gsearch_tab_special)
    RelativeLayout gsearchTabSpecial;
    @BindView(R.id.gsearch_tab_goods)
    RelativeLayout gsearchTabGoods;
    @BindView(R.id.gsearch_tab_container)
    LinearLayout gsearchTabCotainer;
    @BindView(R.id.gsearch_hint_cotainer)
    LinearLayout hintContainer;
    @BindView(R.id.gsearch_result_vp)
    ViewPager mViewPager;
    String mSearchKey, lastKey;
    boolean isRefresh, isLoadMore, isInit;
    LiveSearchAdapter liveAdapter;
    AttentionOrFansRecyAdapter userAdapter;
    ThemeAdapter themeAdapter;
    MoreRecyclerAdapter articleAdapter;
    MoreRecyclerAdapter productAdapter;
    int pageType;
    XRecyclerView gsearchResultXrv;
    TextView emptv;
    List<View> mViews;
    int liveIndex, userIndex, specialIndex, articleIndex, goodsIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_global_search);
        ButterKnife.bind(this);
        pageType = getPageType();
        setLoaderCallBack(new GloabSearchCallBack());
        gsearchEdit.setOnEditorActionListener(this);
        mContentloader.getSearhHot();
        initViewPager(getRecyclerAdapters());
        AppLog.print("index@\nlive:" + liveIndex + "\nuser:" + userIndex + "\nspecial:" + specialIndex + "\narticle:" + articleIndex + "\ngoods:" + goodsIndex);
    }

    public List<RecyclerView.Adapter> getRecyclerAdapters() {
        liveAdapter = new LiveSearchAdapter(null);
        userAdapter = new AttentionOrFansRecyAdapter(this, null);
        themeAdapter = new ThemeAdapter(this, null);
        articleAdapter = new MoreRecyclerAdapter(this, null, MoreRecyclerAdapter.MODUEL_TYPE_ARTICLE);
        productAdapter = new MoreRecyclerAdapter(this, null, MoreRecyclerAdapter.MODUEL_TYPE_PRODUCT);
        liveAdapter.setOnItemClickListener(itemClickListener);
        userAdapter.setOnAttentionToFansItemClickListener(userItemClickListener);
        articleAdapter.setOnItemClickListener(itemClickListener);
        productAdapter.setOnItemClickListener(itemClickListener);
        List<RecyclerView.Adapter> adapters = new ArrayList<>();
        if (pageType == PageType.PAGE_DESTIATION) {
            gsearchTabLive.setVisibility(View.GONE);
            gsearchTabUser.setVisibility(View.GONE);
            setSelecteTab(gsearchTabSpecial);
        } else {
            setSelecteTab(gsearchTabLive);
            adapters.add(liveAdapter);
            adapters.add(userAdapter);
        }
        adapters.add(themeAdapter);
        adapters.add(articleAdapter);
        adapters.add(productAdapter);
        return adapters;
    }

    private void initViewPager(List<RecyclerView.Adapter> adapterList) {
        LayoutInflater inflater = LayoutInflater.from(this);
        mViews = new ArrayList<>();
        int len = adapterList.size();
        for (int i = 0; i < len; i++) {
            RecyclerView.Adapter adapter = adapterList.get(i);
            if (adapter == liveAdapter) {
                liveIndex = i;
            } else if (adapter == userAdapter) {
                userIndex = i;
            } else if (adapter == articleAdapter) {
                articleIndex = i;
            } else if (adapter == themeAdapter) {
                specialIndex = i;
            } else if (adapter == productAdapter) {
                goodsIndex = i;
            }
            View view = inflater.inflate(R.layout.gsearch_result_item, null);
            XRecyclerView mXRecyclerView = (XRecyclerView) view.findViewById(R.id.gsearch_reslut_item_xrv);
            mXRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mXRecyclerView.setPullRefreshEnabled(true);
            mXRecyclerView.setLoadingMoreEnabled(true);
            mXRecyclerView.setLoadingListener(this);
            mXRecyclerView.setOnTouchListener(this);
            mXRecyclerView.setAdapter(adapter);
            mViews.add(view);
        }
        mViewPager.setAdapter(new SearchPageAdapter(mViews));
        mViewPager.addOnPageChangeListener(pageChangeListener);
        mViewPager.setCurrentItem(0);
        setXRecyclerView(0);
    }


    private void setXRecyclerView(int position) {
        ViewGroup container = (ViewGroup) mViews.get(position);
        gsearchResultXrv = (XRecyclerView) container.findViewById(R.id.gsearch_reslut_item_xrv);
        emptv = (TextView) container.findViewById(R.id.gsearch_reslut_item_empty);
    }

    class SearchPageAdapter extends PagerAdapter {
        List<View> mViews;

        public SearchPageAdapter(List<View> views) {
            mViews = views;
        }

//        @Override
//        public void setPrimaryItem(ViewGroup container, int position, Object object) {
//            AppLog.print("setPrimaryItem_____");
//            gsearchResultXrv = (XRecyclerView) container.findViewById(R.id.gsearch_reslut_item_xrv);
//        }

        @Override
        public int getCount() {
            return mViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mViews.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViews.get(position));
        }
    }


    private void setCurrentView(View view, int index) {
        if (mViewPager.getVisibility() != View.VISIBLE) {
            AppLog.print("viewpage____visible__");
            setSelecteTab(view);
        } else {
            AppLog.print("viewpage____invisible__");
            mViewPager.setCurrentItem(index);
        }
    }


    private void setSelecteTab(View view) {
        if (view.isSelected()) {
            return;
        }
        for (int i = 0; i < gsearchTabCotainer.getChildCount(); i++) {
            RelativeLayout layout = (RelativeLayout) gsearchTabCotainer.getChildAt(i);
            View view1 = layout.getChildAt(0);
            View view2 = layout.getChildAt(1);
            if (view == layout) {
                layout.setSelected(true);
                view1.setActivated(true);
                view2.setActivated(true);
            } else {
                layout.setSelected(false);
                view1.setActivated(false);
                view2.setActivated(false);
            }
        }


    }

    @Override
    public void onRefresh() {
        if (!TextUtils.isEmpty(mSearchKey)) {
            isRefresh = true;
            isInit = false;
            if (gsearchTabLive.isSelected()) {
                mContentloader.getGlobalSearchPlayBack(mSearchKey, 1);
                return;
            }
            if (gsearchTabUser.isSelected()) {
                mContentloader.getGloablSearchUser(mSearchKey, 1);
                return;
            }
            if (gsearchTabTravelnote.isSelected()) {
                mContentloader.getMoreAritleResult(mSearchKey, 1, 10);
                return;
            }
            if (gsearchTabSpecial.isSelected()) {
                mContentloader.getGlobalSearchSpecial(mSearchKey, 1);
                return;
            }
            if (gsearchTabGoods.isSelected()) {
                mContentloader.getMoreProductResult(mSearchKey, 1, 10);
            }
        } else {
            gsearchResultXrv.refreshComplete();
        }
    }

    @Override
    public void onLoadMore() {
        AppLog.print("loadMore___");
        isLoadMore = true;
        isInit = false;
        if (gsearchTabLive.isSelected()) {
            AppLog.print("loadMore_live_isSel_");
            loadMore(gsearchTabLive);
            return;
        }
        if (gsearchTabUser.isSelected()) {
            AppLog.print("loadMore_user_isSel_");
            loadMore(gsearchTabUser);
            return;
        }
        if (gsearchTabTravelnote.isSelected()) {
            AppLog.print("loadMore_note_isSel_");
            loadMore(gsearchTabTravelnote);
            return;
        }
        if (gsearchTabSpecial.isSelected()) {
            AppLog.print("loadMore_special_isSel_");
            loadMore(gsearchTabSpecial);
            return;
        }
        if (gsearchTabGoods.isSelected()) {
            AppLog.print("loadMore_goods_isSel_");
            loadMore(gsearchTabGoods);
        }
    }

    private void loadMore(View selTabView) {
        int toalPages;
        int pageNum;
        toalPages = (int) selTabView.getTag(R.id.toalPages);
        pageNum = (int) selTabView.getTag(R.id.pageNum);
        AppLog.print("pageNum__" + pageNum + ", toalPages__" + toalPages);
        if (pageNum < toalPages) {
            pageNum++;
            selTabView.setTag(R.id.pageNum, pageNum);
            if (selTabView == gsearchTabLive) {
                mContentloader.getGlobalSearchPlayBack(mSearchKey, pageNum);
            } else if (selTabView == gsearchTabUser) {
                mContentloader.getGloablSearchUser(mSearchKey, pageNum);
            } else if (selTabView == gsearchTabTravelnote) {
                mContentloader.getMoreAritleResult(mSearchKey, pageNum, 10);
            } else if (selTabView == gsearchTabSpecial) {
                mContentloader.getGlobalSearchSpecial(mSearchKey, pageNum);
            } else if (selTabView == gsearchTabGoods) {
                mContentloader.getMoreProductResult(mSearchKey, pageNum, 10);
            }
        } else {
            gsearchResultXrv.setNoMore(true);
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String text = v.getText().toString();
            if (TextUtils.isEmpty(text)) {
                CommonUtil.showPromptDialog(this, "搜索词不可为空", null);
                return false;
            }
            if (text.equals(lastKey)) {
                return true;
            }
            mSearchKey = text;
            MobHelper.sendEevent(this, MobEvent.SEARCH_TAG_LIST);
            searchReq();
            saveSearchHistory();
            return true;
        }
        return false;
    }

    private void searchReq() {
        hintContainer.setVisibility(View.GONE);
        if (mViewPager.getVisibility() != View.VISIBLE) {
            mViewPager.setVisibility(View.VISIBLE);
            if (gsearchTabLive.isSelected() && mViewPager.getCurrentItem() != liveIndex) {
                mViewPager.setCurrentItem(liveIndex);
            }
            if (gsearchTabUser.isSelected() && mViewPager.getCurrentItem() != userIndex) {
                mViewPager.setCurrentItem(userIndex);
            }
            if (gsearchTabSpecial.isSelected() && mViewPager.getCurrentItem() != specialIndex) {
                mViewPager.setCurrentItem(specialIndex);
            }
            if (gsearchTabTravelnote.isSelected() && mViewPager.getCurrentItem() != articleIndex) {
                mViewPager.setCurrentItem(articleIndex);
            }
            if (gsearchTabGoods.isSelected() && mViewPager.getCurrentItem() != goodsIndex) {
                mViewPager.setCurrentItem(goodsIndex);
            }
        }
        isInit = true;
        lastKey = mSearchKey;
        if (pageType != PageType.PAGE_DESTIATION) {
            mContentloader.getGlobalSearchLive(mSearchKey);
            mContentloader.getGloablSearchUser(mSearchKey, 1);
        }
        mContentloader.getMoreAritleResult(mSearchKey, 1, 10);
        mContentloader.getGlobalSearchSpecial(mSearchKey, 1);
        mContentloader.getMoreProductResult(mSearchKey, 1, 10);
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            KeyboardUtil.hidenSoftKey(gsearchEdit);
        }
        return false;
    }

    class GloabSearchCallBack extends ICallBack {
        List<LiveRowsBean> rowsBeanList = new ArrayList<>();
        List<LiveRowsBean> playBackList = new ArrayList<>();
        List<LiveRowsBean> liveList = new ArrayList<>();
        List<LiveFansOrAttentionRowsBean> userList = new ArrayList<>();
        List<RecommendRowsBean> specialList = new ArrayList<>();
        List<SearchItem> articleItems = new ArrayList<>();
        List<SearchItem> productItems = new ArrayList<>();

        @Override
        public void onGetGLiveSearch(List<LiveRowsBean> rowList, String name) {
            mSearchKey = name;
            if (isInit) {
                rowsBeanList.clear();
            }
            if (liveList.size() > 0) {
                liveList.clear();
            }
            liveList.addAll(rowList);
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
                    TextView historyTv = new TextView(TestGlobalSearchActivity.this);
                    historyTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, res.getDimension(R.dimen.text_size_16_sp));
                    historyTv.setTextColor(res.getColor(R.color.color_50191000));
                    HistoryItem item = historyItems.get(i);
                    historyTv.setText(Html.fromHtml("<u>" + item.getName() + "</u>"));
                    historyTv.setTag(R.id.search_id, ID_SEARCH_HISTORY);
                    historyTv.setTag(item.getName());
                    historyTv.setOnClickListener(hintClickListener);
                    hintContainer.addView(historyTv, hParams);
                }
                TextView hotTitleTv = new TextView(TestGlobalSearchActivity.this);
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
                    TextView keyTv = new TextView(TestGlobalSearchActivity.this);
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
                                LinearLayout multiTextCotainer = new LinearLayout(TestGlobalSearchActivity.this);
                                TextView key2Tv = new TextView(TestGlobalSearchActivity.this);
                                String key2 = hotKeys.get(nextPos);
                                key2Tv.setTag(key2);
                                key2Tv.setTag(R.id.search_id, ID_SEARCH_HOT);
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
                    keyTv.setTag(R.id.search_id, ID_SEARCH_HOT);
                    hintContainer.addView(keyTv, keyParams);
                }


            } else {
                TextView titleTv = new TextView(TestGlobalSearchActivity.this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.topMargin = (int) res.getDimension(R.dimen.dimen_size_50_dp);
                params.bottomMargin = (int) res.getDimension(R.dimen.dimen_size_20_dp);
                titleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, res.getDimension(R.dimen.text_size_24_sp));
                titleTv.setTextColor(res.getColor(R.color.color_191000));
                titleTv.setText("你可以这样搜");
                hintContainer.addView(titleTv, params);
                for (String key : hotKeys) {
                    TextView keyText = new TextView(TestGlobalSearchActivity.this);
                    keyText.setTextSize(TypedValue.COMPLEX_UNIT_PX, res.getDimension(R.dimen.text_size_16_sp));
                    keyText.setTextColor(res.getColor(R.color.color_50191000));
                    keyText.setText(Html.fromHtml("<u>" + key + "</u>"));
                    LinearLayout.LayoutParams keyParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    keyParams.bottomMargin = (int) res.getDimension(R.dimen.dimen_size_10_dp);
                    keyText.setTag(key);
                    keyText.setTag(R.id.search_id, ID_SEARCH_HOT);
                    keyText.setOnClickListener(hintClickListener);
                    hintContainer.addView(keyText, keyParams);
                }
            }
        }

        @Override
        public void onGetGPlayBackSearch(int pageNum, int toalPages, List<LiveRowsBean> rowList) {
            updateItems(gsearchTabLive, playBackList, rowList, pageNum, toalPages, null);
            if (rowsBeanList.size() > 0) {
                rowsBeanList.clear();
            }
            rowsBeanList.addAll(liveList);
            rowsBeanList.addAll(playBackList);
            AppLog.print("tab@gsearchTabLive___isSel__" + gsearchTabLive.isSelected());
            if (rowsBeanList.size() > 0) {
                liveAdapter.updatItems(rowsBeanList);
                showDataResult(gsearchTabLive);
            } else {
                showEmptResult(gsearchTabLive);
            }
        }


        @Override
        public void onGetGUserSearch(String key, int pageNum, int toalPages, List<LiveFansOrAttentionRowsBean> beanList) {
            updateItems(gsearchTabUser, userList, beanList, pageNum, toalPages, key);
            AppLog.print("tab@gsearchTabUser___isSel__" + gsearchTabUser.isSelected());
            if (userList.size() > 0) {
                userAdapter.refresh(userList);
                showDataResult(gsearchTabUser);
            } else {
                showEmptResult(gsearchTabUser);
            }
        }


        @Override
        public void onGetMoreItems(int type, String key, int pageNumber, int totalPages, List<SearchItem> items) {
            switch (type) {
                case MoreRecyclerAdapter.MODUEL_TYPE_ARTICLE:
                    updateItems(gsearchTabTravelnote, articleItems, items, pageNumber, totalPages, key);
                    if (articleItems.size() > 0) {
                        articleAdapter.updateItems(articleItems);
                        showDataResult(gsearchTabTravelnote);
                    } else {
                        showEmptResult(gsearchTabTravelnote);
                    }
                    break;
                case MoreRecyclerAdapter.MODUEL_TYPE_PRODUCT:
                    updateItems(gsearchTabGoods, productItems, items, pageNumber, totalPages, key);
                    if (productItems.size() > 0) {
                        productAdapter.updateItems(productItems);
                        showDataResult(gsearchTabGoods);
                    } else {
                        showEmptResult(gsearchTabGoods);
                    }
                    break;
            }
        }

        @Override
        public void onGetGSpecialSearch(String key, int pageNum, int toalPages, List<
                RecommendRowsBean> beanList) {
            updateItems(gsearchTabSpecial, specialList, beanList, pageNum, toalPages, key);
            if (specialList.size() > 0) {
                themeAdapter.setRefresh(specialList);
                showDataResult(gsearchTabSpecial);
            } else {
                showEmptResult(gsearchTabSpecial);
            }
        }

        public <T> void updateItems(View selTabView, List<T> toalItems, List<T> items, int pageNumber, int totalPages, String key) {
            if (!TextUtils.isEmpty(key)) {
                mSearchKey = key;
            }
            selTabView.setTag(R.id.pageNum, pageNumber);
            selTabView.setTag(R.id.toalPages, totalPages);
            if (isRefresh) {
                isRefresh = false;
                gsearchResultXrv.refreshComplete();
                toalItems.clear();
            }
            if (isLoadMore) {
                isLoadMore = false;
                gsearchResultXrv.loadMoreComplete();
            }
            if (isInit) {
                toalItems.clear();
            }
            toalItems.addAll(items);
        }

    }


    public void showEmptResult(View selView) {
//        ViewGroup container = (ViewGroup) mViews.get(position);
//        TextView emptv = (TextView) container.findViewById(R.id.gsearch_reslut_item_empty);
        if (selView.isSelected()) {
            gsearchResultXrv.setVisibility(View.INVISIBLE);
            emptv.setVisibility(View.VISIBLE);
        }
    }

    public void showDataResult(View selView) {
//        ViewGroup container = (ViewGroup) mViews.get(position);
//        TextView emptv = (TextView) container.findViewById(R.id.gsearch_reslut_item_empty);
        if (selView.isSelected()) {
            emptv.setVisibility(View.INVISIBLE);
            gsearchResultXrv.setVisibility(View.VISIBLE);
        }
    }


    public String getName() {
        return gsearchEdit.getText().toString();
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

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            AppLog.print("onPageScrolled___@pos:" + position);

        }

        @Override
        public void onPageSelected(int position) {
            AppLog.print("onPageSelected___@pos:" + position);

            if (position == liveIndex) {
                setSelecteTab(gsearchTabLive);
            } else if (position == userIndex) {
                setSelecteTab(gsearchTabUser);
            } else if (position == specialIndex) {
                setSelecteTab(gsearchTabSpecial);
            } else if (position == articleIndex) {
                setSelecteTab(gsearchTabTravelnote);
            } else if (position == goodsIndex) {
                setSelecteTab(gsearchTabGoods);
            }
            setXRecyclerView(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private View.OnClickListener hintClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object tagObj = v.getTag();
            if (tagObj != null) {
                Object idObj = v.getTag(R.id.search_id);
                if (idObj != null) {
                    int searchId = (int) idObj;
                    if (searchId == ID_SEARCH_HISTORY) {
                        MobHelper.sendEevent(TestGlobalSearchActivity.this, MobEvent.SEARCH_HISTORY);
                    } else if (searchId == ID_SEARCH_HOT) {
                        MobHelper.sendEevent(TestGlobalSearchActivity.this, MobEvent.SEARCH_HOT);
                    }
                }
                mSearchKey = (String) tagObj;
                gsearchEdit.setText(mSearchKey);
                searchReq();
            }
        }
    };

    private AttentionOrFansRecyAdapter.OnAttentionToFansItemClickListener userItemClickListener = new AttentionOrFansRecyAdapter.OnAttentionToFansItemClickListener() {
        @Override
        public void onItemClick(int id) {
            Intent intent = new Intent(TestGlobalSearchActivity.this, LiveHomePageActivity.class);
            intent.putExtra("userId", String.valueOf(id));
            //  intent.putExtra("back","2");
            startActivity(intent);
        }
    };

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
                    Intent intent = new Intent(TestGlobalSearchActivity.this, PlayBackActivity.class);
                    intent.putExtra("id", String.valueOf(liveRowsBean.getId()));
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(TestGlobalSearchActivity.this, AudienceActivity.class);
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

                }


            }

        }
    };

    @OnClick({R.id.gsearch_cancel_tv, R.id.gsearch_tab_live, R.id.gsearch_tab_user, R.id.gsearch_tab_travelnote, R.id.gsearch_tab_special, R.id.gsearch_tab_goods})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gsearch_cancel_tv:
                MobHelper.sendEevent(this, MobEvent.SEARCH_CANCEL);
                finish();
                break;
            case R.id.gsearch_tab_live:
                setCurrentView(view, liveIndex);
                break;
            case R.id.gsearch_tab_user:
                setCurrentView(view, userIndex);
                break;
            case R.id.gsearch_tab_travelnote:
                setCurrentView(view, articleIndex);
                break;
            case R.id.gsearch_tab_special:
                setCurrentView(view, specialIndex);
                break;
            case R.id.gsearch_tab_goods:
                setCurrentView(view, goodsIndex);
                break;
        }
    }


}
