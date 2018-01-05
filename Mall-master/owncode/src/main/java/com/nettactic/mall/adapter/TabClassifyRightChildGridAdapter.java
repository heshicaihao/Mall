package com.nettactic.mall.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.heshicai.hao.xutils.BitmapUtils;
import com.heshicai.hao.xutils.bitmap.BitmapDisplayConfig;
import com.heshicai.hao.xutils.bitmap.core.BitmapSize;
import com.nettactic.mall.R;
import com.nettactic.mall.activity.SearchActivity;
import com.nettactic.mall.activity.SearchResultActivity;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.BitmapHelp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TabClassifyRightChildGridAdapter extends BaseAdapter {

    private int mScreenWidth;
    private LayoutInflater mInflater;
    private JSONArray mData;

    private Activity mActivity;
    private Context mContext;

    private BitmapUtils bitmapUtils;
    private BitmapDisplayConfig bigPicDisplayConfig;

    public TabClassifyRightChildGridAdapter(Activity activity, Context context, JSONArray mData) {
        this.mData = mData;
        this.mContext = context;
        this.mActivity = activity;
        this.mInflater = LayoutInflater.from(context);
        this.mScreenWidth = AndroidUtils.getScreenWidth(mActivity);
        if (bitmapUtils == null) {
            bitmapUtils = BitmapHelp.getBitmapUtils(mContext);
        }
        bigPicDisplayConfig = new BitmapDisplayConfig();
        bigPicDisplayConfig.setBitmapConfig(Bitmap.Config.RGB_565);
        bigPicDisplayConfig.setBitmapMaxSize(new BitmapSize(
                mScreenWidth / 5, mScreenWidth / 5));
        bitmapUtils.configDefaultLoadingImage(R.mipmap.blank_bg);
        bitmapUtils.configDefaultLoadFailedImage(R.mipmap.blank_bg);
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
        bitmapUtils.configDefaultShowOriginal(false);
        bitmapUtils.configDiskCacheEnabled(false);
    }

    @Override
    public int getCount() {
        int count = mData == null ? 0 : mData.length();
        return count;
    }

    @Override
    public Object getItem(int position) {
        try {
            return mData == null ? null : mData.get(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
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
                    R.layout.item_tab_classify_right_child_grid, null);
            holder = new ViewHolder();
            holder.grid_imageIv = (ImageView) convertView
                    .findViewById(R.id.right_child_grid_image);

            holder.cat_nameTv = (TextView) convertView
                    .findViewById(R.id.right_child_cat_name);

            holder.grid_itemLl = (LinearLayout) convertView
                    .findViewById(R.id.grid_item_ll);
            ViewGroup.LayoutParams lp = holder.grid_imageIv.getLayoutParams();
            int m = AndroidUtils.dip2px(mContext, 103);
            lp.width = (mScreenWidth - m) / 3;
            lp.height = lp.width;
            holder.grid_imageIv.setLayoutParams(lp);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            JSONObject object = mData.getJSONObject(position);
            final String cat_name = object.optString("title");
            holder.cat_nameTv.setText(cat_name);
            String picurl = object.optString("picurl");
            bitmapUtils.display(holder.grid_imageIv,picurl,bigPicDisplayConfig);
            holder.grid_itemLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoSearchResult(cat_name);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return convertView;

    }

    private void gotoSearchResult(String key) {
        Intent intent = new Intent(mActivity, SearchResultActivity.class);
        intent.putExtra("keyword", key);
        mActivity.startActivity(intent);
        AndroidUtils.enterActvityAnim(mActivity);

    }

    public static class ViewHolder {
        public TextView cat_nameTv;
        public ImageView grid_imageIv;
        public LinearLayout grid_itemLl;

    }

}


