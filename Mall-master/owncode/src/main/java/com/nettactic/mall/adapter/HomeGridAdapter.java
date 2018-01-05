package com.nettactic.mall.adapter;

/**
 * Created by heshicaihao on 2017/6/16.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nettactic.mall.R;
import com.nettactic.mall.utils.AndroidUtils;

import java.util.List;

/**
 * 选择图片每一个日期的Grid 的适配器
 *
 * @author heshicaihao
 */
public class HomeGridAdapter extends BaseAdapter {

    private int mScreenWidth;
    private LayoutInflater mInflater;
    private List<Integer> mList;

    private Activity mActivity;
    private Context mContext;


    public HomeGridAdapter(Activity activity, Context context,
                            List<Integer> list) {
        this.mList = list;
        this.mContext = context;
        this.mActivity = activity;
        this.mInflater = LayoutInflater.from(context);
        this.mScreenWidth = AndroidUtils.getScreenWidth(mActivity);
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
                    R.layout.item_home_grid, null);
            holder = new ChoicePictureGridViewHolder();
            holder.mImageView = (ImageView) convertView
                    .findViewById(R.id.grid_image);
            holder.mGridItem = (RelativeLayout) convertView
                    .findViewById(R.id.grid_item);


            ViewGroup.LayoutParams lp = holder.mImageView.getLayoutParams();
            lp.width = mScreenWidth / 4;
            lp.height = lp.width;
            holder.mImageView.setLayoutParams(lp);
            convertView.setTag(holder);
        } else {
            holder = (ChoicePictureGridViewHolder) convertView.getTag();
        }
        Integer mPath = mList.get(position);
        holder.mImageView.setImageResource(mPath);
        return convertView;
    }
}
