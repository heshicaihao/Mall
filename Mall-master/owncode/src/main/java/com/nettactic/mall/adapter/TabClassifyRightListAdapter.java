package com.nettactic.mall.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nettactic.mall.R;
import com.nettactic.mall.widget.MyGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TabClassifyRightListAdapter extends BaseAdapter {

	private String TAG = "TabClassifyRightListAdapter";
	private JSONArray mData;

	private Context mContext;
	private Activity mActivity;
	private LayoutInflater mInflater;

	public TabClassifyRightListAdapter(Activity mActivity, Context context,
                                       JSONArray jsonArray) {
		this.mData = jsonArray;
		this.mContext = context;
		this.mActivity = mActivity;
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
			convertView = mInflater.inflate(R.layout.item_tab_classify_right_list, null);
			holder = new ViewHolder();
			holder.cat_nameTv = (TextView) convertView
					.findViewById(R.id.right_cat_name);
			holder.gridview = (MyGridView) convertView
					.findViewById(R.id.gridview);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		try {
			JSONObject object = mData.getJSONObject(position);
			String cat_name = object.optString("title");
			holder.cat_nameTv.setText(cat_name);
			JSONArray content = object.optJSONArray("content");
			TabClassifyRightChildGridAdapter  ChildGridAdapter = new TabClassifyRightChildGridAdapter(mActivity,mContext,content);
			holder.gridview.setAdapter(ChildGridAdapter);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return convertView;
	}

	public static class ViewHolder {
		public TextView cat_nameTv;
		public MyGridView gridview;

	}
}
