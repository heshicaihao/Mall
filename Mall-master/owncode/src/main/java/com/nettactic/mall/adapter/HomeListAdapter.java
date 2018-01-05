package com.nettactic.mall.adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nettactic.mall.R;
import com.nettactic.mall.activity.WLGoodsDetailsActivity;
import com.nettactic.mall.cache.ACache;
import com.nettactic.mall.common.MyApplication;
import com.nettactic.mall.constants.MyConstants;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.LogUtils;
import com.nettactic.mall.utils.StringUtils;

public class HomeListAdapter extends BaseAdapter {

	private JSONArray mData;

	private Context mContext;
	private Activity mActivity;
	private LayoutInflater mInflater;

	private String TAG = "MainFragment";
	private ACache mCache;

	public HomeListAdapter(Activity mActivity, Context context,
			JSONArray jsonArray) {
		this.mData = jsonArray;
		this.mContext = context;
		this.mActivity = mActivity;
		mCache = ACache.get(MyApplication.getContext());
		mInflater = LayoutInflater.from(context);

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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_home_list, null);
			holder = new ViewHolder();
			holder.imageview = (ImageView) convertView
					.findViewById(R.id.goods_imageview);
			holder.cat_nameTv = (TextView) convertView
					.findViewById(R.id.cat_name);
			holder.cat_remarksTv = (TextView) convertView
					.findViewById(R.id.cat_remarks);
			holder.cat_priceTv = (TextView) convertView
					.findViewById(R.id.cat_price);
			holder.custom_madeTv = (TextView) convertView
					.findViewById(R.id.custom_made);

			holder.imageview.setScaleType(ImageView.ScaleType.FIT_CENTER);
			holder.imageview.setAdjustViewBounds(true);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		try {
			JSONObject object = mData.getJSONObject(position);
			String cat_name = object.optString("cat_name");
			holder.cat_nameTv.setText(cat_name);
			String remarks = object.optString("remarks");
			holder.cat_remarksTv.setText(remarks);
			String price = object.optString("price");
			String cat_priceStr = null;
			if (StringUtils.isEmpty(price) || price.equals("0")) {
				cat_priceStr = "";
			} else {
				String format2point = StringUtils.format2point(price);
				cat_priceStr = format2point
						+ MyApplication.getContext().getString(R.string.yuan);
			}
			holder.cat_priceTv.setText(cat_priceStr);

			String imgUrl = object.optString("img_url");
			LogUtils.logd(TAG, "default_img_url:" + imgUrl);
			if (!TextUtils.isEmpty(imgUrl)) {
				Glide.with(mContext).load(imgUrl)
						.placeholder(R.mipmap.wait_im).centerCrop()
						.crossFade().into(holder.imageview);
			}
			holder.custom_madeTv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					clickEvent(position);
				}

			});
			holder.imageview.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					clickEvent(position);
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return convertView;

	}

	private void clickEvent(int position) {
		try {
			JSONObject data = (JSONObject) mData.get(position);
			String cat_id = data.optString("cat_id");
			String remarks = data.optString("remarks");
			mCache.put(MyConstants.GOODSTYPE, remarks);
			// String cat_name = data.optString("cat_name");
			// String price = data.optString("price");
			// mCache.put(MyConstants.GOODSNAME, cat_name);
			// mCache.put(MyConstants.GOODSPRICE, price);

			gotoGoodsDetails(cat_id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void gotoGoodsDetails(String cat_id) {
		Intent intent = new Intent(mContext, WLGoodsDetailsActivity.class);
		intent.putExtra("cat_id", cat_id);

		mActivity.startActivity(intent);
		AndroidUtils.enterActvityAnim(mActivity);
	}

	public static class ViewHolder {
		public ImageView imageview;
		public TextView cat_nameTv;
		public TextView cat_remarksTv;
		public TextView cat_priceTv;
		public TextView custom_madeTv;

	}
}
