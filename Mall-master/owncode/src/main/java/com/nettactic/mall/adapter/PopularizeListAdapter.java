package com.nettactic.mall.adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nettactic.mall.R;
import com.nettactic.mall.common.MyApplication;
import com.nettactic.mall.utils.StringUtils;

public class PopularizeListAdapter extends BaseAdapter {

	private JSONArray mData;
	private LayoutInflater mInflater;

	public PopularizeListAdapter(Context context,
			JSONArray jsonArray) {
		this.mData = jsonArray;
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
			convertView = mInflater.inflate(R.layout.item_popularize_order_list, null);
			holder = new ViewHolder();
			holder.order_id_numTv = (TextView) convertView
					.findViewById(R.id.order_id_num);
			holder.get_gainsTv = (TextView) convertView
					.findViewById(R.id.get_gains);
			holder.timeTv = (TextView) convertView
					.findViewById(R.id.time);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		try {
			JSONObject object = mData.getJSONObject(position);
			String cat_name = object.optString("order_no");
			holder.order_id_numTv.setText(cat_name);
			
			String get_gains = object.optString("promote_amount");
			String format2point = StringUtils.format2point(get_gains);
			String get_gainsStr = MyApplication.getContext().getString(R.string.add)+format2point
					+ MyApplication.getContext().getString(R.string.yuan);
			
			holder.get_gainsTv.setText(get_gainsStr);
			
			String createtimeStr = object.optString("updatetime");
			long createtime = Long.parseLong(createtimeStr);
			String time = StringUtils.longToDate(createtime);
			holder.timeTv.setText(time);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return convertView;

	}

	public static class ViewHolder {
		
		public TextView order_id_numTv;
		public TextView get_gainsTv;
		public TextView phone_numTv;
		public TextView timeTv;

	}
}
