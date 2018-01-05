/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nettactic.mall.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nettactic.mall.R;
import com.nettactic.mall.common.MyApplication;
import com.nettactic.mall.utils.AndroidUtils;

/**
 * 底部单行滚动条（网易新闻导航条变种）
 * 
 * @author heshicaihao
 */
public class MyHScrollView extends HorizontalScrollView {

	private float currentPositionOffset = 0f;
	private int tabCount;
	private int currentPosition = 0;
	private int scrollOffset = 54;
	private int selectedPosition = 0;
	private int lastScrollX = 0;
	private int tabWidth = AndroidUtils.dip2px(MyApplication.getContext(), 50);
	private int tabHeight = AndroidUtils.dip2px(MyApplication.getContext(), 77);
	private int tabMargins = AndroidUtils.dip2px(MyApplication.getContext(), 4);
	private String tabBackgroundColor = "#90dfdb";
	private String tabselectedBackgroundColor = "#f3f2ee";
	private List<Integer> tabDatas = new ArrayList<Integer>();
	private LinearLayout tabsContainer;
	private ViewPager viewPager;
	private final PageListener pageListener = new PageListener();
	private final RightListener rightListener = new RightListener();
	private final LeftListener leftListener = new LeftListener();
	public OnPageChangeListener delegatePageListener;
	private ImageView mLeft;
	private ImageView mRight;

	public MyHScrollView(Context context) {
		this(context, null);
	}

	public MyHScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyHScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
//		ACache mCache = ACache.get(MyApplication.getContext());
//		String asString = mCache.getAsString(MyConstants.SCREENWIDTH);
//		int screenwidth = Integer.parseInt(asString);
//		tabWidth = (screenwidth - tabMargins * 2 * 6 - (int) (2 * 30 * screenDensity)) / 6;
		setFillViewport(true);
		setWillNotDraw(false);
		tabsContainer = new LinearLayout(context);
		tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
		tabsContainer.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));

		addView(tabsContainer);
	}

	public void setViewPager(ViewPager pager, ImageView leftBtn,
			ImageView rightBtn, List<Integer> datas) {
		this.viewPager = pager;
		this.tabDatas = datas;
		this.mLeft = leftBtn;
		this.mRight = rightBtn;
		if (pager.getAdapter() == null) {
			throw new IllegalStateException(
					"ViewPager does not have adapter instance.");
		}
		pager.setOnPageChangeListener(pageListener);
		rightBtn.setOnClickListener(rightListener);
		leftBtn.setOnClickListener(leftListener);
		notifyDataSetChanged();
	}

	// public void setViewPager(ViewPager pager, List<Integer> datas) {
	// this.viewPager = pager;
	// this.tabDatas = datas;
	// if (pager.getAdapter() == null) {
	// throw new IllegalStateException(
	// "ViewPager does not have adapter instance.");
	// }
	// pager.setOnPageChangeListener(pageListener);
	// notifyDataSetChanged();
	// }

	public void setOnPageChangeListener(OnPageChangeListener listener) {
		this.delegatePageListener = listener;
	}

	public void notifyDataSetChanged() {

		tabsContainer.removeAllViews();
		tabCount = viewPager.getAdapter().getCount();
		for (int i = 0; i < tabCount; i++) {

			if (viewPager.getAdapter() instanceof IconTabProvider) {
				addIconTab(i,
						((IconTabProvider) viewPager.getAdapter())
								.getPageIconResId(i));
			} else {
				addImageTab(i, viewPager.getAdapter().getPageTitle(i)
						.toString());
			}
		}

		updateTabStyles();

		getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@SuppressWarnings("deprecation")
					@Override
					public void onGlobalLayout() {
						getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						currentPosition = viewPager.getCurrentItem();
						scrollToChild(currentPosition, 0);
					}
				});

	}

	private void addImageTab(final int position, String title) {

		ImageView tab = new ImageView(getContext());
		tab.setImageResource(tabDatas.get(position));
		tab.setScaleType(ImageView.ScaleType.FIT_XY);
		addTab(position, tab);
	}

	private void addIconTab(final int position, int resId) {

		ImageButton tab = new ImageButton(getContext());
		tab.setImageResource(resId);
		addTab(position, tab);

	}

	private void addTab(final int position, View tab) {
		tab.setFocusable(true);
		tab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				viewPager.setCurrentItem(position);
			}
		});

		tab.setPadding(tabMargins, tabMargins, tabMargins, tabMargins);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(tabWidth,
				tabHeight);
		tabsContainer.addView(tab, position, lp);
	}

	private void updateTabStyles() {

		for (int i = 0; i < tabCount; i++) {

			View v = tabsContainer.getChildAt(i);

			if (v instanceof ImageView) {
				ImageView tab = (ImageView) v;
				if (i == selectedPosition) {
					tab.setBackgroundColor(Color.parseColor(tabBackgroundColor));
					if (selectedPosition == 0) {
						mLeft.setImageResource(R.mipmap.left_normal);
						mRight.setImageResource(R.mipmap.right_pressed);
					} else if (selectedPosition == tabCount - 1) {
						mLeft.setImageResource(R.mipmap.left_pressed);
						mRight.setImageResource(R.mipmap.right_normal);
					} else {
						mLeft.setImageResource(R.mipmap.left_pressed);
						mRight.setImageResource(R.mipmap.right_pressed);
					}
				} else {
					tab.setBackgroundColor(Color
							.parseColor(tabselectedBackgroundColor));

				}
			}
		}

	}

	private void scrollToChild(int position, int offset) {
		if (tabCount == 0) {
			return;
		}
		int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;
		if (position > 0 || offset > 0) {
			newScrollX -= scrollOffset;
		}
		if (newScrollX != lastScrollX) {
			lastScrollX = newScrollX;
			scrollTo(newScrollX, 0);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (isInEditMode() || tabCount == 0) {
			return;
		}
		// default: line below current tab
		View currentTab = tabsContainer.getChildAt(currentPosition);
		float lineLeft = currentTab.getLeft();
		float lineRight = currentTab.getRight();
		// if there is an offset, start interpolating left and right coordinates
		// between current and next tab
		if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {

			View nextTab = tabsContainer.getChildAt(currentPosition + 1);
			final float nextTabLeft = nextTab.getLeft();
			final float nextTabRight = nextTab.getRight();
			lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset)
					* lineLeft);
			lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset)
					* lineRight);
		}

	}

	private class RightListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			int mNextItem = viewPager.getCurrentItem() < tabCount ? (viewPager
					.getCurrentItem() + 1) : viewPager.getCurrentItem();
			viewPager.setCurrentItem(mNextItem, true);

		}

	}

	private class LeftListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			int mNextItem = viewPager.getCurrentItem() > 0 ? (viewPager
					.getCurrentItem() - 1) : viewPager.getCurrentItem();
			viewPager.setCurrentItem(mNextItem, true);

		}

	}

	private class PageListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {

			currentPosition = position;
			currentPositionOffset = positionOffset;
			scrollToChild(position, (int) (positionOffset * tabsContainer
					.getChildAt(position).getWidth()));

			invalidate();

			if (delegatePageListener != null) {
				delegatePageListener.onPageScrolled(position, positionOffset,
						positionOffsetPixels);
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_IDLE) {
				scrollToChild(viewPager.getCurrentItem(), 0);
			}

			if (delegatePageListener != null) {
				delegatePageListener.onPageScrollStateChanged(state);
			}
		}

		@Override
		public void onPageSelected(int position) {
			selectedPosition = position;
			updateTabStyles();
			if (delegatePageListener != null) {
				delegatePageListener.onPageSelected(position);
			}
		}

	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		SavedState savedState = (SavedState) state;
		super.onRestoreInstanceState(savedState.getSuperState());
		currentPosition = savedState.currentPosition;
		requestLayout();
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState savedState = new SavedState(superState);
		savedState.currentPosition = currentPosition;
		return savedState;
	}

	static class SavedState extends BaseSavedState {
		int currentPosition;

		public SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			currentPosition = in.readInt();
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeInt(currentPosition);
		}

		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
			@Override
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			@Override
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}

	public interface IconTabProvider {
		public int getPageIconResId(int position);
	}

}
