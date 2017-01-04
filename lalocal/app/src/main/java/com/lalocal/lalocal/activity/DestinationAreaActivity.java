package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HeaderViewListAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.help.PageType;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.me.LLoginActivity;
import com.lalocal.lalocal.model.ProductItem;
import com.lalocal.lalocal.model.SearchItem;
import com.lalocal.lalocal.model.SiftModle;
import com.lalocal.lalocal.model.SpecialToH5Bean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.view.CustomTitleView;
import com.lalocal.lalocal.view.SiftPoupWindow;
import com.lalocal.lalocal.view.adapter.AreaDetailAdapter;
import com.lalocal.lalocal.view.listener.OnItemClickListener;
import com.lalocal.lalocal.view.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DestinationAreaActivity extends BaseActivity {
    public static final String AREA_ID = "area_id";
    public static final String AREA_NAME = "area_name";
    public static final String YITU8_URL = "yitu8_url";

    @BindView(R.id.destion_search_btn)
    Button searchBtn;
    @BindView(R.id.destination_area_title)
    CustomTitleView destinationAreaTitle;
    @BindView(R.id.des_areanav_menu_pickup)
    TextView desAreanavMenuPickup;
    @BindView(R.id.des_areanav_menu_hot)
    TextView desAreanavMenuHot;
    @BindView(R.id.des_areanav_menu_strategy)
    TextView desAreanavMenuStrategy;
    @BindView(R.id.des_areanav_menu_packagetour)
    TextView desAreanavMenuPackagetour;
    @BindView(R.id.des_areanav_menu_freewarker)
    TextView desAreanavMenuFreewarker;
    @BindView(R.id.des_areanav_menu_lacoalplay)
    TextView desAreanavMenuLacoalplay;
    @BindView(R.id.des_area_menunav_container)
    LinearLayout desAreaMenunavContainer;
    @BindView(R.id.des_area_items_xlv)
    XListView desAreaItemsXlv;
    @BindView(R.id.page_base_loading)
    View loadingPage;
    @BindView(R.id.destion_area_container)
    RelativeLayout destionAreaContainer;
    int areaId;
    String areaTile = "%1$s地区";
    ContentLoader loader;
    boolean isHotLoading;
    List<SearchItem> toalItems = new ArrayList<>();
    List<SearchItem> hotItems = new ArrayList<>();
    List<SearchItem> productItems = new ArrayList<>();
    List<SearchItem> routeItems = new ArrayList<>();
    AreaDetailAdapter hotAdapter, strategyAdpater, packageAdpater, freeAdpater, localAdapter;
    SiftPoupWindow poupWindow;
    @BindView(R.id.destination_area_emptv)
    TextView destinationAreaEmpView;
    String yiut8Url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destination_area_layout);
        ButterKnife.bind(this);
        String areaName = getIntent().getStringExtra(AREA_NAME);
        yiut8Url = getIntent().getStringExtra(YITU8_URL);
        destinationAreaTitle.setTitle(String.format(areaTile, areaName));
        desAreaItemsXlv.setPullRefreshEnable(true);
        desAreaItemsXlv.setOnItemClickListener(areaDetailClickListener);
        setSeletedMenu(desAreanavMenuHot);
        intLoader();
    }

    private void intLoader() {
        loader = new ContentLoader(this);
        loader.setCallBack(new AreaCallBack());
        areaId = getIntent().getIntExtra(AREA_ID, -1);
        isHotLoading = true;
        //分类标签-热门
        loader.getHotProducts(areaId);
        loader.getHotRoutes(areaId);
        //分类标签-攻略
        loader.getDesAreaRoutes(10, 1, areaId, 2);
        //分类标签-跟团游
        loader.getAreaProducts(10, 1, areaId, 0, -1);
        //分类标签-自由行
        loader.getAreaProducts(10, 1, areaId, 1, -1);
        //分类标签-易途8
        loader.getYiTu8Citys(areaId);
    }


    public void setSeletedMenu(TextView seletedMenu) {
        if (seletedMenu.isSelected()) {
            return;
        }
        if (seletedMenu == desAreanavMenuLacoalplay) {
            desAreanavMenuLacoalplay.setSelected(true);
            Drawable drawable = getResources().getDrawable(R.drawable.des_area_sift_ic_sel);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            desAreanavMenuLacoalplay.setCompoundDrawables(null, drawable, null, null);
        } else {
            desAreanavMenuLacoalplay.setSelected(false);
            Drawable drawable = getResources().getDrawable(R.drawable.des_area_combinedshape);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            desAreanavMenuLacoalplay.setCompoundDrawables(null, drawable, null, null);
        }

        for (int i = 0; i < desAreaMenunavContainer.getChildCount(); i++) {
            TextView view = (TextView) desAreaMenunavContainer.getChildAt(i);
            if (view == seletedMenu && !view.isSelected()) {
                view.setSelected(true);
                Drawable drawable = getResources().getDrawable(R.drawable.des_area_tagselect_ic);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                view.setCompoundDrawables(drawable, null, null, null);
            } else {
                view.setSelected(false);
                view.setCompoundDrawables(null, null, null, null);
            }


        }

    }


    @Override
    protected void onDestroy() {
        if (poupWindow != null && poupWindow.isShowing()) {
            poupWindow.dismiss();
        }
        super.onDestroy();
    }


    class AreaCallBack extends ICallBack implements XListView.IXListViewListener {


        int pageNumber;
        int toalPages;
        boolean isLoadMore;
        //该地区下是否有易途8接送机服务
        @Override
        public void onGetYitu8City(boolean hasCity) {
            if (hasCity) {
                desAreanavMenuPickup.setVisibility(View.VISIBLE);
            } else {
                desAreanavMenuPickup.setVisibility(View.GONE);
            }

        }
        //热门商品、路线
        @Override
        public void onGetHotItems(List<SearchItem> items, int type) {
            if (!desAreanavMenuHot.isSelected()) {
                return;
            }
            hidenLoadingView();
            AppLog.print("onGetHotItems  item size___" + items.size());
            if (type == AreaDetailAdapter.MODULE_TYPE_PRODUCT) {
                if (items.size() > 0) {
                    SearchItem productTile = new SearchItem();
                    productTile.setTitle("热门商品");
                    productItems.add(productTile);
                    productItems.addAll(items);
                }
            } else if (type == AreaDetailAdapter.MODULE_TYPE_ROUTE) {
                if (items.size() > 0) {
                    SearchItem routeTile = new SearchItem();
                    routeTile.setTitle("热门攻略");
                    routeItems.add(routeTile);
                    routeItems.addAll(items);
                }
            }
            if (isHotLoading) {
                isHotLoading = false;
                return;
            }
            if (hotItems.size() > 0) {
                hotItems.clear();
            }
            AppLog.print("routeItems size__" + routeItems.size() + ",, productItems__" + productItems.size());


            if (productItems.size() > 0) {
                hotItems.addAll(productItems);
            }
            if (routeItems.size() > 0) {
                hotItems.addAll(routeItems);
            }
            if (hotItems.size() > 0) {
                hidenEmptView();
                if (desAreanavMenuHot.getVisibility() != View.VISIBLE) {
                    desAreanavMenuHot.setVisibility(View.VISIBLE);
                }
                AppLog.print("get hot toalItems ___size___" + hotItems.size());
                desAreaItemsXlv.setXListViewListener(this);
                desAreaItemsXlv.setPullLoadEnable(false);
                desAreaItemsXlv.closeLoadMore();
                if (hotAdapter == null) {
                    hotAdapter = new AreaDetailAdapter(DestinationAreaActivity.this, hotItems);
                    hotAdapter.setPageType(AreaDetailAdapter.PAGE_HOT);
                    desAreaItemsXlv.setAdapter(hotAdapter);
                } else {
                    hotAdapter.updateItems(hotItems);
                }
            } else {
                showEmptView();
            }
        }


        @Override
        public void onError(VolleyError error) {
            hidenLoadingView();
            showEmptView();
            isLoadMore = false;
            isHotLoading = false;
        }

        @Override
        public void onResponseFailed(int code, String message) {
            showEmptView();
        }

        @Override
        public void onGetAreaItems(int pageNumber, int totalPages, List<SearchItem> items, int type, int collectionId) {
            hidenLoadingView();
            AppLog.print("getareaItems___type___" + type + ", itemsize___" + items.size());
            this.pageNumber = pageNumber;
            this.toalPages = totalPages;
            if (isLoadMore) {
                isLoadMore = false;
            } else {
                toalItems.clear();
            }
            toalItems.addAll(items);
            switch (type) {
                case 0://跟团游
                    if (toalItems.size() > 0) {
                        if (desAreanavMenuPackagetour.getVisibility() != View.VISIBLE) {
                            desAreanavMenuPackagetour.setVisibility(View.VISIBLE);
                        } else {
                            if (desAreanavMenuPackagetour.isSelected()) {
                                hidenEmptView();
                                setAreaItemXlv();
                                if (packageAdpater == null) {
                                    packageAdpater = new AreaDetailAdapter(DestinationAreaActivity.this, toalItems);
                                    setItemType(packageAdpater, type);
                                    desAreaItemsXlv.setAdapter(packageAdpater);
                                } else {
                                    setItemType(packageAdpater, type);
                                    if (pageNumber <= 1) {
                                        desAreaItemsXlv.setAdapter(packageAdpater);
                                    }
                                    packageAdpater.updateItems(toalItems);
                                }
                            }
                        }
                    }
                    break;
                case 1://自由行
                    if (toalItems.size() > 0) {
                        if (desAreanavMenuFreewarker.getVisibility() != View.VISIBLE) {
                            desAreanavMenuFreewarker.setVisibility(View.VISIBLE);
                        } else {
                            if (desAreanavMenuFreewarker.isSelected()) {
                                hidenEmptView();
                                setAreaItemXlv();
                                if (freeAdpater == null) {
                                    freeAdpater = new AreaDetailAdapter(DestinationAreaActivity.this, toalItems);
                                    setItemType(freeAdpater, type);
                                    desAreaItemsXlv.setAdapter(freeAdpater);
                                } else {
                                    setItemType(freeAdpater, type);
//                                updateListView(freeAdpater,toalItems);
                                    if (pageNumber <= 1) {
                                        desAreaItemsXlv.setAdapter(freeAdpater);
                                    }
                                    freeAdpater.updateItems(toalItems);
                                }
                            }
                        }
                    }
                    break;
                case 2://分类标签-攻略
                    if (toalItems.size() > 0) {
                        if (desAreanavMenuStrategy.getVisibility() != View.VISIBLE) {
                            desAreanavMenuStrategy.setVisibility(View.VISIBLE);
                        } else {
                            if (desAreanavMenuStrategy.isSelected()) {
                                hidenEmptView();
                                setAreaItemXlv();
                                if (strategyAdpater == null) {
                                    strategyAdpater = new AreaDetailAdapter(DestinationAreaActivity.this, toalItems);
                                    setItemType(strategyAdpater, type);
                                    desAreaItemsXlv.setAdapter(strategyAdpater);
                                } else {
                                    setItemType(strategyAdpater, type);
//                                updateListView(strategyAdpater,toalItems);
                                    if (pageNumber <= 1) {
                                        desAreaItemsXlv.setAdapter(strategyAdpater);
                                    }
                                    strategyAdpater.updateItems(toalItems);
                                }
                            }
                        }
                    }
                    break;
                case -1://本地玩乐
                    if (desAreanavMenuLacoalplay.isSelected()) {
                        if (toalItems.size() > 0) {
                            hidenEmptView();
                            setAreaItemXlv();
                            if (localAdapter == null) {
                                localAdapter = new AreaDetailAdapter(DestinationAreaActivity.this, toalItems);
                                setItemType(localAdapter, type);
                                desAreaItemsXlv.setAdapter(localAdapter);
                            } else {
                                setItemType(localAdapter, type);
                                if (pageNumber <= 1) {
                                    desAreaItemsXlv.setAdapter(localAdapter);
                                }
                                localAdapter.updateItems(toalItems);
                            }
                            localAdapter.setCollectionId(collectionId);
                        } else {
                            showEmptView();
                        }
                    }
                    break;
            }


        }

        public void updateListView(AreaDetailAdapter areaAdapter, List<SearchItem> items) {
            ListAdapter adapter = ((HeaderViewListAdapter) desAreaItemsXlv.getAdapter()).getWrappedAdapter();
            AppLog.print("adpater___" + adapter);
            AppLog.print("areaAdapter____" + areaAdapter);
            AppLog.print("____=====___" + (areaAdapter == adapter));
            areaAdapter.updateItems(items);
            if (areaAdapter != adapter) {
                desAreaItemsXlv.setAdapter(areaAdapter);
            }

        }

        private void setAreaItemXlv() {
            desAreaItemsXlv.setXListViewListener(this);
            desAreaItemsXlv.setPullLoadEnable(true);
            desAreaItemsXlv.openLoadMore();
        }


        private void setItemType(AreaDetailAdapter adapter, int type) {
            if (type == 0) {
                adapter.setPageType(AreaDetailAdapter.PAGE_PACKAGETOUR);
            } else if (type == 1) {
                adapter.setPageType(AreaDetailAdapter.PAGE_FREEWARKER);
            } else if (type == 2) {
                adapter.setPageType(AreaDetailAdapter.PAGE_STRATEGY);
            } else if (type == -1) {
                adapter.setPageType(AreaDetailAdapter.PAGE_LOCAL);
            }
        }

        @Override
        public void onRefresh() {
            desAreaItemsXlv.stopRefresh();
        }

        @Override
        public void onLoadMore() {
            HeaderViewListAdapter headerViewListAdapter = (HeaderViewListAdapter) desAreaItemsXlv.getAdapter();
            AreaDetailAdapter adapter = (AreaDetailAdapter) headerViewListAdapter.getWrappedAdapter();
            if (pageNumber < toalPages) {
                isLoadMore = true;
                int type = adapter.getPageType();
                int numb = pageNumber + 1;
                switch (type) {
                    case AreaDetailAdapter.PAGE_STRATEGY:
                        loader.getDesAreaRoutes(10, numb, areaId, 2);
                        break;
                    case AreaDetailAdapter.PAGE_FREEWARKER:
                        loader.getAreaProducts(10, numb, areaId, 1, -1);
                        break;
                    case AreaDetailAdapter.PAGE_PACKAGETOUR:
                        loader.getAreaProducts(10, numb, areaId, 0, -1);
                        break;
                    case AreaDetailAdapter.PAGE_LOCAL:
                        loader.getAreaProducts(10, numb, areaId, -1, adapter.getCollectionId());
                        break;
                }

            } else {
                isLoadMore = false;
                if (!toalItems.contains(null)) {
                    desAreaItemsXlv.closeLoadMore();
                    toalItems.add(null);
                    adapter.updateItems(toalItems);
                }
            }
        }
    }


    public void hidenEmptView() {
        if (destinationAreaEmpView != null && destinationAreaEmpView.getVisibility() == View.VISIBLE) {
            destinationAreaEmpView.setVisibility(View.GONE);
        }

    }

    public void showEmptView() {
        if (destinationAreaEmpView != null && destinationAreaEmpView.getVisibility() != View.VISIBLE) {
            destinationAreaEmpView.setVisibility(View.VISIBLE);
        }
    }


    private AdapterView.OnItemClickListener areaDetailClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SearchItem item = (SearchItem) view.getTag(R.id.areaDetialItem);
            if (item != null) {
                if (item instanceof ProductItem) {
                    gotoProductDetail(item);
                } else if (item instanceof SearchItem) {
                    gotoRouteDetail(item);
                }
            }
        }
    };

    private void gotoProductDetail(SearchItem item) {
        Intent intent = new Intent();
        intent.setClass(DestinationAreaActivity.this, ProductDetailsActivity.class);
        SpecialToH5Bean bean = new SpecialToH5Bean();
        bean.setTargetId(item.getId());
        intent.putExtra("productdetails", bean);
        startActivity(intent);
    }

    private void gotoRouteDetail(SearchItem item) {
        Intent intent = new Intent();
        intent.setClass(DestinationAreaActivity.this, RouteDetailActivity.class);
        intent.putExtra(RouteDetailActivity.DETAILS_ID, item.getId());
        startActivity(intent);
    }


    @OnClick({R.id.des_areanav_menu_pickup, R.id.des_areanav_menu_hot, R.id.des_areanav_menu_strategy, R.id.des_areanav_menu_packagetour, R.id.des_areanav_menu_freewarker, R.id.des_areanav_menu_lacoalplay})
    public void onClick(View view) {
//        clearItems();
        switch (view.getId()) {
            case R.id.des_areanav_menu_lacoalplay:
                MobHelper.sendEevent(this, MobEvent.DESTINATION_PRODUCT_LOCAL_LIST);
                setSeletedMenu(desAreanavMenuLacoalplay);
                if (poupWindow == null) {
                    View contentView = LayoutInflater.from(this).inflate(R.layout.sift_poupwidow, null);
                    poupWindow = new SiftPoupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    poupWindow.setSiftItemClickListener(siftItemClickListener);
                    poupWindow.setOutsideTouchable(true);
                }
                poupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                break;

            case R.id.des_areanav_menu_hot:
                MobHelper.sendEevent(this, MobEvent.DESTINATION_PRODUCT_LIST);
                setSeletedMenu(desAreanavMenuHot);
                showLoadingView();
                isHotLoading = true;

                if (productItems.size() > 0) {
                    productItems.clear();
                }
                if (routeItems.size() > 0) {
                    routeItems.clear();
                }
                if (hotAdapter != null) {
                    desAreaItemsXlv.setAdapter(hotAdapter);
                }
                loader.getHotProducts(areaId);
                loader.getHotRoutes(areaId);
                break;
            case R.id.des_areanav_menu_strategy:
                MobHelper.sendEevent(this, MobEvent.DESTINATION_PRODUCT_LIST);
                setSeletedMenu(desAreanavMenuStrategy);
                showLoadingView();
                if (strategyAdpater != null) {
                    desAreaItemsXlv.setAdapter(strategyAdpater);
                }
                loader.getDesAreaRoutes(10, 1, areaId, 2);
                break;
            case R.id.des_areanav_menu_packagetour:
                MobHelper.sendEevent(this, MobEvent.DESTINATION_PRODUCT_LIST);
                setSeletedMenu(desAreanavMenuPackagetour);
                showLoadingView();
                if (packageAdpater != null) {
                    desAreaItemsXlv.setAdapter(packageAdpater);
                }
                loader.getAreaProducts(10, 1, areaId, 0, -1);
                break;
            case R.id.des_areanav_menu_freewarker:
                MobHelper.sendEevent(this, MobEvent.DESTINATION_PRODUCT_LIST);
                setSeletedMenu(desAreanavMenuFreewarker);
                showLoadingView();
                if (freeAdpater != null) {
                    desAreaItemsXlv.setAdapter(freeAdpater);
                }
                loader.getAreaProducts(10, 1, areaId, 1, -1);
                break;
            case R.id.des_areanav_menu_pickup:
                if (UserHelper.isLogined(this)) {
                    Intent intent = new Intent(this, BookActivity.class);
                    intent.putExtra(BookActivity.BOOK_URL, yiut8Url);
                    startActivity(intent);
                } else {
                    LLoginActivity.start(DestinationAreaActivity.this);
                }
                break;
        }
    }

    public Drawable getLayoutDrawabe(View view) {
//        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
//                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//        int width = size*40;
//
//        view.layout(0, 0, width, view.getMeasuredHeight());  //根据字符串的长度显示view的宽度
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return new BitmapDrawable(bitmap);
    }


    @OnClick(R.id.destion_search_btn)
    public void search() {
        Intent intent = new Intent(this, GlobalSearchActivity.class);
        intent.putExtra(KeyParams.PAGE_TYPE, PageType.PAGE_DESTIATION);
        startActivity(intent);
    }

    private OnItemClickListener siftItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClickListener(View view, int position) {
            poupWindow.dismiss();
            SiftModle item = (SiftModle) view.getTag();
            if (item != null) {
                showLoadingView();
                desAreanavMenuLacoalplay.setText(item.getName());
                //本地玩乐
                loader.getAreaProducts(10, 1, areaId, -1, item.getId());
            }

        }
    };

    public void showLoadingView() {
        if (loadingPage != null && loadingPage.getVisibility() != View.VISIBLE) {
            loadingPage.setVisibility(View.VISIBLE);
        }
    }

    public void hidenLoadingView() {
        if (loadingPage != null && loadingPage.getVisibility() == View.VISIBLE) {
            loadingPage.setVisibility(View.GONE);
        }
    }


}
