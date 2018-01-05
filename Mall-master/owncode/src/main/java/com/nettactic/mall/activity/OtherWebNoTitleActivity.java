package com.nettactic.mall.activity;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nettactic.mall.R;
import com.nettactic.mall.base.BaseActivity;
import com.nettactic.mall.bean.UserBean;
import com.nettactic.mall.common.UserController;
import com.nettactic.mall.dialog.CustomProgressDialog;
import com.nettactic.mall.net.JavaScriptObject;
import com.nettactic.mall.net.NetHelper;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.LogUtils;
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
 * 无头Web网页
 * 
 * @author heshicaihao
 * 
 */
@SuppressWarnings("deprecation")
@SuppressLint("SetJavaScriptEnabled")
public class OtherWebNoTitleActivity extends BaseActivity implements
		OnClickListener {

	private TextView mTitle;
	private ImageView mBack;
	private WebView mWebview;
	private String mTitleStr;
	private String mUrl;
	private CustomProgressDialog mDialog;
	private UserBean mUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_other_no_title_web);
		mUser = UserController.getInstance(this).getUserInfo();
		initView();
		initData();
	}

	@Override
	public void onResume() {
		super.onResume();
		initPopularizeinfo();
	}

	private void initData() {
		getDataIntent();
		mTitle.setText(mTitleStr);
		if (!StringUtils.isEmpty(mUrl)) {
			initWebView(mWebview, mUrl);
		}
		
		initPopularizeinfo();
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
		mWebview.addJavascriptInterface(new JavaScriptObject(getApplication(),
				this,mUser.getId()), "toApp");
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

	private void initPopularizeinfo() {
		String promotecode = AndroidUtils.getPromoteCodeInfo(mUser.getId());
		if (StringUtils.areEmpty(promotecode)) {
			NetHelper.getPromoteInfo(mUser.getId(), new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					LogUtils.logd(TAG, "initPopularizeinfo+onSuccess");
					LogUtils.logd(TAG, "mUser.getId()" + mUser.getId());
					resolvePromoteInfo(arg2);

				}

				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2,
						Throwable arg3) {
					LogUtils.logd(TAG, "initPopularizeinfo+onFailure");

				}
			});
		}
	}

	/**
	 * 解析 我的推广信息
	 * 
	 * @param arg2
	 */
	private void resolvePromoteInfo(byte[] arg2) {
		try {
			String json = new String(arg2, "UTF-8");
			LogUtils.logd(TAG, "json:" + json);
			JSONObject obj = new JSONObject(json);
			String code = obj.optString("code");
			if ("0".equals(code)) {
				JSONObject result = obj.optJSONObject("result");
				String	mPromoteCode = result.optString("promote_code");
				AndroidUtils.savePromoteCodeInfo(mUser.getId(), mPromoteCode);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
}
