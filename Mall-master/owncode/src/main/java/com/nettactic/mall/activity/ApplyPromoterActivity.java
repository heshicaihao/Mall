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
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ta.utdid2.android.utils.StringUtils;
import com.nettactic.mall.R;
import com.nettactic.mall.base.BaseActivity;
import com.nettactic.mall.bean.UserBean;
import com.nettactic.mall.common.MyApplication;
import com.nettactic.mall.common.UserController;
import com.nettactic.mall.net.NetHelper;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.LogUtils;
import com.nettactic.mall.utils.ToastUtils;

/**
 * 成为推广人
 * 
 * @author heshicaihao
 * 
 */
@SuppressWarnings("deprecation")
public class ApplyPromoterActivity extends BaseActivity implements
		OnClickListener, OnCheckedChangeListener {

	private ImageView mBack;
	private TextView mTitle;
	private RadioGroup mRGroup;
	private TextView mStep01;
	private TextView mStep02;
	private TextView mStep03;
	private EditText mPromoCodeET;
	private Button mSubmitBt;
	private Boolean mIsExistCode = true;
	private UserBean mUser;
	private String mAccountId;
	private String mPromoteCode;
	private RelativeLayout mViewWaitRl;
	private Button mViewWaitBackBt;
	private InputMethodManager mImm;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apply_promoter);
		mUser = UserController.getInstance(this).getUserInfo();
		initView();
		initData();

	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {

		case R.id.back:
			AndroidUtils.finishActivity(this);
			break;
			
		case R.id.back_bt:
			mImm.hideSoftInputFromWindow(this.getCurrentFocus()
					.getWindowToken(), 0);
			mViewWaitRl.setVisibility(View.GONE);
//			AndroidUtils.finishActivity(this);
			break;
			
		case R.id.submit:
			ApplyPromoter();
			break;
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		switch (arg1) {
		case R.id.exist_promo_code:
			changeView(R.string.fill_in_promo_code, R.string.order_ok,
					R.string.apply_promoter, R.string.submit_ok, View.VISIBLE);
			mIsExistCode = true;
			break;

		case R.id.no_promo_code:
			changeView(R.string.submit_apply, R.string.reviewed_ok,
					R.string.apply_promoter, R.string.now_apply, View.GONE);
			mIsExistCode = false;
			break;

		default:
			break;
		}
	}

	/**
	 * 切换UI
	 * 
	 * @param step01
	 * @param step02
	 * @param step03
	 * @param submit
	 * @param promoview
	 */
	private void changeView(int step01, int step02, int step03, int submit,
			int promoview) {
		mStep01.setText(MyApplication.getContext().getString(step01));
		mStep02.setText(MyApplication.getContext().getString(step02));
		mStep03.setText(MyApplication.getContext().getString(step03));
		mSubmitBt.setText(MyApplication.getContext().getString(submit));
		mPromoCodeET.setVisibility(promoview);
	}

	private void initView() {
		mBack = (ImageView) findViewById(R.id.back);
		mTitle = (TextView) findViewById(R.id.title);
		mRGroup = (RadioGroup) findViewById(R.id.radiogroup);
		mStep01 = (TextView) findViewById(R.id.step01);
		mStep02 = (TextView) findViewById(R.id.step02);
		mStep03 = (TextView) findViewById(R.id.step03);
		mPromoCodeET = (EditText) findViewById(R.id.promo_code_edit_text);
		mSubmitBt = (Button) findViewById(R.id.submit);
		mViewWaitRl  = (RelativeLayout)findViewById(R.id.view_wait_rl);
		mViewWaitBackBt =(Button) findViewById(R.id.back_bt);

		mTitle.setText(R.string.apply_promoter);
		mRGroup.setOnCheckedChangeListener(this);
		mSubmitBt.setOnClickListener(this);
		mBack.setOnClickListener(this);
		mViewWaitBackBt.setOnClickListener(this);
	}

	private void initData() {
		mAccountId = mUser.getId();
		mPromoteCode = mPromoCodeET.getText().toString().trim();
		mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				mImm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}, 500);
	}

	/**
	 * 申请成为推广人
	 */
	private void ApplyPromoter() {
		mAccountId = mUser.getId();
		LogUtils.logd(TAG, "mAccountId:" + mAccountId);
		if (mIsExistCode) {
			mPromoteCode = mPromoCodeET.getText().toString().trim();
			if (StringUtils.isEmpty(mPromoteCode)) {
				ToastUtils.show(R.string.please_input_promote_code_null);
				return;
			}
			ApplyPromoter(mAccountId, mPromoteCode);
		} else {
			ApplyPromoter(mAccountId);
		}
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
	 * 申请 成为推广人 无推广码
	 * 
	 * @param account_id
	 */
	private void ApplyPromoter(String account_id) {
		NetHelper.setPromoter(account_id, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				LogUtils.logd(TAG, "setPromoter无推广码+onSuccess");
				resolvesetPromoter(arg2);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				LogUtils.logd(TAG, "setPromoter无推广码+onFailure");
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
//				ToastUtils.shortShow(MyApplication.getContext().getString(
//						R.string.apply_promoter_ok));
				mViewWaitRl.setVisibility(View.VISIBLE);
			} else {
				mViewWaitRl.setVisibility(View.GONE);
				ToastUtils.shortShow(msg);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
