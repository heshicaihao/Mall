package com.nettactic.mall.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import cn.jpush.android.api.JPushInterface;

import com.nettactic.mall.MainActivity;
import com.nettactic.mall.R;
import com.nettactic.mall.base.BaseActivity;
import com.nettactic.mall.cache.JsonDao;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.SharedpreferncesUtil;

/**
 * 欢迎页
 * 
 * @author heshicaihao
 */
public class StartActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_start);
		AndroidUtils.getScreenInfo(this);
		DelayedJumpNext();

	}

	/**
	 * 延时跳转下一页
	 */
	private void DelayedJumpNext() {
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				runOnUiThread(new Runnable() {

					public void run() {
						if (SharedpreferncesUtil
								.getGuided(getApplicationContext())) {
							JsonDao.getMaterial();
							gotoMainActivity();
						} else {
							gotoWelcomeActivity();
						}
					}
				});
			}
		}, 2000);
	}

	private void gotoMainActivity() {
		int CurrentTabNum = 0;
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.putExtra("CurrentTabNum", CurrentTabNum);
		startActivity(intent);
		StartActivity.this.finish();
	}

	private void gotoWelcomeActivity() {
		Intent intent = new Intent(StartActivity.this, WelcomeActivity.class);
		StartActivity.this.startActivity(intent);
		StartActivity.this.finish();
	}

	@Override
	protected void onResume() {
		JPushInterface.onResume(this);
		super.onResume();

	}

	@Override
	protected void onPause() {
		JPushInterface.onPause(this);
		super.onPause();

	}

}
