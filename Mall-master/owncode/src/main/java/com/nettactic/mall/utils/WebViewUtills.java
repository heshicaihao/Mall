/*
 * WebViewUtills.java
 * classes : com.gionee.client.business.util.WebViewUtills
 * @author yuwei
 * 
 * Create at 2015-2-5 下午2:46:51
 */
package com.nettactic.mall.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressWarnings("deprecation")
public class WebViewUtills {
    /**
     * void TODO init the webView complete the basic settings
     */
    @SuppressLint({"SetJavaScriptEnabled", "NewApi"})
    public static void initWebView(WebView mWebView, boolean isUseCache,String url) {
    	mWebView.loadUrl(url);
        WebViewReflect.setDomStorage(mWebView.getSettings());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.requestFocus();
        mWebView.setSelected(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
        mWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
        mWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setBlockNetworkImage(true);
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        mWebView.setLongClickable(true);
        mWebView.setWebViewClient(new WebViewClient());
        int sdkVersion = AndroidUtils.getAndroidSDKVersion();
        if (sdkVersion >= 11) {
            mWebView.getSettings().setTextSize(TextSize.NORMAL);
            WebViewReflect.setDisplayZoomControls(mWebView.getSettings(), false);
        }
        if (sdkVersion >= 9) {
            mWebView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
        if (isUseCache) {
            initCacheWebViewSettings(mWebView);
        }
        mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
        initLocationSettings(mWebView);
    }

    public static void initCacheWebViewSettings(WebView cachedWebView) {
        cachedWebView.getSettings().setAppCacheEnabled(true);
        cachedWebView.getSettings().setDatabaseEnabled(true);
        cachedWebView.getSettings().setDomStorageEnabled(true);
        cachedWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 80);
        cachedWebView.getSettings().setAppCachePath(
                cachedWebView.getContext().getDir("appcache", 0).getPath());
        cachedWebView.getSettings().setDatabasePath(
                cachedWebView.getContext().getDir("databases", 0).getPath());

    }

    public static void clearCookies(Context context) {

        try {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void initLocationSettings(WebView mWebView) {
        mWebView.getSettings().setGeolocationEnabled(true);
        mWebView.getSettings().setGeolocationDatabasePath(
                mWebView.getContext().getDir("geolocation", 0).getPath());
    }

}
