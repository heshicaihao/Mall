package com.nettactic.mall.adapter;

/**
 * Created by heshicaihao on 2017/6/28.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nettactic.mall.R;
import com.nettactic.mall.utils.AndroidUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 单行Grid 的适配器
 *
 * @author heshicaihao
 */
public class SingleGridAdapter extends BaseAdapter {

    private int mScreenWidth;
    private LayoutInflater mInflater;
    private JSONArray mList;

    private Activity mActivity;
    private Context mContext;

    public SingleGridAdapter(Activity activity, Context context,
                             JSONArray list) {
        this.mList = list;
        this.mContext = context;
        this.mActivity = activity;
        this.mInflater = LayoutInflater.from(context);
        this.mScreenWidth = AndroidUtils.getScreenWidth(mActivity);
    }

    @Override
    public int getCount() {
        int count = mList.length();
        return count;
    }

    @Override
    public Object getItem(int position) {
        try {
            return mList == null ? null : mList.get(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.item_single_grid, null);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView
                    .findViewById(R.id.grid_tv);
            holder.mGridItem = (RelativeLayout) convertView
                    .findViewById(R.id.grid_item);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            JSONObject jsonobject = (JSONObject) mList.get(position);
            String name = jsonobject.optString("name");
            holder.mTextView.setText(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    public class ViewHolder {
        public TextView mTextView;
        public RelativeLayout mGridItem;


    }
}
