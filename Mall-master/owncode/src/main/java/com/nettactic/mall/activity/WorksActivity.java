package com.nettactic.mall.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heshicai.hao.xutils.BitmapUtils;
import com.heshicai.hao.xutils.bitmap.BitmapDisplayConfig;
import com.heshicai.hao.xutils.bitmap.core.BitmapSize;
import com.nettactic.mall.R;
import com.nettactic.mall.adapter.WorksHolder;
import com.nettactic.mall.base.BaseActivity;
import com.nettactic.mall.bean.UserBean;
import com.nettactic.mall.bean.WorksInfoBean;
import com.nettactic.mall.cache.ACache;
import com.nettactic.mall.common.MyApplication;
import com.nettactic.mall.common.UserController;
import com.nettactic.mall.constants.MyConstants;
import com.nettactic.mall.database.SelectPicSQLHelper;
import com.nettactic.mall.database.WorksSQLHelper;
import com.nettactic.mall.dialog.CustomDialog;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.BitmapHelp;
import com.nettactic.mall.utils.FileUtil;
import com.nettactic.mall.utils.LogUtils;
import com.nettactic.mall.utils.StringUtils;
import com.nettactic.mall.utils.ToastUtils;

/**
 * 作品列表页
 * 
 * @author heshicaihao
 */
@SuppressLint("UseSparseArrays")
public class WorksActivity extends BaseActivity implements OnClickListener,
		OnItemClickListener {

	private int mCoutChoice = 0;

	private ImageView mBack;
	private TextView mTitle;
	private TextView mSave;
	private TextView mChoiceTV;
	private TextView mNowBuy;
	private ImageView mChoiceBtn;
	private Button mNullLeftBt;
	private Button mGotoHomeBt;
	private RelativeLayout mChoiceRL;
	private static RelativeLayout mViewNull;
	private static RelativeLayout mViewBuy;
	private static ListView mListView;

	private ArrayList<String> mWorksIds = new ArrayList<String>();
	private List<WorksInfoBean> mlist;
	private WorksAdapter mAdapter;
	private UserBean mUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_works);
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
		case R.id.save:
			// mWorksIds
			showAlertDialog();
			break;
		case R.id.now_buy:
			if (AndroidUtils.isNoFastClick()) {
				if (!AndroidUtils.isNetworkAvailable(this)) {
					ToastUtils.show(R.string.no_net);
				} else {
					gotoSubmitOrder();
				}
			}
			break;
		case R.id.choice_rl:
			ChoiceWorks();
			break;
		case R.id.goto_home_bt:
			AndroidUtils.finishActivity(this);
			break;
		case R.id.null_left_bt:
			AndroidUtils.finishActivity(this);
			break;
		case R.id.back:
			AndroidUtils.finishActivity(this);

			break;
		default:
			break;
		}
	}

	/**
	 * 删除对话框
	 * 
	 */
	private void showAlertDialog() {
		CustomDialog.Builder builder = new CustomDialog.Builder(
				WorksActivity.this);
		builder.setMessage(this.getString(R.string.delete_works));
		builder.setPositiveButton(this.getString(R.string.ok),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						for (int i = 0; i < mWorksIds.size(); i++) {
							String work_id = mWorksIds.get(i);
							WorksSQLHelper.deleteWorks(work_id);
							SelectPicSQLHelper.deleteWorkid(work_id);
							FileUtil.deleteWorks(work_id);
						}
						getWorksData();
						dialog.dismiss();
					}
				});

		builder.setNegativeButton(this.getString(R.string.cancel),
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						LogUtils.logd(TAG, "position:");
						dialog.dismiss();
					}
				});
		builder.create().show();

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long arg3) {
		WorksHolder holder = (WorksHolder) view.getTag();
		// 在每次获取点击的item时改变checkbox的状态
		holder.checkbox.toggle();
		WorksAdapter.mIsSelecteds.put(position, holder.checkbox.isChecked()); // 同时修改map的值保存状态
		if (holder.checkbox.isChecked() == true) {
			mWorksIds.add(mlist.get(position).getWorkid());
		} else {
			mWorksIds.remove(mlist.get(position).getWorkid());
		}

		int allKey = WorksAdapter.mIsSelecteds.size();
		int trueCount = 0;

		for (int i = 0; i < allKey; i++) {
			if (WorksAdapter.mIsSelecteds.get(i)) {
				trueCount++;
			}
		}
		if (allKey <= trueCount) {
			// 所有的value都是true
			mChoiceTV.setText(R.string.all_no_choice);
			mChoiceBtn.setImageResource(R.mipmap.checkbox_pressed);
		}
		if (trueCount == 0) {
			mChoiceTV.setText(R.string.all_choice);
			mChoiceBtn.setImageResource(R.mipmap.checkbox_normal);
		}

	}

	private void ChoiceWorks() {
		mCoutChoice++;
		if (mCoutChoice % 2 == 1) {
			mWorksIds = new ArrayList<String>();
			for (int i = 0; i < mlist.size(); i++) {
				WorksAdapter.mIsSelecteds.put(i, true);
				mWorksIds.add(mlist.get(i).getWorkid());
			}
			mAdapter.notifyDataSetChanged();
			mChoiceTV.setText(R.string.all_no_choice);
			mChoiceBtn.setImageResource(R.mipmap.checkbox_pressed);
		} else if (mCoutChoice % 2 == 0) {
			for (int i = 0; i < mlist.size(); i++) {
				if (WorksAdapter.mIsSelecteds.get(i) == true) {
					WorksAdapter.mIsSelecteds.put(i, false);
					mWorksIds.remove(mlist.get(i).getWorkid());
				}
			}
			mAdapter.notifyDataSetChanged();
			mChoiceTV.setText(R.string.all_choice);
			mChoiceBtn.setImageResource(R.mipmap.checkbox_normal);
		}
	}

	private void gotoSubmitOrder() {

		if (mWorksIds.size() == 0) {
			ToastUtils.show(R.string.choice_works);
			return;
		}
		showmeidialog();
		Intent intent = new Intent(this, SubmitOrderActivity.class);
		intent.putExtra(MyConstants.WORKS_IDS, mWorksIds);
		// ArrayList<WorksInfoBean> mListData = (ArrayList<WorksInfoBean>)
		// DataHelper
		// .getInstance(getApplication()).queryWorks(mWorksIds);
		startActivity(intent);
		LogUtils.logd(TAG, "end");
		// mListData.clear();
		mWorksIds.clear();
		for (int i = 0; i < mlist.size(); i++) {
			if (WorksAdapter.mIsSelecteds.get(i) == true) {
				WorksAdapter.mIsSelecteds.put(i, false);
				mWorksIds.remove(mlist.get(i).getWorkid());
			}
		}
		mAdapter.notifyDataSetChanged();
		mChoiceTV.setText(R.string.all_choice);
		mChoiceBtn.setImageResource(R.mipmap.checkbox_normal);
		dismissmeidialog();
		AndroidUtils.enterActvityAnim(this);

	}

	private void initView() {
		mBack = (ImageView) findViewById(R.id.back);
		mTitle = (TextView) findViewById(R.id.title);
		mSave = (TextView) findViewById(R.id.save);
		mChoiceTV = (TextView) findViewById(R.id.checkbox_text);
		mNowBuy = (TextView) findViewById(R.id.now_buy);
		mChoiceBtn = (ImageView) findViewById(R.id.all_choice);
		mChoiceRL = (RelativeLayout) findViewById(R.id.choice_rl);
		mViewNull = (RelativeLayout) findViewById(R.id.in_view_null);
		mViewBuy = (RelativeLayout) findViewById(R.id.in_view_buy);
		mNullLeftBt = (Button) findViewById(R.id.null_left_bt);
		mGotoHomeBt = (Button) findViewById(R.id.goto_home_bt);
		mListView = (ListView) findViewById(R.id.listview);

		mSave.setText(R.string.delete);
		mSave.setVisibility(View.GONE);
		mTitle.setText(R.string.my_works);
		mViewNull.setVisibility(View.VISIBLE);
		mViewBuy.setVisibility(View.GONE);
		mListView.setVisibility(View.GONE);

		mBack.setOnClickListener(this);
		mSave.setOnClickListener(this);
		mNowBuy.setOnClickListener(this);
		mChoiceRL.setOnClickListener(this);
		mNullLeftBt.setOnClickListener(this);
		mGotoHomeBt.setOnClickListener(this);
		mListView.setOnItemClickListener(this);

	}

	private void initData() {

		getWorksData();
	}

	/***
	 * 获取作品信息并展示
	 * 
	 */
	private void getWorksData() {
		if (mUser.isIs_login()) {
			mlist = WorksSQLHelper.queryWorksByUserId(mUser.getId());
			Collections.sort(mlist, new WorksInfoBean());
			if (mlist.size() != 0) {
				mAdapter = new WorksAdapter(this, MyApplication.getContext(),
						mlist);
				mListView.setAdapter(mAdapter);
				mChoiceTV.setText(R.string.all_choice);
				mChoiceBtn.setImageResource(R.mipmap.checkbox_normal);

				mSave.setVisibility(View.VISIBLE);
				mViewNull.setVisibility(View.GONE);
				mViewBuy.setVisibility(View.VISIBLE);
				mListView.setVisibility(View.VISIBLE);
			} else {
				mSave.setVisibility(View.GONE);
				mViewNull.setVisibility(View.VISIBLE);
				mViewBuy.setVisibility(View.GONE);
				mListView.setVisibility(View.GONE);
			}
		} else {
			mSave.setVisibility(View.GONE);
			mViewNull.setVisibility(View.VISIBLE);
			mViewBuy.setVisibility(View.GONE);
			mListView.setVisibility(View.GONE);
		}
	}

	public static class WorksAdapter extends BaseAdapter {
		private String TAG = "WorksAdapter";
		public static HashMap<Integer, Boolean> mIsSelecteds;
		private List<WorksInfoBean> mData;

		private Context mContext;
		private Activity mActivity;
		private LayoutInflater mInflater;
		private WorksHolder mHolder;

		private BitmapUtils bitmapUtils;
		private BitmapDisplayConfig bigPicDisplayConfig;
		private ACache mCache;

		public WorksAdapter(Activity mActivity, Context context,
				List<WorksInfoBean> mData) {
			this.mData = mData;
			this.mContext = context;
			this.mActivity = mActivity;
			mCache = ACache.get(context);
			mInflater = LayoutInflater.from(context);
			initCheckBox();

			int mScreenWidth = AndroidUtils.getScreenWidth(mActivity);
			if (bitmapUtils == null) {
				bitmapUtils = BitmapHelp.getBitmapUtils(mContext);
			}
			bigPicDisplayConfig = new BitmapDisplayConfig();
			bigPicDisplayConfig.setBitmapConfig(Bitmap.Config.RGB_565);
			bigPicDisplayConfig.setBitmapMaxSize(new BitmapSize(
					mScreenWidth / 5, mScreenWidth / 5));
			bitmapUtils.configDefaultLoadingImage(R.mipmap.blank_bg);
			bitmapUtils.configDefaultLoadFailedImage(R.mipmap.blank_bg);
			bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
			bitmapUtils.configDiskCacheEnabled(false);

			// AlphaAnimation 在一些android系统上表现不正常, 造成图片列表中加载部分图片后剩余无法加载, 目前原因不明.
			// 可以模仿下面示例里的fadeInDisplay方法实现一个颜色渐变动画。
			AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
			animation.setDuration(1000);
			bitmapUtils.configDefaultImageLoadAnimation(animation);
		}

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			DeleteOnClickListener deleteListener = null;
			EditOnClickListener editListener = null;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_works_list, null);
				mHolder = new WorksHolder();
				getItemView(convertView);

				convertView.setTag(mHolder);
				deleteListener = new DeleteOnClickListener(position);
				editListener = new EditOnClickListener(position);
				mHolder.delete_Iv.setOnClickListener(deleteListener);
				mHolder.edit_Iv.setOnClickListener(editListener);

			} else {
				mHolder = (WorksHolder) convertView.getTag();
			}
			setData2UI(position);

			return convertView;

		}

		/**
		 * 给UI加载数据
		 * 
		 * @param position
		 */
		private void setData2UI(int position) {
			WorksInfoBean works = mData.get(position);

			String mCoverId = works.getImageurl();
			bitmapUtils.display(mHolder.goods_imageIv, mCoverId,
					bigPicDisplayConfig);

			String goods_name = works.getGoodsname();
			mHolder.goods_nameTv.setText(goods_name);

			String goods_type = works.getType();
			mHolder.goods_typeTv.setText(goods_type);

			String editstate = works.getEditstate();
			if ("0".equals(editstate)) {
				mHolder.edit_Iv.setVisibility(View.VISIBLE);
			} else {
				mHolder.edit_Iv.setVisibility(View.INVISIBLE);
			}

			double price = Double.parseDouble(works.getPrice());
			String subtotalStr = StringUtils.format2point(price + "")
					+ MyApplication.getContext().getString(R.string.yuan);
			mHolder.goods_priceTv.setText(subtotalStr);
			mHolder.checkbox.setChecked(mIsSelecteds.get(position));

		}

		/**
		 * 找到Item 的 控件
		 * 
		 * @param convertView
		 */
		private void getItemView(View convertView) {
			mHolder.checkbox = (CheckBox) convertView
					.findViewById(R.id.checkbox);
			mHolder.goods_imageIv = (ImageView) convertView
					.findViewById(R.id.goods_image);
			mHolder.goods_nameTv = (TextView) convertView
					.findViewById(R.id.goods_name);
			mHolder.goods_typeTv = (TextView) convertView
					.findViewById(R.id.goods_type);
			mHolder.goods_priceTv = (TextView) convertView
					.findViewById(R.id.goods_price);
			mHolder.edit_Iv = (TextView) convertView.findViewById(R.id.edit_iv);
			mHolder.delete_Iv = (TextView) convertView
					.findViewById(R.id.delete_iv);
		}

		// 初始化 设置所有checkbox都为未选择
		@SuppressLint("UseSparseArrays")
		private void initCheckBox() {
			mIsSelecteds = new HashMap<Integer, Boolean>();
			for (int i = 0; i < mData.size(); i++) {
				mIsSelecteds.put(i, false);
			}
		}

		/**
		 * 删除对话框
		 * 
		 * @param position
		 */
		private void showAlertDialog(final int position) {
			CustomDialog.Builder builder = new CustomDialog.Builder(mActivity);
			builder.setMessage(mContext.getString(R.string.delete_works));
			builder.setPositiveButton(mContext.getString(R.string.ok),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							WorksInfoBean works = mData.get(position);
							String workid = works.getWorkid();
							WorksSQLHelper.deleteWorks(works);
							SelectPicSQLHelper.deleteWorkid(workid);
							FileUtil.deleteWorks(mData.get(position)
									.getWorkid());
							mData.remove(position);
							if (mData.size() == 0) {
								mViewNull.setVisibility(View.VISIBLE);
								mViewBuy.setVisibility(View.GONE);
								mListView.setVisibility(View.GONE);
							}
							notifyDataSetChanged();
							dialog.dismiss();

						}
					});

			builder.setNegativeButton(mContext.getString(R.string.cancel),
					new android.content.DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							LogUtils.logd(TAG, "position:");
							dialog.dismiss();
						}
					});
			builder.create().show();

		}

		private class DeleteOnClickListener implements OnClickListener {
			int mPosition;

			public DeleteOnClickListener(int inPosition) {
				mPosition = inPosition;
			}

			@Override
			public void onClick(View v) {
				showAlertDialog(mPosition);
			}

		}

		private class EditOnClickListener implements OnClickListener {
			int mPosition;

			public EditOnClickListener(int inPosition) {
				mPosition = inPosition;
			}

			@Override
			public void onClick(View v) {
				WorksInfoBean works = mData.get(mPosition);
				mCache.put(MyConstants.WORKID, works.getWorkid());
				// mCache.put(MyConstants.GOODSNAME, works.getGoodsname());
				mCache.put(MyConstants.TOTALPAGE, works.getJsonstring());
				mCache.put(MyConstants.TEMPLATEID, works.getPicurl());
				LogUtils.logd(TAG, "templateid:" + works.getPicurl());
				Intent intent = new Intent(mContext, BrowseActivity.class);
				mActivity.startActivity(intent);
				AndroidUtils.enterActvityAnim(mActivity);
			}
		}
	}

}
