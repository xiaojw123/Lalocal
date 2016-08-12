package com.lalocal.lalocal.test;

import android.os.Bundle;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.MarkerOptions;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;


public class TestActivity extends BaseActivity implements AMap.OnMapLoadedListener {
    public static final LatLng BEIJING = new LatLng(39.90403, 116.407525);// 北京市经纬度
    public static final LatLng ZHONGGUANCUN = new LatLng(39.983456, 116.3154950);// 北京市中关村经纬度
    public static final LatLng SHANGHAI = new LatLng(31.238068, 121.501654);// 上海市经纬度
    public static final LatLng FANGHENG = new LatLng(39.989614, 116.481763);// 方恒国际中心经纬度
    public static final LatLng CHENGDU = new LatLng(30.679879, 104.064855);// 成都市经纬度
    public static final LatLng XIAN = new LatLng(34.341568, 108.940174);// 西安市经纬度
    public static final LatLng ZHENGZHOU = new LatLng(34.7466, 113.625367);// 郑州市经纬度
    private AMap aMap;
    private MapView mapView;
    private LatLng latlng = new LatLng(36.061, 103.834);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_widget);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        addMarker();
    }

    private void addMarker() {
        aMap= mapView.getMap();
        aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
        MarkerOptions options=new MarkerOptions();
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.index_article_tipes_icon_location_1));
        options.position(CHENGDU);
        aMap.addMarker(options);
    }


    @Override
    public void onMapLoaded() {
        // 设置所有maker显示在当前可视区域地图中
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(XIAN).include(CHENGDU)
                .include(latlng).include(ZHENGZHOU).include(BEIJING).build();
        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));

    }
    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

}
