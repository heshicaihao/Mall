package com.nettactic.mall.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nettactic.mall.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TabClassifyLeftListAdapter extends BaseAdapter {

	private String TAG = "TabClassifyLeftListAdapter";
	private JSONArray mData;

	private Context mContext;
	private Activity mActivity;
	private LayoutInflater mInflater;
	private int clickTemp = 0;// 选中的位置

	public TabClassifyLeftListAdapter(Activity mActivity, Context context,
                                      JSONArray jsonArray) {
		this.mData = jsonArray;
		this.mContext = context;
		this.mActivity = mActivity;
		mInflater = LayoutInflater.from(context);

	}

	public void setSeclection(int position) {
		clickTemp = position;
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
			convertView = mInflater.inflate(R.layout.item_tab_classify_left_list, null);
			holder = new ViewHolder();
			holder.cat_nameTv = (TextView) convertView
					.findViewById(R.id.left_cat_name);
			holder.left_view = (LinearLayout) convertView
					.findViewById(R.id.left_view);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		try {
			JSONObject object = mData.getJSONObject(position);
			String cat_name = object.optString("title");
			holder.cat_nameTv.setText(cat_name);
			if (clickTemp == position) {
				holder.left_view
						.setBackgroundColor(Color.parseColor("#FFFFFF"));
				holder.cat_nameTv.setTextColor(Color.parseColor("#000000"));
			} else {
				holder.left_view
						.setBackgroundColor(Color.parseColor("#EDEDED"));
				holder.cat_nameTv.setTextColor(Color.parseColor("#666666"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return convertView;
	}

	public static class ViewHolder {
		public TextView cat_nameTv;
		public LinearLayout left_view;

	}
}
