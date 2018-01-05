package com.nettactic.mall.activity;

import com.nettactic.mall.R;
import com.nettactic.mall.base.BaseActivity;
import com.nettactic.mall.dialog.CustomProgressDialog;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.StringUtils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 有头Web网页
 * 
 * @author heshicaihao
 * 
 */
@SuppressLint("SetJavaScriptEnabled")
public class OtherWebActivity extends BaseActivity implements OnClickListener {

	private TextView mTitle;
	private ImageView mBack;
	private WebView mWebview;
	private String mTitleStr;
	private String mUrl;
	private CustomProgressDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_other_web);

		initView();
		initData();
	}

	private void initData() {
		getDataIntent();

		mTitle.setText(mTitleStr);
		if (!StringUtils.isEmpty(mUrl)) {
			initWebView(mWebview, mUrl);
		}
	}

	private void getDataIntent() {
		Intent intent = getIntent();
		mTitleStr = intent.getStringExtra("title");
		mUrl = intent.getStringExtra("url");
	}

	private void initView() {
		mTitle = (TextView) findViewById(R.id.title);
		mBack = (ImageView) findViewById(R.id.back);
		mWebview = (WebView) findViewById(R.id.webview);

		mBack.setOnClickListener(this);
		mDialog = new CustomProgressDialog(this, this.getResources().getString(
				R.string.loading));
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {

		case R.id.back:
			AndroidUtils.finishActivity(this);
			break;
		default:
			break;
		}
	}

	public void initWebView(final WebView Wv, String url) {

		WebSettings webSettings = mWebview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		mWebview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				mDialog.dismiss();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return false;
			}
		});
		mWebview.loadUrl(url);
		mDialog.show();
		mWebview.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK
							&& mWebview.canGoBack()) { // 表示按返回键
						mWebview.goBack(); // 后退
						return true; // 已处理
					}
				}
				return false;
			}
		});

		mWebview.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_UP:
					v.requestFocusFromTouch();
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				case MotionEvent.ACTION_CANCEL:
					break;
				}
				return false;
			}

		});
	}

}
