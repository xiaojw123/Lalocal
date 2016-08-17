package com.lalocal.lalocal.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**地图定位
 * Created by xiaojw on 2016/8/13.
 */
public class LocWebView extends WebView {
    String formartLocUrl = "http://m.mafengwo.cn/app/map/map?center=%1$s,%2$s&zoomlevel=%3$s";
    Context mContext;
    int zoomLevel = 14;
    double mLat = 39.920000000000002D;
    double mLng = 116.45999999999999D;


    public LocWebView(Context context, double lat, double lng) {
        super(context);
        mLat = lat;
        mLng = lng;
        mContext = context;
        init();
    }

    public LocWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LocWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void init() {
        setWebChromeClient(new WebChromeClient());
        setWebViewClient(mWebViewClient);
        setDownloadListener(mDownloadListener);
        WebSettings setting = getSettings();
        setting.setJavaScriptEnabled(true);
//        addJavascriptInterface(new JsInterface((Activity) this.mContext, this), "android");
        setting.setUseWideViewPort(true);
        setting.setLoadWithOverviewMode(true);
//        setting.setUserAgentString(WebUtils.getUA());
        CookieSyncManager.createInstance(getContext());
        CookieSyncManager.getInstance().startSync();
        CookieManager localCookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= 21)
            localCookieManager.setAcceptThirdPartyCookies(this, true);
        setting.setGeolocationEnabled(true);
        setting.setAppCacheEnabled(true);
        setting.setDatabaseEnabled(true);
        setting.setDomStorageEnabled(true);
        setting.setAppCachePath(mContext.getDir("appcache", 0).getPath());
        setting.setAppCacheMaxSize(8388608L);
        setting.setDatabasePath(mContext.getDir("databases", 0).getPath());
        setting.setGeolocationDatabasePath(mContext.getDir("geolocation", 0).getPath());
        Log.d("xiaojw","url___"+String.format(formartLocUrl, mLat, mLng, zoomLevel));
        loadUrl(String.format(formartLocUrl, mLat, mLng, zoomLevel));
//        loadUrl("http://m.mafengwo.cn/app/map/map");
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return false;
        }
    };

    public void addMarker(){
        String str = "[{'id': 1212121212,'position': {'lat': " +mLat + ", 'lng': " + mLng+ "}, 'title': 'test', 'icon': {'url': '" + "http://images.mafengwo.net/images/mobile/appmap/poi_map_small_marker.png" + "', 'scaledSize': {'type': 'Size', 'args':[" + "36,36" + "]},'anchor':{'type':'Point', 'args':[18,18]}},'overlay':{'data':{'title':'" + "你妹地图" + "', '': '+des+'}, 'default_show':1}}]";
        loadUrl("javascript:gmap.call('addMarker', " + str + ")");
    }

    private DownloadListener mDownloadListener = new DownloadListener() {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        }
    };


}
