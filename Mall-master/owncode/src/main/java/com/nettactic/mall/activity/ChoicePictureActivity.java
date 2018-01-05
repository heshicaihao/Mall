package com.nettactic.mall.activity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.heshicai.hao.xutils.BitmapUtils;
import com.heshicai.hao.xutils.bitmap.BitmapDisplayConfig;
import com.heshicai.hao.xutils.bitmap.core.BitmapSize;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nettactic.mall.R;
import com.nettactic.mall.adapter.ChoicePictureGridViewHolder;
import com.nettactic.mall.adapter.ChoicePictureListViewHolder;
import com.nettactic.mall.adapter.SelectPicViewHolder;
import com.nettactic.mall.base.BaseActivity;
import com.nettactic.mall.bean.ImageBean;
import com.nettactic.mall.bean.SelectpicBean;
import com.nettactic.mall.bean.UserBean;
import com.nettactic.mall.cache.ACache;
import com.nettactic.mall.cache.JsonDao;
import com.nettactic.mall.common.MyApplication;
import com.nettactic.mall.common.UserController;
import com.nettactic.mall.constants.MyConstants;
import com.nettactic.mall.database.SelectPicSQLHelper;
import com.nettactic.mall.dialog.TemplateCustomDialog;
import com.nettactic.mall.net.NetHelper;
import com.nettactic.mall.picdeal.DealTask;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.BitmapHelp;
import com.nettactic.mall.utils.DateFormatUtils;
import com.nettactic.mall.utils.FileUtil;
import com.nettactic.mall.utils.GUID;
import com.nettactic.mall.utils.JSONUtil;
import com.nettactic.mall.utils.LogUtils;
import com.nettactic.mall.utils.ServiceUtils;
import com.nettactic.mall.utils.StringUtils;
import com.nettactic.mall.utils.ToastUtils;
import com.nettactic.mall.widget.ErrorHintView;
import com.nettactic.mall.widget.ErrorHintView.OperateListener;
import com.nettactic.mall.widget.MyGridView;

/**
 * 选择用户图片页
 * 
 * @author heshicaihao
 */
@SuppressLint("HandlerLeak")
@SuppressWarnings("deprecation")
public class ChoicePictureActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {

	private static final int IMAGE_OK = 1001;
	private static final int DOWNLOAD_OK = 1002;
	private static final int Dialog_OK = 1003;
	private static final int VIEW_LIST = 1;
	private static final int VIEW_WIFIFAILUER = 2;
	private static final int VIEW_LOADFAILURE = 3;
	private static final int VIEW_LOADING = 4;
	private int mCoutOK = 3;
	private int mDownloadCout = 0;
	private int mTemplateCount = 0;
	private int mPage = 0;
	private String mWorkId;
	private String mTotalpage;
	private String mMasklayoutheight;
	private String mBackupMasklayoutheight;
	private String mMaskwidth;
	private String mMaskheight;
	private String mBackupMaskwidth;
	private String mBackupMaskheight;
	private String mAccountId;
	private String mToken;
	private String mTemplateId;
	private String mGoodsId;
	private String mTemplatePath;
	private String mFrameMaskUrl;
	private String mBackupframeMaskUrl;
	private TreeMap<Integer, List<String>> mGruopMap = new TreeMap<Integer, List<String>>();
	private List<String> mSelectPaths = new ArrayList<String>();
	private List<String> mListCard;

	private ImageView mBack;
	private ImageView mCheckBox;
	private TextView mTitle;
	private TextView mSave;
	private View mBlankView;
	private ListView mListView;
	private GridView mGridView;
	private LinearLayout mContentView;
	private ErrorHintView mErrorHintView;
	private HorizontalScrollView mHScrollview;
	private TemplateCustomDialog mTemplateCustomDialog;
	private MyListAdapter mListAdapter;
	private SelectPicAdapter mGridAdapter = new SelectPicAdapter();
	private ACache mCache;
	private UserBean mUser;
	private Handler mDownloadHandler;
	private Handler mDialogHandler;
	private Handler mImageHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choice_picture);
		ServiceUtils.startUploadService(this, ChoicePictureActivity.this);
		ServiceUtils.startDealService(this, ChoicePictureActivity.this);
		mUser = UserController.getInstance(this).getUserInfo();
		mAccountId = mUser.getId();
		mToken = mUser.getToken();
		initView();
		initData();

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String path = mSelectPaths.get(position);
		IsExist(path);
		IsShowHScrollview();
		setGridAdapter(mSelectPaths);
		mListAdapter.notifyDataSetChanged();
		setTitleInfo();

	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.save:
			gotoSave();
			break;

		case R.id.back:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mDownloadCout = 0;

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mTemplateCustomDialog != null
					&& mTemplateCustomDialog.isShowing()) {
				mTemplateCustomDialog.show();
				SelectPicSQLHelper.deleteWorkid(mWorkId);
				FileUtil.deleteWorks(mWorkId);
				finish();
			}
		}
		return false;
	}

	// 返回键按下
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	private void gotoBrowse() {
		Intent intent = new Intent(this, BrowseActivity.class);
		startActivity(intent);
		AndroidUtils.enterActvityAnim(this);
		finish();
	}

	private void initView() {

		mTitle = (TextView) findViewById(R.id.title);
		mBack = (ImageView) findViewById(R.id.back);
		mSave = (TextView) findViewById(R.id.save);
		mErrorHintView = (ErrorHintView) findViewById(R.id.hint_view);
		mContentView = (LinearLayout) findViewById(R.id.content_view);
		mListView = (ListView) findViewById(R.id.popup_list_view);
		mBlankView = (View) findViewById(R.id.view_back);
		mHScrollview = (HorizontalScrollView) findViewById(R.id.h_scrollview);
		mGridView = (GridView) findViewById(R.id.gridview);

		mHScrollview.setHorizontalScrollBarEnabled(false);
		mSave.setText(R.string.ok);
		mSave.setVisibility(View.VISIBLE);
		IsShowHScrollview();
		mGridView.setOnItemClickListener(this);
		mBack.setOnClickListener(this);
		mSave.setOnClickListener(this);
		mTemplateCustomDialog = new TemplateCustomDialog(this);
		mTemplateCustomDialog.setCanceledOnTouchOutside(false);

	}

	/**
	 * 校对 是否显示 选中mGridView
	 * 
	 */
	private void IsShowHScrollview() {
		if (mSelectPaths.size() == 0) {
			mHScrollview.setVisibility(View.GONE);
			mBlankView.setVisibility(View.GONE);
		} else {
			mHScrollview.setVisibility(View.VISIBLE);
			mBlankView.setVisibility(View.VISIBLE);
		}
	}

	private void initData() {
		mDownloadCout = 0;
		showLoading(VIEW_LOADING);
		getDataCache();
		getDataIntent();
		// 获取图片信息
		getImagesFromSD();
		setListAdapter();

		getDataNet();

	}

	private void getDataIntent() {
		Intent intent = getIntent();
		String data = intent.getStringExtra("data");
		if (data != null) {
			try {
				JSONObject obj = new JSONObject(data);
				mTemplateId = obj.optString("template_id");
				LogUtils.logd(TAG, "mTemplateId:" + mTemplateId);
				mCache.put(MyConstants.TEMPLATEID, mTemplateId);
				mGoodsId = obj.optString("goods_id");
				mCache.put(MyConstants.GOODSID, mGoodsId);
				LogUtils.logd(TAG, "mGoodsId:" + mGoodsId);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 请求网络
	 * 
	 */
	private void getDataNet() {

		initWorkId();
		initTemplate();
		initGoodsInfo(mGoodsId);
		refreshUI();

	}

	/**
	 * 
	 * 获取模板信息
	 */
	private void initTemplate() {
		mTemplatePath = FileUtil.getFilePath(MyConstants.TEMPLATE, mTemplateId,
				MyConstants.JSON);
		if (FileUtil.fileIsExists(mTemplatePath)) {
			getSDTemplateData();
		} else {
			initTemplateInfo(mTemplateId);
		}
	}

	private void getDataCache() {
		mCache = ACache.get(this);
	}

	/**
	 * 设置GridView的Adapter
	 */
	private void setGridAdapter(List<String> mSelectPaths) {
		float screenensity = AndroidUtils.getScreenensity(this);
		int cWidth = (int) (MyConstants.EDITBOTTOM_WIDTH * screenensity);
		int hSpacing = (int) (2 * screenensity);
		mGridAdapter = new SelectPicAdapter(this, ChoicePictureActivity.this,
				mSelectPaths);
		mGridView.setAdapter(mGridAdapter);
		int mGridwidth = mGridAdapter.getCount() * (cWidth + hSpacing * 2);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				mGridwidth, LinearLayout.LayoutParams.WRAP_CONTENT);
		mGridView.setLayoutParams(params);
		mGridView.setColumnWidth(cWidth + hSpacing);
		mGridView.setHorizontalSpacing(hSpacing);
		mGridView.setStretchMode(GridView.NO_STRETCH);
		mGridView.setNumColumns(mGridAdapter.getCount());
		setScrollRight(mGridwidth);

	}

	@SuppressLint("HandlerLeak")
	private void setListAdapter() {
		mImageHandler = new Handler() {
			@Override
			// 扫描完成之后，发送的消息被handleMessage接收
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case IMAGE_OK:
					mListAdapter = new MyListAdapter(
							ChoicePictureActivity.this,
							getApplicationContext(),
							subGroupOfImage(mGruopMap), mGruopMap);
					mListView.setAdapter(mListAdapter);
					break;
				}
			}
		};
	}

	/**
	 * 获取数据
	 * 
	 * @param gruopMap
	 * @return
	 */
	protected List<ImageBean> subGroupOfImage(
			TreeMap<Integer, List<String>> gruopMap) {
		if (gruopMap.size() == 0) {
			return null;
		}
		List<ImageBean> list = new ArrayList<ImageBean>();
		Iterator<Entry<Integer, List<String>>> it = gruopMap.entrySet()
				.iterator();
		while (it.hasNext()) {
			Entry<Integer, List<String>> entry = it.next();
			ImageBean mImageBean = new ImageBean();
			Integer key = entry.getKey();
			List<String> value = entry.getValue();
			mImageBean.setFolderName(key + "");
			mImageBean.setImageCount(value.size());
			mImageBean.setPaths(value);
			mImageBean.setTopImagePath(value.get(0));// 获取该组的第一张图片
			list.add(mImageBean);
		}
		List<ImageBean> mListImage = new ArrayList<ImageBean>();
		for (int i = 0; i < list.size(); i++) {
			mListImage.add(list.get(list.size() - i - 1));
		}

		return mListImage;
	}

	/**
	 * 获取图片信息
	 */
	private void getImagesFromSD() {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return;
		}
		// 显示进度条
		new Thread(new Runnable() {
			@Override
			public void run() {
				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = getApplicationContext()
						.getContentResolver();
				// 只查询jpeg和png的图片
				Cursor mCursor = mContentResolver.query(mImageUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=?",
						new String[] { "image/jpeg", "image/png" },
						MediaStore.Images.Media.DATE_MODIFIED);

				while (mCursor.moveToNext()) {
					String path = mCursor.getString(mCursor
							.getColumnIndex(MediaStore.Images.Media.DATA));
					long longDate = mCursor.getLong(mCursor
							.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN));
					String transferLongToDate = DateFormatUtils
							.transferLongToDate("yyyyMMdd", longDate);
					int date = Integer.parseInt(transferLongToDate);
					// 根据父路径名将图片放入到mGruopMap中
					if (!mGruopMap.containsKey(date)) {
						List<String> chileList = new ArrayList<String>();
						chileList.add(path);
						mGruopMap.put(date, chileList);
					} else {
						mGruopMap.get(date).add(path);
					}
				}
				mCursor.close();
				// 通知Handler扫描图片完成
				mImageHandler.sendEmptyMessage(IMAGE_OK);
			}
		}).start();
	}

	/**
	 * 设置滚动到最有右边
	 * 
	 * @param X
	 */
	private void setScrollRight(final int X) {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				mHScrollview.smoothScrollTo(X, 0);
			}
		}, 100L);
	}

	/**
	 * 判断是否选中，没有选中的 就选中；选中的就不选中
	 * 
	 * @param path
	 */
	private void IsExist(String path) {
		boolean contains = mSelectPaths.contains(path);
		if (contains) {
			mSelectPaths.remove(path);
		} else {
			mSelectPaths.add(path);
		}
	}

	private void setTitleInfo() {
		int size = mGridAdapter.getCount();
		String mTitle1;
		if (size == 0) {
			mTitle1 = "";
		} else {
			mTitle1 = "已选" + size + "张/";
		}
		String mTitle2 = "可选" + mTotalpage + "张";
		mTitle.setText(mTitle1 + mTitle2);
	}

	/**
	 * 选择图片日期的List 的适配器
	 * 
	 * @author heshicaihao
	 */
	class MyListAdapter extends BaseAdapter {

		private List<ImageBean> mList;
		private TreeMap<Integer, List<String>> mGroup;
		private List<String> mChildList = new ArrayList<String>();

		private Activity mActivity;
		private Context mContext;
		private LayoutInflater mInflater;
		private ChildGridAdapter mChildAdapter;

		private int mMinPicWidth = MyConstants.INIT_MINWIDTH;
		private int mMinPicHeight = MyConstants.INIT_MINHEIGHT;

		public MyListAdapter(Activity activity, Context context,
				List<ImageBean> list, TreeMap<Integer, List<String>> gruopMap) {
			this.mList = list;
			this.mGroup = gruopMap;
			this.mContext = context;
			this.mActivity = activity;
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			int count = mList == null ? 0 : mList.size();
			return count;
		}

		@Override
		public Object getItem(int position) {
			return mList == null ? null : mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ChoicePictureListViewHolder holder = null;
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.item_choice_picture_list, null);
				holder = new ChoicePictureListViewHolder();
				holder.mTitle = (TextView) convertView.findViewById(R.id.date);
				holder.mGrid = (MyGridView) convertView
						.findViewById(R.id.mGrid);
				convertView.setTag(holder);
			} else {
				holder = (ChoicePictureListViewHolder) convertView.getTag();
			}

			ImageBean mImageBean = mList.get(position);
			final List<String> mPaths = mImageBean.getPaths();
			String folderName = mImageBean.getFolderName();
			String mTitleString = folderName.substring(0, 4) + "\n"
					+ folderName.substring(4, folderName.length());
			holder.mTitle.setText(mTitleString);
			mChildList = mGroup.get(Integer.parseInt(mList.get(position)
					.getFolderName()));
			mChildAdapter = new ChildGridAdapter(mActivity, mContext,
					mChildList);
			holder.mGrid.setAdapter(mChildAdapter);
			holder.mGrid.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					mCheckBox = (ImageView) view.findViewById(R.id.checkbox);
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					BitmapFactory.decodeFile(mPaths.get(position), options);
					int outWidth = options.outWidth;
					int outHeight = options.outHeight;

					if (mCache.getAsString(MyConstants.MINWIDTH) != null) {
						mMinPicWidth = Integer.parseInt(mCache
								.getAsString(MyConstants.MINWIDTH));
					}
					if (mCache.getAsString(MyConstants.MINHEIGHT) != null) {
						mMinPicHeight = Integer.parseInt(mCache
								.getAsString(MyConstants.MINHEIGHT));
					}
					if (outWidth > mMinPicWidth && outHeight > mMinPicHeight) {
						String oldpath = mPaths.get(position);
						boolean contains = mSelectPaths.contains(oldpath);
						if (mSelectPaths.size() < Integer.parseInt(mTotalpage)) {
							if (contains) {
								mSelectPaths.remove(oldpath);
								mCheckBox
										.setImageResource(R.mipmap.checkbox_normal);
								LogUtils.logd(TAG, "checkbox_normal");
							} else {
								mSelectPaths.add(oldpath);
								mCheckBox
										.setImageResource(R.mipmap.checkbox_pressed);
								LogUtils.logd(TAG, "checkbox_pressed");
							}

							IsShowHScrollview();
							setGridAdapter(mSelectPaths);
							setTitleInfo();
							gotoTask(oldpath);

						} else {
							if (contains) {
								mCheckBox
										.setImageResource(R.mipmap.checkbox_normal);
								LogUtils.logd(TAG, "else+checkbox_normal");
								mSelectPaths.remove(oldpath);
								IsShowHScrollview();
								setGridAdapter(mSelectPaths);
								setTitleInfo();
								gotoTask(oldpath);
							} else {
								ToastUtils.show(R.string.pic_choice_ok);
							}
						}
					} else {
						ToastUtils.show(R.string.choice_picture_again);
					}

				}
			});
			return convertView;

		}

	}

	/**
	 * 选择图片每一个日期的Grid 的适配器
	 * 
	 * @author heshicaihao
	 */
	class ChildGridAdapter extends BaseAdapter {

		private int mScreenWidth;
		private LayoutInflater mInflater;
		private List<String> mList;

		private Activity mActivity;
		private Context mContext;

		private BitmapUtils bitmapUtils;
		private BitmapDisplayConfig bigPicDisplayConfig;

		public ChildGridAdapter(Activity activity, Context context,
				List<String> list) {
			this.mList = list;
			this.mContext = context;
			this.mActivity = activity;
			this.mInflater = LayoutInflater.from(context);
			this.mScreenWidth = AndroidUtils.getScreenWidth(mActivity);
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
			bitmapUtils.configDefaultShowOriginal(false);
			bitmapUtils.configDiskCacheEnabled(false);
		}

		@Override
		public int getCount() {
			int count = mList == null ? 0 : mList.size();
			return count;
		}

		@Override
		public Object getItem(int position) {
			return mList == null ? null : mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ChoicePictureGridViewHolder holder = null;
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.item_choice_picture_grid, null);
				holder = new ChoicePictureGridViewHolder();
				holder.mImageView = (ImageView) convertView
						.findViewById(R.id.grid_image);
				holder.mGridItem = (RelativeLayout) convertView
						.findViewById(R.id.grid_item);

				holder.checkbox = (ImageView) convertView
						.findViewById(R.id.checkbox);

				LayoutParams lp = holder.mImageView.getLayoutParams();
				int m = AndroidUtils.dip2px(mContext, 103);
				lp.width = (mScreenWidth - m) / 3;
				lp.height = lp.width;
				holder.mImageView.setLayoutParams(lp);
				convertView.setTag(holder);
			} else {
				holder = (ChoicePictureGridViewHolder) convertView.getTag();
			}
			String mPath = mList.get(position);
			if (mSelectPaths.contains(mPath)) {
				holder.checkbox.setImageResource(R.mipmap.checkbox_pressed);
			} else {
				holder.checkbox.setImageResource(R.mipmap.checkbox_normal);
			}
			Glide.with(mContext).load(mPath).placeholder(R.mipmap.blank_bg)
					.into(holder.mImageView);
			return convertView;

		}

	}

	/**
	 * 蒙版的单行适配器
	 * 
	 * @author heshicaihao
	 */
	@SuppressLint("ResourceAsColor")
	class SelectPicAdapter extends BaseAdapter {
		public String TAG = getClass().getName();
		private Context mContext;
		LayoutInflater mInflater;
		List<String> Data = new ArrayList<String>();
		private int clickTemp = -1;// 选中的位置

		public SelectPicAdapter() {

		}

		public SelectPicAdapter(Context c, Activity mActivity,
				List<String> mSelectPaths) {
			mContext = c;
			Data = mSelectPaths;
			mInflater = LayoutInflater.from(mContext);

		}

		@Override
		public int getCount() {

			return Data.size();
		}

		public void setSeclection(int position) {
			clickTemp = position;
		}

		@Override
		public Object getItem(int position) {

			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			SelectPicViewHolder holder;
			if (convertView == null) {
				holder = new SelectPicViewHolder();
				convertView = mInflater.inflate(R.layout.item_mask_grid, null);
				holder.mImageView = (ImageView) convertView
						.findViewById(R.id.mask_grid_image);
				holder.mMask_ll = (RelativeLayout) convertView
						.findViewById(R.id.mask_ll);
			} else {
				holder = (SelectPicViewHolder) convertView.getTag();
			}
			convertView.setTag(holder);
			if (clickTemp == position) {
				convertView.setBackgroundResource(R.color.main_color);
			} else {
				convertView.setBackgroundColor(Color.TRANSPARENT);
			}
			String path = Data.get(position);
			Glide.with(mContext).load(path).placeholder(R.mipmap.blank_bg)
					.into(holder.mImageView);
			return convertView;
		}
	}

	/**
	 * 去下载任务
	 * 
	 * @param oldpath
	 */
	private void gotoTask(String oldpath) {
		String imgid = null;
		List<SelectpicBean> list = SelectPicSQLHelper
				.query2DB(oldpath, mWorkId);
		if (list.size() == 0) {
			imgid = GUID.getGUID();
			SelectpicBean bean = new SelectpicBean();
			bean.setImgid(imgid);
			bean.setWorkid(mWorkId);
			bean.setTotalpage(mTotalpage);
			bean.setOldpath(oldpath);
			bean.setNewpath("");
			bean.setWorkpage("-1");
			bean.setIsselect("false");
			bean.setIscondense("false");
			bean.setIssave("false");
			bean.setIsnet("false");
			bean.setIsupload("false");
			bean.setIsexist("false");
			bean.setIscompute("false");
			bean.setTranslatex("0");
			bean.setTranslatey("0");
			bean.setInitscale("1");
			bean.setScale("1");
			bean.setMasklayoutheight(mMasklayoutheight);
			bean.setMaskheight(mMaskheight);
			bean.setMaskwidth(mMaskwidth);
			bean.setPicheight("");
			bean.setPicwidth("");
			bean.setCutx("");
			bean.setCuty("");
			bean.setCutwidth("");
			bean.setCutheight("");
			bean.setCardinfo("");

			SelectPicSQLHelper.add2DB(bean);
		} else {
			SelectpicBean bean = list.get(0);
			imgid = bean.getImgid();
		}
		MyApplication.getDealTaskManager().addTask(
				new DealTask(imgid, false, DealTask.YSBCSC));
	}

	/**
	 * 显示加载页面
	 * 
	 * @param i
	 */
	private void showLoading(int i) {
		mErrorHintView.setVisibility(View.GONE);
		mContentView.setVisibility(View.GONE);
		switch (i) {
		case VIEW_LIST:
			mErrorHintView.hideLoading();
			mContentView.setVisibility(View.VISIBLE);
			break;

		case VIEW_WIFIFAILUER:
			mErrorHintView.hideLoading();
			mErrorHintView.netError(new OperateListener() {
				@Override
				public void operate() {
					showLoading(VIEW_LOADING);
					getDataNet();
				}
			});
			break;

		case VIEW_LOADFAILURE:
			mErrorHintView.hideLoading();
			mErrorHintView.loadFailure(new OperateListener() {
				@Override
				public void operate() {
					showLoading(VIEW_LOADING);
					getDataNet();
				}
			});

		case VIEW_LOADING:
			mErrorHintView.loadingData();
			break;
		}
	}

	// //////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////

	private void initWorkId() {
		mUser = UserController.getInstance(this).getUserInfo();
		mAccountId = mUser.getId();
		mToken = mUser.getToken();
		NetHelper.getTempWorkId(mAccountId, mToken,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						LogUtils.logd(TAG, "getTempWorkId+onSuccess");
						resolveWorkId(responseBody);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "getTempWorkId+onFailure");
						showLoading(VIEW_WIFIFAILUER);
					}
				});
	}

	/**
	 * 初始化商品信息
	 */
	private void initGoodsInfo(String mGoodsId) {
		NetHelper.getGoodsInfo(mGoodsId, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				LogUtils.logd(TAG, "initGoodsInfoonSuccess");
				resolveGoodsInfo(responseBody);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				LogUtils.logd(TAG, "initGoodsInfoonFailure");
				showLoading(VIEW_WIFIFAILUER);
			}
		});
	}

	/**
	 * 初始化模板信息
	 */
	private void initTemplateInfo(String template_id) {
		NetHelper.getTemplateInfo(template_id, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				LogUtils.logd(TAG, "initTemplateInfo+InfoSuccess");
				resolveTemplateInfo(responseBody);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				LogUtils.logd(TAG, "initTemplateInfo+Failure");
				showLoading(VIEW_WIFIFAILUER);
			}
		});

	}

	/**
	 * 解析作品id 返回信息
	 * 
	 * @param responseBody
	 */
	private void resolveWorkId(byte[] responseBody) {
		try {
			String json = new String(responseBody, "UTF-8");
			JSONObject JSONObject = new JSONObject(json);
			String code = JSONObject.optString("code");
			JSONObject result = JSONObject.optJSONObject("result");
			LogUtils.logd(TAG, "mWorkId:+json" + json);
			if ("0".equals(code)) {
				if (result != null) {
					mWorkId = result.optString("work_id");
					if (!StringUtils.isEmpty(mWorkId)) {
						LogUtils.logd(TAG, "mWorkId:" + mWorkId);
						mCache.put(MyConstants.WORKID, mWorkId);
						mDownloadCout++;
						LogUtils.logd(TAG, "WorkId+mDownloadCout:"
								+ mDownloadCout);
						if (mDownloadCout == mCoutOK) {
							mDownloadHandler.sendEmptyMessage(DOWNLOAD_OK);
						}
					}
				}
			} else {
				showLoading(VIEW_LOADFAILURE);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 解析商品信息 返回信息
	 * 
	 * @param responseBody
	 */
	private void resolveGoodsInfo(byte[] responseBody) {
		try {
			JSONObject result = JSONUtil.resolveResult(responseBody);
			if (result != null) {
				LogUtils.logd(TAG, "result:" + result);
				JSONArray skus = result.optJSONArray("skus");
				JSONObject object2 = (JSONObject) skus.get(0);
				JSONArray skus_content = object2.optJSONArray("skus_content");
				JSONObject object3 = (JSONObject) skus_content.get(0);
				String mProductId = object3.optString("product_id");
				LogUtils.logd(TAG, "mProductId:" + mProductId);
				String mPrice = StringUtils.format2point(object3
						.optString("price"));
				String title = result.optString("title");
				String default_img_url = result.optString("default_img_url");
				mCache.put(MyConstants.GOODSNAME, title);
				mCache.put(MyConstants.GOODSPRICE, mPrice);
				mCache.put(MyConstants.GOODSURL, default_img_url);
				mDownloadCout++;
				LogUtils.logd(TAG, "GoodsInfo+mDownloadCout:" + mDownloadCout);
				if (mDownloadCout == mCoutOK) {
					mDownloadHandler.sendEmptyMessage(DOWNLOAD_OK);
				}
				if (result.optString("maxWidth") != null) {
					mCache.put(MyConstants.MAXWIDTH,
							result.getString("maxWidth"));
				}
				if (result.optString("maxHeight") != null) {
					mCache.put(MyConstants.MAXHEIGHT,
							result.getString("maxHeight"));
				}
				if (result.optString("minWidth") != null) {
					mCache.put(MyConstants.MINWIDTH,
							result.getString("minWidth"));
				}
				if (result.optString("minHeight") != null) {
					mCache.put(MyConstants.MINWIDTH,
							result.getString("minHeight"));
				}
			} else {
				showLoading(VIEW_LOADFAILURE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			showLoading(VIEW_LOADFAILURE);
		}
	}

	/**
	 * 解析模板信息 返回信息
	 * 
	 * @param responseBody
	 */
	private void resolveTemplateInfo(byte[] responseBody) {
		try {
			JSONObject result = JSONUtil.resolveResult(responseBody);
			if (result != null) {
				JSONObject data = result.optJSONObject("content");
				FileUtil.saveFile(data.toString(), MyConstants.TEMPLATE,
						mTemplateId, MyConstants.JSON);
				getTemplateData(data);
			} else {
				showLoading(VIEW_LOADFAILURE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			showLoading(VIEW_LOADFAILURE);
		}
	}

	/**
	 * 获取模板数据
	 * 
	 * @param data
	 */
	private void getTemplateData(JSONObject data) {
		mTotalpage = data.optString("pagetype");
		setTitleInfo();
		mPage = Integer.parseInt(mTotalpage);
		mCache.put(MyConstants.TOTALPAGE, mTotalpage);
		mListCard = JsonDao.getListSinglePoker(data);
		getMyNeedData(mPage);

	}

	private void getMyNeedData(int mPage) {
		try {
			String Card = mListCard.get(0);
			JSONArray bigace = new JSONArray(Card);
			JSONArray framelist = JsonDao.getElementTypeArray(bigace, "frame");
			JSONObject frame = (JSONObject) framelist.get(0);
			mFrameMaskUrl = frame.optString("frameMaskUrl");
			mCache.put(MyConstants.MYFRAMEMASKURL, mFrameMaskUrl);
			mMasklayoutheight = frame.optString("height");
			mCache.put(MyConstants.MASKLAYOUTHEIGHT, mMasklayoutheight);
			String backupCard = null;
			if (mPage ==2) {
				backupCard = Card;
			}else{
				backupCard = mListCard.get(2);
			}
			JSONArray backup = new JSONArray(backupCard);
			JSONArray backupframelist = JsonDao.getElementTypeArray(backup,
					"frame");
			JSONObject backupframe = (JSONObject) backupframelist.get(0);
			mBackupframeMaskUrl = backupframe.optString("frameMaskUrl");
			mBackupMasklayoutheight = backupframe.optString("height");
			mCache.put(MyConstants.MYBACKUPFRAMEMASKURL, mBackupframeMaskUrl);
			String mMaskPath = FileUtil.getUrl2Path(mFrameMaskUrl);
			String mBackupMaskpath = FileUtil.getUrl2Path(mBackupframeMaskUrl);
			if (new File(mMaskPath).exists()
					&& new File(mBackupMaskpath).exists()) {
				getMaskPicInfo(mMaskPath, mBackupMaskpath);
			} else {
				JsonDao.getMaterial();
				getMaskPicInfo(mMaskPath, mBackupMaskpath);
			}

		} catch (JSONException e) {
			e.printStackTrace();
			showLoading(VIEW_LOADFAILURE);
		}

	}

	/**
	 * 获取画框和蒙版 资源
	 */
	private void getMaskPicInfo(String mMaskPath, String mBackupMaskpath) {

		// Mask资源
		Bitmap mMaskBitmap = FileUtil.getSDCardImg(mMaskPath);
		mMaskwidth = mMaskBitmap.getWidth() + "";
		mMaskheight = mMaskBitmap.getHeight() + "";
		mCache.put(MyConstants.MASKWIDTH, mMaskwidth);
		mCache.put(MyConstants.MASKHEIGHT, mMaskheight);
		// BackupMask资源

		Bitmap mBackupMaskBitmap = FileUtil.getSDCardImg(mBackupMaskpath);
		mBackupMaskwidth = mBackupMaskBitmap.getWidth() + "";
		mBackupMaskheight = mBackupMaskBitmap.getHeight() + "";
		mCache.put(MyConstants.BACKUPMASKWIDTH, mBackupMaskwidth);
		mCache.put(MyConstants.BACKUPMASKHEIGHT, mBackupMaskheight);
		mDownloadCout++;
		LogUtils.logd(TAG, "mBackupMaskBitmapBitmap+mDownloadCout:"
				+ mDownloadCout);
		if (mDownloadCout == mCoutOK) {
			mDownloadHandler.sendEmptyMessage(DOWNLOAD_OK);
		}
	}

	/**
	 * 切换画框 更新UI
	 */
	@SuppressLint("HandlerLeak")
	private void refreshUI() {

		mDownloadHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case DOWNLOAD_OK:
					showLoading(VIEW_LIST);
					break;
				}
			}

		};
	}

	/**
	 * 处理照片牌
	 * 
	 * @param oldpath
	 */
	private void gotoBackupTask(String oldpath) {
		// List<SelectpicBean> list = SelectPicSQLHelper
		// .query2DB(oldpath, mWorkId);
		// if (list.size() == 0) {
		String mImgid = GUID.getGUID();
		SelectpicBean bean = new SelectpicBean();
		bean.setImgid(mImgid);
		bean.setWorkid(mWorkId);
		bean.setTotalpage(mTotalpage);
		bean.setOldpath(oldpath);
		bean.setNewpath("");
		bean.setWorkpage("3");
		bean.setIsselect("true");
		bean.setIscondense("false");
		bean.setIssave("false");
		bean.setIsnet("false");
		bean.setIsupload("false");
		bean.setIsexist("false");
		bean.setIscompute("false");
		bean.setTranslatex("0");
		bean.setTranslatey("0");
		bean.setInitscale("1");
		bean.setScale("1");
		bean.setMasklayoutheight(mBackupMasklayoutheight);
		bean.setMaskheight(mBackupMaskheight);
		bean.setMaskwidth(mBackupMaskwidth);
		bean.setPicheight("");
		bean.setPicwidth("");
		bean.setCutx("");
		bean.setCuty("");
		bean.setCutwidth("");
		bean.setCutheight("");
		bean.setCardinfo(mListCard.get(2));
		SelectPicSQLHelper.add2DB(bean);

		MyApplication.getDealTaskManager().addTask(
				new DealTask(mImgid, false, DealTask.ALL));
		// }

	}

	/**
	 * 从SD中获取模板数据
	 */
	private void getSDTemplateData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					String json = FileUtil.readFile(MyConstants.TEMPLATE,
							mTemplateId, MyConstants.JSON);
					JSONObject object = new JSONObject(json);
					getTemplateData(object);
				} catch (JSONException e) {
					e.printStackTrace();
					showLoading(VIEW_LOADFAILURE);
				}
			}
		}).start();

	}

	/**
	 * 
	 * 执行 保存功能
	 * 
	 */
	private void gotoSave() {
		if (mGridAdapter.getCount() < mPage) {
			String rerStr = getString(R.string.mini_choice) + mTotalpage
					+ getString(R.string.zhang);
			ToastUtils.show(rerStr);
		} else {
			mTemplateCustomDialog.show();
			if (!mFrameMaskUrl.equals(mBackupframeMaskUrl)) {
				if (mPage == 4) {
					delayed(2000);
				} else if (mPage == 16) {
					delayed(1000);
				} else if (mPage == 55) {
					delayed(100);
				}
			} else {
				delayed(100);
			}
			refreshTemplataUI();
		}
	}

	/**
	 * 
	 * 设置延时操作 防止内存溢出
	 * 
	 * @param time
	 */
	private void delayed(long time) {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				getInitPic();
			}
		}, time);
	}

	/**
	 * 
	 * 显示 加载模板进度条
	 */
	private void showTemplateDialog() {
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				mTemplateCount = getCout();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (mTemplateCustomDialog != null
								&& mTemplateCustomDialog.isShowing()) {
							mTemplateCustomDialog.setProgress(mTemplateCount);
							LogUtils.logd(TAG, "count:" + mTemplateCount);
						}
					}
				});
				if (mTemplateCount >= 100) {
					mDialogHandler.sendEmptyMessage(Dialog_OK);
					LogUtils.logd(TAG, "mTemplateCustomDialog:" + "OK");
					LogUtils.logd(TAG, "count:" + mTemplateCount);
					timer.cancel();

				}
			}
		}, 0, 500);
		mTemplateCustomDialog
				.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						if (timer != null)
							timer.cancel();
						mTemplateCount = 0;
						mTemplateCustomDialog.dismiss();
					}
				});
	}

	/***
	 * 初始化出图片
	 * 
	 */
	private void getInitPic() {
		for (int i = 0; i < mSelectPaths.size(); i++) {
			List<SelectpicBean> list02 = SelectPicSQLHelper.query2DB(
					mSelectPaths.get(i), mWorkId);
			SelectpicBean bean = list02.get(0);
			String singlePokerJSONArrayStr = mListCard.get(i);
			SelectPicSQLHelper.updateSelectPic(bean.getImgid(), i + 1 + "",
					singlePokerJSONArrayStr);
		}
		List<SelectpicBean> listSelectpic = SelectPicSQLHelper.queryByIsselect(
				"true", mWorkId);
		Collections.sort(listSelectpic, new SelectpicBean());
		for (int i = 0; i < listSelectpic.size(); i++) {
			SelectpicBean bean = listSelectpic.get(i);
			String imgid = bean.getImgid();
			String oldpath = bean.getOldpath();
			if (!mFrameMaskUrl.equals(mBackupframeMaskUrl) && i == 2) {
				SelectPicSQLHelper.deleteImgid(imgid);
				FileUtil.deleteFromSD(FileUtil.getWorksDir(imgid), imgid,
						MyConstants.JPEG);
				FileUtil.deleteFromSD(FileUtil.getWorksDir(imgid), mWorkId
						+ imgid, MyConstants.PNG);
				gotoBackupTask(oldpath);
			} else {
				MyApplication.getDealTaskManager().addTask(
						new DealTask(imgid, false, DealTask.MOSAICJSON));
			}
		}
		showTemplateDialog();
	}

	/**
	 * 实时获取百分比
	 * 
	 * @return
	 */
	private int getCout() {
		List<String> list = SelectPicSQLHelper.querySelectPicImgid2(mWorkId,
				"true", "false");
		int size = list.size();
		int f = mPage - size;
		double d = f / (double) mPage;
		int count = (int) (d * 100);
		if (count == 0) {
			count = 10;
		}
		return count;
	}

	/**
	 * 切换画框 更新UI
	 */
	private void refreshTemplataUI() {

		mDialogHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case Dialog_OK:

					gotoBrowse();

					break;
				}
			}
		};
	}

}
