package com.lalocal.lalocal.live.entertainment.activity;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.model.LiveHistoryItem;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.view.adapter.LocationHistoryAdapter;
import com.lalocal.lalocal.view.decoration.LinearItemDecoration;
import com.lalocal.lalocal.view.listener.OnItemClickListener;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by android on 2016/11/18.
 */
public class LiveLocationActivity extends BaseActivity implements View.OnLayoutChangeListener,PoiSearch.OnPoiSearchListener {

    @BindView(R.id.live_location_title_back)
    ImageView liveLocationTitleBack;
    @BindView(R.id.live_location_complete)
    TextView liveLocationComplete;
    @BindView(R.id.live_location_tv)
    TextView liveLocationTv;
    @BindView(R.id.live_again_location)
    ImageView liveAgainLocation;
    @BindView(R.id.location_recycler)
    RecyclerView locationRecycler;
    @BindView(R.id.live_location_input_et)
    EditText liveLocationInputEt;
    @BindView(R.id.live_input_location_tv)
    TextView liveInputLocationTv;
    @BindView(R.id.live_location_input_layout)
    ScrollView liveLocationInputLayout;
    @BindView(R.id.live_location_no)
    TextView liveLocationNo;
    @BindView(R.id.live_location_layout)
    LinearLayout liveLocationLayout;
    private Handler handler = new Handler();
    private static final int SHOW_LAYOUT_DELAY = 200;
    private int screenHeight;
    private int keyHeight;
    private LocationHistoryAdapter hintAdapter;
    List<String> datas = new ArrayList<>();
    List<SparseArray<String>> locationDatas = new ArrayList<>();
    private String location;
    private PoiSearch poiSearch;
    private double longitude;
    private double latitude;
    private PoiSearch.Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_location_layout);
        ButterKnife.bind(this);
        location = getIntent().getStringExtra("location");
        liveLocationLayout.addOnLayoutChangeListener(this);
        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
        initView();
        initHistoryData();
        //初始化定位
        initLocation();
    }

    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
        startLocation();

    }

    private void locationPoi(String newText, String city) {
        query = new PoiSearch.Query("", "", city);
        query.setPageSize(5);
        query.setPageNum(0);
        query.setCityLimit(true);
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        LatLonPoint latLonPoint = new LatLonPoint(longitude,latitude);
        poiSearch.setBound(new PoiSearch.SearchBound(latLonPoint,5000));
        poiSearch.searchPOIAsyn();

    }

    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setNeedAddress(true);
        mOption.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        return mOption;
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {

        @Override
        public void onLocationChanged(AMapLocation loc) {
            try {
                String errorInfo = loc.getErrorInfo();
                if("success".equals(errorInfo)){
                    //解析定位结果
                    LiveActivity.locationY=true;
                    latitude = loc.getLatitude();
                    longitude = loc.getLongitude();
                    LiveActivity.latitude=String.valueOf(latitude);
                    LiveActivity.latitude=String.valueOf(longitude);
                    String result = loc.getCountry() + "·" + loc.getProvince() + "·" + loc.getCity();
                    LiveActivity.liveLocation=result;
                    liveLocationTv.setText("当前地位: " + result);
                    AppLog.i("TAG", "获取详细地理位置信息" + loc.getAddress() + "    " + loc.getAoiName());
                    //获取周边相似位置
                    locationPoi(loc.getAoiName(), loc.getCity());
                }else {
                    liveLocationTv.setText("定位失败!");
                    LiveActivity.liveLocation="Lalocal神秘之地";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onPoiSearched(PoiResult result, int rCode) {

        if(rCode==AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {
                    ArrayList<PoiItem> pois = result.getPois();
                    if(pois==null||pois.size()<1){
                        return;
                    }
                    SparseArray<String> hotTile = new SparseArray<>();
                    hotTile.put(LocationHistoryAdapter.ITEM_HOT_TITLE, "附近其他");
                    locationDatas.add(hotTile);
                    for (PoiItem poiItem : pois) {
                        String address= poiItem.getProvinceName()+"·"+poiItem.getCityName()+"·"+poiItem.getSnippet();
                        SparseArray<String> sp = new SparseArray<>();
                        sp.put(LocationHistoryAdapter.ITEM_HOT, address);
                        locationDatas.add(sp);
                    }
                    if(hintAdapter!=null){
                        hintAdapter.updateItems(locationDatas);
                    }
                }}
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int rCode) {
    }
    private void initHistoryData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                locationDatas.clear();
                Cursor cursor = null;
                try {
                    cursor = Connector.getDatabase().rawQuery("select * from LiveHistoryItem order by name",
                            null);
                    if (cursor.moveToFirst()) {
                        SparseArray<String> titleSp = new SparseArray<>();
                        titleSp.put(LocationHistoryAdapter.ITEM_HISTORY_TITLE, "历史记录");
                        locationDatas.add(titleSp);
                        do {
                            String name = cursor.getString(cursor.getColumnIndex("name"));
                            if(!TextUtils.isEmpty(name)){
                                SparseArray<String> sp = new SparseArray<>();
                                sp.put(LocationHistoryAdapter.ITEM_HISTORY, name);
                                locationDatas.add(sp);
                            }

                        } while (cursor.moveToNext());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hintAdapter.updateItems(locationDatas);
                        }
                    });
                }
            }
        }).start();
    }

    private void initView() {
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        locationRecycler.setLayoutManager(lm);
        locationRecycler.setHasFixedSize(true);
        hintAdapter = new LocationHistoryAdapter(this, locationDatas);
        locationRecycler.addItemDecoration(new LinearItemDecoration(this));
        locationRecycler.setAdapter(hintAdapter);

        hintAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                String text = (String) view.getTag();
                LiveActivity.liveLocation = text;
                LiveActivity.locationY=true;
                try{
                    List<LiveHistoryItem> all = DataSupport.findAll(LiveHistoryItem.class);
                    if(all!=null&&all.size()>0){
                        for(LiveHistoryItem historyItem: all){
                            if(historyItem.getName().equals(text.trim())){
                                finish();
                                return;
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                LiveHistoryItem item = new LiveHistoryItem();
                item.setName(LiveActivity.liveLocation);
                item.save();
                finish();
            }
        });

    }

    @OnClick({R.id.live_location_no, R.id.live_input_location_tv, R.id.live_location_complete, R.id.live_again_location})
    public void clickBtn(View view) {
        switch (view.getId()) {
            case R.id.live_location_no:
                liveLocationNo.setVisibility(View.GONE);
                liveLocationInputLayout.setVisibility(View.VISIBLE);
                liveLocationInputEt.requestFocus();
                handler.postDelayed(showTextRunnable, SHOW_LAYOUT_DELAY);
                break;
            case R.id.live_input_location_tv:
                String trim = liveLocationInputEt.getText().toString().trim();
                if(trim.length()>0){
                    LiveActivity.locationY=true;
                    LiveActivity.liveLocation =trim;
                    try{
                        List<LiveHistoryItem> all = DataSupport.findAll(LiveHistoryItem.class);
                        if(all!=null&&all.size()>0){
                            for(LiveHistoryItem historyItem:all){
                                if(historyItem.getName().equals(trim)){
                                    finish();
                                    return;
                                }
                            }
                        }
                        LiveHistoryItem item = new LiveHistoryItem();
                        item.setName(LiveActivity.liveLocation);
                        item.save();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    finish();
                }else{
                    LiveActivity.locationY=false;
                    Toast.makeText(this,"请输入地址!",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.live_location_complete:
                finish();
                break;
            case R.id.live_again_location:
                initHistoryData();
                startLocation();
                break;
        }
    }

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = new AMapLocationClientOption();

    private void startLocation() {
        //根据控件的选择，重新设置定位参数
        resetOption();
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    private void resetOption() {
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        locationOption.setGpsFirst(false);
        // 设置是否开启缓存
        locationOption.setLocationCacheEnable(true);
        //设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
        locationOption.setOnceLocationLatest(true);
        //设置是否使用传感器
        locationOption.setSensorEnable(false);

    }

    private Runnable showTextRunnable = new Runnable() {
        @Override
        public void run() {
            showInputMethod();
        }
    };

    private void showInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(liveLocationInputEt, 0);
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

        if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {//隐藏
            liveLocationInputLayout.setVisibility(View.GONE);
            liveLocationNo.setVisibility(View.VISIBLE);
        }
    }
}
