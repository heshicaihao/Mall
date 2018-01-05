package com.nettactic.mall.adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nettactic.mall.R;
import com.nettactic.mall.utils.LogUtils;
import com.nettactic.mall.utils.StringUtils;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * 选择配送方式 List 的适配器
 * 
 * @author heshicaihao
 */
public class ChoiceShippingListAdapter extends BaseAdapter {
	
	private String TAG = "ChoiceShippingListAdapter";
	private JSONArray mData;
	

	@SuppressWarnings("unused")
	private Context mContext;
	private LayoutInflater mInflater;
	private int clickTemp = 0;// 选中的位置

	public ChoiceShippingListAdapter(Context context, JSONArray jsonArray) {
		this.mData = jsonArray;
		this.mContext = context;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_choice_shipping_list,
					null);
			holder = new ViewHolder();
			holder.nameTv = (TextView) convertView.findViewById(R.id.shipping_name);
			holder.infoTv = (TextView) convertView.findViewById(R.id.shipping_info);
			holder.choiceIv = (ImageView) convertView.findViewById(R.id.shipping_choice_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		try {
			JSONObject object = mData.getJSONObject(position);
			String ship_name = object.optString("dt_name");
			CharSequence CS_ship_name = Html.fromHtml(ship_name);
			holder.nameTv.setText(CS_ship_name);
			
			String ship_mobile = object.optString("detail");
			String eqDetail=ship_mobile.replace("&nbsp;", "");
			LogUtils.logd(TAG, "detail:"+eqDetail);
			if (!StringUtils.isEmpty(eqDetail)) {
				LogUtils.logd(TAG, "NoisEmpty:");
				CharSequence CS_ship_mobile = Html.fromHtml(ship_mobile);
				holder.infoTv.setText(CS_ship_mobile);
//				holder.infoTv.setVisibility(View.VISIBLE);
				holder.infoTv.setVisibility(View.GONE);
			}else {
				LogUtils.logd(TAG, "isEmpty:");
				holder.infoTv.setVisibility(View.GONE);
			}
			if (clickTemp == position) {
				holder.choiceIv.setVisibility(View.VISIBLE);
			} else {
				holder.choiceIv.setVisibility(View.GONE);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return convertView;

	}

	public static class ViewHolder {
		public TextView nameTv;
		public TextView infoTv;
		public ImageView choiceIv;
	}

}
