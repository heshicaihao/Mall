package com.nettactic.mall.activity;

import java.util.ArrayList;
import java.util.List;

import com.nettactic.mall.MainActivity;
import com.nettactic.mall.R;
import com.nettactic.mall.adapter.SuperViewPagerAdapter;
import com.nettactic.mall.base.BaseActivity;
import com.nettactic.mall.utils.AnimationUtil;
import com.nettactic.mall.utils.SharedpreferncesUtil;
import com.nettactic.mall.widget.ViewPagerPoint;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * 引导页
 * 
 * @author heshicaihao
 */
public class WelcomeActivity extends BaseActivity implements
		OnPageChangeListener, OnClickListener {

	private ViewPager mViewpager;
	private ViewPagerPoint mPagerPoint;
	private TextView mJump;
	private TextView mStart;

	private SuperViewPagerAdapter mAdapter;

	private List<View> mViews;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);
		initView();
		init();

	}

	private void initView() {
		mViewpager = (ViewPager) findViewById(R.id.viewpager);
		mPagerPoint = (ViewPagerPoint) findViewById(R.id.viewpage_item);
		mJump = (TextView) findViewById(R.id.jump_text);
		mStart = (TextView) findViewById(R.id.start_text);

		mViewpager.setOnPageChangeListener(this);
		mJump.setOnClickListener(this);
		mStart.setOnClickListener(this);
	}

	private void init() {

		mViews = getViewData();

		mPagerPoint.setBitmap(R.mipmap.no_icon, R.mipmap.yes_icon);
		mPagerPoint.setCount(mViews.size());
		mPagerPoint.notifyDataSetChanged(0);

		mViewpager.setOffscreenPageLimit(mViews.size());
		mAdapter = new SuperViewPagerAdapter(mViews);
		mViewpager.setAdapter(mAdapter);
		initUserInfo();

	}

	/**
	 * 向List加 数据
	 * 
	 * @return
	 */
	public List<View> getViewData() {
		mViews = new ArrayList<View>();

		View view1 = View.inflate(this, R.layout.viewpager_one, null);
		View view2 = View.inflate(this, R.layout.viewpager_two, null);
		View view3 = View.inflate(this, R.layout.viewpager_three, null);

		mViews.add(view1);
		mViews.add(view2);
		mViews.add(view3);

		return mViews;
	}

	@Override
	public void onPageSelected(int index) {
		mPagerPoint.notifyDataSetChanged(index);
		setItemShow(index);

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.jump_text:
			if (SharedpreferncesUtil.getGuided(getApplicationContext())) {
				WelcomeActivity.this.finish();
				AnimationUtil.getIntence().cancelAlphaAnimation();
			} else {
				SharedpreferncesUtil.setGuided(getApplicationContext());
				// deskfast();
				startActivity(this, MainActivity.class);
				AnimationUtil.getIntence().cancelAlphaAnimation();
				WelcomeActivity.this.finish();

			}
			break;
		case R.id.start_text:
			if (SharedpreferncesUtil.getGuided(getApplicationContext())) {
				WelcomeActivity.this.finish();
				AnimationUtil.getIntence().cancelAlphaAnimation();
			} else {
				SharedpreferncesUtil.setGuided(getApplicationContext());
				// deskfast();
				startActivity(this, MainActivity.class);
				AnimationUtil.getIntence().cancelAlphaAnimation();
				WelcomeActivity.this.finish();

			}
			break;
		default:
			break;
		}
	}

	/**
	 * 根据传入参数设置显示 和 动画
	 * 
	 * @param index
	 */
	public void setItemShow(int index) {
		if (index == mViews.size() - 1) {
			AnimationUtil.getIntence().startAlphaAnimation(mStart);
			mPagerPoint.setVisibility(View.GONE);
			mJump.setVisibility(View.GONE);
			mStart.setVisibility(View.VISIBLE);
		} else {
			AnimationUtil.getIntence().cancelAlphaAnimation();
			mPagerPoint.setVisibility(View.VISIBLE);
			mJump.setVisibility(View.VISIBLE);
			mStart.setVisibility(View.GONE);
		}
	}

	/**
	 * 初始化 用户信息
	 */
	private void initUserInfo() {
		user.setUname("");
		user.setPassword("");
		user.setId("");
		user.setToken("");
		user.setIs_login(false);
		user.setTemp_id("");
		user.setTemp_token("");
		user.setIs_temp_login(false);
		user.setOpen_id("");
		user.setType("");
		user.setUsername("");
		user.setHead_portrait("");
		user.setIs_three_login(false);
		user.setIs_order_null(true);
		mUserController.saveUserInfo(user);
	}
}
