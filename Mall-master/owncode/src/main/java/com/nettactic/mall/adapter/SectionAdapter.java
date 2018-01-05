package com.nettactic.mall.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nettactic.mall.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 管理员分组 列表 List 的适配器
 *
 * @author heshicaihao
 */
public class SectionAdapter extends BaseAdapter {

    private final Context context;
    private final JSONArray mData;
    private final int selectid;

    public SectionAdapter(Context context, JSONArray mData, int selectid) {
        this.context = context;
        this.mData = mData;
        this.selectid = selectid;
    }

    @Override
    public int getCount() {
        return mData.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return mData.get(position);
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
        DataList data = null;
        if (convertView == null) {
            data = new DataList();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.view_secect_category_item, null);
            data.mText = (TextView) convertView.findViewById(R.id.client_name);
            data.mImage = (ImageView) convertView
                    .findViewById(R.id.checkimg);
            convertView.setTag(data);
        } else {
            data = (DataList) convertView.getTag();
        }
        try {
            JSONObject jsonobject = (JSONObject) mData.get(position);
            String title = jsonobject.optString("title");
            data.mText.setText(title);
            if (selectid == position) {
                data.mImage.setVisibility(View.VISIBLE);
                convertView.setBackgroundResource(R.color.tab_view_press);
                data.mText.setTextColor(context.getResources()
                        .getColor(R.color.main_color));
            } else {
                data.mImage.setVisibility(View.INVISIBLE);
                convertView.setBackgroundResource(R.color.white);
                data.mText.setTextColor(Color.BLACK);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    public void freshdata() {
        this.notifyDataSetChanged();
    }

    private class DataList {
        public TextView mText;
        public ImageView mImage;
    }
}