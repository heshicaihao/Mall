package com.nettactic.mall.activity;

import java.io.UnsupportedEncodingException;
import java.util.Set;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ta.utdid2.android.utils.StringUtils;
import com.nettactic.mall.R;
import com.nettactic.mall.base.BaseActivity;
import com.nettactic.mall.constants.MyConstants;
import com.nettactic.mall.net.NetHelper;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.FileUtil;
import com.nettactic.mall.utils.LogUtils;
import com.nettactic.mall.utils.SharedpreferncesUtil;
import com.nettactic.mall.utils.ToastUtils;
import com.nettactic.mall.widget.SwitchView;
import com.nettactic.mall.widget.SwitchView.OnStateChangedListener;

/**
 * 
 * 重置密码
 * 
 * @author heshicaihao
 * 
 */
@SuppressLint("HandlerLeak")
@SuppressWarnings("deprecation")
public class ResetPasswordActivity extends BaseActivity implements
		OnClickListener, OnStateChangedListener {

	private TextView mTitle;
	private ImageView mBack;
	private ImageView mAccountDelete;
	private ImageView mPassDelete;
	private ImageView mCodeDelete;
	private EditText mAccount;
	private EditText mPassword;
	private EditText mCode;
	private Button mResetPassword;
	private Button mGetCode;

	private String mPhoneStr;
	private String mAuthcodeStr;
	private String mPasswordStr;

	private TimeCount mTime;
	private InputMethodManager mImm;
	private SwitchView mSwitchView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_password);
		initView();
		initData();
	}

	@Override
	public void onClick(View view) {
		mPhoneStr = mAccount.getText().toString().trim();
		mPasswordStr = mPassword.getText().toString().trim();
		mAuthcodeStr = mCode.getText().toString().trim();

		switch (view.getId()) {
		case R.id.get_code:
			getCode();
			break;
		case R.id.reset_password:
			mImm.hideSoftInputFromWindow(this.getCurrentFocus()
					.getWindowToken(), 0);
			if (AndroidUtils.isNoFastClick()) {
				if (!proofInputInfo()) {
					return;
				}
				resetPW(mPhoneStr, mAuthcodeStr, mPasswordStr);
			}
			break;
		case R.id.account_delete:
			mAccount.setText("");
			mAccountDelete.setVisibility(View.GONE);
			break;
		case R.id.password_delete:
			mPassword.setText("");
			mPassDelete.setVisibility(View.GONE);
			break;
		case R.id.code_delete:
			mCode.setText("");
			mCodeDelete.setVisibility(View.GONE);
			break;
		case R.id.back:
			AndroidUtils.finishActivity(this);
			break;

		default:
			break;
		}
	}

	@Override
	public void toggleToOn() {
		mSwitchView.toggleSwitch(true);
		mPassword.setTransformationMethod(HideReturnsTransformationMethod
				.getInstance());
		mPassword.setSelection(mPasswordStr.length());
	}

	@Override
	public void toggleToOff() {
		mSwitchView.toggleSwitch(false);
		mPassword.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
		mPassword.setSelection(mPasswordStr.length());
	}

	/**
	 * 校对用户输入是否合规
	 */
	private boolean proofInputInfo() {
		if (StringUtils.isEmpty(mPhoneStr)) {
			ToastUtils.show(R.string.please_input_phone_null);
			return false;
		}
		if (!AndroidUtils.isPhoneNumberValid(mPhoneStr)) {
			ToastUtils.show(R.string.please_input_phone_again);
			mAccount.setText("");
			mPhoneStr = null;
			return false;
		}
		if (StringUtils.isEmpty(mAuthcodeStr)) {
			ToastUtils.show(R.string.please_input_code_null);
			return false;
		}
		if (StringUtils.isEmpty(mPasswordStr)) {
			ToastUtils.show(R.string.please_input_password_null);
			return false;
		}

		return true;
	}

	private void resetPW(String phoneStr, String authcode, String pwcode) {
		NetHelper.resetPassword(phoneStr, authcode, pwcode,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						LogUtils.logd(TAG, "register+onSuccess:");
						resolveResetPassword(arg2);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "register+onFailure:");
					}
				});

	}

	private void resolveResetPassword(byte[] arg2) {
		try {
			String json = new String(arg2, "UTF-8");
			LogUtils.logd(TAG, "json:" + json);
			JSONObject obj = new JSONObject(json);
			String code = obj.optString("code");
			String msg = obj.optString("msg");
			if ("0".equals(code)) {
				JSONObject result = obj.optJSONObject("result");
				savaLoginInfo(result);
			} else {
				ToastUtils.show(msg);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void getCode() {
		if (StringUtils.isEmpty(mPhoneStr)) {
			ToastUtils.show(R.string.please_input_phone_null);
			return;
		}
		if (!AndroidUtils.isPhoneNumberValid(mPhoneStr)) {
			ToastUtils.show(R.string.please_input_phone_again);
			mAccount.setText("");
			mPhoneStr = null;
			return;
		}
		NetHelper.forgotSendCode(mPhoneStr, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				LogUtils.logd(TAG, "getCode+onSuccess:");
				resolvegetCode(arg2);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				LogUtils.logd(TAG, "getCode+onFailure:");
				ToastUtils.show(R.string.request_service_failure);
			}
		});
	}

	private void resolvegetCode(byte[] responseBody) {
		try {
			String json = new String(responseBody, "UTF-8");
			LogUtils.logd(TAG, json);
			JSONObject obj = new JSONObject(json);
			String code = obj.optString("code");
			String msg = obj.optString("msg");
			if ("0".equals(code)) {
				ToastUtils.show(R.string.send_code_ok);
				mTime.start();
			} else {
				ToastUtils.show(msg);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void initView() {
		mTime = new TimeCount(60000, 1000);

		mTitle = (TextView) findViewById(R.id.title);
		mAccount = (EditText) findViewById(R.id.account);
		mCode = (EditText) findViewById(R.id.code);
		mPassword = (EditText) findViewById(R.id.password);
		mAccountDelete = (ImageView) findViewById(R.id.account_delete);
		mPassDelete = (ImageView) findViewById(R.id.password_delete);
		mCodeDelete = (ImageView) findViewById(R.id.code_delete);
		mBack = (ImageView) findViewById(R.id.back);
		mResetPassword = (Button) findViewById(R.id.reset_password);
		mGetCode = (Button) findViewById(R.id.get_code);
		mSwitchView = (SwitchView) findViewById(R.id.view_switch);

		mTitle.setText(R.string.reset_password);

		mAccountDelete.setVisibility(View.GONE);
		mPassDelete.setVisibility(View.GONE);
		mCodeDelete.setVisibility(View.GONE);
		setTextChangedListener();

		mAccountDelete.setOnClickListener(this);
		mPassDelete.setOnClickListener(this);
		mCodeDelete.setOnClickListener(this);
		mResetPassword.setOnClickListener(this);
		mGetCode.setOnClickListener(this);
		mBack.setOnClickListener(this);
		mSwitchView.setOnStateChangedListener(this);

	}

	private void initData() {
		mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		// new Timer().schedule(new TimerTask() {
		// @Override
		// public void run() {
		// mImm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		// }
		// }, 500);
	}

	/**
	 * 添加编辑框监听
	 */
	private void setTextChangedListener() {
		mAccount.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				if (arg0.toString().length() > 0) {
					mAccountDelete.setVisibility(View.VISIBLE);
				} else {
					mAccountDelete.setVisibility(View.GONE);
				}
			}
		});
		mCode.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				if (arg0.toString().length() > 0) {
					mCodeDelete.setVisibility(View.VISIBLE);
				} else {
					mCodeDelete.setVisibility(View.GONE);
				}
			}
		});
		mPassword.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				mPasswordStr = mPassword.getText().toString().trim();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				if (arg0.toString().length() > 0) {
					mPassDelete.setVisibility(View.VISIBLE);
				} else {
					mPassDelete.setVisibility(View.GONE);
				}
			}
		});
	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			mGetCode.setText(getString(R.string.send_code));
			mGetCode.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			mGetCode.setClickable(false);
			mGetCode.setText(millisUntilFinished / 1000
					+ getString(R.string.get_code_s));
		}
	}

	/**
	 * 登录成功后，保存用户信息
	 * 
	 * @param result
	 */
	private void savaLoginInfo(JSONObject result) {

		//
		JSONObject accountinfo = result.optJSONObject("account_info");
		String token = accountinfo.optString("token");
		String id = accountinfo.optString("id");

		user.setUname(mPhoneStr);
		user.setPassword(mPasswordStr);
		user.setId(id);
		user.setToken(token);
		user.setIs_login(true);
		user.setType("");
		user.setOpen_id("");
		user.setUsername("");
		user.setIs_three_login(false);

		if (!SharedpreferncesUtil.getAlias(getApplication())) {
			LogUtils.logd(TAG, id);
			JPushInterface.setAliasAndTags(getApplicationContext(), id, null,
					new TagAliasCallback() {

						@Override
						public void gotResult(int code, String alias,
								Set<String> tags) {
						}

					});
		}
		getOrderInfo();
	}

	private void getOrderInfo() {
		LogUtils.logd(TAG, "user.getId()" + user.getId());
		LogUtils.logd(TAG, "user.getToken()" + user.getToken());

		NetHelper.getOrderList(user.getId(), user.getToken(), "all", 1, 5,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						LogUtils.logd(TAG, "getOrderList+onSuccess");
						resolveOrderList(arg2);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "getOrderList+onFailure");
					}
				});
	}

	private void resolveOrderList(byte[] arg2) {
		try {
			String json = new String(arg2, "UTF-8");
			LogUtils.logd(TAG, "arr== nulljson:" + json);
			JSONObject obj = new JSONObject(json);
			JSONObject result = obj.optJSONObject("result");
			JSONArray arr = result.optJSONArray("orderData");
			if (arr != null && arr.length() != 0) {
				FileUtil.saveFile(arr.toString(), MyConstants.ORDERLIST,
						MyConstants.ORDER_LIST_INFO, MyConstants.TXT);
				SharedpreferncesUtil.setOrder(getApplication(), true);
			} else {
				SharedpreferncesUtil.setOrder(getApplication(), false);
			}
			mUserController.saveUserInfo(user);
			ToastUtils.show(R.string.reset_password_ok);
			finish();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}