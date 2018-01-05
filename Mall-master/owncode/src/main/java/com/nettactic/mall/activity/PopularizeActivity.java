package com.nettactic.mall.activity;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nettactic.mall.R;
import com.nettactic.mall.adapter.PopularizeListAdapter;
import com.nettactic.mall.base.BaseActivity;
import com.nettactic.mall.bean.UserBean;
import com.nettactic.mall.common.MyApplication;
import com.nettactic.mall.common.UserController;
import com.nettactic.mall.net.MyURL;
import com.nettactic.mall.net.NetHelper;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.LogUtils;
import com.nettactic.mall.utils.StringUtils;
import com.nettactic.mall.utils.ToastUtils;
import com.nettactic.mall.widget.MyListView;

/**
 * 推广首页
 * 
 * @author heshicaihao
 */
@SuppressWarnings("deprecation")
public class PopularizeActivity extends BaseActivity implements OnClickListener {

	private Boolean mIsPromoter = false;
	private Boolean mIsApply = false;

	private LinearLayout mGoPopularizeLL;
	private LinearLayout mGoCashLL;
	private RelativeLayout mOrderNullRL;
	private ImageView mBack;
	private TextView mTitle;
	private TextView mExecuteTV;
	private TextView mHintsTV;
	private TextView mGainsTV;
	private TextView mPromotionOrderNumTV;
	private TextView mTotalGainsTV;
	private TextView mMonthPeopleNumTV;
	private TextView mTotalPeopleNumTV;
	private MyListView mListview;

	private UserBean mUser;
	private double mBalanceD = 0;
	private int status = 0;
	private String promote_code;
	private ScrollView mScrollView;
	private LinearLayout mExecuteLL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_popularize);
		mUser = UserController.getInstance(this).getUserInfo();

		initView();
		initData();

	}

	@Override
	public void onResume() {
		super.onResume();
		initData();
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {

		case R.id.back:
			AndroidUtils.finishActivity(this);

			break;
		case R.id.execute_tv:
			gotoExecute();

			break;
		case R.id.my_think_popularize:
			startOtherWebNoTitle(this, "", MyURL.PROMOTE_PROPAGATE_URL);

			break;
		case R.id.my_think_cash:
			if (mBalanceD > 0) {
				startActivity(this, WithdrawalsActivity.class);
			}

			break;
		default:
			break;
		}
	}

	private void initView() {
		mBack = (ImageView) findViewById(R.id.back);
		mTitle = (TextView) findViewById(R.id.title);
		mExecuteTV = (TextView) findViewById(R.id.execute_tv);
		mHintsTV = (TextView) findViewById(R.id.hints_tv);
		mGoPopularizeLL = (LinearLayout) findViewById(R.id.my_think_popularize);
		mGoCashLL = (LinearLayout) findViewById(R.id.my_think_cash);

		mOrderNullRL = (RelativeLayout) findViewById(R.id.popularize_order_null_rl);
		mListview = (MyListView) findViewById(R.id.popularize_order_listview);
		mScrollView = (ScrollView) findViewById(R.id.my_scrollview);
		mExecuteLL = (LinearLayout) findViewById(R.id.execute_ll);
		mGainsTV = (TextView) findViewById(R.id.gains_value);
		mPromotionOrderNumTV = (TextView) findViewById(R.id.promotion_order_num);
		mTotalGainsTV = (TextView) findViewById(R.id.total_gains);
		mMonthPeopleNumTV = (TextView) findViewById(R.id.this_month_promotion_people_num);
		mTotalPeopleNumTV = (TextView) findViewById(R.id.total_promotion_people_num);

		// 设置 mScrollView 滚到顶部 先设置焦点
		mListview.setFocusable(false);
		mExecuteLL.setFocusable(true);
		mExecuteLL.setFocusableInTouchMode(true);
		mExecuteLL.requestFocus();

		mGoCashLL.setBackgroundColor(Color.parseColor("#c3c3c3"));
		mTitle.setText(R.string.me_popularize);
		mBack.setOnClickListener(this);
		mGoPopularizeLL.setOnClickListener(this);
		mGoCashLL.setOnClickListener(this);
		mExecuteTV.setOnClickListener(this);
	}

	public static void scrollToBottom(final ScrollView scroll, final int offset) {
		Handler mHandler = new Handler();

		mHandler.post(new Runnable() {
			public void run() {
				if (scroll == null) {
					return;
				}
				scroll.fullScroll(ScrollView.FOCUS_UP);// 将滚动条放到顶部
			}
		});
	}

	private void initData() {
		initPopularizeinfo();
		scrollToBottom(mScrollView, 0);
	}

	private void initPopularizeinfo() {
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
				showExecute(result);
				showNum(result);
				showOrderList(result);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 显示 提示项
	 * 
	 * @param result
	 */
	private void showExecute(JSONObject result) {
		mIsPromoter = result.optBoolean("is_promoter", false);
		mIsApply = result.optBoolean("is_apply", false);
		promote_code = result.optString("promote_code");
		AndroidUtils.savePromoteCodeInfo(mUser.getId(), promote_code);
		String promote_status = result.optString("promote_status");
		if (!StringUtils.isEmpty(promote_status)) {
			status = Integer.parseInt(promote_status);
		} else {
			status = 0;
		}
		String promote_msg = result.optString("promote_msg");
		boolean mIsCodeNull = StringUtils.isEmpty(promote_code);
		if (!mIsPromoter) {
			// 不是推广人 并且没有申请推广码
			mHintsTV.setVisibility(View.VISIBLE);
			mHintsTV.setText(MyApplication.getContext().getString(
					R.string.you_are_not_the_promotion_of_people));
			mExecuteTV.setText(MyApplication.getContext().getString(
					R.string.becoming_a_promoter));
			mExecuteTV.setBackgroundColor(Color.parseColor("#FFAC13"));
		} else {
			if (mIsApply) {
				switch (status) {
				case 0:
					// 不是推广人 申请了推广码 下了单 等待审核
					mHintsTV.setVisibility(View.GONE);
					mExecuteTV.setText(promote_msg);
					mExecuteTV.setBackgroundColor(Color.parseColor("#c3c3c3"));
					break;

				case 1:
					// 不是推广人 申请了 推广码 等待下单
					mHintsTV.setVisibility(View.VISIBLE);
					mHintsTV.setText(promote_msg);
					mExecuteTV.setText(MyApplication.getContext().getString(
							R.string.choice_goods_and_place_an_order));
					mExecuteTV.setBackgroundColor(Color.parseColor("#FFAC13"));

					break;
				case 2:
					// 是推广人 正常 显示推广码
					mHintsTV.setVisibility(View.GONE);
					if (!mIsCodeNull) {
						mExecuteTV.setText(MyApplication.getContext()
								.getString(R.string.me_promotion_code_is)
								+ promote_code);
					}
					mExecuteTV.setBackgroundColor(Color.parseColor("#FFAC13"));

					break;
				case 3:
					// 是推广人 终止 推广
					mHintsTV.setVisibility(View.GONE);
					mExecuteTV.setText(promote_msg);
					mExecuteTV.setBackgroundColor(Color.parseColor("#c3c3c3"));
					break;

				case -1:
					// 是推广人 拒绝推广
					mHintsTV.setVisibility(View.GONE);
					mExecuteTV.setText(promote_msg);
					mExecuteTV.setBackgroundColor(Color.parseColor("#c3c3c3"));
					break;

				default:
					break;
				}
			}
		}
	}

	/**
	 * 显示 收益明细
	 * 
	 * @param result
	 */
	private void showNum(JSONObject result) {
		String profit = result.optString("profit");
		String gainsStr = StringUtils.format2point(profit)
				+ MyApplication.getContext().getString(R.string.yuan);
		mBalanceD = Double.parseDouble(profit);
		if (mBalanceD > 0) {
			mGoCashLL.setBackgroundColor(Color.parseColor("#70CDB4"));
		} else {
			mGoCashLL.setBackgroundColor(Color.parseColor("#c3c3c3"));
		}
		mGainsTV.setText(gainsStr);

		String promotes_order_all = result.optString("promotes_order_all");
		String promotion_order_numStr = promotes_order_all
				+ MyApplication.getContext().getString(R.string.bi);
		mPromotionOrderNumTV.setText(promotion_order_numStr);

		String promote_amount_all = result.optString("promote_amount_all");
		String total_gainsStr = StringUtils.format2point(promote_amount_all)
				+ MyApplication.getContext().getString(R.string.yuan);

		mTotalGainsTV.setText(total_gainsStr);

		String promote_order_smonth = result.optString("promote_order_smonth");
		mMonthPeopleNumTV.setText(promote_order_smonth);

		String promote_amount_smonth = result
				.optString("promote_amount_smonth");
		mTotalPeopleNumTV.setText(promote_amount_smonth);

	}

	/**
	 * 显示 我推广的订单列表
	 * 
	 * @param result
	 */
	private void showOrderList(JSONObject result) {
		JSONArray order_list = result.optJSONArray("order_list");
		if (order_list == null || order_list.length() == 0) {
			mOrderNullRL.setVisibility(View.VISIBLE);
			mListview.setVisibility(View.GONE);
		} else {
			mOrderNullRL.setVisibility(View.GONE);
			mListview.setVisibility(View.VISIBLE);
			PopularizeListAdapter adapter = new PopularizeListAdapter(
					MyApplication.getContext(), order_list);
			mListview.setAdapter(adapter);
			// 设置 mScrollView 滚到顶部 listview 加载数据之后设置坐标
			mScrollView.smoothScrollTo(0, 0);
		}

	}

	/**
	 * 
	 * 执行 点击事件
	 */
	private void gotoExecute() {
		if (!mIsPromoter) {
			startActivity(this, ApplyPromoterActivity.class);
		} else {
			if (mIsApply) {
				switch (status) {
				case 0:
					// 不是推广人 申请了推广码 下了单 等待审核
					break;

				case 1:
					// 不是推广人 申请了 推广码 等待下单
					AndroidUtils.finishActivity(this);
					break;
				case 2:
					// 是推广人 正常 显示推广码
					if (!StringUtils.isEmpty(promote_code)) {
						AndroidUtils.copy(promote_code, this);
						ToastUtils.show(R.string.copy_promote_code);
					}
					break;
				case 3:
					// 是推广人 终止 推广
					break;

				case -1:
					// 是推广人 拒绝推广
					break;

				default:
					break;
				}
			}
		}
	}

}
