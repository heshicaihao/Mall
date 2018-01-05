/*
 * Copyright 2014 trinea.cn All right reserved. This software is the
 * confidential and proprietary information of trinea.cn
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with trinea.cn.
 */
package com.nettactic.mall.ad;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nettactic.mall.R;
import com.nettactic.mall.activity.OtherWebActivity;
import com.nettactic.mall.bean.ADBean;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.StringUtils;

/**
 * ImagePagerAdapter
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-2-23
 */
public class ImagePagerAdapter extends RecyclingPagerAdapter {

	private Context context;
	private List<ADBean> imageIdList;
	private int size;
	private boolean isInfiniteLoop;
	private Activity mActivity;
	private LayoutInflater mInflater;

	public ImagePagerAdapter(Activity mActivity, Context context,
			List<ADBean> imageIdList) {
		this.mActivity = mActivity;
		this.context = context;
		this.imageIdList = imageIdList;
		this.size = ListUtils.getSize(imageIdList);
		isInfiniteLoop = false;
		mInflater = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {
		return isInfiniteLoop ? Integer.MAX_VALUE : ListUtils
				.getSize(imageIdList);
	}

	/**
	 * get really position
	 * 
	 * @param position
	 * @return
	 */
	private int getPosition(int position) {
		return isInfiniteLoop ? position % size : position;
	}

	@Override
	public View getView(final int position, View convertView,
			ViewGroup container) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_ad, null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.imageview);
			LayoutParams lp = holder.imageView.getLayoutParams();
			lp.width = AndroidUtils.getScreenWidth(mActivity);
			lp.height = lp.width*400/750;
			holder.imageView.setLayoutParams(lp);
			holder.imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.i("TAG", "position " + position);
					String url = imageIdList.get(getPosition(position))
							.getUrl();
					String title = imageIdList.get(getPosition(position))
							.getTitle();
					if (!StringUtils.isEmpty(url)) {
						startOtherWeb(context, title, url);
					}
				}
			});
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String url = imageIdList.get(getPosition(position)).getImage_url();
		if (!StringUtils.isEmpty(url)) {
			Glide.with(context).load(url).placeholder(R.mipmap.wait_im)
					.crossFade().into(holder.imageView);
		}
		return convertView;
	}

	/**
	 * 打开H5界面
	 * 
	 * @param context
	 */
	public void startOtherWeb(Context context, String title, String url) {
		Intent intent = new Intent(context, OtherWebActivity.class);
		intent.putExtra("title", title);
		intent.putExtra("url", url);
		mActivity.startActivity(intent);
	}

	private static class ViewHolder {

		ImageView imageView;
	}

	/**
	 * @return the isInfiniteLoop
	 */
	public boolean isInfiniteLoop() {
		return isInfiniteLoop;
	}

	/**
	 * @param isInfiniteLoop
	 *            the isInfiniteLoop to set
	 */
	public ImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
		this.isInfiniteLoop = isInfiniteLoop;
		return this;
	}
}
