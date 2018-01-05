package com.nettactic.mall.adapter;


import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.heshicai.hao.xutils.BitmapUtils;
import com.heshicai.hao.xutils.bitmap.BitmapCommonUtils;
import com.heshicai.hao.xutils.bitmap.BitmapDisplayConfig;
import com.nettactic.mall.R;
import com.nettactic.mall.utils.BitmapHelp;


/**
 * 商品详情 List 的适配器
 * 
 * @author heshicaihao
 */
public class GoodsDetailsListAdapter extends BaseAdapter {

	private JSONArray mData;

	private LayoutInflater mInflater;

	private BitmapUtils bitmapUtils;
	private BitmapDisplayConfig bigPicDisplayConfig;
	@SuppressWarnings("unused")
	private Context mContext;
	@SuppressWarnings("unused")
	private Activity mActivity;
	private ViewHolder holder = null;
	private String imgUrl;

	public GoodsDetailsListAdapter(Context mContext, Activity mActivity,
			JSONArray jsonArray) {
		this.mData = jsonArray;
		this.mContext = mContext;
		this.mActivity = mActivity;
		mInflater = LayoutInflater.from(mContext);
		if (bitmapUtils == null) {
			bitmapUtils = BitmapHelp.getBitmapUtils(mContext);
		}

		bigPicDisplayConfig = new BitmapDisplayConfig();
		// 图片太大时容易OOM。
		bigPicDisplayConfig.setBitmapConfig(Bitmap.Config.RGB_565);
		bigPicDisplayConfig.setBitmapMaxSize(BitmapCommonUtils
				.getScreenSize(mActivity));
		bitmapUtils.configDefaultLoadingImage(R.mipmap.wait_im);
		bitmapUtils.configDefaultLoadFailedImage(R.mipmap.wait_im);
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		// AlphaAnimation 在一些android系统上表现不正常, 造成图片列表中加载部分图片后剩余无法加载, 目前原因不明.
		// 可以模仿下面示例里的fadeInDisplay方法实现一个颜色渐变动画。
		AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(1000);
		bitmapUtils.configDefaultImageLoadAnimation(animation);

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
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_goods_details_list,
					null);
			holder = new ViewHolder();
			holder.imageview = (ImageView) convertView
					.findViewById(R.id.goods_imageview);
			holder.imageview.setScaleType(ImageView.ScaleType.FIT_XY);
			holder.imageview.setAdjustViewBounds(true);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		try {
			if (mData.length() != 0) {
				imgUrl = (String) mData.get(position);
				if (!TextUtils.isEmpty(imgUrl)) {
					bitmapUtils.display(holder.imageview, imgUrl);
					
//					Glide.with(mActivity).load(imgUrl).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
//	                    @Override
//	                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//	                        int imageWidth = resource.getWidth();
//	                        int imageHeight = resource.getHeight();
//	                        int height = AndroidUtils.getScreenWidth(mActivity) * imageHeight / imageWidth;
//	                        ViewGroup.LayoutParams para = holder.imageview.getLayoutParams();
//	                        para.height = height;
//	                        holder.imageview.setLayoutParams(para);
//	                        Glide.with(mActivity).load(imgUrl).asBitmap().into(holder.imageview);
//	                    }
//
//	                });
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return convertView;

	}

	public static class ViewHolder {
		public ImageView imageview;
	}

}
