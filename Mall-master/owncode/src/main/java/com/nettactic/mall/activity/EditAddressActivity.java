package com.nettactic.mall.activity;

import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ta.utdid2.android.utils.StringUtils;
import com.nettactic.mall.R;
import com.nettactic.mall.base.BaseActivity;
import com.nettactic.mall.constants.MyConstants;
import com.nettactic.mall.dialog.SelectAreaDialog;
import com.nettactic.mall.dialog.SelectAreaDialog.SelectCallBack;
import com.nettactic.mall.net.NetHelper;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.FileUtil;
import com.nettactic.mall.utils.LogUtils;
import com.nettactic.mall.utils.ToastUtils;

/**
 * 编辑地址
 * 
 * @author heshicaihao
 * 
 */
@SuppressWarnings("deprecation")
public class EditAddressActivity extends BaseActivity implements
		OnClickListener {

	private boolean mIsDefault = false;
	private boolean mIsNoAddress = false;
	private boolean mIsEditAddress = false;
	private String mNameStr;
	private String mPhoneStr;
	private String mHomeTelStr;
	private String mPostcodeStr;
	private String mAreaStr;
	private String mAreaNetStr;
	private String mDetaileAreaStr;
	private String mAccountId;
	private String mAccountToken;
	private String mShipId;
	private String[] mShipIdArr = new String[1];

	private LinearLayout mAreaLL;
	private RelativeLayout mEditRL;
	private ImageView mBack;
	private TextView mTitle;
	private TextView mSave;
	private TextView mArea;
	private TextView mDelete;
	private TextView mIsDefaultTV;
	private EditText mName;
	private EditText mPhone;
	private EditText mPostcode;
	private EditText mDetaileArea;
	private InputMethodManager mImm;
	private String mAreaData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_address);

		initView();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setIsDefault();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.area:
			SelectAreaDialogShow();
			
			break;
		case R.id.save:
			mImm.hideSoftInputFromWindow(this.getCurrentFocus()
					.getWindowToken(), 0);
			if (AndroidUtils.isNoFastClick()) {
				getInfoInput();
				if (mIsEditAddress) {
					updateAddress();
				} else {
					addAddress2Net();
				}
			}
			break;
		case R.id.delete_text:
			mImm.hideSoftInputFromWindow(this.getCurrentFocus()
					.getWindowToken(), 0);
			doDeleteAddress();

			break;
		case R.id.default_address:
			mImm.hideSoftInputFromWindow(this.getCurrentFocus()
					.getWindowToken(), 0);
			if (!mIsDefault) {
				setAddressDefault();
			}
			break;
		case R.id.back:
			AndroidUtils.finishActivity(this);
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * 显示选择地址对话框
	 */
	private void SelectAreaDialogShow() {
		SelectAreaDialog dialog = new SelectAreaDialog(this,
				mAreaData);
		dialog.onCreateDialog();
		dialog.setCallBack(new SelectCallBack() {
			@Override
			public void isConfirm(String callBackData) {
				mAreaNetStr = callBackData ;
				String[] arr = mAreaNetStr.split("\\:");
				String arr1 = arr[1];
				mAreaStr = arr1.replaceAll("/", " ");
				mArea.setText(mAreaStr);
			}
		});
	}

	private void initView() {

		mTitle = (TextView) findViewById(R.id.title);
		mBack = (ImageView) findViewById(R.id.back);
		mSave = (TextView) findViewById(R.id.save);
		mArea = (TextView) findViewById(R.id.area_edit);
		mDelete = (TextView) findViewById(R.id.delete_text);
		mIsDefaultTV = (TextView) findViewById(R.id.default_address);
		mName = (EditText) findViewById(R.id.name_edit);
		mPhone = (EditText) findViewById(R.id.phone_edit);
		mPostcode = (EditText) findViewById(R.id.postcode_edit);
		mDetaileArea = (EditText) findViewById(R.id.detailed_area_edit);
		mEditRL = (RelativeLayout) findViewById(R.id.edit_rl);
		mAreaLL = (LinearLayout) findViewById(R.id.area);

		setTextListener();
		mTitle.setText(R.string.add_address);
		mSave.setText(R.string.save);
		mSave.setVisibility(View.VISIBLE);
		mSave.setOnClickListener(this);
		mBack.setOnClickListener(this);
		mDelete.setOnClickListener(this);
		mAreaLL.setOnClickListener(this);
		mIsDefaultTV.setOnClickListener(this);
	}

	private void initData() {
		getDataIntent();
		mAccountId = mUserController.getUserInfo().getId();
		mAccountToken = mUserController.getUserInfo().getToken();
		mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		initAreaViewData();
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				mImm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}, 500);
	}

	private void getDataIntent() {
		Intent intent = getIntent();
		mIsNoAddress = intent.getBooleanExtra("mIsNoAddress", false);
		mIsEditAddress = intent.getBooleanExtra("mIsEditAddress", false);
		if (mIsEditAddress) {
			mEditRL.setVisibility(View.VISIBLE);
			mTitle.setText(R.string.revise_address);
		} else {
			mEditRL.setVisibility(View.GONE);
		}
		String data = intent.getStringExtra("data");
		if (data != null) {
			JSONObject obj;
			try {
				obj = new JSONObject(data);
				showAddress(obj);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private void initAreaViewData() {
		mAreaData = FileUtil.readFile(MyConstants.AREA_DATA_DIR,
				MyConstants.AREA_DATA, MyConstants.TXT);

	}

	private void showAddress(JSONObject object) throws JSONException {
		String ship_name = object.optString("ship_name");
		String ship_mobile = object.optString("ship_mobile");
		String ship_zip = object.optString("ship_zip");
		mAreaNetStr = object.optString("ship_area");
		String[] arr = mAreaNetStr.split("\\:");
		String arr1 = arr[1];
		String ship_area = arr1.replaceAll("/", " ");
		// String mCode = arr[2];
		String ship_addr = object.optString("ship_addr");
		mShipId = object.optString("ship_id");
		mIsDefault = object.optBoolean("is_default");
		mShipIdArr[0] = mShipId;
		mName.setText(ship_name);
		mPhone.setText(ship_mobile);
		mPostcode.setText(ship_zip);
		mArea.setText(ship_area);
		mDetaileArea.setText(ship_addr);
		setIsDefault();
	}

	private void setIsDefault() {
		if (mIsDefault) {
			mIsDefaultTV.setText(getString(R.string.default_address_ed));
			// mIsDefaultTV.setTextColor(android.graphics.Color
			// .parseColor("#9e9e9e"));
			Drawable drawable = getResources().getDrawable(
					R.mipmap.default_address_normal);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			mIsDefaultTV.setCompoundDrawables(drawable, null, null, null);
		} else {
			mIsDefaultTV.setText(getString(R.string.setting_default_address));
			// mIsDefaultTV.setTextColor(android.graphics.Color
			// .parseColor("#E34250"));
			Drawable drawable = getResources().getDrawable(
					R.mipmap.default_address_pressed);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			mIsDefaultTV.setCompoundDrawables(drawable, null, null, null);
		}
	}

	private void getInfoInput() {

		mNameStr = mName.getText().toString().trim();
		mPhoneStr = mPhone.getText().toString().trim();
		mHomeTelStr = "";
		mPostcodeStr = mPostcode.getText().toString().trim();
		mAreaStr = mArea.getText().toString().trim();
		mDetaileAreaStr = mDetaileArea.getText().toString().trim();
		if (!mIsEditAddress) {
			mIsDefault = true;
		}

	}

	private void setTextListener() {
		mName.addTextChangedListener(new TextWatcher() {
			String tmp = "";
			String digits = "/\\:*?<>|\"\n\t";

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mName.setSelection(s.length());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				tmp = s.toString();
			}

			@Override
			public void afterTextChanged(Editable s) {
				String str = s.toString();
				if (str.equals(tmp)) {
					return;
				}
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < str.length(); i++) {
					if (digits.indexOf(str.charAt(i)) < 0) {
						sb.append(str.charAt(i));
					}
				}
				tmp = sb.toString();
				mName.setText(tmp);
			}
		});
	}

	private void goBackResult() {
		String data = getData("", mPhoneStr, mNameStr, mHomeTelStr, "true",
				mDetaileAreaStr, mAreaNetStr, mPostcodeStr);
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("address", data);
		this.setResult(SubmitOrderActivity.RESULT_OK, intent);

	}

	private String getData(String ship_id, String ship_mobile,
			String ship_name, String ship_tel, String is_default,
			String ship_addr, String ship_area, String ship_zip) {
		JSONObject object = new JSONObject();
		try {
			object.put("ship_id", ship_id);
			object.put("ship_mobile", ship_mobile);
			object.put("ship_name", ship_name);
			object.put("ship_tel", ship_tel);
			object.put("is_default", is_default);
			object.put("ship_addr", ship_addr);
			object.put("ship_area", ship_area);
			object.put("ship_zip", ship_zip);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String data = object.toString();
		return data;
	}

	/**
	 * 设置 默认地址
	 */
	private void setAddressDefault() {
		NetHelper.setDefault(mAccountId, mAccountToken, mShipId,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						LogUtils.logd(TAG, "setDefault+Success");
						resolvSetDefault(responseBody);

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "setDefault+Failure");

					}
				});
	}

	private void resolvSetDefault(byte[] responseBody) {
		try {
			String json = new String(responseBody, "UTF-8");
			LogUtils.logd(TAG, "resolvSetDefault+json:" + json);
			JSONObject JSONObject = new JSONObject(json);
			String code = JSONObject.optString("code");
			String msg = JSONObject.optString("msg");
			if ("0".equals(code)) {
				ToastUtils.show(R.string.set_default_address_success);
				mIsDefaultTV.setText(getString(R.string.default_address_ed));
				Drawable drawable = getResources().getDrawable(
						R.mipmap.default_address_normal);
				// / 这一步必须要做,否则不会显示.
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				mIsDefaultTV.setCompoundDrawables(drawable, null, null, null);
				mIsDefault = true;
			} else {
				ToastUtils.show(msg);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 删除地址
	 */
	private void doDeleteAddress() {
		LogUtils.logd(TAG, "mAccountId:" + mAccountId + ",mAccountToken"
				+ mAccountToken + ",mShipIdArr:" + mShipId);
		NetHelper.deleteAddress(mAccountId, mAccountToken, mShipIdArr,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						LogUtils.logd(TAG, "initGetAddressListonSuccess");
						resolvEdeleteAddress(responseBody);

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "initGetAddressListonFailure");
					}
				});
	}

	private void resolvEdeleteAddress(byte[] responseBody) {
		try {
			String json = new String(responseBody, "UTF-8");
			LogUtils.logd(TAG, "EdeleteAddress+json:" + json);
			JSONObject JSONObject = new JSONObject(json);
			String code = JSONObject.optString("code");
			String msg = JSONObject.optString("msg");
			if ("0".equals(code)) {
				ToastUtils.show(R.string.delete_address_success);
				Drawable drawable = getResources().getDrawable(
						R.mipmap.delete_address_normal);
				// / 这一步必须要做,否则不会显示.
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				mDelete.setCompoundDrawables(drawable, null, null, null);
				AndroidUtils.finishActivity(EditAddressActivity.this);
			} else {
				ToastUtils.show(msg);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 修改地址
	 */
	private void updateAddress() {
		NetHelper.reviseAddress(mAccountId, mAccountToken, mShipId, mNameStr,
				mAreaNetStr, mDetaileAreaStr, mPostcodeStr, mHomeTelStr,
				mPhoneStr, mIsDefault, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						LogUtils.logd(TAG, "reviseAddress+onSuccess:");
						resolveUpdateAddress(arg2);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "reviseAddress+onFailure:");

					}
				});
	}

	/**
	 * 新增地址
	 */
	private void addAddress2Net() {
		if (StringUtils.isEmpty(mNameStr)) {
			ToastUtils.show(R.string.please_input_name);
			LogUtils.logd(TAG, "mNameStr" + "Null");
			return;
		}
		if (StringUtils.isEmpty(mPhoneStr)) {
			ToastUtils.show(R.string.please_input_phone);
			LogUtils.logd(TAG, "mPhoneStr" + "Null");
			return;
		}
		if (!AndroidUtils.isPhoneNumberValid(mPhoneStr)) {
			ToastUtils.show(R.string.please_input_phone_again);
			mPhone.setText("");
			mPhoneStr = null;
			return;
		}
		if (StringUtils.isEmpty(mAreaStr)) {
			ToastUtils.show(R.string.please_input_area);
			LogUtils.logd(TAG, "mAreaStr" + "Null");
			return;
		}
		if (StringUtils.isEmpty(mDetaileAreaStr)) {
			ToastUtils.show(R.string.please_input_detaile_area);
			LogUtils.logd(TAG, "mDetaileAreaStr" + "Null");
			return;
		}
		// LogUtils.logd(TAG, "mAccountId:"+mAccountId
		// +",mAccountToken:"+ mAccountToken
		// +",mNameStr:"+mNameStr
		// +",mAreaNetStr:"+mAreaNetStr
		// +",mDetaileAreaStr:"+mDetaileAreaStr
		// +",mPostcodeStr:"+mPostcodeStr
		// +",mHomeTelStr:"+mHomeTelStr
		// +",mPhoneStr:"+mPhoneStr
		// +",mIsDefault:"+mIsDefault );
		NetHelper.addAddress(mAccountId, mAccountToken, mNameStr, mAreaNetStr,
				mDetaileAreaStr, mPostcodeStr, mHomeTelStr, mPhoneStr,
				mIsDefault, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						LogUtils.logd(TAG, "addAddress2Net+onSuccess:");
						resolveaddAddress2Net(arg2);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "addAddress2Net+onFailure:");

					}
				});
	}

	private void resolveUpdateAddress(byte[] arg2) {
		try {
			String json = new String(arg2, "UTF-8");
			LogUtils.logd(TAG, "json:" + json);
			JSONObject JSONObject = new JSONObject(json);
			String code = JSONObject.optString("code");
			String msg = JSONObject.optString("msg");
			if ("0".equals(code)) {
				ToastUtils.show(R.string.add_address_success);
				AndroidUtils.finishActivity(EditAddressActivity.this);
			} else {
				ToastUtils.show(msg);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void resolveaddAddress2Net(byte[] arg2) {
		try {
			String json = new String(arg2, "UTF-8");
			LogUtils.logd(TAG, "json:" + json);
			JSONObject JSONObject = new JSONObject(json);
			String code = JSONObject.optString("code");
			String msg = JSONObject.optString("msg");
			if ("0".equals(code)) {
				ToastUtils.show(R.string.add_address_success);
				if (mIsNoAddress) {
					goBackResult();
				}
				AndroidUtils.finishActivity(EditAddressActivity.this);
			} else {
				ToastUtils.show(msg);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
