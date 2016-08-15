package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.HeaderViewListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
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
import butterknife.Unbinder;

public class DestinationAreaActivity extends BaseActivity {
    public static final String AREA_ID = "area_id";
    public static final String AREA_NAME = "area_name";

    @BindView(R.id.destination_area_title)
    CustomTitleView destinationAreaTitle;
    @BindView(R.id.search_view)
    FrameLayout searchView;
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
    Unbinder unbinder;
    @BindView(R.id.des_area_menunav_container)
    LinearLayout desAreaMenunavContainer;
    @BindView(R.id.des_area_items_xlv)
    XListView desAreaItemsXlv;
    int areaId;
    String areaTile="%1$s地区";
    ContentLoader loader;
    boolean isHotLoading;
    List<SearchItem> toalItems = new ArrayList<>();
    List<SearchItem> hotItems = new ArrayList<>();
    List<SearchItem> productItems = new ArrayList<>();
    List<SearchItem> routeItems = new ArrayList<>();
    AreaDetailAdapter hotAdapter, strategyAdpater, packageAdpater, freeAdpater, localAdapter;
    SiftPoupWindow poupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destination_area_layout);
        unbinder = ButterKnife.bind(this);
        String areaName=getIntent().getStringExtra(AREA_NAME);
        destinationAreaTitle.setTitle(String.format(areaTile,areaName));
        desAreaItemsXlv.setOnItemClickListener(areaDetailClickListener);
        setSeletedMenu(desAreanavMenuHot);
        intLoader();
    }

    private void intLoader() {
        loader = new ContentLoader(this);
        loader.setCallBack(new AreaCallBack());
        areaId = getIntent().getIntExtra(AREA_ID, -1);
        isHotLoading = true;
        loader.getHotRoutes(areaId);
        loader.getHotProducts(areaId);
        loader.getDesAreaRoutes(10, 1, areaId, 2);
        loader.getAreaProducts(10, 1, areaId, 0, -1);
        loader.getAreaProducts(10, 1, areaId, 1, -1);
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
        super.onDestroy();
        unbinder.unbind();
    }

    public void clearItems() {
        if (toalItems != null && toalItems.size() > 0) {
            toalItems.clear();
        }
    }


    class AreaCallBack extends ICallBack implements XListView.IXListViewListener {


        int pageNumber;
        int toalPages;
        boolean isLoadMore;


        @Override
        public void onGetHotItems(List<SearchItem> items, int type) {

            if (type == AreaDetailAdapter.MODULE_TYPE_PRODUCT) {
                SearchItem productTile = new SearchItem();
                productTile.setTitle("热门商品");
                productItems.add(productTile);
                productItems.addAll(items);
            } else if (type == AreaDetailAdapter.MODULE_TYPE_ROUTE) {
                SearchItem routeTile = new SearchItem();
                routeTile.setTitle("热门攻略");
                routeItems.add(routeTile);
                routeItems.addAll(items);
            }
            if (isHotLoading) {
                isHotLoading = false;
                return;
            }
            if (hotItems.size() > 0) {
                hotItems.clear();
            }
            hotItems.addAll(routeItems);
            hotItems.addAll(productItems);
            if (hotItems.size() > 0) {
                if (desAreanavMenuHot.getVisibility() != View.VISIBLE) {
                    desAreanavMenuHot.setVisibility(View.VISIBLE);
                }
                AppLog.print("get hot toalItems ___size___" + hotItems.size());
                desAreaItemsXlv.setPullLoadEnable(false);
                desAreaItemsXlv.setPullRefreshEnable(false);
                desAreaItemsXlv.closeLoadMore();
                if (hotAdapter == null) {
                    hotAdapter = new AreaDetailAdapter(DestinationAreaActivity.this, hotItems);
                    hotAdapter.setPageType(AreaDetailAdapter.PAGE_HOT);
                    desAreaItemsXlv.setAdapter(hotAdapter);
                } else {
                    hotAdapter.setPageType(AreaDetailAdapter.PAGE_HOT);
                    hotAdapter.updateItems(hotItems);
                }
            }
        }

        @Override
        public void onRequestFailed() {
            isLoadMore = false;
            isHotLoading = false;
        }

        @Override
        public void onGetAreaItems(int pageNumber, int totalPages, List<SearchItem> items, int type) {
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
                case 0:
                    if (toalItems.size() > 0) {
                        if (desAreanavMenuPackagetour.getVisibility() != View.VISIBLE) {
                            desAreanavMenuPackagetour.setVisibility(View.VISIBLE);
                        } else {
                            setAreaItemXlv();
                            if (packageAdpater == null) {
                                packageAdpater = new AreaDetailAdapter(DestinationAreaActivity.this, toalItems);

                                setItemType(packageAdpater, type);
                                desAreaItemsXlv.setAdapter(packageAdpater);
                            } else {
                                setItemType(packageAdpater, type);
                                packageAdpater.updateItems(toalItems);
                            }
                        }
                    }
                    break;
                case 1:
                    if (toalItems.size() > 0) {
                        if (desAreanavMenuFreewarker.getVisibility() != View.VISIBLE) {
                            desAreanavMenuFreewarker.setVisibility(View.VISIBLE);
                        } else {
                            setAreaItemXlv();
                            if (freeAdpater == null) {
                                freeAdpater = new AreaDetailAdapter(DestinationAreaActivity.this, toalItems);
                                setItemType(freeAdpater, type);
                                desAreaItemsXlv.setAdapter(freeAdpater);
                            } else {
                                setItemType(freeAdpater, type);
                                freeAdpater.updateItems(toalItems);
                            }
                        }
                    }
                    break;
                case 2:
                    if (toalItems.size() > 0) {
                        if (desAreanavMenuStrategy.getVisibility() != View.VISIBLE) {
                            desAreanavMenuStrategy.setVisibility(View.VISIBLE);
                        } else {
                            setAreaItemXlv();
                            if (strategyAdpater == null) {
                                strategyAdpater = new AreaDetailAdapter(DestinationAreaActivity.this, toalItems);
                                setItemType(strategyAdpater, type);
                                desAreaItemsXlv.setAdapter(strategyAdpater);
                            } else {
                                setItemType(strategyAdpater, type);
                                strategyAdpater.updateItems(toalItems);
                            }
                        }
                    }
                    break;
                case -1:
                    AppLog.print("onGetAreaItems____-1");
                    if (toalItems.size() > 0) {
                        setAreaItemXlv();
                        if (localAdapter == null) {
                            localAdapter = new AreaDetailAdapter(DestinationAreaActivity.this, toalItems);
                            setItemType(localAdapter, type);
                            desAreaItemsXlv.setAdapter(localAdapter);
                        } else {
                            setItemType(localAdapter, type);
                            localAdapter.updateItems(toalItems);
                        }
                    }
                    break;
            }


        }

        private void setAreaItemXlv() {
            desAreaItemsXlv.setPullLoadEnable(true);
            desAreaItemsXlv.setPullRefreshEnable(false);
            desAreaItemsXlv.openLoadMore();
            desAreaItemsXlv.setXListViewListener(this);
        }


        private void setItemType(AreaDetailAdapter adapter, int type) {
            if (type == 0) {
                adapter.setPageType(AreaDetailAdapter.PAGE_PACKAGETOUR);
            } else if (type == 1) {
                adapter.setPageType(AreaDetailAdapter.PAGE_FREEWARKER);
            } else if (type == 2) {
                adapter.setPageType(AreaDetailAdapter.PAGE_STRATEGY);
            }
        }

        @Override
        public void onRefresh() {

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
        intent.putExtra(RouteDetailActivity.DETAILS_ID,item.getId());
        startActivity(intent);
    }


    @OnClick({R.id.des_areanav_menu_hot, R.id.des_areanav_menu_strategy, R.id.des_areanav_menu_packagetour, R.id.des_areanav_menu_freewarker, R.id.des_areanav_menu_lacoalplay})
    public void onClick(View view) {
        clearItems();
        AppLog.print("onclick____toalItems_size__" + toalItems.size());
        switch (view.getId()) {
            case R.id.des_areanav_menu_lacoalplay:
                setSeletedMenu(desAreanavMenuLacoalplay);
                View contentView = LayoutInflater.from(this).inflate(R.layout.sift_poupwidow, null);
                poupWindow = new SiftPoupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                poupWindow.setSiftItemClickListener(siftItemClickListener);
                poupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                poupWindow.setOutsideTouchable(true);
                break;

            case R.id.des_areanav_menu_hot:
                setSeletedMenu(desAreanavMenuHot);
                isHotLoading = true;
                if (routeItems.size() > 0) {
                    routeItems.clear();
                }
                if (productItems.size() > 0) {
                    productItems.clear();
                }
                if (hotAdapter != null) {
                    desAreaItemsXlv.setAdapter(hotAdapter);
                }
                loader.getHotRoutes(areaId);
                loader.getHotProducts(areaId);
                break;
            case R.id.des_areanav_menu_strategy:
                setSeletedMenu(desAreanavMenuStrategy);
                if (strategyAdpater != null) {
                    desAreaItemsXlv.setAdapter(strategyAdpater);
                }
                loader.getDesAreaRoutes(10, 1, areaId, 2);
                break;
            case R.id.des_areanav_menu_packagetour:
                setSeletedMenu(desAreanavMenuPackagetour);
                if (packageAdpater != null) {
                    desAreaItemsXlv.setAdapter(packageAdpater);
                }
                loader.getAreaProducts(10, 1, areaId, 0, -1);
                break;
            case R.id.des_areanav_menu_freewarker:
                setSeletedMenu(desAreanavMenuFreewarker);
                if (freeAdpater != null) {
                    desAreaItemsXlv.setAdapter(freeAdpater);
                }
                loader.getAreaProducts(10, 1, areaId, 1, -1);
                break;
        }
    }
    @OnClick(R.id.search_view)
    public void search(){
        Intent intent=new Intent(this,SearchActivity.class);
        startActivity(intent);
    }

    private OnItemClickListener siftItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClickListener(View view, int position) {
            poupWindow.dismiss();
            SiftModle item = (SiftModle) view.getTag();
            if (item != null) {
                desAreanavMenuLacoalplay.setText(item.getName());
                loader.getAreaProducts(10, 1, areaId, -1, item.getId());
            }

        }
    };

}
