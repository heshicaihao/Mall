package com.nettactic.mall.activity;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nettactic.mall.R;
import com.nettactic.mall.WLMainActivity;
import com.nettactic.mall.adapter.OrderDetailsAdapter;
import com.nettactic.mall.base.BaseActivity;
import com.nettactic.mall.bean.UserBean;
import com.nettactic.mall.common.MyApplication;
import com.nettactic.mall.common.UserController;
import com.nettactic.mall.dialog.CustomDialog;
import com.nettactic.mall.net.NetHelper;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.JSONUtil;
import com.nettactic.mall.utils.LogUtils;
import com.nettactic.mall.utils.StringUtils;
import com.nettactic.mall.utils.ToastUtils;
import com.nettactic.mall.widget.SildingFinishLayout;
import com.nettactic.mall.widget.SildingFinishLayout.OnSildingFinishListener;

/**
 * 作品详情页
 * 
 * @author heshicaihao
 * 
 */
@SuppressWarnings("deprecation")
public class OrderDetailsActivity extends BaseActivity implements
		OnClickListener {

	private TextView mTitle;
	private ImageView mBack;
	private String mOutTradeNo;
	private String mTotalStr;
	private ListView mListView;
	private TextView mOrderId;
	private TextView mOrderState;
	private TextView mOrderTime;
	private TextView mFreightMoney;
	private TextView mGoodsMoney;
	private TextView mTotalMoney;
	private TextView mConsignee;
	private TextView mConsigneePhone;
	private TextView mShippingAddress;
	private TextView mCancelOrder;
	private TextView mPay;
	private LinearLayout mBottomLL;

	private UserBean mUser;
	public View mHeader;
	public View mFooter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_details);
		initView();
		initData();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			gotoMainActivity();
			break;
		case R.id.cancel_order_btn:
			showDialog(this);
			break;
		case R.id.pay_btn:
			gotoOrderPayActivity();
			break;
		default:
			break;
		}
	}

	private void initView() {
		mTitle = (TextView) findViewById(R.id.title);
		mBack = (ImageView) findViewById(R.id.back);
		mCancelOrder = (TextView) findViewById(R.id.cancel_order_btn);
		mPay = (TextView) findViewById(R.id.pay_btn);
		mBottomLL = (LinearLayout) findViewById(R.id.bottom_ll);
		mListView = (ListView) findViewById(R.id.listview);
		mHeader = getLayoutInflater().inflate(
				R.layout.view_header_order_details, null);
		getLayoutInflater();
		mFooter = LayoutInflater.from(this).inflate(
				R.layout.view_footer_order_details, null);
		mListView.addHeaderView(mHeader, null, true);
		mListView.addFooterView(mFooter, null, true);
		// 显示头部出现分割线
		mListView.setHeaderDividersEnabled(false);
		// 禁止底部出现分割线
		mListView.setFooterDividersEnabled(false);
		mOrderState = (TextView) mHeader.findViewById(R.id.order_state);
		mOrderId = (TextView) mHeader.findViewById(R.id.order_id);
		mOrderTime = (TextView) mHeader.findViewById(R.id.order_time);
		mConsignee = (TextView) mHeader.findViewById(R.id.consignee);
		mConsigneePhone = (TextView) mHeader.findViewById(R.id.consignee_phone);
		mShippingAddress = (TextView) mHeader
				.findViewById(R.id.shipping_address);

		mGoodsMoney = (TextView) mFooter.findViewById(R.id.goods_money);
		mFreightMoney = (TextView) mFooter.findViewById(R.id.freight_money);
		mTotalMoney = (TextView) mFooter.findViewById(R.id.real_payment);

		setSildingFinish();
		mTitle.setText(R.string.order_details);
		mBack.setOnClickListener(this);
		mCancelOrder.setOnClickListener(this);
		mPay.setOnClickListener(this);
	}

	private void initData() {
		mUser = UserController.getInstance(this).getUserInfo();
		getDataIntent();
		getOrderInfo();
	}

	/**
	 * 设置右划退出
	 */
	private void setSildingFinish() {
		SildingFinishLayout mSildingFinishLayout = (SildingFinishLayout) findViewById(R.id.sildingFinishLayout);
		mSildingFinishLayout
				.setOnSildingFinishListener(new OnSildingFinishListener() {

					@Override
					public void onSildingFinish() {
						OrderDetailsActivity.this.finish();
					}
				});

		mSildingFinishLayout.setTouchView(mListView);
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
				showOrderInfo(order);
				setListAdapter(order);
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

	private void setListAdapter(JSONObject order) throws JSONException {
		JSONObject goodsinfo = order.getJSONObject("goodsinfo");
		JSONArray goods = goodsinfo.getJSONArray("goods");

		OrderDetailsAdapter mAdapter = new OrderDetailsAdapter(this, this,
				goods);
		mListView.setAdapter(mAdapter);
	}

	private void showOrderInfo(JSONObject order) throws JSONException {
		// 订单信息
		mOutTradeNo = order.optString("order_id");
		mOrderId.setText(mOutTradeNo);
		String createtimeStr = order.optString("createtime");
		long createtime = Long.parseLong(createtimeStr);
		String time = StringUtils.longToDate(createtime);
		mOrderTime.setText(time);
		String order_status = order.optString("orderStatus");
		String pay_status = order.optString("payStatus");
		if ("3".equals(order_status)) {
			mOrderState.setText(getString(R.string.cancelled));
			mBottomLL.setVisibility(View.GONE);
		} else {
			if ("0".equals(pay_status)) {
				mOrderState.setText(getString(R.string.no_pay));
				mBottomLL.setVisibility(View.VISIBLE);
			} else if ("1".equals(pay_status)) {
				mOrderState.setText(getString(R.string.paymented));
				mBottomLL.setVisibility(View.GONE);
			}
		}
		// 地址信息
		JSONObject consignee = order.optJSONObject("consignee");
		String name = consignee.optString("name");
		mConsignee.setText(name);

		String mobile = consignee.optString("mobile");
		mConsigneePhone.setText(mobile);

		String area = consignee.optString("area");
		String addr = consignee.optString("addr");
		mShippingAddress.setText(area + addr);

		// 货款信息
		JSONObject total = order.optJSONObject("total");
		String real_amount = total.optString("real_amount");
		String real_amount2p = StringUtils.format2point(real_amount);
		String goodsmoneyStr = real_amount2p
				+ MyApplication.getContext().getString(R.string.yuan);
		mGoodsMoney.setText(goodsmoneyStr);

		String cost_freight = total.optString("cost_freight");
		String cost_freight2p = StringUtils.format2point(cost_freight);
		String freightmoneyStr = MyApplication.getContext().getString(
				R.string.add)
				+ cost_freight2p
				+ MyApplication.getContext().getString(R.string.yuan);
		mFreightMoney.setText(freightmoneyStr);

		mTotalStr = total.optString("total_amount");
		String total_amount2p = StringUtils.format2point(mTotalStr);
		String totalmoneyStr = total_amount2p
				+ MyApplication.getContext().getString(R.string.yuan);
		mTotalMoney.setText(totalmoneyStr);

	}

	private void getDataIntent() {
		Intent intent = getIntent();
		mOutTradeNo = intent.getStringExtra("mOutTradeNo");
	}

	private void gotoMainActivity() {
		int CurrentTabNum = 2;
		Intent intent = new Intent(this, WLMainActivity.class);
		intent.putExtra("CurrentTabNum", CurrentTabNum);
		startActivity(intent);
		AndroidUtils.finishActivity(this);
	}

	private void gotoOrderPayActivity() {
		Intent intent = new Intent(this, OrderPayActivity.class);
		intent.putExtra("mTotalStr", mTotalStr);
		intent.putExtra("mGoodsNameStr", "");
		intent.putExtra("mGoodsInfoStr", "");
		intent.putExtra("mOutTradeNo", mOutTradeNo);

		startActivity(intent);
		AndroidUtils.enterActvityAnim(this);
	}

	/**
	 * 显示对话框
	 * 
	 * @param mContext
	 */
	private void showDialog(Context mContext) {
		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		builder.setMessage(mContext.getString(R.string.no_way_change));
		builder.setPositiveButton(mContext.getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (AndroidUtils.isNoFastClick()) {
							dialog.dismiss();

						}
					}
				});

		builder.setNegativeButton(mContext.getString(R.string.ok),
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (AndroidUtils.isNoFastClick()) {
							dialog.dismiss();
							netCancelOrder();
						}
					}
				});

		builder.create().show();
	}

	/**
	 * 网络请求取消订单
	 * 
	 */
	private void netCancelOrder() {
		NetHelper.cancelOrder(mUser.getId(), mUser.getToken(), mOutTradeNo,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						LogUtils.logd(TAG, "onSuccess");
						resolvecancelOrder(arg2);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "onFailure");
					}
				});
	}

	private void resolvecancelOrder(byte[] arg2) {
		try {
			String json = new String(arg2, "UTF-8");
			LogUtils.logd(TAG, "json:" + json);
			JSONObject obj = new JSONObject(json);
			String code = obj.optString("code");
			String msg = obj.optString("msg");
			if (code.equals("0")) {
				ToastUtils.show(R.string.cancel_order_ok);
				mBottomLL.setVisibility(View.GONE);
				AndroidUtils.finishActivity(this);
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
