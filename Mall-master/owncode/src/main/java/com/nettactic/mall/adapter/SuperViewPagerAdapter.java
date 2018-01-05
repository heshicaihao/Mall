package com.nettactic.mall.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 引导页界面的适配器
 * 
 * @author heshicaihao
 */
public class SuperViewPagerAdapter extends PagerAdapter {

	private List<View> mViews;

	public SuperViewPagerAdapter() {
	}

	public SuperViewPagerAdapter(List<View> views) {
		this.mViews = views;
	}

	@Override
	public int getCount() {
		return mViews.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(mViews.get(position));
	}

	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager) container).addView(mViews.get(position));
		return mViews.get(position);
	}

	/**
	 * 插入一个View到viewpager中
	 * 
	 * @param view
	 * @return
	 */
	public boolean insert(View view) {
		if (this.mViews == null) {
			this.mViews = new ArrayList<View>();
		}

		return this.mViews.add(view);
	}
}
