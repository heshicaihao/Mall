package com.nettactic.mall.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nettactic.mall.R;
import com.nettactic.mall.adapter.ChoicePictureGridViewHolder;
import com.nettactic.mall.adapter.ChoicePictureListViewHolder;
import com.nettactic.mall.base.BaseActivity;
import com.nettactic.mall.bean.ImageBean;
import com.nettactic.mall.bean.SelectpicBean;
import com.nettactic.mall.cache.ACache;
import com.nettactic.mall.common.MyApplication;
import com.nettactic.mall.constants.MyConstants;
import com.nettactic.mall.database.SelectPicSQLHelper;
import com.nettactic.mall.picdeal.DealTask;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.DateFormatUtils;
import com.nettactic.mall.utils.FileUtil;
import com.nettactic.mall.utils.GUID;
import com.nettactic.mall.utils.LogUtils;
import com.nettactic.mall.utils.ServiceUtils;
import com.nettactic.mall.utils.ToastUtils;
import com.nettactic.mall.widget.MyGridView;

/**
 * 更新用户图片页
 * 
 * @author heshicaihao
 */
public class UpdatePictureActivity extends BaseActivity implements
		OnClickListener {

	private static final int IMAGE_OK = 1001;
	private String mWorkId;
	private String mSelectImgid;
	private String mImgid;
	private TreeMap<Integer, List<String>> mGruopMap = new TreeMap<Integer, List<String>>();
	private List<String> mOldSelectPaths = new ArrayList<String>();

	private ImageView mBack;
	private ImageView mCheckbox;
	private TextView mTitle;
	private ListView mListView;
	private MyListAdapter mListAdapter;
	private Handler mImageHandler;
	private ACache mCache;
	private SelectpicBean mOldData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 和选择图片公用一个布局
		setContentView(R.layout.activity_update_picture);
		ServiceUtils.startUploadService(this, UpdatePictureActivity.this);
		ServiceUtils.startDealService(this, UpdatePictureActivity.this);
		initView();
		initData();

	}

	@Override
	protected void onStart() {
		super.onStart();
		getOldSelectPaths();
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.back:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return false;
	}

	private void initView() {
		mTitle = (TextView) findViewById(R.id.title);
		mBack = (ImageView) findViewById(R.id.back);
		mListView = (ListView) findViewById(R.id.popup_list_view);

		mTitle.setText(R.string.one_picture);
		mBack.setOnClickListener(this);

	}

	private void initData() {
		getDataCache();
		getOldSelectPaths();
		getImagesFromSD();
		setListAdapter();

	}

	/**
	 * 获取上次选中的图片路径
	 */
	private void getDataCache() {
		mCache = ACache.get(this);
		mSelectImgid = mCache.getAsString(MyConstants.SELECT_IMGID);

		mOldData = SelectPicSQLHelper.queryByImgId2DB(mSelectImgid);
		mWorkId = mOldData.getWorkid();

	}

	private void getOldSelectPaths() {
		mOldSelectPaths.clear();
		List<SelectpicBean> list = SelectPicSQLHelper.queryByIsselect("true",
				mWorkId);
		for (int i = 0; i < list.size(); i++) {
			SelectpicBean bean = list.get(i);
			String oldpath = bean.getOldpath();
			mOldSelectPaths.add(oldpath);
		}
		LogUtils.logd(TAG, "mOldSelectPaths:" + mOldSelectPaths.toString());
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
	 * 选择图片日期的List 的适配器
	 * 
	 * @author heshicaihao
	 */
	class MyListAdapter extends BaseAdapter {

		private List<ImageBean> mList;
		private TreeMap<Integer, List<String>> mGroup;
		private List<String> mChildList = new ArrayList<String>();

		private LayoutInflater mInflater;
		private ChildGridAdapter mChildAdapter;
		private int mMinPicWidth = MyConstants.INIT_MINWIDTH;
		private int mMinPicHeight = MyConstants.INIT_MINHEIGHT;

		public MyListAdapter(List<ImageBean> list,
				TreeMap<Integer, List<String>> gruopMap) {
			this.mList = list;
			this.mGroup = gruopMap;
			mInflater = LayoutInflater.from(MyApplication.getContext());
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
			mChildAdapter = new ChildGridAdapter(mChildList);
			holder.mGrid.setAdapter(mChildAdapter);
			holder.mGrid.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					boolean mIsOldSelectPaths = mOldSelectPaths.contains(mPaths
							.get(position));
					if (mIsOldSelectPaths) {
						ToastUtils.show(R.string.complex_picture);
					} else {
						mCheckbox = (ImageView) view
								.findViewById(R.id.checkbox);
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
						if (outWidth > mMinPicWidth
								&& outHeight > mMinPicHeight) {
							String oldpath = mPaths.get(position);
							mCheckbox
									.setImageResource(R.mipmap.checkbox_pressed);
							SelectPicSQLHelper.deleteImgid(mSelectImgid);
							FileUtil.deleteFromSD(
									FileUtil.getWorksDir(mWorkId),
									mSelectImgid, MyConstants.JPEG);
							gotoTask(oldpath);
							Intent intent = new Intent();
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.putExtra("Path", oldpath);
							UpdatePictureActivity.this.setResult(
									EditPictureActivity.RESULT_OK, intent);
							finish();
						} else {
							ToastUtils.show(R.string.choice_picture_again);
						}
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

		public ChildGridAdapter(List<String> list) {
			this.mList = list;
			this.mInflater = LayoutInflater.from(MyApplication.getContext());
			this.mScreenWidth = AndroidUtils
					.getScreenWidth(UpdatePictureActivity.this);
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
				int m = AndroidUtils.dip2px(MyApplication.getContext(), 103);
				lp.width = (mScreenWidth - m) / 3;
				lp.height = lp.width;
				holder.mImageView.setLayoutParams(lp);
				convertView.setTag(holder);
			} else {
				holder = (ChoicePictureGridViewHolder) convertView.getTag();
			}
			String mPath = mList.get(position);
			if (mOldSelectPaths.contains(mPath)) {
				holder.checkbox.setImageResource(R.mipmap.checkbox_pressed);
			} else {
				holder.checkbox.setImageResource(R.mipmap.checkbox_normal);
			}
			Glide.with(getApplicationContext()).load(mPath)
					.placeholder(R.mipmap.blank_bg).into(holder.mImageView);
			return convertView;

		}

	}

	/**
	 * 去下载任务
	 * 
	 * @param oldpath
	 */
	private void gotoTask(String oldpath) {
		List<SelectpicBean> list = SelectPicSQLHelper
				.query2DB(oldpath, mWorkId);
		if (list.size() == 0) {
			mImgid = GUID.getGUID();
			SelectpicBean bean = new SelectpicBean();
			bean.setImgid(mImgid);
			bean.setWorkid(mWorkId);
			bean.setTotalpage(mOldData.getTotalpage());
			bean.setOldpath(oldpath);
			bean.setNewpath("");
			bean.setWorkpage(mOldData.getWorkpage());
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
			bean.setMasklayoutheight(mOldData.getMasklayoutheight());
			bean.setMaskheight(mOldData.getMaskheight());
			bean.setMaskwidth(mOldData.getMaskwidth());
			bean.setPicheight("");
			bean.setPicwidth("");
			bean.setCutx("");
			bean.setCuty("");
			bean.setCutwidth("");
			bean.setCutheight("");
			bean.setCardinfo(mOldData.getCardinfo());

			SelectPicSQLHelper.add2DB(bean);
		} else {
			SelectpicBean bean = list.get(0);
			mImgid = bean.getImgid();
		}
		MyApplication.getDealTaskManager().addTask(
				new DealTask(mImgid, true, DealTask.YSBCSC));

	}

}
