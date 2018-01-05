package com.nettactic.mall.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nettactic.mall.R;
import com.nettactic.mall.base.BaseFragment;

public class GoodsDetailLeftFragment extends BaseFragment {

    public WebView wv_detail;
    private View mView;
    private WebSettings webSettings;
    private LayoutInflater inflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        mView = inflater.inflate(R.layout.fragment_goods_details_left, null);
        initWebView(mView);
        return mView;
    }

    public void initWebView(View rootView) {
        String url = "http://m.okhqb.com/item/description/1000335599.html?fromApp=true";
        wv_detail = (WebView) rootView.findViewById(R.id.wv_detail);
        wv_detail.setFocusable(false);
        wv_detail.loadUrl(url);
        webSettings = wv_detail.getSettings();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setBlockNetworkImage(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        wv_detail.setWebViewClient(new GoodsDetailWebViewClient());
    }


    private class GoodsDetailWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            webSettings.setBlockNetworkImage(false);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return true;
        }
    }


}
