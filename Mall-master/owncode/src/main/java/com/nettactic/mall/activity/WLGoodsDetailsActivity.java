package com.nettactic.mall.activity;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nettactic.mall.R;
import com.nettactic.mall.adapter.GoodsDetailsListAdapter;
import com.nettactic.mall.adapter.PopupHolder;
import com.nettactic.mall.base.BaseActivity;
import com.nettactic.mall.bean.UserBean;
import com.nettactic.mall.common.MyApplication;
import com.nettactic.mall.common.UserController;
import com.nettactic.mall.constants.MyConstants;
import com.nettactic.mall.net.NetHelper;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.FileUtil;
import com.nettactic.mall.utils.LogUtils;
import com.nettactic.mall.utils.StringUtils;
import com.nettactic.mall.utils.ToastUtils;
import com.nettactic.mall.widget.MyListView;
import com.nettactic.mall.widget.SildingFinishLayout;
import com.nettactic.mall.widget.SildingFinishLayout.OnSildingFinishListener;

/**
 * 商品详情
 * 
 * @author heshicaihao
 * 
 */
@SuppressWarnings("deprecation")
public class WLGoodsDetailsActivity extends BaseActivity implements
		OnClickListener {

	private int mFirstPosition = 0;
	private String mCatId;
	private String mDataIntent;

	private View mMenuView;
	private ImageView mBack;
	private TextView mTitle;
	private ImageView mShare;
	private TextView mCustomMade;
	private TextView mOKBtn;
	private ListView mListView;
	private MyListView mPopupListview;

	private MyPopupWindow mPopupWindow;
	private JSONArray mDataPopupWindow = null;
	private UserBean mUser;
	public boolean mIsPicSaveOK = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wl_activity_goods_details);
		mUser = UserController.getInstance(this).getUserInfo();
		initView();
		initData();
	}

	@Override
	public void onResume() {
		super.onResume();
		mUser = UserController.getInstance(this).getUserInfo();
		if (mUser.isIs_login()) {
			String promote_code_info_path = FileUtil.getFilePath(
					MyConstants.PCODE, MyConstants.PCODE_INFO + mUser.getId(),
					MyConstants.JSON);
			if (!FileUtil.fileIsExists(promote_code_info_path)) {
				initPopularizeinfo();
			}
			if (!StringUtils.isEmpty(mDataIntent)) {
				gotoChoicePicture(mDataIntent);
				mPopupWindow.dismiss();
				mDataIntent = null;
				finish();
			}
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			AndroidUtils.finishActivity(this);
			break;

		case R.id.share:
			String promotecode = null;
			if (mUser.isIs_login()) {
				promotecode = AndroidUtils.getPromoteCodeInfo(mUser.getId());
			}
			share(promotecode);
			break;

		case R.id.custom_made_btn:
			if (mDataPopupWindow != null) {
				mPopupWindow = new MyPopupWindow(WLGoodsDetailsActivity.this,
						itemsOnClick);
				mPopupWindow.showAtLocation(WLGoodsDetailsActivity.this
						.findViewById(R.id.custom_made_btn), Gravity.BOTTOM
						| Gravity.CENTER_HORIZONTAL, 0, 0);
			} else {
				ToastUtils.show(R.string.temporary_no_data);
			}
			break;
		default:
			break;
		}
	}

	private void initView() {
		mTitle = (TextView) findViewById(R.id.title);
		mBack = (ImageView) findViewById(R.id.back);
		mShare = (ImageView) findViewById(R.id.share);
		mCustomMade = (TextView) findViewById(R.id.custom_made_btn);
		mListView = (ListView) findViewById(R.id.goods_details_listview);

		mCustomMade.setClickable(false);
		mCustomMade.setBackgroundColor(Color.parseColor("#c3c3c3"));

		setSildingFinish();

		mCustomMade.setOnClickListener(this);
		mBack.setOnClickListener(this);
		mShare.setOnClickListener(this);
	}

	private void initData() {
		getDataIntent();
		// String mImageurl = MyURL.LOGO_URL;
		// mUMImage = new UMImage(this, mImageurl);
		initGoodsList();
		if (mUser.isIs_login()) {
			initPopularizeinfo();
		}
	}

	private void getDataIntent() {
		Intent intent = getIntent();
		mCatId = intent.getStringExtra("cat_id");
		LogUtils.logd(TAG, "cat_id:" + mCatId);

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
						WLGoodsDetailsActivity.this.finish();
					}
				});

		mSildingFinishLayout.setTouchView(mListView);
	}

	private void initGoodsList() {
		NetHelper.getNewGoodsList(mCatId, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				LogUtils.logd(TAG, "getNewGoodsList+onSuccess");
				resolveNewGoodsList(responseBody);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				LogUtils.logd(TAG, "getNewGoodsList+onFailure");
			}
		});
	}

	private void resolveNewGoodsList(byte[] responseBody) {
		try {
			String json = new String(responseBody, "UTF-8");
			LogUtils.logd(TAG, "NewGoodsList+json:" + json);
			JSONObject JSONObject = new JSONObject(json);
			JSONObject result = JSONObject.optJSONObject("result");
			JSONObject cat = result.optJSONObject("cat");
			String cat_name = cat.optString("cat_name");
			mTitle.setText(cat_name);
			JSONArray content = cat.optJSONArray("content");
			GoodsDetailsListAdapter mAdapter = new GoodsDetailsListAdapter(
					this, WLGoodsDetailsActivity.this, content);
			mListView.setAdapter(mAdapter);
			mDataPopupWindow = result.optJSONArray("items");
			if (mDataPopupWindow.length() != 0) {
				mCustomMade.setClickable(true);
				mCustomMade.setBackgroundColor(Color.parseColor("#70CDB4"));
			} else {
				ToastUtils.show(R.string.temporary_no_data);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.ok_btn:
				mUser = UserController.getInstance(MyApplication.getContext())
						.getUserInfo();
				if (mUser.isIs_login()) {
					LogUtils.logd(TAG, "_ok_btn_data:" + mDataIntent);
					if (!StringUtils.isEmpty(mDataIntent)) {
						gotoChoicePicture(mDataIntent);
						mPopupWindow.dismiss();
						mDataIntent = null;
						finish();
					} else {
						ToastUtils.show(R.string.please_choose_plan);
					}
				} else {
					Intent intent = new Intent(MyApplication.getContext(),
							LoginActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					AndroidUtils.enterActvityAnim(WLGoodsDetailsActivity.this);
				}
				break;
			default:
				break;
			}
		}
	};

	private void gotoChoicePicture(String data) {
		Intent intent = new Intent(this, ChoicePictureActivity.class);
		intent.putExtra("data", data);
		startActivity(intent);
		AndroidUtils.enterActvityAnim(this);
	}

	/**
	 * 弹出层 选材质 选型号
	 * 
	 * @author heshicaihao
	 * 
	 */
	class MyPopupWindow extends PopupWindow {

		public PopupListAdapter mPopupAdapter;
		public Activity context;

		public MyPopupWindow(Activity context, OnClickListener itemsOnClick) {
			super(context);
			this.context = context;
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mMenuView = inflater.inflate(
					R.layout.view_popup_window_goods_detaile, null);
			initView(mMenuView, itemsOnClick);
		}

		/**
		 * 找ID
		 * 
		 * @param view
		 * @param itemsOnClick
		 */
		private void initView(View view, OnClickListener itemsOnClick) {
			mOKBtn = (TextView) view.findViewById(R.id.ok_btn);
			mPopupListview = (MyListView) view
					.findViewById(R.id.popup_list_view);
			mOKBtn.setOnClickListener(itemsOnClick);
			setPopupWindow();
			setMenuTouch();

			if (mDataPopupWindow.length() != 0) {
				mPopupAdapter = new PopupListAdapter(context, mDataPopupWindow);
				mPopupListview.setAdapter(mPopupAdapter);
				try {
					JSONObject object = (JSONObject) mDataPopupWindow.get(0);
					String goods_id = object.optString("goods_id");
					String template_id = object.optString("template_id");
					mDataIntent = getData(goods_id, template_id);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			setFristOnItemClick();

		}

		/**
		 * 设置 材质的 ItemClick 事件
		 */
		private void setFristOnItemClick() {
			mPopupListview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					mFirstPosition = position;
					JSONObject object;
					try {
						object = (JSONObject) mDataPopupWindow.get(position);
						String goods_id = object.optString("goods_id");
						String template_id = object.optString("template_id");
						mDataIntent = getData(goods_id, template_id);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					LogUtils.logd(TAG, "mFirstPosition:" + mFirstPosition);
					mPopupAdapter.setSeclection(position);
					mPopupAdapter.notifyDataSetInvalidated();
				}
			});
		}

		/**
		 * 拼Json 给编辑图片页
		 * 
		 * @param goods_id
		 * @param template_id
		 * @return
		 */
		private String getData(String goods_id, String template_id) {
			JSONObject object = new JSONObject();
			try {
				object.put("goods_id", goods_id);
				object.put("template_id", template_id);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			String data = object.toString();
			return data;
		}

		/**
		 * 设置Touch PopupWindow
		 */
		private void setMenuTouch() {
			// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
			mMenuView.setOnTouchListener(new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {

					int height = mMenuView.findViewById(R.id.pop_layout)
							.getTop();
					int y = (int) event.getY();
					if (event.getAction() == MotionEvent.ACTION_UP) {
						if (y < height) {
							mDataIntent = null;
							dismiss();
						}
					}
					return true;
				}
			});
		}

		/**
		 * 设置PopupWindow
		 */
		private void setPopupWindow() {
			// 设置SelectPicPopupWindow的View
			this.setContentView(mMenuView);
			// 设置SelectPicPopupWindow弹出窗体的宽
			this.setWidth(LayoutParams.FILL_PARENT);
			// 设置SelectPicPopupWindow弹出窗体的高
			this.setHeight(LayoutParams.WRAP_CONTENT);
			// 设置SelectPicPopupWindow弹出窗体可点击
			this.setFocusable(true);
			// 设置SelectPicPopupWindow弹出窗体动画效果
			this.setAnimationStyle(R.style.animbottom);
			// 实例化一个ColorDrawable颜色为半透明
			ColorDrawable dw = new ColorDrawable(0xb0000000);
			// 设置SelectPicPopupWindow弹出窗体的背景
			this.setBackgroundDrawable(dw);
			// 因为某些机型是虚拟按键的,所以要加上以下设置防止挡住按键.
			this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
			this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		}

	}

	/**
	 * 材质 内容的 适配器
	 * 
	 * @author PC
	 * 
	 */
	class PopupListAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		private JSONArray mList;
		private int clickTemp = 0;// 选中的位置

		public PopupListAdapter(Context context, JSONArray list) {
			this.mList = list;
			this.mInflater = LayoutInflater.from(context);
		}

		public void setSeclection(int position) {
			clickTemp = position;
		}

		@Override
		public int getCount() {
			int count = mList == null ? 0 : mList.length();
			return count;
		}

		@Override
		public Object getItem(int position) {
			try {
				return mList == null ? null : mList.get(position);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			PopupHolder holder = null;
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.item_popup_window_list, null);
				holder = new PopupHolder();
				holder.titleImageView = (ImageView) convertView
						.findViewById(R.id.title_iv);
				holder.titleTextView = (TextView) convertView
						.findViewById(R.id.title_tv);
				holder.contentTextView = (TextView) convertView
						.findViewById(R.id.content_tv);
				holder.frameRelativeLayout = (RelativeLayout) convertView
						.findViewById(R.id.frame_rl);
				holder.iconImageView = (ImageView) convertView
						.findViewById(R.id.icon_iv);

				convertView.setTag(holder);
			} else {
				holder = (PopupHolder) convertView.getTag();
			}
			if (clickTemp == position) {
				holder.titleImageView.setImageResource(R.mipmap.pic_pressed);
				holder.frameRelativeLayout
						.setBackgroundResource(R.drawable.plan_bg_pressed);
				holder.iconImageView.setVisibility(View.VISIBLE);
				holder.titleTextView.setTextColor(Color.parseColor("#FFAC13"));
				holder.contentTextView
						.setTextColor(Color.parseColor("#FFAC13"));
			} else {
				holder.titleImageView.setImageResource(R.mipmap.pic_normal);
				holder.frameRelativeLayout
						.setBackgroundResource(R.drawable.plan_bg_normal);
				holder.iconImageView.setVisibility(View.GONE);
				holder.titleTextView.setTextColor(Color.parseColor("#666666"));
				holder.contentTextView
						.setTextColor(Color.parseColor("#666666"));
			}

			try {
				JSONObject object = (JSONObject) mList.get(position);
				String title = object.optString("title");
				holder.titleTextView.setText(title);
				String title_content = object.optString("title_content");
				holder.contentTextView.setText(title_content);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return convertView;
		}
	}

	private void initPopularizeinfo() {
		String promotecode = AndroidUtils.getPromoteCodeInfo(mUser.getId());
		if (StringUtils.areEmpty(promotecode)) {
			NetHelper.getPromoteInfo(mUser.getId(),
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							LogUtils.logd(TAG, "initPopularizeinfo+onSuccess");
							LogUtils.logd(TAG, "mUser.getId()" + mUser.getId());
							resolvePromoteInfo(arg2);

						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							LogUtils.logd(TAG, "initPopularizeinfo+onFailure");

						}
					});
		}
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
				String mPromoteCode = result.optString("promote_code");
				AndroidUtils.savePromoteCodeInfo(mUser.getId(), mPromoteCode);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
