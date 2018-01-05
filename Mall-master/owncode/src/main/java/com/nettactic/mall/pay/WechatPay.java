package com.nettactic.mall.pay;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.nettactic.mall.R;
import com.nettactic.mall.constants.MyConstants;
import com.nettactic.mall.net.NetHelper;
import com.nettactic.mall.utils.JSONUtil;
import com.nettactic.mall.utils.LogUtils;
import com.nettactic.mall.utils.ToastUtils;

@SuppressWarnings("deprecation")
public class WechatPay {
	private String TAG = "WechatPay";
	private String APPID = MyConstants.WX_APP_ID;
	private IWXAPI mApi;
	private String mAccountId;
	private String mAccountToken;

	private Activity mActivity;
	private String mGoodsNameStr;
	@SuppressWarnings("unused")
	private String mGoodsInfoStr;
	private String mOutTradeNo;
	private String mTotalStr;

	public WechatPay() {
		super();
	}

	public WechatPay(Activity mActivity, Context mContext,
			String mGoodsNameStr, String mGoodsInfoStr, String mOutTradeNo,
			String mTotalStr, String mAccountId, String mAccountToken) {
		super();
		this.mActivity = mActivity;
		this.mGoodsNameStr = mGoodsNameStr;
		this.mGoodsInfoStr = mGoodsInfoStr;
		this.mOutTradeNo = mOutTradeNo;
		this.mTotalStr = mTotalStr;
		this.mAccountId = mAccountId;
		this.mAccountToken = mAccountToken;
		mApi = WXAPIFactory.createWXAPI(mContext, APPID);
		mApi.registerApp(APPID);
	}

	public void pay() {

		boolean isPaySupported = mApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
		if (isPaySupported) {
			sendPay();
		} else {
			ToastUtils.show(R.string.wechat_inpaysupported);
			// mActivity.finish();
		}

	}

	private void sendPay() {
		LogUtils.logd(TAG, "mOutTradeNo:" + mOutTradeNo);
		LogUtils.logd(TAG, "mGoodsNameStr:" + mGoodsNameStr);
		LogUtils.logd(TAG, "mTotalStr:" + mTotalStr);
		LogUtils.logd(TAG, "mAccountId:" + mAccountId);
		LogUtils.logd(TAG, "mAccountToken:" + mAccountToken);
		NetHelper.wechatPay(mOutTradeNo, mGoodsNameStr, mTotalStr, mAccountId,
				mAccountToken, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						LogUtils.logd(TAG, "sendPay+Success");
						resolveSendPay(responseBody);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "sendPay+onFailure");
						ToastUtils.show(R.string.server_failure);
					}
				});
	}

	private void resolveSendPay(byte[] responseBody) {
		try {
			String json = new String(responseBody, "UTF-8");
			LogUtils.logd(TAG, "json:" + json.toString());
			JSONObject result = JSONUtil.resolveResult(responseBody);
			LogUtils.logd(TAG, "result:" + result.toString());
			PayReq request = new PayReq();
			request.appId = APPID;
			request.partnerId = result.optString("partener_id");
			request.prepayId = result.optString("prepay_id");
			request.packageValue = result.optString("package");
			request.nonceStr = result.optString("nonce_str");
			request.timeStamp = result.optString("timestamp");
			request.sign = result.optString("sign");
			request.extData = "app data";
			mApi.sendReq(request);
			mActivity.finish();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}
