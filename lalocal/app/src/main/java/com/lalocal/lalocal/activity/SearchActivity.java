package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.ArticleItem;
import com.lalocal.lalocal.model.HistoryItem;
import com.lalocal.lalocal.model.ProductItem;
import com.lalocal.lalocal.model.RouteItem;
import com.lalocal.lalocal.model.SearchItem;
import com.lalocal.lalocal.model.SpecialToH5Bean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.view.ClearEditText;
import com.lalocal.lalocal.view.adapter.MoreAdpater;
import com.lalocal.lalocal.view.adapter.SearchHintAdapter;
import com.lalocal.lalocal.view.adapter.SearchResultAapter;
import com.lalocal.lalocal.view.adapter.SearchTagAdapter;
import com.lalocal.lalocal.view.decoration.LinearItemDecoration;
import com.lalocal.lalocal.view.listener.OnItemClickListener;
import com.lalocal.lalocal.view.xlistview.XListView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity implements TextView.OnEditorActionListener, TextWatcher {

    @BindView(R.id.search_key_cet)
    ClearEditText searchKeyCet;
    @BindView(R.id.seach_key_hint)
    TextView seachKeyHint;
    @BindView(R.id.search_key_container)
    FrameLayout searchKeyContainer;
    @BindView(R.id.search_hint_rlv)
    RecyclerView searchHintRlv;
    @BindView(R.id.search_key_cancel)
    TextView searchKeyCancel;
    @BindView(R.id.search_tag_rlv)
    RecyclerView searchTagRlv;
    @BindView(R.id.search_result_rlv)
    RecyclerView searchResultRlv;
    @BindView(R.id.search_result_xlv)
    XListView searchResultMoreXlv;
    ContentLoader loader;
    SearchTagAdapter tagAdapter;
    boolean isResultSearch;
    @BindView(R.id.search_back_img)
    ImageView searchBackImg;
    SearchCallBack searchCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        ButterKnife.bind(this);
        searchKeyCet.setOnEditorActionListener(this);
        searchKeyCet.addTextChangedListener(this);
        initLoader();
    }

    private void initLoader() {
        loader = new ContentLoader(this);
        searchCallBack = new SearchCallBack();
        loader.setCallBack(searchCallBack);
        loader.getSearhHot();

    }


    @OnClick(R.id.search_back_img)
    public void back() {
        searchBackImg.setVisibility(View.INVISIBLE);
        showListView(searchResultRlv);
    }


    @OnClick(R.id.search_key_cancel)
    public void cancel() {
        if (searchBackImg.getVisibility() == View.VISIBLE) {
            searchBackImg.setVisibility(View.INVISIBLE);
            showListView(searchResultRlv);
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        AppLog.print("onBackPressed___");
        if (searchBackImg.getVisibility() == View.VISIBLE) {
            searchBackImg.setVisibility(View.INVISIBLE);
            showListView(searchResultRlv);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        String text = v.getText().toString();
        if (actionId == EditorInfo.IME_ACTION_SEARCH && !TextUtils.isEmpty(text)) {
            //TODO: showListView
//            showListView(searchResultRlv);
//            if (text.contains(" ")){
//               char[] values=text.toCharArray();
//                for (int i=0;i<values.length;i++){
//                       char value=values[i];
//                        if (" ".equals(value)){
//                            text= CommonUtil.removeCharAt(text,i);
//                        }else{
//                            break;
//                        }
//                }
//                searchKeyCet.setText(text);
//            }
            AppLog.print("onEditorAction___actionSearch__text__" + text);
            loader.getSearchResult(text);
            try {
                List<HistoryItem> items = DataSupport.findAll(HistoryItem.class);
                if (items == null) {
                    items = new ArrayList<>();
                }

                for (HistoryItem item : items) {
                    if (text.equals(item.getName())) {
                        DataSupport.deleteAll(HistoryItem.class, "name=?", text);
                        items = DataSupport.findAll(HistoryItem.class);
                    }
                }
                HistoryItem item = new HistoryItem();
                item.setName(text);
                items.add(item);
                Collections.reverse(items);


                if (items.size() > 5) {
                    List<HistoryItem> deltes = items.subList(5, items.size());
                    for (HistoryItem d : deltes) {
                        DataSupport.deleteAll(HistoryItem.class, "name=?", d.getName());
                    }
                    items.removeAll(items.subList(5, items.size()));
                }
                DataSupport.saveAll(items);

            } catch (Exception e) {
            }
            return true;
        }

        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
            if (seachKeyHint.getVisibility() == View.VISIBLE) {
                seachKeyHint.setVisibility(View.INVISIBLE);
            }
            if (!isResultSearch) {
                String text = s.toString().trim();
                if (text.length() == 0) {
                    searchCallBack.onGetSearchTag(null);
                } else {
                    loader.getSearchTag(s.toString());
                }
            } else {
                Editable editable = searchKeyCet.getEditableText();
                int len = 0;
                if (editable != null) {
                    String text = editable.toString();
                    if (!TextUtils.isEmpty(text)) {
                        len = text.length();
                    }
                }
                searchKeyCet.setSelection(len);
                isResultSearch = false;
            }

        } else {
            if (seachKeyHint.getVisibility() != View.VISIBLE) {
                seachKeyHint.setVisibility(View.VISIBLE);
            }
            showListView(searchHintRlv);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    class SearchCallBack extends ICallBack implements OnItemClickListener, XListView.IXListViewListener {
        int morePageNumb;
        int moreToalPages;
        MoreAdpater moreAdpater;
        SearchResultAapter resultAapter;
        SearchHintAdapter hintAdapter;
        boolean isRefresh;
        String title;
        String key;
        List<SearchItem> toalItems = new ArrayList<>();

        @Override
        public void onError(VolleyError volleyError) {
            isRefresh = false;
        }

        @Override
        public void onGetMoreItems(int pageNumber, int totalPages, List<SearchItem> items) {
            searchBackImg.setVisibility(View.VISIBLE);
            morePageNumb = pageNumber;
            moreToalPages = totalPages;
            if (isRefresh) {
                isRefresh = false;
            } else {
                toalItems.clear();
            }
            toalItems.addAll(items);
            if (moreAdpater == null) {
                moreAdpater = new MoreAdpater(SearchActivity.this, toalItems);
                searchResultMoreXlv.setPullLoadEnable(true);
                searchResultMoreXlv.setXListViewListener(this);
                searchResultMoreXlv.setOnItemClickListener(moreItemClickListener);
                searchResultMoreXlv.setAdapter(moreAdpater);
            } else {
                if (pageNumber == 1) {
                    searchResultMoreXlv.setAdapter(moreAdpater);
                }
                moreAdpater.updateItems(toalItems);
            }
            showListView(searchResultMoreXlv);
        }

        @Override
        public void onGetSearchResult(String key, List<ArticleItem> articleItems, int aritcleToalNmb, List<ProductItem> productItems, int productToalNmb, List<RouteItem> routeItems, int routeToalNumb) {
            AppLog.print("onGetSearchResult____size___" + articleItems.size());
            List<SearchItem> items = new ArrayList<>();
            if (articleItems.size() > 0) {
                //标题
                items.add(getModuleTitle("文章", SearchResultAapter.MODE_TYPE_TITLE, null));
                items.addAll(articleItems);
                if (aritcleToalNmb > articleItems.size()) {
                    //更多
                    items.add(getModuleTitle("查看更多相关文章", SearchResultAapter.MODE_TYPE_MORE, key));
                }
            }
            if (routeItems.size() > 0) {
                items.add(getModuleTitle("路线", SearchResultAapter.MODE_TYPE_TITLE, null));
                items.addAll(routeItems);
                if (routeToalNumb > routeItems.size()) {
                    items.add(getModuleTitle("查看更多相关路线", SearchResultAapter.MODE_TYPE_MORE, key));
                }
            }
            if (productItems.size() > 0) {
                items.add(getModuleTitle("商品", SearchResultAapter.MODE_TYPE_TITLE, null));
                items.addAll(productItems);
                if (productToalNmb > productItems.size()) {
                    items.add(getModuleTitle("查看更多相关商品", SearchResultAapter.MODE_TYPE_MORE, key));
                }
            }
            if (resultAapter == null) {
                AppLog.print("onGetSearchResult____items__" + items.size());
                resultAapter = new SearchResultAapter(SearchActivity.this, items);
                resultAapter.setOnItemClickListener(resultItemClickListener);
                searchResultRlv.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                searchResultRlv.addItemDecoration(new LinearItemDecoration(SearchActivity.this));
                searchResultRlv.setAdapter(resultAapter);
            } else {
                resultAapter.updateItems(items);
            }
            showListView(searchResultRlv);
        }


        public SearchItem getModuleTitle(String title, int type, String key) {
            SearchItem modelTitle = new SearchItem();
            if (!TextUtils.isEmpty(key)) {
                modelTitle.setKey(key);
            }
            modelTitle.setTitle(title);
            modelTitle.setModeltype(type);

            return modelTitle;
        }


        @Override
        public void onGetSearchTag(List<String> keys) {
            if (tagAdapter == null) {
                tagAdapter = new SearchTagAdapter(SearchActivity.this, keys);
                LinearLayoutManager lm = new LinearLayoutManager(SearchActivity.this);
                searchTagRlv.setLayoutManager(lm);
                searchTagRlv.addItemDecoration(new LinearItemDecoration(SearchActivity.this));
                tagAdapter.setOnItemClickListener(this);
                searchTagRlv.setAdapter(tagAdapter);
            } else {
                tagAdapter.updateItems(keys);
            }
            showListView(searchTagRlv);
        }

        @Override
        public void onGetSearchHot(List<String> keys) {
            AppLog.print("onGetSearchHot___keys size_" + keys.size());
            List<SparseArray<String>> datas = new ArrayList<>();
            try {
                List<HistoryItem> items = DataSupport.findAll(HistoryItem.class);
                AppLog.print("历史搜索记录—————item—" + items + ", size___" + items.size());
                Collections.reverse(items);
                if (items != null && items.size() > 0) {
                    SparseArray<String> titleSp = new SparseArray<>();
                    titleSp.put(SearchHintAdapter.ITEM_HISTORY_TITLE, "历史记录");
                    datas.add(titleSp);
                    for (HistoryItem item : items) {
                        SparseArray<String> sp = new SparseArray<>();
                        sp.put(SearchHintAdapter.ITEM_HISTORY, item.getName());
                        datas.add(sp);

                    }
                }
            } catch (Exception e) {
            }
            SparseArray<String> hotTile = new SparseArray<>();
            hotTile.put(SearchHintAdapter.ITEM_HOT_TITLE, "热门搜索");
            datas.add(hotTile);
            for (String key : keys) {
                SparseArray<String> sp = new SparseArray<>();
                sp.put(SearchHintAdapter.ITEM_HOT, key);
                datas.add(sp);
            }
            if (hintAdapter == null) {
                hintAdapter = new SearchHintAdapter(SearchActivity.this, datas);
                hintAdapter.setOnItemClickListener(this);
                LinearLayoutManager lm = new LinearLayoutManager(SearchActivity.this);
                lm.setOrientation(LinearLayoutManager.VERTICAL);
                searchHintRlv.addItemDecoration(new LinearItemDecoration(SearchActivity.this));
                searchHintRlv.setLayoutManager(lm);
                searchHintRlv.setHasFixedSize(true);
                searchHintRlv.setAdapter(hintAdapter);
            } else {
                hintAdapter.updateItems(datas);
            }
            showListView(searchHintRlv);

        }

        @Override
        public void onItemClickListener(View view, int position) {
//            showListView(searchResultRlv);
            String text = (String) view.getTag();
            if (!TextUtils.isEmpty(text)) {
                isResultSearch = true;
                searchKeyCet.setText(text);
                loader.getSearchResult(text);
            }
        }

        private AdapterView.OnItemClickListener moreItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchItem item = (SearchItem) view.getTag(R.id.moreSearchItemId);
                AppLog.print("moreItemClickListener item__" + item);
                if (item != null) {
                    switch (item.getModeltype()) {
                        case MoreAdpater.MODUEL_TYPE_ARTICLE:
                            gotoArticleDetail(item);
                            break;
                        case MoreAdpater.MODUEL_TYPE_PRODUCT:
                            gotoProductDetail(item);
                            break;
                        case MoreAdpater.MODUEL_TYPE_ROUTE:
                            gotoRouteDetail(item);
                            break;
                    }
                }
            }
        };
        private OnItemClickListener resultItemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                SearchItem item = (SearchItem) view.getTag();
                if (item != null) {
                    switch (item.getModeltype()) {
                        case SearchResultAapter.MODE_TYPE_ARTICLE:
                            gotoArticleDetail(item);
                            break;
                        case SearchResultAapter.MODE_TYPE_PRODUCT:
                            gotoProductDetail(item);
                            break;
                        case SearchResultAapter.MODE_TYPE_ROUTE:
                            gotoRouteDetail(item);
                            break;
                        case SearchResultAapter.MODE_TYPE_MORE:
                            title = item.getTitle();
                            key = item.getKey();
                            if (title.contains("文章")) {
                                loader.getMoreAritleResult(key, 1, 10);
                            } else if (title.contains("商品")) {
                                loader.getMoreProductResult(key, 1, 10);
                            } else if (title.contains("路线")) {
                                loader.getMoreRouteResult(key, 1, 10);
                            }
                            break;
                    }


                }


            }
        };

        private void gotoRouteDetail(SearchItem item) {
            Intent intent = new Intent();
            intent.setClass(SearchActivity.this, RouteDetailActivity.class);
            intent.putExtra(RouteDetailActivity.DETAILS_ID, item.getId());
            startActivity(intent);
        }

        private void gotoProductDetail(SearchItem item) {
            Intent intent = new Intent();
            intent.setClass(SearchActivity.this, ProductDetailsActivity.class);
            SpecialToH5Bean bean = new SpecialToH5Bean();
            bean.setTargetId(item.getId());
            intent.putExtra("productdetails", bean);
            startActivity(intent);
        }

        private void gotoArticleDetail(SearchItem item) {
            Intent intent = new Intent();
            intent.setClass(SearchActivity.this, ArticleActivity.class);
            intent.putExtra("targetID", String.valueOf(item.getId()));
            startActivity(intent);
        }

        @Override
        public void onRefresh() {
            searchResultMoreXlv.stopRefresh();
        }

        @Override
        public void onLoadMore() {
            if (morePageNumb < moreToalPages) {
                isRefresh = true;
                int pageNumb = morePageNumb + 1;
                if (title.contains("文章")) {
                    loader.getMoreAritleResult(key, pageNumb, 10);
                } else if (title.contains("商品")) {
                    loader.getMoreProductResult(key, pageNumb, 10);
                } else if (title.contains("路线")) {
                    loader.getMoreRouteResult(key, pageNumb, 10);
                }
            } else {
                isRefresh = false;
                Toast.makeText(SearchActivity.this, "没有更多了", Toast.LENGTH_LONG).show();
            }
            searchResultMoreXlv.stopLoadMore();
        }
    }

    public void showListView(View view) {
        if (view == null) {
            return;
        }
        int id = view.getId();
        switch (id) {
            case R.id.search_hint_rlv:
                searchTagRlv.setVisibility(View.INVISIBLE);
                searchResultRlv.setVisibility(View.INVISIBLE);
                searchResultMoreXlv.setVisibility(View.INVISIBLE);
                break;
            case R.id.search_tag_rlv:
                searchHintRlv.setVisibility(View.INVISIBLE);
                searchResultRlv.setVisibility(View.INVISIBLE);
                searchResultMoreXlv.setVisibility(View.INVISIBLE);
                break;
            case R.id.search_result_rlv:
                searchHintRlv.setVisibility(View.INVISIBLE);
                searchTagRlv.setVisibility(View.INVISIBLE);
                searchResultMoreXlv.setVisibility(View.INVISIBLE);
                break;
            case R.id.search_result_xlv:
                searchHintRlv.setVisibility(View.INVISIBLE);
                searchTagRlv.setVisibility(View.INVISIBLE);
                searchResultRlv.setVisibility(View.INVISIBLE);
                break;
        }
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
    }
}
