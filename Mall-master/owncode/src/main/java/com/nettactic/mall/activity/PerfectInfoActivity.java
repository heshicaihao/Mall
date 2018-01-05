package com.nettactic.mall.activity;

import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nettactic.mall.R;
import com.nettactic.mall.base.BaseActivity;
import com.nettactic.mall.bean.UserBean;
import com.nettactic.mall.common.MyApplication;
import com.nettactic.mall.common.UserController;
import com.nettactic.mall.net.NetHelper;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.LogUtils;
import com.nettactic.mall.utils.SharedpreferncesUtil;
import com.nettactic.mall.utils.ToastUtils;

/**
 * 完善信息页
 * 
 * @author heshicaihao
 * 
 */
@SuppressWarnings("deprecation")
public class PerfectInfoActivity extends BaseActivity implements
		OnClickListener {

	private ImageView mBack;
	private TextView mTitle;
	private EditText mPromoCodeET;
	private Button mRegisterBt;
	private Button mJumpBt;

	private UserBean mUser;
	private String mAccountId;
	private String mPromoteCode;
	private InputMethodManager mImm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_perfect_info);
		mUser = UserController.getInstance(this).getUserInfo();
		SharedpreferncesUtil.setPerfectInfo(getApplication(), true);
		initView();
		initData();

	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {

		case R.id.back:
			AndroidUtils.finishActivity(this);

			break;
		case R.id.register:
			mAccountId = mUser.getId();
			mPromoteCode = mPromoCodeET.getText().toString().trim();
			ApplyPromoter(mAccountId, mPromoteCode);

			break;

		case R.id.jump:
			mImm.hideSoftInputFromWindow(this.getCurrentFocus()
					.getWindowToken(), 0);
			AndroidUtils.finishActivity(this);

			break;

		default:
			break;
		}
	}

	private void initView() {
		mBack = (ImageView) findViewById(R.id.back);
		mTitle = (TextView) findViewById(R.id.title);
		mPromoCodeET = (EditText) findViewById(R.id.promo_code_edit_text);
		mRegisterBt = (Button) findViewById(R.id.register);
		mJumpBt = (Button) findViewById(R.id.jump);

		mTitle.setText(R.string.perfect_info);
		mBack.setOnClickListener(this);
		mJumpBt.setOnClickListener(this);
		mRegisterBt.setOnClickListener(this);
	}

	private void initData() {
		mAccountId = mUser.getId();
//		getInitCode();
		mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				mImm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}, 500);
		mPromoteCode = mPromoCodeET.getText().toString().trim();

	}

	/**
	 * 申请 成为推广人 有推广码
	 * 
	 * @param account_id
	 * @param promote_code
	 */
	private void ApplyPromoter(String account_id, String promote_code) {
		NetHelper.setPromoter(account_id, promote_code,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						LogUtils.logd(TAG, "setPromoter有推广码+onSuccess");
						resolvesetPromoter(arg2);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "setPromoter有推广码+onFailure");
					}
				});
	}

	/**
	 * 解析申请推广数据
	 * 
	 * @param arg2
	 */
	protected void resolvesetPromoter(byte[] arg2) {
		try {
			String json = new String(arg2, "UTF-8");
			LogUtils.logd(TAG, "json:" + json);
			JSONObject obj = new JSONObject(json);
			String code = obj.optString("code");
			String msg = obj.optString("msg");
			if ("0".equals(code)) {
				ToastUtils.shortShow(MyApplication.getContext().getString(
						R.string.apply_promoter_ok));
				AndroidUtils.finishActivity(this);
			} else {
				ToastUtils.shortShow(msg);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取默认推广码
	 */
//	private void getInitCode() {
//		NetHelper.getUserCode(new AsyncHttpResponseHandler() {
//			
//			@Override
//			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//				// TODO Auto-generated method stub
//				LogUtils.logd(TAG, "setPromoter有推广码+onSuccess");
//				resolveInitCode(arg2);
//			}
//			
//			@Override
//			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
//				// TODO Auto-generated method stub
//				
//			}
//		});

//	}

	/**
	 * 解析默认推广码
	 * 
	 * @param arg2
	 */
//	private void resolveInitCode(byte[] arg2) {
//		try {
//			String json = new String(arg2, "UTF-8");
//			LogUtils.logd(TAG, "json:" + json);
//			JSONObject obj = new JSONObject(json);
//			String code = obj.optString("code");
//			if ("0".equals(code)) {
//				JSONObject result = obj.optJSONObject("result");
//				if (result!=null) {
//					String promocode = result.optString("code");
//					if (!StringUtils.isEmpty(promocode)) {
//						mPromoCodeET.setText(promocode);
//					}
//				}
//			} 
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		
//	}

}
