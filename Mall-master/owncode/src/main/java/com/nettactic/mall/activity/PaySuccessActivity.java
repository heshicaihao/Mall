package com.nettactic.mall.activity;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nettactic.mall.R;
import com.nettactic.mall.WLMainActivity;
import com.nettactic.mall.base.BaseActivity;
import com.nettactic.mall.bean.UserBean;
import com.nettactic.mall.cache.ACache;
import com.nettactic.mall.common.MyApplication;
import com.nettactic.mall.common.UserController;
import com.nettactic.mall.constants.MyConstants;
import com.nettactic.mall.net.NetHelper;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.JSONUtil;
import com.nettactic.mall.utils.LogUtils;
import com.nettactic.mall.utils.StringUtils;
import com.nettactic.mall.utils.ToastUtils;

/**
 * 
 * 支付成功界面
 * 
 * @author heshicaihao
 * 
 */
@SuppressWarnings("deprecation")
public class PaySuccessActivity extends BaseActivity implements OnClickListener {

	private TextView mTitle;
	private ImageView mBack;
	private TextView mPayModeValue;
	private TextView mPayMoney;
	private TextView mDepositPayName;
	private TextView mDepositPayValue;
	private TextView mOtherPayName;
	private TextView mOtherPayValue;
	private LinearLayout mDepositPayLL;
	private LinearLayout mOtherPayLL;
	private Button mGotoHome;
	private Button mOrderIdLL;
	private String mOutTradeNo;

	private ACache mCache;
	private UserBean mUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_success);
		initView();
		initData();
	}

	private void initView() {
		mBack = (ImageView) findViewById(R.id.back);
		mTitle = (TextView) findViewById(R.id.title);
		mPayModeValue = (TextView) findViewById(R.id.pay_mode_value);
		mPayMoney = (TextView) findViewById(R.id.pay_value);
		mGotoHome = (Button) findViewById(R.id.goto_home);
		mOrderIdLL = (Button) findViewById(R.id.order_id_ll);

		mDepositPayName = (TextView) findViewById(R.id.deposit_pay_name);
		mDepositPayValue = (TextView) findViewById(R.id.deposit_pay_value);
		mOtherPayName = (TextView) findViewById(R.id.other_pay_name);
		mOtherPayValue = (TextView) findViewById(R.id.other_pay_value);
		mDepositPayLL = (LinearLayout) findViewById(R.id.deposit_pay_ll);
		mOtherPayLL = (LinearLayout) findViewById(R.id.other_pay_ll);

		mTitle.setText(R.string.pay_success);
		mOrderIdLL.setOnClickListener(this);
		mGotoHome.setOnClickListener(this);
		mBack.setOnClickListener(this);

	}

	private void initData() {
		mCache = ACache.get(this);
		mOutTradeNo = mCache.getAsString(MyConstants.ORDERID);
		mUser = UserController.getInstance(this).getUserInfo();

		getOrderInfo();

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.goto_home:
			gotoMainActivity();
			break;
		case R.id.order_id_ll:
			gotoOrderDetailsActivity();
			break;
		case R.id.back:
			AndroidUtils.finishActivity(this);
			break;

		default:
			break;
		}
	}

	private void gotoMainActivity() {
		Intent intent = new Intent(this, WLMainActivity.class);
		startActivity(intent);
		AndroidUtils.enterActvityAnim(this);
	}

	private void gotoOrderDetailsActivity() {
		Intent intent = new Intent(this, OrderDetailsActivity.class);
		intent.putExtra("mOutTradeNo", mOutTradeNo);
		startActivity(intent);
		AndroidUtils.enterActvityAnim(this);
	}

	private void getOrderInfo() {

		NetHelper.getOrderDetail(mUser.getId(), mUser.getToken(), mOutTradeNo,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						LogUtils.logd(TAG, "onSuccess");
						resolveOrderDetail(arg2);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "onFailure");
					}
				});

	}

	private void resolveOrderDetail(byte[] responseBody) {
		try {
			String json = new String(responseBody, "UTF-8");
			LogUtils.logd(TAG, "json:" + json);
			JSONObject result = JSONUtil.resolveResult(responseBody);
			if (result != null) {
				JSONObject order = result.optJSONObject("order");
				showInfo(order);
			} else {
				JSONObject JSONObject = new JSONObject(json);
				String msg = JSONObject.optString("msg");
				ToastUtils.show(msg);
				AndroidUtils.finishActivity(this);
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void showInfo(JSONObject order) throws JSONException {
		JSONObject total = order.optJSONObject("total");
		String mTotalStr = total.optString("total_amount");
		String total_amount2p = StringUtils.format2point(mTotalStr);
		String totalmoneyStr = total_amount2p
				+ MyApplication.getContext().getString(R.string.yuan);

		mPayMoney.setText(totalmoneyStr);
		String paymodeStr = "";
		String deposit_pay_name = "";
		String deposit_pay_value = "";
		String other_pay_name = "";
		String other_pay_value = "";
		JSONObject deposit_pay = order.optJSONObject("other_pay");
		if (deposit_pay != null) {
			deposit_pay_name = deposit_pay.optString("payname");
			String deposit_pay_valueStr = deposit_pay.optString("amount");
			deposit_pay_value = StringUtils.format2point(deposit_pay_valueStr)
					+ MyApplication.getContext().getString(R.string.yuan);
		}

		JSONObject other_pay = order.optJSONObject("payinfo");
		if (other_pay != null) {
			other_pay_name = other_pay.optString("payname");
			String other_pay_valueStr = other_pay.optString("amount");
			other_pay_value = StringUtils.format2point(other_pay_valueStr)
					+ MyApplication.getContext().getString(R.string.yuan);
		}

		if (!StringUtils.isEmpty(deposit_pay_name)
				&& StringUtils.isEmpty(other_pay_name)) {

			paymodeStr = deposit_pay_name;
			mDepositPayName.setText(deposit_pay_name);
			mDepositPayValue.setText(deposit_pay_value);
			mDepositPayLL.setVisibility(View.VISIBLE);

			mOtherPayLL.setVisibility(View.GONE);

			mPayModeValue.setText(paymodeStr);
			mPayModeValue.setVisibility(View.VISIBLE);
		} else if (StringUtils.isEmpty(deposit_pay_name)
				&& !StringUtils.isEmpty(other_pay_name)) {

			paymodeStr = other_pay_name;
			mOtherPayName.setText(other_pay_name);
			mOtherPayValue.setText(other_pay_value);
			mOtherPayLL.setVisibility(View.VISIBLE);

			mDepositPayLL.setVisibility(View.GONE);

			mPayModeValue.setText(paymodeStr);
			mPayModeValue.setVisibility(View.VISIBLE);
		} else if (!StringUtils.isEmpty(deposit_pay_name)
				&& !StringUtils.isEmpty(other_pay_name)) {

			paymodeStr = deposit_pay_name + "+" + other_pay_name;
			mDepositPayName.setText(deposit_pay_name);
			mDepositPayValue.setText(deposit_pay_value);
			mDepositPayLL.setVisibility(View.VISIBLE);

			mOtherPayName.setText(other_pay_name);
			mOtherPayValue.setText(other_pay_value);
			mOtherPayLL.setVisibility(View.VISIBLE);

			mPayModeValue.setText(paymodeStr);
			mPayModeValue.setVisibility(View.VISIBLE);
		} else if (StringUtils.isEmpty(deposit_pay_name)
				&& StringUtils.isEmpty(other_pay_name)) {
			mDepositPayLL.setVisibility(View.GONE);
			mOtherPayLL.setVisibility(View.GONE);
			mPayModeValue.setVisibility(View.GONE);

		}
	}

}
