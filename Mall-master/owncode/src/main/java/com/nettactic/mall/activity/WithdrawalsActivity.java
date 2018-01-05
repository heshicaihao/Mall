package com.nettactic.mall.activity;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nettactic.mall.R;
import com.nettactic.mall.adapter.ChoiceShippingListAdapter;
import com.nettactic.mall.base.BaseActivity;
import com.nettactic.mall.bean.UserBean;
import com.nettactic.mall.cache.JsonDao;
import com.nettactic.mall.common.MyApplication;
import com.nettactic.mall.common.UserController;
import com.nettactic.mall.net.NetHelper;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.AssetsUtils;
import com.nettactic.mall.utils.LogUtils;
import com.nettactic.mall.utils.StringUtils;
import com.nettactic.mall.utils.ToastUtils;

/**
 * 提现界面
 * 
 * @author heshicaihao
 * 
 */
@SuppressWarnings("deprecation")
public class WithdrawalsActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {

	private RelativeLayout mWithdrawalsStylesView;
	private LinearLayout mWithdrawalsStylesLL;
	private LinearLayout mAliInfoLL;
	private LinearLayout mUnionpayInfoLL;
	private TextView mTitle;
	private TextView mTotalBalance;
	private TextView mTotalBalanceRemark;
	private TextView mAvailableBalance;
	private TextView mWithdrawalsStylesTv;
	private ListView mWithdrawalsStylesList;
	private Button mSubmitBt;
	private ImageView mBack;
	private EditText mNumEt;
	private EditText mAliNameEt;
	private EditText mAliAccountEt;
	private EditText mUnionpayNameEt;
	private EditText mUnionpayAccountEt;
	private EditText mUnionpayBankNameEt;
	private TextView withdrawals_notes;

	private String mNumStr;
	private String mAliNameStr;
	private String mAliAccountStr;
	private String mUnionpayNameStr;
	private String mUnionpayAccountStr;
	private String mUnionpayBankNameStr;
	private String mWithdrawalsStylesStr;
	private String mNoteStr;
	private String mNameStr;
	private double mABalanceD;

	private UserBean mUser;
	private String mAccountId;
	private JSONArray mWithdrawalsStylesArray;
	private ChoiceShippingListAdapter mWithdrawalsStylesAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_withdrawals);
		mUser = UserController.getInstance(this).getUserInfo();
		initView();
		initData();

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		try {
			JSONObject object = mWithdrawalsStylesArray.getJSONObject(position);
			setWithdrawalsStyles(object);
			mWithdrawalsStylesAdapter.setSeclection(position);
			mWithdrawalsStylesAdapter.notifyDataSetInvalidated();
			mWithdrawalsStylesView.setVisibility(View.GONE);
			if (position == 0) {
				mAliInfoLL.setVisibility(View.VISIBLE);
				mUnionpayInfoLL.setVisibility(View.GONE);
				withdrawals_notes.setText(this
						.getString(R.string.withdrawals_notes_ali));
			} else {
				mAliInfoLL.setVisibility(View.GONE);
				mUnionpayInfoLL.setVisibility(View.VISIBLE);
				withdrawals_notes.setText(this
						.getString(R.string.withdrawals_notes_unionpay));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {

		case R.id.back:
			AndroidUtils.finishActivity(this);

			break;

		case R.id.withdrawals_styles:
			mWithdrawalsStylesView.setVisibility(View.VISIBLE);
			break;

		case R.id.submit:
			withdrawals();
			break;

		default:
			break;
		}
	}

	/**
	 * 
	 * 提现
	 */
	private void withdrawals() {
		initEtStr();
		if (StringUtils.isEmpty(mNumStr)) {
			ToastUtils.show(R.string.please_input_num_null);
			return;
		}
		if (StringUtils.isEmpty(mWithdrawalsStylesStr)) {
			ToastUtils.show(R.string.please_input_withdrawals_styles_null);
			return;
		}
		if (MyApplication.getContext().getString(R.string.al_pay)
				.equals(mWithdrawalsStylesStr)) {
			if (StringUtils.isEmpty(mAliNameStr)) {
				ToastUtils.show(R.string.please_input_ali_name_null);
				return;
			}
			if (StringUtils.isEmpty(mAliAccountStr)) {
				ToastUtils.show(R.string.please_input_ali_account_null);
				return;
			}
			mNameStr = mAliNameStr;
			mNoteStr = JsonDao.getAlpayNoteInfo(mWithdrawalsStylesStr,
					mAliNameStr, mAliAccountStr);
		} else if (MyApplication.getContext()
				.getString(R.string.union_pay_card)
				.equals(mWithdrawalsStylesStr)) {
			if (StringUtils.isEmpty(mUnionpayNameStr)) {
				ToastUtils.show(R.string.please_input_unionpay_name_null);
				return;
			}
			if (StringUtils.isEmpty(mUnionpayAccountStr)) {
				ToastUtils.show(R.string.please_input_unionpay_account_null);
				return;
			}
			if (StringUtils.isEmpty(mUnionpayBankNameStr)) {
				ToastUtils.show(R.string.please_input_unionpay_bank_name_null);
				return;
			}
			mNameStr = mUnionpayNameStr;
			mNoteStr = JsonDao
					.getUnionPayNoteInfo(mWithdrawalsStylesStr,
							mUnionpayNameStr, mUnionpayAccountStr,
							mUnionpayBankNameStr);

		}
		LogUtils.logd(TAG, "mNoteStr:" + mNoteStr);
		setWithdrawals(mNumStr, mNameStr, mNoteStr);

	}

	private void initView() {
		mBack = (ImageView) findViewById(R.id.back);
		mSubmitBt = (Button) findViewById(R.id.submit);
		mTitle = (TextView) findViewById(R.id.title);
		mTotalBalance = (TextView) findViewById(R.id.total_balance);
		mTotalBalanceRemark = (TextView) findViewById(R.id.total_balance_remark);
		mAvailableBalance = (TextView) findViewById(R.id.available_balance);
		mWithdrawalsStylesTv = (TextView) findViewById(R.id.withdrawals_styles_edit);
		withdrawals_notes = (TextView) findViewById(R.id.withdrawals_notes);
		mWithdrawalsStylesView = (RelativeLayout) findViewById(R.id.shipping_view);
		mWithdrawalsStylesLL = (LinearLayout) findViewById(R.id.withdrawals_styles);
		mAliInfoLL = (LinearLayout) findViewById(R.id.ali_info);
		mUnionpayInfoLL = (LinearLayout) findViewById(R.id.unionpay_info);
		mWithdrawalsStylesList = (ListView) findViewById(R.id.shipping_list);
		mNumEt = (EditText) findViewById(R.id.withdrawals_num_edit);
		mAliNameEt = (EditText) findViewById(R.id.ali_name_edit);
		mAliAccountEt = (EditText) findViewById(R.id.ali_account_edit);
		mUnionpayNameEt = (EditText) findViewById(R.id.unionpay_name_edit);
		mUnionpayAccountEt = (EditText) findViewById(R.id.unionpay_account_edit);
		mUnionpayBankNameEt = (EditText) findViewById(R.id.unionpay_bank_name_edit);

		mTitle.setText(R.string.my_think_cash);
		mBack.setOnClickListener(this);
		mWithdrawalsStylesLL.setOnClickListener(this);
		mSubmitBt.setOnClickListener(this);
		mWithdrawalsStylesList.setOnItemClickListener(this);
		setTextChangedListener();

	}

	/**
	 * 添加编辑框监听
	 */
	private void setTextChangedListener() {
		mNumEt.addTextChangedListener(new TextWatcher() {

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
				String str = arg0.toString();
				if (arg0.toString().length() > 0) {
					double parseDouble = Double.parseDouble(str);
					String format2point = StringUtils.format2point(mABalanceD
							+ "");
					if (parseDouble > mABalanceD) {
						mNumEt.setText(format2point);
						String info = MyApplication.getContext().getString(
								R.string.you_max_withdrawals);
						info += format2point;
						info += MyApplication.getContext().getString(
								R.string.yuan);
						ToastUtils.show(info);
					}
				}
			}
		});

	}

	private void initData() {
		mAccountId = mUser.getId();
		initEtStr();
		initWithdrawals(mAccountId);
		initWithdrawalsStylesData();
		setAdapter();
	}

	/**
	 * 获取输入框内容
	 */
	private void initEtStr() {
		mNumStr = mNumEt.getText().toString().trim();
		mAliNameStr = mAliNameEt.getText().toString().trim();
		mAliAccountStr = mAliAccountEt.getText().toString().trim();
		mUnionpayNameStr = mUnionpayNameEt.getText().toString().trim();
		mUnionpayAccountStr = mUnionpayAccountEt.getText().toString().trim();
		mUnionpayBankNameStr = mUnionpayBankNameEt.getText().toString().trim();
	}

	/**
	 * 初始化 体现方式 数据
	 */
	private void initWithdrawalsStylesData() {
		String json = AssetsUtils.getJson(MyApplication.getContext(),
				"withdrawals_styles.json");
		try {
			mWithdrawalsStylesArray = new JSONArray(json);
			JSONObject object = (JSONObject) mWithdrawalsStylesArray.get(0);
			mWithdrawalsStylesStr = object.optString("dt_name");
			mWithdrawalsStylesTv.setText(mWithdrawalsStylesStr);
			mAliInfoLL.setVisibility(View.VISIBLE);
			mUnionpayInfoLL.setVisibility(View.GONE);
			withdrawals_notes.setText(this
					.getString(R.string.withdrawals_notes_ali));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 提现方式 添加适配器
	 */
	private void setAdapter() {
		mWithdrawalsStylesAdapter = new ChoiceShippingListAdapter(this,
				mWithdrawalsStylesArray);
		mWithdrawalsStylesList.setAdapter(mWithdrawalsStylesAdapter);
	}

	/**
	 * 显示提现方式
	 * 
	 * @param object
	 * @throws JSONException
	 */
	private void setWithdrawalsStyles(JSONObject object) throws JSONException {
		mWithdrawalsStylesStr = object.optString("dt_name");
		mWithdrawalsStylesTv.setText(mWithdrawalsStylesStr);
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
				showBalance(result);
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
	 * 请求提现
	 * 
	 * @param num
	 *            金额
	 * @param name
	 *            名字
	 * @param note
	 *            账户信息
	 */
	private void setWithdrawals(String num, String name, String note) {
		NetHelper.setWithdraw(mAccountId, num, name, note,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						LogUtils.logd(TAG, "setWithdrawals+onSuccess");
						resolvesetWithdrawals(arg2);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "setWithdrawals+onFailure");

					}
				});

	}

	/**
	 * 
	 * 解析 提现 结果
	 * 
	 * @param arg2
	 */
	private void resolvesetWithdrawals(byte[] arg2) {
		try {
			String json = new String(arg2, "UTF-8");
			LogUtils.logd(TAG, "resolvesetWithdrawalsjson:" + json);
			JSONObject obj = new JSONObject(json);
			String code = obj.optString("code");
			String msg = obj.optString("msg");
			JSONObject result = obj.optJSONObject("result");
			if ("0".equals(code)) {
				showBalance(result);
				ToastUtils.shortShow(R.string.withdrawals_ok);
				this.finish();
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
	 * 显示 余额信息
	 * 
	 * @param result
	 */
	private void showBalance(JSONObject result) {
		if (result != null) {
			String balance = result.optString("balance");
			String balanceformat2point = StringUtils.format2point(balance);
			String balanceStr = balanceformat2point
					+ MyApplication.getContext().getString(R.string.yuan);
			mAvailableBalance.setText(balanceStr);

			String frozen_amount = result.optString("frozen_amount");
			if (!frozen_amount.equals("0")) {
				String frozen_amount2point = StringUtils
						.format2point(frozen_amount);
				String frozen_amountStr = MyApplication.getContext().getString(
						R.string.contain_frozen_amount_first)
						+ frozen_amount2point
						+ MyApplication.getContext().getString(
								R.string.contain_frozen_amount_second);
				mTotalBalanceRemark.setText(frozen_amountStr);
			} else {
				mTotalBalanceRemark.setText("");
			}

			String amount = result.optString("amount");
			String amountformat2point = StringUtils.format2point(amount);
			String amountStr = amountformat2point
					+ MyApplication.getContext().getString(R.string.yuan);
			mABalanceD = Double.parseDouble(amount);
			mTotalBalance.setText(amountStr);
		}
	}

}
