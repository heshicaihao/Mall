package com.nettactic.mall.adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.heshicai.hao.xutils.BitmapUtils;
import com.heshicai.hao.xutils.bitmap.BitmapDisplayConfig;
import com.heshicai.hao.xutils.bitmap.core.BitmapSize;
import com.nettactic.mall.R;
import com.nettactic.mall.common.MyApplication;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.BitmapHelp;
import com.nettactic.mall.utils.LogUtils;
import com.nettactic.mall.utils.StringUtils;
import com.nettactic.mall.utils.ToastUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderDetailsAdapter extends BaseAdapter {
	public String TAG = getClass().getName();
	private JSONArray mData;

	private Context mContext;
	private LayoutInflater mInflater;
	private ViewHolder mHolder;

	private BitmapUtils bitmapUtils;
	private BitmapDisplayConfig bigPicDisplayConfig;

	public OrderDetailsAdapter(Activity mActivity, Context context,
			JSONArray mData) {
		this.mData = mData;
		this.mContext = context;
		mInflater = LayoutInflater.from(context);

		int mScreenWidth = AndroidUtils.getScreenWidth(mActivity);
		if (bitmapUtils == null) {
			bitmapUtils = BitmapHelp.getBitmapUtils(mContext);
		}

		bigPicDisplayConfig = new BitmapDisplayConfig();
		bigPicDisplayConfig.setBitmapConfig(Bitmap.Config.RGB_565);
		bigPicDisplayConfig.setBitmapMaxSize(new BitmapSize(mScreenWidth / 5,
				mScreenWidth / 5));
		bitmapUtils.configDefaultLoadingImage(R.mipmap.blank_bg);
		bitmapUtils.configDefaultLoadFailedImage(R.mipmap.blank_bg);
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		// bitmapUtils.configDiskCacheEnabled(false);

	}

	@Override
	public int getCount() {
		return mData.length();
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
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_order_details_list,
					null);
			mHolder = new ViewHolder();
			getItemView(convertView);

			convertView.setTag(mHolder);

		} else {
			mHolder = (ViewHolder) convertView.getTag();
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
		try {
			JSONObject object = (JSONObject) mData.get(position);
			String url = object.optString("goods_pic");
			if (!StringUtils.isEmpty(url)) {
				bitmapUtils.display(mHolder.goods_image, url,
						bigPicDisplayConfig);
				// String enURlurl = FileUtil.getenURl(url);
				// String mPath = FileUtil.getFilePath(MyConstants.CACHE_DIR,
				// enURlurl, MyConstants.JPEG);
				// if (FileUtil.fileIsExists(mPath)) {
				// Bitmap mBitmap = FileUtil.getSDCardImg(mPath);
				// mHolder.goods_image.setImageBitmap(mBitmap);
				// } else {
				// loadImageView(url, enURlurl, mHolder.goods_image);
				// }
			}

			String goods_name = object.optString("goods_name");
			mHolder.goods_name.setText(goods_name);
			String goods_type = object.optString("remarks");
			mHolder.goods_type.setText(goods_type);
			String goods_price = object.optString("price");
			String format2point = StringUtils.format2point(goods_price);
			String price = format2point
					+ MyApplication.getContext().getString(R.string.yuan);
			mHolder.goods_price.setText(price);
			String num = object.optString("quantity");
			mHolder.goods_num.setText("x" + num);

			JSONObject work_info = object.optJSONObject("work_info");
			LogUtils.logd(TAG, "work_info:" + work_info);
			String work_data_state = work_info.optString("work_data_state");
			LogUtils.logd(TAG, "work_data_state:" + work_data_state);
			if ("complete".equals(work_data_state)) {
				
//				ToastUtils.show(R.string.works_pic_complete);
			} else if ("incomplete".equals(work_data_state)) {
				JSONObject work_miss_info = work_info
						.optJSONObject("work_miss_info");
				LogUtils.logd(TAG, "work_miss_info:" + work_miss_info);
				JSONArray picid_miss = work_miss_info
						.getJSONArray("picid_miss");
				String miss_count = work_miss_info.optString("miss_count");
				LogUtils.logd(TAG, "miss_count:" + miss_count);
				int length = picid_miss.length();
				LogUtils.logd(TAG, "picid_miss.length():" + length);
				if (length != 0) {
					ToastUtils.show(R.string.works_pic_incomplete);

				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 找到Item 的 控件
	 * 
	 * @param convertView
	 */
	private void getItemView(View convertView) {
		mHolder.goods_image = (ImageView) convertView
				.findViewById(R.id.goods_image);
		mHolder.goods_name = (TextView) convertView
				.findViewById(R.id.goods_name);
		mHolder.goods_type = (TextView) convertView
				.findViewById(R.id.goods_type);
		mHolder.goods_price = (TextView) convertView
				.findViewById(R.id.goods_price);
		mHolder.goods_num = (TextView) convertView.findViewById(R.id.goods_num);
	}

	public class ViewHolder {

		public ImageView goods_image = null;
		public TextView goods_name = null;
		public TextView goods_type = null;
		public TextView goods_price = null;
		public TextView goods_num = null;

	}

}
