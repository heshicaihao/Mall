package com.nettactic.mall.adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nettactic.mall.R;
import com.nettactic.mall.cache.ACache;
import com.nettactic.mall.constants.MyConstants;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



/**
 * 管理地址列表 List 的适配器
 * 
 * @author heshicaihao
 */
public class ManageAddressAdapter extends BaseAdapter {

	private JSONArray mData;
	private Boolean mIsChoice;

	@SuppressWarnings("unused")
	private Context mContext;
	private LayoutInflater mInflater;
	private int clickTemp = -1;// 选中的位置
	private ACache mCache;
	
//	mCache.getAsString(MyConstants.MINWIDTH)
	public ManageAddressAdapter(Context context, JSONArray jsonArray,
			Boolean mIsChoice) {
		mCache = ACache.get(context);
		this.mData = jsonArray;
		this.mContext = context;
		this.mIsChoice = mIsChoice;
		mInflater = LayoutInflater.from(context);
		notifyDataSetChanged();
		clickTemp =Integer.parseInt(mCache
				.getAsString(MyConstants.SELECTED_ADDRESS));
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_address_list, null);
			holder = new ViewHolder();
			holder.consigneeTv = (TextView) convertView
					.findViewById(R.id.consignee);
			holder.phoneTv = (TextView) convertView
					.findViewById(R.id.consignee_phone);
			holder.addressTv = (TextView) convertView
					.findViewById(R.id.shipping_address);
			holder.checkBox = (ImageView) convertView
					.findViewById(R.id.checkbox);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		JSONObject object;
		try {
			object = mData.getJSONObject(position);
			String ship_name = object.optString("ship_name");
			String ship_mobile = object.optString("ship_mobile");
			String ship_addr = object.optString("ship_addr");
			boolean is_default = object.optBoolean("is_default");

			String mAreaNetStr = object.optString("ship_area");
			String[] arr = mAreaNetStr.split("\\:");
			String arr1 = arr[1];
			String ship_area = arr1.replaceAll("/", "");
			// String mCode = arr[2];
			holder.consigneeTv.setText(ship_name);
			holder.phoneTv.setText(ship_mobile);
			holder.addressTv.setText(ship_area + ship_addr);
			if (is_default) {
				String html = "<font color='#FFAC13'><b>[默认]</b></font>";
				String text = html + ship_area + ship_addr;
				holder.addressTv.setText(Html.fromHtml(text));
			} else {
				holder.addressTv.setText(ship_area + ship_addr);
			}
			if (mIsChoice) {
				holder.checkBox.setVisibility(View.VISIBLE);
				if (clickTemp == position) {
					holder.checkBox
							.setBackgroundResource(R.mipmap.checkbox_pressed);
					mCache.put(MyConstants.SELECTED_ADDRESS, position+"");
				} else {
					holder.checkBox
							.setBackgroundResource(R.mipmap.checkbox_normal);
				}
			} else {
				holder.checkBox.setVisibility(View.GONE);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return convertView;

	}

	public static class ViewHolder {
		public TextView consigneeTv;
		public TextView phoneTv;
		public TextView addressTv;
		public ImageView checkBox;
	}

}
