package com.nettactic.mall.activity;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nettactic.mall.R;
import com.nettactic.mall.base.BaseActivity;
import com.nettactic.mall.cache.ACache;
import com.nettactic.mall.common.MyApplication;
import com.nettactic.mall.constants.MyConstants;
import com.nettactic.mall.net.NetHelper;
import com.nettactic.mall.pay.ALiPay;
import com.nettactic.mall.pay.WechatPay;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.JSONUtil;
import com.nettactic.mall.utils.LogUtils;
import com.nettactic.mall.utils.StringUtils;
import com.nettactic.mall.utils.ToastUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 订单支付页
 * 
 * @author heshicaihao
 * 
 */
@SuppressWarnings("deprecation")
public class OrderPayActivity extends BaseActivity implements OnClickListener {

	private LinearLayout mAliPayLL;
	private LinearLayout mWechatPayLL;
	private LinearLayout mDepositPayLL;
	private TextView mTitle;
	private TextView mTotalMoney;
	private TextView mOrderIdTV;
	private TextView mWalletAmount;
	private TextView mResidualAmount;
	private Button mPay;
	private ImageView mAliIV;
	private ImageView mWXIV;
	private ImageView mDepositIV;
	private ImageView mBack;

	private String mTotalStr;
	private String mGoodsNameStr;
	private String mGoodsInfoStr;
	private String mOutTradeNo;

	private String mAccountId;
	private String mAccountToken;
	private ALiPay alipay;
	private WechatPay wechatpay;
	private ACache mCache;
	private String mWorkId;
	private double mTotalD;
	private double mBalanceD;
	private boolean mIsPicSaveOK = false;
	private boolean mIsDeposit_pay = false;
	private boolean mIsAliPay = false;
	private boolean mIsWechatPay = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_pay);
		initView();
		initData();
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.pay_text:
			if (mIsPicSaveOK) {
				LogUtils.logd(TAG, "mIsDeposit_pay:" + mIsDeposit_pay);
				LogUtils.logd(TAG, "mIsAliPay:" + mIsAliPay);
				LogUtils.logd(TAG, "mIsWechatPay:" + mIsWechatPay);
				gotoPay();
			} else {
				ToastUtils.show(R.string.pic_save_fail);
			}

			break;

		case R.id.deposit_pay_ll:
			onOnClickDepositPay();
			break;

		case R.id.al_pay_ll:
			onOnClickAliPay();
			break;

		case R.id.wechat_pay_ll:
			onOnClickWechatPay();
			break;

		case R.id.back:
			AndroidUtils.finishActivity(this);
			break;

		default:
			break;
		}

	}

	private void initView() {
		mBack = (ImageView) findViewById(R.id.back);
		mTitle = (TextView) findViewById(R.id.title);
		mTotalMoney = (TextView) findViewById(R.id.pay_money_value);
		mOrderIdTV = (TextView) findViewById(R.id.order_id_value);
		mPay = (Button) findViewById(R.id.pay_text);
		mAliIV = (ImageView) findViewById(R.id.al_iv);
		mWXIV = (ImageView) findViewById(R.id.wx_iv);
		mDepositIV = (ImageView) findViewById(R.id.deposit_iv);
		mAliPayLL = (LinearLayout) findViewById(R.id.al_pay_ll);
		mWechatPayLL = (LinearLayout) findViewById(R.id.wechat_pay_ll);
		mDepositPayLL = (LinearLayout) findViewById(R.id.deposit_pay_ll);
		mWalletAmount = (TextView) findViewById(R.id.wallet_amount);
		mResidualAmount = (TextView) findViewById(R.id.residual_amount);
		mTitle.setText(R.string.order_pay);
		mBack.setOnClickListener(this);
		mPay.setOnClickListener(this);
		mAliPayLL.setOnClickListener(this);
		mWechatPayLL.setOnClickListener(this);
		mDepositPayLL.setOnClickListener(this);

	}

	private void initData() {
		mCache = ACache.get(this);
		mAccountId = mUserController.getUserInfo().getId();
		mAccountToken = mUserController.getUserInfo().getToken();
		mWorkId = mCache.getAsString(MyConstants.WORKID);

		getDataIntent();
		initWithdrawals(mAccountId);
		getWorksStatus();
		initOtherPay();

	}

	private void getDataIntent() {
		Intent intent = getIntent();
		mTotalStr = intent.getStringExtra("mTotalStr");
		mOutTradeNo = intent.getStringExtra("mOutTradeNo");
		mGoodsNameStr = "i定制扑克牌";
		mGoodsInfoStr = "i定制扑克牌";
		mCache.put(MyConstants.ORDERID, mOutTradeNo);
		mOrderIdTV.setText(mOutTradeNo);
		mTotalD = Double.parseDouble(mTotalStr);
		String mTotalStr2point = StringUtils.format2point(mTotalStr);
		String totalmoneystr = mTotalStr2point
				+ MyApplication.getContext().getString(R.string.yuan);
		mTotalMoney.setText(totalmoneystr);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		alipay = null;
		wechatpay = null;
	}

	/**
	 * 
	 * 去支付
	 */
	private void gotoPay() {
		if (mBalanceD == 0) {
			if (mIsAliPay) {
				alipay.pay();
			}
			if (mIsWechatPay) {
				wechatpay.pay();
			}
		} else {
			if (mBalanceD < mTotalD) {
				if (mIsDeposit_pay && mIsWechatPay) {
					depositPay();
				} else if (mIsDeposit_pay && mIsAliPay) {
					depositPay();
				} else if (!mIsDeposit_pay && mIsAliPay) {
					alipay.pay();
				} else if (!mIsDeposit_pay && mIsWechatPay) {
					wechatpay.pay();
				}
			} else {
				if (mIsDeposit_pay) {
					depositPay();
				} else if (mIsAliPay) {
					alipay.pay();
				} else if (mIsWechatPay) {
					wechatpay.pay();
				}
			}
		}
	}

	/**
	 * 
	 * 点击微信支付
	 */
	private void onOnClickWechatPay() {
		if (!mIsWechatPay) {
			mAliIV.setImageResource(R.mipmap.checkbox_normal);
			mWXIV.setImageResource(R.mipmap.checkbox_pressed);
			mIsAliPay = false;
			mIsWechatPay = true;
		} else {
			mAliIV.setImageResource(R.mipmap.checkbox_pressed);
			mWXIV.setImageResource(R.mipmap.checkbox_normal);
			mIsAliPay = true;
			mIsWechatPay = false;
		}
	}

	/**
	 * 
	 * 点击支付宝支付
	 */
	private void onOnClickAliPay() {
		if (!mIsAliPay) {
			mAliIV.setImageResource(R.mipmap.checkbox_pressed);
			mWXIV.setImageResource(R.mipmap.checkbox_normal);
			mIsAliPay = true;
			mIsWechatPay = false;
		} else {
			mAliIV.setImageResource(R.mipmap.checkbox_normal);
			mWXIV.setImageResource(R.mipmap.checkbox_pressed);
			mIsAliPay = false;
			mIsWechatPay = true;
		}
	}

	/**
	 * 
	 * 点击余额支付
	 */
	private void onOnClickDepositPay() {
		if (mBalanceD < mTotalD) {
			if (!mIsDeposit_pay) {
				mDepositIV.setImageResource(R.mipmap.checkbox_pressed);
				mAliIV.setImageResource(R.mipmap.checkbox_pressed);
				mWXIV.setImageResource(R.mipmap.checkbox_normal);
				mIsDeposit_pay = true;
				mIsAliPay = true;
				mIsWechatPay = false;
				mAliPayLL.setEnabled(true);
				mWechatPayLL.setEnabled(true);
			} else {
				mDepositIV.setImageResource(R.mipmap.checkbox_normal);
				mAliIV.setImageResource(R.mipmap.checkbox_pressed);
				mWXIV.setImageResource(R.mipmap.checkbox_normal);
				mIsDeposit_pay = false;
				mIsAliPay = true;
				mIsWechatPay = false;
				mAliPayLL.setEnabled(true);
				mWechatPayLL.setEnabled(true);
			}
		} else {
			if (!mIsDeposit_pay) {
				mDepositIV.setImageResource(R.mipmap.checkbox_pressed);
				mAliIV.setImageResource(R.mipmap.checkbox_normal);
				mWXIV.setImageResource(R.mipmap.checkbox_normal);
				mIsDeposit_pay = true;
				mIsAliPay = false;
				mIsWechatPay = false;
				mAliPayLL.setEnabled(false);
				mWechatPayLL.setEnabled(false);

			} else {
				mDepositIV.setImageResource(R.mipmap.checkbox_normal);
				mAliIV.setImageResource(R.mipmap.checkbox_pressed);
				mWXIV.setImageResource(R.mipmap.checkbox_normal);
				mIsDeposit_pay = false;
				mIsAliPay = true;
				mIsWechatPay = false;
				mAliPayLL.setEnabled(true);
				mWechatPayLL.setEnabled(true);
			}
		}
	}

	/**
	 * 检查作品上传是否完整
	 * 
	 */
	private void getWorksStatus() {

		NetHelper.getStatus(mAccountId, mAccountToken, mWorkId,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						LogUtils.logd(TAG, "resolvegetStatus+onSuccess");
						resolvegetStatus(arg2);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "resolvegetStatus+onFailure");
					}
				});

	}

	/**
	 * 解析 检查作品上传是否完整 数据
	 * 
	 * @param responseBody
	 */
	private void resolvegetStatus(byte[] responseBody) {
		try {
			String json = new String(responseBody, "UTF-8");
			LogUtils.logd(TAG, "getStatusjson:" + json);
			JSONObject work_info = JSONUtil.resolveResult(responseBody);
			if (work_info != null) {
				LogUtils.logd(TAG, "work_info:" + work_info);
				String work_data_state = work_info.optString("work_data_state");
				LogUtils.logd(TAG, "work_data_state:" + work_data_state);
				if ("complete".equals(work_data_state)) {
					mIsPicSaveOK = true;
					// ToastUtils.show(R.string.works_pic_complete);
				} else if ("incomplete".equals(work_data_state)) {
					ToastUtils.show(R.string.works_pic_incomplete);
					JSONObject work_miss_info = work_info
							.optJSONObject("work_miss_info");
					JSONArray picid_miss = work_miss_info
							.getJSONArray("picid_miss");
					int length = picid_miss.length();
					if (length != 0) {
						mIsPicSaveOK = false;
						ToastUtils.show(R.string.works_pic_incomplete);
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化 获取提现金额
	 * 
	 * @param account_id
	 */
	private void initWithdrawals(String account_id) {
		NetHelper.getWithdraw(account_id, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				LogUtils.logd(TAG, "initWithdrawals+onSuccess");
				resolveInitWithdrawals(arg2);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				LogUtils.logd(TAG, "initWithdrawals+onFailure");
			}
		});
	}

	/**
	 * 解析 初始化 获取提现金额
	 * 
	 * @param arg2
	 */
	protected void resolveInitWithdrawals(byte[] arg2) {
		try {
			String json = new String(arg2, "UTF-8");
			LogUtils.logd(TAG, "json:" + json);
			JSONObject obj = new JSONObject(json);
			String code = obj.optString("code");
			String msg = obj.optString("msg");
			JSONObject result = obj.optJSONObject("result");
			if ("0".equals(code)) {
				if (result != null) {
					String balance = result.optString("balance");
					mBalanceD = Double.parseDouble(balance);
					if (mBalanceD > 0) {
						String balanceformat2point = StringUtils
								.format2point(balance);
						String balanceStr = balanceformat2point;
						mWalletAmount.setText(balanceStr);
						mDepositPayLL.setVisibility(View.VISIBLE);
						mResidualAmount.setVisibility(View.VISIBLE);
						if (mBalanceD < mTotalD) {
							mDepositIV
									.setImageResource(R.mipmap.checkbox_pressed);
							mAliIV.setImageResource(R.mipmap.checkbox_pressed);
							mWXIV.setImageResource(R.mipmap.checkbox_normal);
							mIsDeposit_pay = true;
							mIsAliPay = true;
							mIsWechatPay = false;
						} else {
							mDepositIV
									.setImageResource(R.mipmap.checkbox_pressed);
							mAliIV.setImageResource(R.mipmap.checkbox_normal);
							mWXIV.setImageResource(R.mipmap.checkbox_normal);
							mIsDeposit_pay = true;
							mIsAliPay = false;
							mIsWechatPay = false;
						}
					} else {
						String balanceformat2point = StringUtils
								.format2point(balance);
						String balanceStr = balanceformat2point;
						mWalletAmount.setText(balanceStr);
						mDepositPayLL.setVisibility(View.GONE);
						mResidualAmount.setVisibility(View.GONE);
						mDepositIV.setImageResource(R.mipmap.checkbox_normal);
						mAliIV.setImageResource(R.mipmap.checkbox_pressed);
						mWXIV.setImageResource(R.mipmap.checkbox_normal);
						mIsDeposit_pay = false;
						mIsAliPay = true;
						mIsWechatPay = false;
					}
				}
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
	 * 余额支付
	 * 
	 */
	private void depositPay() {

		NetHelper.depositPay(mOutTradeNo, mAccountId, mAccountToken,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						LogUtils.logd(TAG, "depositPay+onSuccess");
						resolvedepositPay(arg2);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "depositPay+onFailure");
						mDepositIV.setImageResource(R.mipmap.checkbox_normal);
						mAliIV.setImageResource(R.mipmap.checkbox_pressed);
						mWXIV.setImageResource(R.mipmap.checkbox_normal);
						mIsDeposit_pay = false;
						mIsAliPay = true;
						mIsWechatPay = false;
						ToastUtils.show(R.string.net_failure);
					}
				});

	}

	/**
	 * 余额支付 结果解析
	 * 
	 */
	private void resolvedepositPay(byte[] arg2) {
		try {
			String json = new String(arg2, "UTF-8");
			LogUtils.logd(TAG, "depositPay+json:" + json);
			JSONObject obj = new JSONObject(json);
			String code = obj.optString("code");
			String msg = obj.optString("msg");
			JSONObject result = obj.optJSONObject("result");
			if ("0".equals(code)) {
				if (result != null) {
					LogUtils.logd(TAG, "!= null:");
					String not_payed = result.optString("not_payed");
					double not_payedD = Double.parseDouble(not_payed);
					LogUtils.logd(TAG, "not_payedD:" + not_payedD);
					if (not_payedD == 0) {
						ToastUtils.shortShow(R.string.deposit_pay_ok);
						LogUtils.logd(TAG, "not_payedD:== 0");
						gotoPaySuccess();
					} else if (not_payedD > 0) {
						LogUtils.logd(TAG, "not_payedD:!= 0" + not_payedD);
						// mOutTradeNo = result.optString("order_id");
						mTotalStr = result.optString("not_payed");
						initOtherPay();
						if (mIsAliPay) {
							alipay.pay();
							// this.finish();
						}
						if (mIsWechatPay) {
							LogUtils.logd(TAG, "mTotalStr:" + mTotalStr);
							LogUtils.logd(TAG, "mTotalStr:" + mTotalStr);
							LogUtils.logd(TAG, "mTotalStr:" + mTotalStr);
							LogUtils.logd(TAG, "mTotalStr:" + mTotalStr);
							LogUtils.logd(TAG, "mTotalStr:" + mTotalStr);
							wechatpay.pay();
							// this.finish();
						}
					}
				}
			} else {
				ToastUtils.shortShow(msg);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void gotoPaySuccess() {
		Intent intent = new Intent(this, PaySuccessActivity.class);
		this.startActivity(intent);
		AndroidUtils.enterActvityAnim(this);
		this.finish();
	}

	/**
	 * 初始化 三方支付
	 */
	private void initOtherPay() {
		alipay = new ALiPay(this, this, mGoodsNameStr, mGoodsInfoStr,
				mOutTradeNo, mTotalStr);
		wechatpay = new WechatPay(this, this, mGoodsNameStr, mGoodsInfoStr,
				mOutTradeNo, mTotalStr, mAccountId, mAccountToken);
	}

}
